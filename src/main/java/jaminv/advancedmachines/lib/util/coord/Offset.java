package jaminv.advancedmachines.lib.util.coord;

import java.util.Vector;

import net.minecraft.util.math.Vec2f;

/**
 * Pixel Coordinate Offset
 * 
 * Although technically very similar to `Pos`, in concept they are very different.
 * Therefore, they are treated separately in code, and are not interchangeable.
 * 
 * @author Jamin VanderBerg
 */
public interface Offset {
	public int getX();
	public int getY();
	
	/**
	 * Adds two offsets together and returns a new offset.
	 * Mutable implementations may return the same object.
	 * @param offset
	 * @return Offset
	 */
	public Offset add(Offset other);

	/**
	 * Inverts the offset and returns a new offset.
	 * Negative offset values become positive, positive offset values become negative.
	 * Mutable implementations may return the same object.
	 * @return Offset
	 */
	public Offset invert();
}
