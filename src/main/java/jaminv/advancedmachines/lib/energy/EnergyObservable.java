package jaminv.advancedmachines.lib.energy;

public interface EnergyObservable {
	public static interface IObserver {
		public void onEnergyChanged();
	}
	
	public void addObserver(IObserver observer);
}
