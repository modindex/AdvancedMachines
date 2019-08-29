package jaminv.advancedmachines.lib.render;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;

public class ModelBakeryHelper {
    public static BakedQuad createQuad(VertexFormat format, Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite tex, float umin, float umax, float vmin, float vmax, boolean inverted) {
        Vec3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();
     
        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
        builder.setTexture(tex);
        if (!inverted) {
	        putVertex(format, builder, normal, tex, v1.x, v1.y, v1.z, umin, vmin);
	        putVertex(format, builder, normal, tex, v2.x, v2.y, v2.z, umin, vmax);
	        putVertex(format, builder, normal, tex, v3.x, v3.y, v3.z, umax, vmax);
	        putVertex(format, builder, normal, tex, v4.x, v4.y, v4.z, umax, umin);
        } else {
	        putVertex(format, builder, normal, tex, v1.x, v1.y, v1.z, umin, vmin);
	        putVertex(format, builder, normal, tex, v4.x, v4.y, v4.z, umax, vmin);
	        putVertex(format, builder, normal, tex, v3.x, v3.y, v3.z, umax, vmax);
	        putVertex(format, builder, normal, tex, v2.x, v2.y, v2.z, umin, vmax);
        }
        return builder.build();
    }
    
    public static BakedQuad createQuad(VertexFormat format, Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite tex, boolean inverted) {
    	return createQuad(format, v1, v2, v3, v4, tex, 0, 16, 0, 16, inverted);
    }

    public static BakedQuad createQuad(VertexFormat format, Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite tex) {
    	return createQuad(format, v1, v2, v3, v4, tex, 0, 16, 0, 16, false);
    }
    
    public static void putVertex(VertexFormat format, UnpackedBakedQuad.Builder builder, Vec3d normal, TextureAtlasSprite tex, double x, double y, double z, float u, float v) {
 	
        for (int e = 0; e < format.getElementCount(); e++) {
            switch (format.getElement(e).getUsage()) {
                case POSITION:
                    builder.put(e, (float)x, (float)y, (float)z, 1.0f);
                    break;
                case COLOR:
                    builder.put(e, 1.0f, 1.0f, 1.0f, 1.0f);
                    break;
                case UV:
                    if (format.getElement(e).getIndex() == 0) {
                        u = tex.getInterpolatedU(u);
                        v = tex.getInterpolatedV(v);
                        builder.put(e, u, v, 0f, 1f);
                        break;
                    }
                case NORMAL:
                    builder.put(e, (float) normal.x, (float) normal.y, (float) normal.z, 0f);
                    break;
                default:
                    builder.put(e);
                    break;
            }
        }
    }	
}
