package jaminv.advancedmachines.objects.blocks.fluid;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;

public class FluidTankObservable extends FluidTank {

	public static interface IObserver {
		public void onTankContentsChanged();
	}
	
	public FluidTankObservable(int capacity) {
		super(capacity);
	}
	
	private List<IObserver> observers = new ArrayList<>();
	
	public void addObserver(IObserver obv) {
		observers.add(obv);
	}
	
	public void removeObserver(IObserver obv) {
		observers.remove(obv);
	}

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
}
