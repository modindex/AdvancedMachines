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
public class MachineFluidHandler implements IFluidHandlerMachine {

	private List<IObserver> observers = new ArrayList<>();
	public void addObserver(IObserver obv) { observers.add(obv); }
	
	public void onTankContentsChanged() {
		for (IObserver obv : observers) {
			obv.onTankContentsChanged();
		}
	}	
	
	protected List<IFluidTankAdvanced> input = new ArrayList<IFluidTankAdvanced>();
	protected List<IFluidTankAdvanced> output = new ArrayList<IFluidTankAdvanced>();
	private int defaultCapacity, maxFill, maxDrain;
	
	public MachineFluidHandler(int defaultCapacity, int maxFill, int maxDrain) {
		this.defaultCapacity = defaultCapacity;
		this.maxFill = maxFill;
		this.maxDrain = maxDrain;
	}
	
	public MachineFluidHandler(int defaultCapacity, int maxTransmit) {
		this(defaultCapacity, maxTransmit, maxTransmit);		
	}
	
	public MachineFluidHandler addInputTanks(int numTanks) {
		for (int i = 0; i < numTanks; i++) {
			IFluidTankAdvanced tank = new FluidTankAdvanced(defaultCapacity, maxFill, maxDrain);
			input.add(tank);
		}
		return this;
	}
	
	public MachineFluidHandler addOutputTanks(int numTanks) {
		for (int i = 0; i < numTanks; i++) {
			IFluidTankAdvanced tank = new FluidTankAdvanced(defaultCapacity, maxFill, maxDrain);
			output.add(tank);
		}
		return this;
	}
	
	public MachineFluidHandler addInputTank(FluidTankAdvanced tank) { input.add(tank); return this; }
	public MachineFluidHandler addOutputTank(FluidTankAdvanced tank) { output.add(tank); return this; }
	
	@Override public int getTankCount() { return input.size() + output.size(); }

	@Override
	public IFluidTank getTank(int index) {
		if (index >= input.size()) { return output.get(index - input.size()); }
		return input.get(index);
	}

	/** Sets capacity for ALL tanks. */
	public void setFluidCapacity(int capacity) {
		this.defaultCapacity = capacity;
		for (IFluidTankAdvanced tank : input) { tank.setCapacity(capacity); }
		for (IFluidTankAdvanced tank : output) { tank.setCapacity(capacity); }
	}
	
	public MachineFluidHandler setDefaultCapacity(int capacity) { this.defaultCapacity = capacity; return this; }
	public MachineFluidHandler setMaxTransfer(int maxTransfer) { this.maxDrain = maxTransfer; this.maxFill = maxTransfer; return this; }
	public MachineFluidHandler setMaxFill(int maxFill) { this.maxFill = maxFill; return this; }
	public MachineFluidHandler setMaxDrain(int maxDrain) { this.maxDrain = maxDrain; return this; }	

	@Override
	public IFluidTankProperties[] getTankProperties() {
		IFluidTankProperties[] props = new IFluidTankProperties[input.size() + output.size()];
		
		int i = 0;
		for (IFluidTankAdvanced tank : input) {
			props[i] = new FluidTankProperties(tank, true, false); i++;
		}
		for (IFluidTankAdvanced tank : output) {
			props[i] = new FluidTankProperties(tank, false, true); i++;
		}
		return props;
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		int amount = Math.min(maxFill, resource.amount);
		return fill(new FluidStack(resource, amount), doFill, input, false);
	}
	
	public int fillInternal(FluidStack resource, boolean doFill) {
		return fill(resource, doFill, output, true);
	}
	
	protected int fill(FluidStack resource, boolean doFill, List<IFluidTankAdvanced> tanks, boolean internal) {
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
		return drain(new FluidStack(resource, amount), doDrain, output, false);
	}
	
	@Override
	public FluidStack drainInternal(FluidStack resource, boolean doDrain) {
		return drain(resource, doDrain, input, true);
	}
	
	protected FluidStack drain(FluidStack resource, boolean doDrain, List<IFluidTankAdvanced> tanks, boolean internal) {
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
		return drain(Math.min(this.maxDrain, maxDrain), doDrain, output, false);
	}
	
	public FluidStack drainInternal(int maxDrain, boolean doDrain) {
		return drain(maxDrain, doDrain, input, true);
	}
	
	protected FluidStack drain(int maxDrain, boolean doDrain, List<IFluidTankAdvanced> tanks, boolean internal) {
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
	
	/* IFluidHandlerMachine */
	
	protected FluidStack[] getStacks(List<IFluidTankAdvanced> tanks) {
		FluidStack[] ret = new FluidStack[tanks.size()];
		for (int i = 0; i < tanks.size(); i++) {
			FluidStack fluid = tanks.get(i).getFluid();
			if (fluid == null) { ret[i] = null; } else { ret[i] = fluid.copy(); }
		}
		return ret;
	}
	
	protected IFluidTankInternal[] getTanks(List<IFluidTankAdvanced> tanks) {
		IFluidTankInternal[] ret = new IFluidTankInternal[tanks.size()];
		for (int i = 0; i < tanks.size(); i++) {
			ret[i] = tanks.get(i).copy();
		}
		return ret;
	}
	
	public FluidStack[] getFluidInput() { return getStacks(input); }	
	public IFluidTankInternal[] getFluidOutput() { return getTanks(output); }

	/* INBTSerializable */
	
	protected NBTTagList writeTankNBT(List<IFluidTankAdvanced> tanks) {
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
        nbt.setInteger("inputSize", input.size());
        nbt.setInteger("outputSize", output.size());
        nbt.setTag("input", writeTankNBT(input));
        nbt.setTag("output", writeTankNBT(output));
        
        nbt.setInteger("defaultCapacity", defaultCapacity);
        nbt.setInteger("maxDrain", maxDrain);
        nbt.setInteger("maxFill", maxFill);
        return nbt;
    }
	
	protected void readTankNBT(NBTTagList nbt, List<IFluidTankAdvanced> tanks) {
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
		
		if (nbt.getInteger("inputSize") != this.input.size()) {
			input = new ArrayList<IFluidTankAdvanced>();
			addInputTanks(nbt.getInteger("inputSize"));
		}
		if (nbt.getInteger("outputSize") != this.output.size()) {
			output = new ArrayList<IFluidTankAdvanced>();		
			addOutputTanks(nbt.getInteger("outputSize"));
		}
		
		if (nbt.hasKey("input")) { readTankNBT(nbt.getTagList("input", Constants.NBT.TAG_COMPOUND), input); }
		if (nbt.hasKey("output")) { readTankNBT(nbt.getTagList("output", Constants.NBT.TAG_COMPOUND), output); }
	}
}
