package jaminv.advancedmachines.lib.fluid;

public interface FluidObservable {

	public static interface Observer {
		public void onTankContentsChanged();
	}
	
	public void addObserver(Observer observer);
}
