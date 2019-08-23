package jaminv.advancedmachines.lib.util.coord;

public class CoordRect implements Rect {
	protected final int x, y, w, h;
	
	public CoordRect(int x, int y, int w, int h) {
		this.x = x; this.y = y;
		this.w = w; this.h = h;
	}
	
	@Override public int getX() { return x; }
	@Override public int getY() { return y; }
	@Override public int getW() { return w; }
	@Override public int getH() { return h; }
	
	@Override
	public Rect add(Offset offset) {
		return new CoordRect(getX() + offset.getX(), getY() + offset.getX(), getW(), getH());
	}	

	@Override
	public Offset getOffset(Rect other) {
		return new CoordOffset(other.getX() - getX(), other.getY() - getY());
	}
}
