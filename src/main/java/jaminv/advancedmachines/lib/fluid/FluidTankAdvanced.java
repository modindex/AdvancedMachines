package jaminv.advancedmachines.lib.fluid;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;

public class FluidTankAdvanced extends FluidTank implements IFluidTankAdvanced {

	public FluidTankAdvanced(int capacity) {
		super(capacity);
	}
	
	private List<IObserver> observers = new ArrayList<>();
	public void addObserver(IObserver obv) { observers.add(obv); }
	
	@Override
	protected void onContentsChanged() {
		for (IObserver obv : observers) {
			obv.onTankContentsChanged();
		}
	}
	
	public int fillTank(IFluidTank other, int max_amount) {
		FluidStack stack = drain(max_amount, false);
		int amount = other.fill(stack, false);
		
		if (amount > 0) {
			stack = drain(amount, true);
			other.fill(stack, true);
			return amount;
		}
		
		return 0;
	}
	
	public int fillTank(IFluidTank other) { return fillTank(other, Integer.MAX_VALUE); }
	
	public boolean isEmpty() { 
		return fluid == null;
	}
}
