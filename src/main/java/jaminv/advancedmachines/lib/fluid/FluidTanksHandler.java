package jaminv.advancedmachines.lib.fluid;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

/**
 * Machine Fluid Handler, has separate input/output tanks.
 * 
 * Input tanks are intended to be filled externally and drained internally.
 * Output tanks are intended to be filled internally and drained externally.
 * 
 * @author Jamin VanderBerg
 */
public class FluidTanksHandler implements FluidHandler {

	private List<Observer> observers = new ArrayList<>();
	public void addObserver(Observer obv) { observers.add(obv); }
	
	public void onTankContentsChanged() {
		for (Observer obv : observers) {
			obv.onTankContentsChanged();
		}
	}	
	
	protected List<FluidTankDefault> tanks = new ArrayList<FluidTankDefault>();
	private int defaultCapacity, maxFill, maxDrain;
	private boolean canFill = true, canDrain = true;
	
	public FluidTanksHandler(int defaultCapacity, int maxFill, int maxDrain) {
		this.defaultCapacity = defaultCapacity;
		this.maxFill = maxFill;
		this.maxDrain = maxDrain;
	}
	
	public FluidTanksHandler(int defaultCapacity, int maxTransmit) {
		this(defaultCapacity, maxTransmit, maxTransmit);		
	}
	
	public FluidTanksHandler addTanks(int numTanks) {
		for (int i = 0; i < numTanks; i++) {
			tanks.add(new FluidTankDefault(defaultCapacity, maxFill, maxDrain));
		}
		return this;
	}

	/** Sets capacity for ALL tanks. */
	public void setFluidCapacity(int capacity) {
		this.defaultCapacity = capacity;
		for (FluidTankDefault tank : tanks) { tank.setCapacity(capacity); }
	}
	
	public FluidTanksHandler setDefaultCapacity(int capacity) { this.defaultCapacity = capacity; return this; }
	public FluidTanksHandler setMaxTransfer(int maxTransfer) { this.maxDrain = maxTransfer; this.maxFill = maxTransfer; return this; }
	public FluidTanksHandler setMaxFill(int maxFill) { this.maxFill = maxFill; return this; }
	public FluidTanksHandler setMaxDrain(int maxDrain) { this.maxDrain = maxDrain; return this; }	

	@Override
	public IFluidTankProperties[] getTankProperties() {
		IFluidTankProperties[] props = new IFluidTankProperties[tanks.size()];
		
		int i = 0;
		for (FluidTankDefault tank : tanks) {
			props[i] = new FluidTankProperties(tank, true, false); i++;
		}
		return props;
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if (!canFill) { return 0; }
		int amount = Math.min(maxFill, resource.amount);
		return fill(new FluidStack(resource, amount), doFill, false);
	}
	
	public int fillInternal(FluidStack resource, boolean doFill) {
		return fill(resource, doFill, true);
	}
	
	protected int fill(FluidStack resource, boolean doFill, boolean internal) {
		FluidStack res = resource.copy();
		
		// First check for a tank that already contains that liquid
		for (FluidTankDefault tank : tanks) {
			if (!tank.isEmpty() && tank.getFluid().isFluidEqual(resource)) {
				int result = fill(tank, res, doFill, internal);
				res.amount -= result;
			
			}
			if (res.amount <= 0) { break; } 
		}
		
		// Then check for an empty tank		
		for (FluidTankDefault tank : tanks) {
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
	public int fillSame(FluidStack resource, boolean doFill) {
		int filled = 0;
		
		for (int i = 0; i < tanks.size(); i++) {
			FluidTank tank = tanks.get(i);
			if (resource.isFluidEqual(tank.getFluid())) {
				filled += tank.fill(new FluidStack(resource, resource.amount - filled), doFill);
			}
		}
		return filled;
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		if (!canDrain) { return new FluidStack(resource, 0); }
		int amount = Math.min(maxDrain, resource.amount);
		return drain(new FluidStack(resource, amount), doDrain, false);
	}
	
	@Override
	public FluidStack drainInternal(FluidStack resource, boolean doDrain) {
		return drain(resource, doDrain, true);
	}
	
	protected FluidStack drain(FluidStack resource, boolean doDrain, boolean internal) {
		FluidStack res = resource.copy();
		
		for (FluidTankDefault tank: tanks) {
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
		
		for (FluidTankDefault tank : tanks) {
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

	protected int fill(FluidTankDefault tank, FluidStack resource, boolean doFill, boolean internal) {
		if (!internal) { return tank.fill(resource, doFill); }
		else { return tank.fillInternal(resource, doFill); }
	}

	protected FluidStack drain(FluidTankDefault tank, FluidStack resource, boolean doDrain, boolean internal) {
		if (!internal) { return tank.drain(resource, doDrain); }
		else { return tank.drainInternal(resource, doDrain); }
	}

	protected FluidStack drain(FluidTankDefault tank, int maxDrain, boolean doDrain, boolean internal) {
		if (!internal) { return tank.drain(maxDrain, doDrain); }
		else { return tank.drainInternal(maxDrain, doDrain); }
	}
	
	/* FluidHandler */
	
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
	public FluidTank[] getTanks() {
		FluidTank[] ret = new FluidTank[tanks.size()];
		for (int i = 0; i < tanks.size(); i++) {
			ret[i] = tanks.get(i).copy();
		}
		return ret;
	}
	
	@Override public boolean canFill() { return tanks.size() > 0 && canFill; }
	@Override public boolean canDrain() { return tanks.size() > 0 && canDrain; }	
	
	/* Additional Methods */

	public int getTankCount() { return tanks.size(); }

	public FluidTank getTank(int index) { return tanks.get(index); }
	
	public void setCanFill(boolean can) { this.canFill = can; }
	public void setCanDrain(boolean can) { this.canDrain = can; }

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
