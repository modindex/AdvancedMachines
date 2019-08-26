package jaminv.advancedmachines.lib.render;

import java.util.HashMap;
import java.util.Map;

import javax.vecmath.Vector3f;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraftforge.common.model.TRSRTransformation;

/**
 * Alot of this was copied from CodeChickenLib's `TransformUtils` class,
 * which itself says it was extracted from ForgeBlockStateV1.
 * 
 * The default transformations are so specific, there's no real reason to change much.
 * Although I did change around how they were built... a little.
 *
 * @author Jamin Vanderberg
 * @credit Rain Warrior
 */
public class TransformationMap {
	public static class Builder {
		protected Map<TransformType, TRSRTransformation> map = new HashMap<TransformType, TRSRTransformation>();
		
		public Builder add(TransformType type, TRSRTransformation transform) {
			map.put(type, transform);
			return this;
		}
		
		public Builder add(TransformType type, float tx, float ty, float tz, float rx, float ry, float rz, float s) {
			return add(type, TransformationMap.createTransform(tx, ty, tz, rx, ry, rz, s));
		}
		
		public Map<TransformType, TRSRTransformation> build() { return map; }
	}

	private static final TRSRTransformation flipX = new TRSRTransformation(null, null, new Vector3f(-1, 1, 1), null);
	
	private static final TRSRTransformation blockThirdPerson = createTransform(0f, 2.5f, 0f, 75f, 45f, 0f, 0.375f);
	private static final TRSRTransformation itemThirdPerson = createTransform(0f, 3f, 1f, 0f, 0f, 0f, 0.55f);
	private static final TRSRTransformation itemFirstPerson = createTransform(1.13f, 3.2f, 1.13f, 0f, 90f, 25f, 0.68f);
	
	public static final Map<TransformType, TRSRTransformation> DEFAULT_BLOCK = new Builder()
		.add(TransformType.GUI, 0f, 0f, 0f, 30f, 225f, 0f, 0.625f)
		.add(TransformType.GROUND, 0f, 3f, 0f, 0f, 0f, 0f, 0.25f)
		.add(TransformType.FIXED, 0f, 0f, 0f, 0f, 0f, 0f, 0.5f)
		.add(TransformType.THIRD_PERSON_RIGHT_HAND, blockThirdPerson)
		.add(TransformType.THIRD_PERSON_LEFT_HAND, flipLeft(blockThirdPerson))
		.add(TransformType.FIRST_PERSON_RIGHT_HAND, 0f, 0f, 0f, 0f, 45f, 0f, 0.4f)
		.add(TransformType.FIRST_PERSON_LEFT_HAND, 0f, 0f, 0f, 0f, 225f, 0f, 0.4f)
		.build();
	
	public static final Map<TransformType, TRSRTransformation> DEFAULT_ITEM = new Builder()
		.add(TransformType.GROUND, 0f, 2f, 0f, 0f, 0f, 0f, 0.5f)
		.add(TransformType.HEAD, 1.13f, 3.2f, 1.13f, 0f, -90f, 25f, 0.68f)
		.add(TransformType.THIRD_PERSON_RIGHT_HAND, itemThirdPerson)
		.add(TransformType.THIRD_PERSON_LEFT_HAND, flipLeft(itemThirdPerson))
		.add(TransformType.FIRST_PERSON_RIGHT_HAND, itemFirstPerson)
		.add(TransformType.FIRST_PERSON_LEFT_HAND, flipLeft(itemFirstPerson))
		.build();
		
	public static TRSRTransformation createTransform(float tx, float ty, float tz, float rx, float ry, float rz, float s) {
		return createTransform(new Vector3f(tx/16, ty/16, tz/16), new Vector3f(rx, ry, rz), new Vector3f(s, s, s));
	}
	
	public static TRSRTransformation createTransform(Vector3f transform, Vector3f roation, Vector3f scale) {
		return TRSRTransformation.blockCenterToCorner(new TRSRTransformation(transform, TRSRTransformation.quatFromXYZDegrees(roation), scale, null));
	}

    public static TRSRTransformation flipLeft(TRSRTransformation transform) {
        return TRSRTransformation.blockCenterToCorner(flipX.compose(TRSRTransformation.blockCornerToCenter(transform)).compose(flipX));
    }	
}
