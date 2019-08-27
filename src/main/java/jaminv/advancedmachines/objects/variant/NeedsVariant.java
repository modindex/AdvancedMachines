package jaminv.advancedmachines.objects.variant;

/**
 * Has Mutable Variant
 * 
 * Intended for use on TileEntities, where the variant type set from an outside object.
 * May have other uses.
 *  
 * @author Jamin VanderBerg
 *
 * @param <T> Variant type
 */
public interface NeedsVariant<T extends Variant> extends HasVariant<T> {
	public void setVariant(T variant);
}
