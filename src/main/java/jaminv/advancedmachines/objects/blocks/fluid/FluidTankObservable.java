package jaminv.advancedmachines.objects.blocks.fluid;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;

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
}
