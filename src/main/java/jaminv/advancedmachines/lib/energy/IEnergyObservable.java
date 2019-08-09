package jaminv.advancedmachines.lib.energy;

public interface IEnergyObservable {
	public static interface IObserver {
		public void onEnergyChanged();
	}
	
	public void addObserver(IObserver observer);
}
