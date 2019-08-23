package jaminv.advancedmachines.lib.container.layout;

import java.util.ArrayList;
import java.util.List;

import jaminv.advancedmachines.lib.container.layout.ItemLayoutGrid.HotbarLayout;
import jaminv.advancedmachines.lib.container.layout.ItemLayoutGrid.InventoryLayout;
import jaminv.advancedmachines.lib.container.layout.impl.InputLayout;
import jaminv.advancedmachines.lib.container.layout.impl.OutputLayout;
import jaminv.advancedmachines.lib.recipe.IRecipeManager;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.IItemHandler;

public class JeiLayoutManager implements IJeiLayoutManager {
	private IItemLayout itemInput, itemOutput, itemSecondary, itemAdditional;
	private List<IFluidLayout> fluidInput = new ArrayList<IFluidLayout>(), fluidOutput = new ArrayList<IFluidLayout>(),
		fluidSecondary = new ArrayList<IFluidLayout>(), fluidAdditional = new ArrayList<IFluidLayout>();
	private IItemLayout inventoryLayout, hotbarLayout;
	
	@Override public int getInventorySlots() { 
		return (itemInput != null ? itemInput.getCount() : 0) 
			+ (itemOutput != null ? itemOutput.getCount() : 0)
			+ (itemSecondary != null ? itemSecondary.getCount() : 0)
			+ (itemAdditional != null ? itemAdditional.getCount() : 0); 
	}
	@Override public int getPlayerSlots() {
		return (inventoryLayout != null ? inventoryLayout.getCount() : 0)
			+ (hotbarLayout != null ? hotbarLayout.getCount() : 0);
	}
	
	public JeiLayoutManager setItemInputLayout(IItemLayout layout) {
		itemInput = layout; return this;
	}
	public JeiLayoutManager setItemInputLayout(IRecipeManager recipe, int x, int y) {
		return this.setItemInputLayout(new InputLayout(recipe, x, y));		
	}
	public JeiLayoutManager setItemInputLayout(IRecipeManager recipe, int x, int y, int rows, int cols) {
		return this.setItemInputLayout(new InputLayout(recipe, x, y, rows, cols));
	}
	
	public JeiLayoutManager addFluidInputLayout(IFluidLayout layout) {
		fluidInput.add(layout); return this;
	}
	public JeiLayoutManager addFluidInputLayout(int x, int y, int w, int h) {
		return this.addFluidInputLayout(new FluidLayout(x, y, w, h));
	}
	
	public JeiLayoutManager setItemOutputLayout(IItemLayout layout) {
		itemOutput = layout; return this;
	}
	public JeiLayoutManager setItemOutputLayout(int x, int y) {
		return this.setItemOutputLayout(new OutputLayout(x, y));
	}
	public JeiLayoutManager setItemOutputLayout(int x, int y, int rows, int cols) {
		return this.setItemOutputLayout(new OutputLayout(x, y, rows, cols));
	}
	
	public JeiLayoutManager addFluidOutputLayout(IFluidLayout layout) {
		fluidOutput.add(layout); return this;
	}
	public JeiLayoutManager addFluidOutputLayout(int x, int y, int w, int h) {
		return this.addFluidOutputLayout(new FluidLayout(x, y, w, h));
	}
	
	public JeiLayoutManager setItemSecondaryLayout(IItemLayout layout) {
		itemSecondary = layout; return this;
	}
	public JeiLayoutManager setItemSecondaryLayout(int x, int y) {
		return this.setItemSecondaryLayout(new OutputLayout(x, y));
	}
	public JeiLayoutManager setItemSecondaryLayout(int x, int y, int rows, int cols) {
		return this.setItemSecondaryLayout(new OutputLayout(x, y, rows, cols));
	}
	
	public JeiLayoutManager addFluidSecondaryLayout(IFluidLayout layout) {
		fluidSecondary.add(layout); return this;
	}
	public JeiLayoutManager addFluidSecondaryLayout(int x, int y, int w, int h) {
		return this.addFluidSecondaryLayout(new FluidLayout(x, y, w, h));
	}
	
	public JeiLayoutManager setItemAdditionalLayout(IItemLayout layout) {
		itemAdditional = layout; return this;
	}
	
	public JeiLayoutManager addFluidAdditionalLayout(IFluidLayout layout) {
		fluidAdditional.add(layout); return this;
	}
	public JeiLayoutManager addFluidAdditonalLayout(int x, int y, int w, int h) {
		return this.addFluidAdditionalLayout(new FluidLayout(x, y, w, h));
	}
	
	@Override public IItemLayout getItemInputLayout() { return itemInput; }
	@Override public IItemLayout getItemOutputLayout() { return itemOutput; }
	@Override public IItemLayout getItemSecondaryLayout() { return itemSecondary; } 
	@Override public List<IFluidLayout> getFluidInputLayout() { return fluidInput; }
	@Override public List<IFluidLayout> getFluidOutputLayout() { return fluidOutput; }
	@Override public List<IFluidLayout> getFluidSecondaryLayout() { return fluidSecondary; }
	
	public JeiLayoutManager setInventoryLayout(ItemLayoutGrid layout) { this.inventoryLayout = layout; return this; }
	public JeiLayoutManager setHotbarLayout(ItemLayoutGrid layout) { this.hotbarLayout = layout; return this; }
	public JeiLayoutManager setInventoryLayout(int xpos, int ypos) { this.inventoryLayout = new InventoryLayout(xpos, ypos); return this; }
	public JeiLayoutManager setHotbarLayout(int xpos, int ypos) { this.hotbarLayout = new HotbarLayout(xpos, ypos); return this; }
	
	public IItemLayout getInventoryLayout() { return this.inventoryLayout; } 
	public IItemLayout getHotbarLayout() { return this.hotbarLayout; }	
	
	@Override
	public void addInventorySlots(ILayoutUser container, IItemHandler inventory) {
		int slotIndex = 0;
		slotIndex = LayoutHelper.addSlots(container, inventory, itemInput, slotIndex);
		slotIndex = LayoutHelper.addSlots(container, inventory, itemOutput, slotIndex);
		slotIndex = LayoutHelper.addSlots(container, inventory, itemSecondary, slotIndex);
		LayoutHelper.addSlots(container, inventory, itemAdditional, slotIndex);
	}
	
	@Override
	public void addPlayerSlots(ILayoutUser container, IInventory playerInventory) {
		LayoutHelper.addPlayerSlots(container, inventoryLayout, hotbarLayout, playerInventory);
	}
}
