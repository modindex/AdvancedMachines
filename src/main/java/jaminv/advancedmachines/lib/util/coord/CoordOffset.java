package jaminv.advancedmachines.lib.util.coord;

import javax.annotation.concurrent.Immutable;

/**
 * Default Implementation of `Offset`
 * 
 * Describes an immuatable pixel position.
 * @author Jamin VanderBerg
 */
@Immutable
public class CoordOffset implements Offset {
	protected final int x, y;
	public CoordOffset(int x, int y) {
		this.x = x; this.y = y;
	}
	@Override public int getX() { return x; }
	@Override public int getY() { return y; }
	
	@Override
	public CoordOffset add(Offset other) {
		return new CoordOffset(getX() + other.getX(), getY() + other.getY());
	}
	
	@Override
	public Offset invert() {
		return new CoordOffset(-getX(), -getY());
	}
}
