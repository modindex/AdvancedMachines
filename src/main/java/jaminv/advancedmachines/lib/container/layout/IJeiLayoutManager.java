package jaminv.advancedmachines.lib.container.layout;

import java.util.List;

/**
 * JEI Layout Manager
 * 
 * Layout Manager that requires layouts broken out into sections.
 * 
 * The current implementation uses a single `IItemLayout` for each section.
 * This puts the onus on `IItemLayout` implementations if anything other than 
 * a simple grid is required.
 * 
 * Conversely, there can be multiple fluid layouts, as each describes a single
 * ingredient.
 * 
 * @author jamin
 *
 */
public interface IJeiLayoutManager extends ILayoutManager {
	IItemLayout getItemInputLayout();
	IItemLayout getItemOutputLayout();
	IItemLayout getItemSecondaryLayout();
	List<IFluidLayout> getFluidInputLayout();
	List<IFluidLayout> getFluidOutputLayout();
	List<IFluidLayout> getFluidSecondaryLayout();
}
