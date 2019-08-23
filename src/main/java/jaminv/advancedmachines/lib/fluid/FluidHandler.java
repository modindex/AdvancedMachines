package jaminv.advancedmachines.lib.fluid;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

/**
 * Machine Fluid Handler, has separate input/output tanks.
 * 
 * Input tanks are intended to be filled externally and drained internally.
 * Output tanks are intended to be filled internally and drained externally.
 * 
 * @author Jamin VanderBerg
 */
public class FluidHandler implements IFluidHandlerAdvanced {

	private List<IObserver> observers = new ArrayList<>();
	public void addObserver(IObserver obv) { observers.add(obv); }
	
	public void onTankContentsChanged() {
		for (IObserver obv : observers) {
			obv.onTankContentsChanged();
		}
	}	
	
	protected List<IFluidTankAdvanced> tanks = new ArrayList<IFluidTankAdvanced>();
	private int defaultCapacity, maxFill, maxDrain;
	
	public FluidHandler(int defaultCapacity, int maxFill, int maxDrain) {
		this.defaultCapacity = defaultCapacity;
		this.maxFill = maxFill;
		this.maxDrain = maxDrain;
	}
	
	public FluidHandler(int defaultCapacity, int maxTransmit) {
		this(defaultCapacity, maxTransmit, maxTransmit);		
	}
	
	public FluidHandler addTanks(int numTanks) {
		for (int i = 0; i < numTanks; i++) {
			IFluidTankAdvanced tank = new FluidTankAdvanced(defaultCapacity, maxFill, maxDrain);
			tanks.add(tank);
		}
		return this;
	}
	
	public FluidHandler addTank(FluidTankAdvanced tank) { tanks.add(tank); return this; }

	/** Sets capacity for ALL tanks. */
	public void setFluidCapacity(int capacity) {
		this.defaultCapacity = capacity;
		for (IFluidTankAdvanced tank : tanks) { tank.setCapacity(capacity); }
	}
	
	public FluidHandler setDefaultCapacity(int capacity) { this.defaultCapacity = capacity; return this; }
	public FluidHandler setMaxTransfer(int maxTransfer) { this.maxDrain = maxTransfer; this.maxFill = maxTransfer; return this; }
	public FluidHandler setMaxFill(int maxFill) { this.maxFill = maxFill; return this; }
	public FluidHandler setMaxDrain(int maxDrain) { this.maxDrain = maxDrain; return this; }	

	@Override
	public IFluidTankProperties[] getTankProperties() {
		IFluidTankProperties[] props = new IFluidTankProperties[tanks.size()];
		
		int i = 0;
		for (IFluidTankAdvanced tank : tanks) {
			props[i] = new FluidTankProperties(tank, true, false); i++;
		}
		return props;
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		int amount = Math.min(maxFill, resource.amount);
		return fill(new FluidStack(resource, amount), doFill, false);
	}
	
	public int fillInternal(FluidStack resource, boolean doFill) {
		return fill(resource, doFill, true);
	}
	
	protected int fill(FluidStack resource, boolean doFill, boolean internal) {
		FluidStack res = resource.copy();
		
		// First check for a tank that already contains that liquid
		for (IFluidTankAdvanced tank : tanks) {
			if (!tank.isEmpty() && tank.getFluid().isFluidEqual(resource)) {
				int result = fill(tank, res, doFill, internal);
				res.amount -= result;
			
			}
			if (res.amount <= 0) { break; } 
		}
		
		// Then check for an empty tank		
		for (IFluidTankAdvanced tank : tanks) {
			if (tank.isEmpty()) {
				int result = fill(tank, res, doFill, internal);
				res.amount -= result;
				
			}
			if (res.amount <= 0) { break; }
		}
		
		if (doFill && resource.amount - res.amount > 0) { onTankContentsChanged(); } 
		return resource.amount - res.amount;
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		int amount = Math.min(maxDrain, resource.amount);
		return drain(new FluidStack(resource, amount), doDrain, false);
	}
	
	@Override
	public FluidStack drainInternal(FluidStack resource, boolean doDrain) {
		return drain(resource, doDrain, true);
	}
	
