package jaminv.advancedmachines.lib.render.quad;

import javax.annotation.concurrent.Immutable;

@Immutable
public class Cuboid {
	public static Cuboid UNIT = new Cuboid(0f, 0f, 0f, 1f, 1f, 1f);

	private final float xmin, xmax, ymin, ymax, zmin, zmax;
	
	public Cuboid(float xmin, float ymin, float zmin, float xmax, float ymax, float zmax) {
		this.xmin = xmin; this.xmax = xmax;
		this.ymin = ymin; this.ymax = ymax;
		this.zmin = zmin; this.zmax = zmax;	
	}	
	
	public Cuboid offset(float xoff, float yoff, float zoff) {
		return new Cuboid(xmin + xoff, ymin + yoff, zmin + zoff,
				xmax - xoff, ymax - yoff, zmax - zoff);
	}
	
	public Cuboid offset(float xminOff, float yminOff, float zminOff, float xmaxOff, float ymaxOff, float zmaxOff) {
		return new Cuboid(xmin + xminOff, ymin + yminOff, zmin + zminOff,
				xmax - xmaxOff, ymax - ymaxOff, zmax - zmaxOff);
	}

	public float getXMin() { return xmin; }
	public float getXMax() { return xmax; }
	public float getYMin() { return ymin; }
	public float getYMax() { return ymax; }
	public float getZMin() { return zmin; }
	public float getZMax() { return zmax; }
}
