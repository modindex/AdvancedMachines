package jaminv.advancedmachines.lib.util.coord;

import javax.annotation.concurrent.Immutable;

/**
 * Default Implementation of `Pos`
 * 
 * Describes an immuatable pixel position.
 * @author Jamin VanderBerg
 */
@Immutable
public class CoordPos implements Pos {
	protected final int x, y;
	public CoordPos(int x, int y) {
		this.x = x; this.y = y;
	}
	@Override public int getX() { return x; }
	@Override public int getY() { return y; }
	
	@Override public Pos add(Offset offset) {
		return new CoordPos(getX() + offset.getX(), getY() + offset.getY());
	}
}
