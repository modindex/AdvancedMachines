package jaminv.advancedmachines.lib.container.layout;

public class FluidLayout implements IFluidLayout {
	
	protected int xPos, yPos, width, height;
	public FluidLayout(int xPos, int yPos, int width, int height) {
		this.xPos = xPos; this.yPos = yPos;
		this.width = width; this.height = height;
	}

	@Override public int getXPos() { return xPos; }
	@Override public int getYPos() { return yPos; }
	@Override public int getWidth() { return width; }
	@Override public int getHeight() { return height; }
}
