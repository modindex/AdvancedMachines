package jaminv.advancedmachines.lib.util.coord;

import java.util.Vector;

import net.minecraft.util.math.Vec2f;

/**
 * Pixel Coordinate Position
 * 
 * I don't know if this is strictly necessary, but it was easy enough to implement.
 * This is an abstraction to the DialogPos class. It may provide some additional
 * flexibility in the future, such as not forcing classes to derive from DialogPos
 * (although it is generally easier), or reducing duplicate code across other
 * implementations that may also use pixel positions.
 * @author Jamin VanderBerg
 */
public interface Pos {
	public int getX();
	public int getY();
	
	/**
	 * Adds an offset to a position and returns a new position.
	 * Mutable implementations may return the same object.
	 * @param offset
	 * @return
	 */	
	public Pos add(Offset offset);
}
