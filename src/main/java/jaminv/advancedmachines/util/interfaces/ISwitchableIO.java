package jaminv.advancedmachines.util.interfaces;

public interface ISwitchableIO {
	public void setInputState(boolean state);
	public void setPriority(int priority);
	
	public boolean getInputState();
	public int getPriority();
	
	public boolean canInput();
	public boolean canOutput();
	public boolean hasParent();
}
