package jaminv.advancedmachines.client.quads;

import net.minecraft.client.renderer.vertex.VertexFormat;

public abstract class ModelQuadBase implements IModelQuad {
		
	private VertexFormat format;
	public ModelQuadBase(VertexFormat format) {
		this.format = format;
	}
	
	public VertexFormat getFormat() { return format; }
}
