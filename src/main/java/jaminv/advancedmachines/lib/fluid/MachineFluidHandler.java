package jaminv.advancedmachines.lib.fluid;

import java.util.ArrayList;
import java.util.List;

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
public class MachineFluidHandler implements IFluidHandlerMachine {
	
	public static interface IObserver {
		/**
		 * Issues one event whenever any number of tanks have changed.
		 * 
		 * Due to the nature of how tanks work, multiple tanks can be filled or drained at once.
		 * Only one event is fired in such an instance.
		 */
		public void onTankContentsChanged();
	}
	private List<IObserver> observers = new ArrayList<>();
	
	public void addObserver(IObserver obv) { observers.add(obv); }
	public void removeObserver(IObserver obv) {	observers.remove(obv); }
	
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
		for (int i = 0; i <= numTanks; i++) {
			IFluidTankAdvanced tank = new FluidTankAdvanced(defaultCapacity);
			input.add(tank);
		}
		return this;
	}
	
	public MachineFluidHandler addOutputTanks(int numTanks) {
		for (int i = 0; i <= numTanks; i++) {
			IFluidTankAdvanced tank = new FluidTankAdvanced(defaultCapacity);
			output.add(tank);
		}
		return this;
	}
	
	public MachineFluidHandler addInputTank(FluidTankAdvanced tank) { input.add(tank); return this; }
	public MachineFluidHandler addOutputTank(FluidTankAdvanced tank) { output.add(tank); return this; }
	
	/** Sets capacity for ALL tanks. */
	public MachineFluidHandler setCapacity(int capacity) {
		this.defaultCapacity = capacity;
		for (IFluidTankAdvanced tank : input) { tank.setCapacity(capacity); }
		for (IFluidTankAdvanced tank : output) { tank.setCapacity(capacity); }
		return this;
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
			if (tank.getFluid().isFluidEqual(resource)) {
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
		
		for (IFluidTankAdvanced tank : tanks) {
			if (ret == null && !tank.isEmpty()) {
				ret = drain(tank, maxDrain, doDrain, internal);
			} else {
				FluidStack result = drain(tank, maxDrain - ret.amount, doDrain, internal);
				ret.amount += result.amount;
			}
			
			if (ret.amount >= maxDrain) { break; }
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
		for (int i = 0; i < this.input.size(); i++) {
			FluidStack fluid = this.input.get(i).getFluid();
			if (fluid == null) { ret[i] = null; } else { ret[i] = fluid.copy(); }
		}
		return ret;
	}
	
	public FluidStack[] getInput() { return getStacks(input); }
	public FluidStack[] getOutput() { return getStacks(output); }

}