	protected FluidStack drain(FluidStack resource, boolean doDrain, boolean internal) {
		FluidStack res = resource.copy();
		
		for (IFluidTankAdvanced tank: tanks) {
			FluidStack result = drain(tank, res, doDrain, internal);
			res.amount -= result.amount;
			
			if (res.amount <= 0) { break; }
		}
		
		if (doDrain && resource.amount - res.amount > 0) { onTankContentsChanged(); }
		return new FluidStack(resource, resource.amount - res.amount);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		return drain(Math.min(this.maxDrain, maxDrain), doDrain, false);
	}
	
	public FluidStack drainInternal(int maxDrain, boolean doDrain) {
		return drain(maxDrain, doDrain, true);
	}
	
	protected FluidStack drain(int maxDrain, boolean doDrain, boolean internal) {
		FluidStack ret = null;
		int amount = maxDrain;
		
		for (IFluidTankAdvanced tank : tanks) {
			FluidStack result;
			if (ret != null) {
				result = drain(tank, new FluidStack(ret, amount), doDrain, internal);
			} else {
				result = drain(tank, amount, doDrain, internal);
			}
			
			if (result == null) { continue; }
			amount -= result.amount;
			if (ret == null) { ret = result; } else { ret.amount += result.amount; }
		}
		
		if (doDrain && ret.amount > 0) { onTankContentsChanged(); }
		return ret;		
	}
	
	/* Convenience functions to make fill/drain methods quite a bit cleaner */

	protected int fill(IFluidTankAdvanced tank, FluidStack resource, boolean doFill, boolean internal) {
		if (!internal) { return tank.fill(resource, doFill); }
		else { return tank.fillInternal(resource, doFill); }
	}

	protected FluidStack drain(IFluidTankAdvanced tank, FluidStack resource, boolean doDrain, boolean internal) {
		if (!internal) { return tank.drain(resource, doDrain); }
		else { return tank.drainInternal(resource, doDrain); }
	}

	protected FluidStack drain(IFluidTankAdvanced tank, int maxDrain, boolean doDrain, boolean internal) {
		if (!internal) { return tank.drain(maxDrain, doDrain); }
		else { return tank.drainInternal(maxDrain, doDrain); }
	}
	
	/* IFluidHandlerInternal */
	
	@Override
	public FluidStack[] getStacks() {
		FluidStack[] ret = new FluidStack[tanks.size()];
		for (int i = 0; i < tanks.size(); i++) {
			FluidStack fluid = tanks.get(i).getFluid();
			if (fluid == null) { ret[i] = null; } else { ret[i] = fluid.copy(); }
		}
		return ret;
	}
	
	@Override
	public IFluidTankInternal[] getTanks() {
		IFluidTankInternal[] ret = new IFluidTankInternal[tanks.size()];
		for (int i = 0; i < tanks.size(); i++) {
			ret[i] = tanks.get(i).copy();
		}
		return ret;
	}
	
	/* IFluidHandlerAdvanced */
	
	@Override
	public int getTankCount() { return tanks.size(); }

	@Override
	public IFluidTankInternal getTank(int index) { return tanks.get(index); }

	/* INBTSerializable */

	protected NBTTagList writeTankNBT() {
        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < tanks.size(); i++) {
        	NBTTagCompound tankTag = tanks.get(i).serializeNBT();
        	tankTag.setInteger("tank", i);
        	nbtTagList.appendTag(tankTag);
        }
        return nbtTagList;
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("size", tanks.size());
        nbt.setTag("tanks", writeTankNBT());
        
        nbt.setInteger("defaultCapacity", defaultCapacity);
        nbt.setInteger("maxDrain", maxDrain);
        nbt.setInteger("maxFill", maxFill);
        return nbt;
    }
	
	protected void readTankNBT(NBTTagList nbt) {
		for (int i = 0; i < nbt.tagCount(); i++) {
			NBTTagCompound tankTag = nbt.getCompoundTagAt(i);
			int tank = tankTag.getInteger("tank");
			if (tank >= 0 && tank < tanks.size()) {
				tanks.get(i).deserializeNBT(tankTag);
			}
		}
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		setDefaultCapacity(nbt.getInteger("defaultCapacity"));
		setMaxDrain(nbt.getInteger("maxDrain"));
		setMaxFill(nbt.getInteger("maxFill"));
		
		if (nbt.getInteger("size") <= tanks.size()) {
			addTanks(tanks.size() - nbt.getInteger("size"));
		}
		
		if (nbt.hasKey("tanks")) { readTankNBT(nbt.getTagList("tanks", Constants.NBT.TAG_COMPOUND)); }
	}
}
