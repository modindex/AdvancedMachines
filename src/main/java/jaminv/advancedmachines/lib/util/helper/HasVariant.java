package jaminv.advancedmachines.lib.util.helper;

/**
 * Has Variant
 * 
 * Intended for use on blocks and items, where the variant value is set at instantiation and can't be changed.
 * May have other uses.
 * 
 * @author Jamin VanderBerg
 *
 * @param <T> Variant type
 */
public interface HasVariant<T extends Variant> {
	public T getVariant();
}
