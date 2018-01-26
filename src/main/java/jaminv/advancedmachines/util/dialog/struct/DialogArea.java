package jaminv.advancedmachines.util.dialog.struct;

public class DialogArea {
	protected final int x, y, w, h;
	
	public DialogArea(int x, int y, int w, int h) {
		this.x = x; this.y = y;
		this.w = w; this.h = h;
	}
	
	public int getX() { return x; }
	public int getY() { return y; }
	public int getW() { return w; }
	public int getH() { return h; }
}
