package jaminv.advancedmachines.lib.util.coord;

import net.minecraft.util.math.BlockPos.MutableBlockPos;

/**
 * Pixel Rectangle Coordinate
 * 
 * I don't know if this is strictly necessary, but it was easy enough to implement.
 * This is an abstraction to the DialogArea class. It may provide some additional
 * flexibility in the future, such as not forcing classes to derive from DialogArea
 * (although it is generally easier), or reducing duplicate code across other
 * implementations that may also use pixel rectangles.
 * 
 * Not that Rect /does not/ extend Pos. Although they both have getX() and getY()
 * functions, a Rectangle is not a Position, and it doesn't make sense to pass a
 * Rect to an object that expects a Pos.
 * 
 * @author Jamin VanderBerg
 */
public interface Rect {
	public int getX();
	public int getY();
	public int getW();
	public int getH();
	
	/**
	 * Adds an offset to a rectangle and returns a new rectangle.
	 * Mutable implementations may return the same object.
	 * @param offset
	 * @return
	 */	
	public Rect add(Offset offset);
	
	/**
	 * Gets the offset between two rectangles.
	 * 
	 * Not commutable. A.getOffsetB() != B.getOffsetA().
	 * This is essentially B - A, so if the further left object is A, then the resulting X value will be positive.
	 * Although this might seem backwards, I think it is the most intuitive.
	 * @param offset
	 * @return
	 */	
	public Offset getOffset(Rect other);
}
