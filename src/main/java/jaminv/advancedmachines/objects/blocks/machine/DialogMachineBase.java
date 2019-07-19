package jaminv.advancedmachines.objects.blocks.machine;

import jaminv.advancedmachines.objects.blocks.inventory.DialogInventory;
import jaminv.advancedmachines.objects.blocks.inventory.DialogInventory.ContainerLayout;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogJeiEnergyBar;
import jaminv.advancedmachines.util.dialog.struct.DialogArea;
import jaminv.advancedmachines.util.dialog.struct.DialogTextureMap;
import jaminv.advancedmachines.util.recipe.IRecipeManager;

import java.awt.List;
import java.util.ArrayList;

import jaminv.advancedmachines.integration.jei.element.JeiElement;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public abstract class DialogMachineBase extends DialogInventory {
	
	public class InputLayout extends ContainerLayout {
		IRecipeManager recipe;
		
		public InputLayout(IRecipeManager recipe, int xpos, int ypos, int xspacing, int yspacing, int rows, int cols) {
			super(xpos, ypos, xspacing, yspacing, rows, cols);
			this.recipe = recipe;
		}
		public InputLayout(IRecipeManager recipe, int xpos, int ypos, int rows, int cols) {
			super(xpos, ypos, rows, cols);
			this.recipe = recipe;
		}
		public InputLayout(IRecipeManager recipe, int xpos, int ypos) {
			super(xpos, ypos);
			this.recipe = recipe;
		}
		
		public SlotItemHandler createSlot(IItemHandler itemHandler, int slotIndex, int x, int y) {
			return new ContainerMachine.SlotInput(recipe, itemHandler, slotIndex, x, y);
		}
	}
	
	public class OutputLayout extends ContainerLayout {
		public OutputLayout(int xpos, int ypos, int xspacing, int yspacing, int rows, int cols) {
			super(xpos, ypos, xspacing, yspacing, rows, cols);
		}
		public OutputLayout(int xpos, int ypos, int rows, int cols) {
			super(xpos, ypos, rows, cols);
		}
		public OutputLayout(int xpos, int ypos) {
			super(xpos, ypos);
		}		

		public SlotItemHandler createSlot(IItemHandler itemHandler, int slotIndex, int x, int y) {
			return new ContainerMachine.SlotOutput(itemHandler,slotIndex, x, y);
		}
	}
	
	public DialogMachineBase(String background, int xpos, int ypos, int width, int height) {
		super(background, xpos, ypos, width, height); 
	}
	
	public ResourceLocation getJeiBackground() { return this.getBackground(); }
	public abstract DialogArea getJeiTexture();
	public abstract DialogArea getJeiTarget();
	
	protected ArrayList<JeiElement> jeielement = new ArrayList<JeiElement>();
	protected void addJeiElement(JeiElement element) {
		jeielement.add(element);
	}
	public ArrayList<JeiElement> getJeiElements() { return jeielement; }
	public enum JeiDrawSecondaryChance { LEFT, ABOVE };
	private JeiDrawSecondaryChance chance_pos = JeiDrawSecondaryChance.LEFT;
	protected void setDrawSecondaryChancePos(JeiDrawSecondaryChance pos) { this.chance_pos = pos; }
	public JeiDrawSecondaryChance getDrawSecondaryChancePos() { return chance_pos; }
	
	public int getJeiAdjustX() { return getJeiTexture().getX() - getDialogArea().getX(); }
	public int getJeiAdjustY() { return getJeiTexture().getY() - getDialogArea().getY(); }
	
	protected ContainerLayout getJeiOffset(ContainerLayout texture) {
		DialogArea dialog = this.getDialogArea();
		DialogArea jei = this.getJeiTexture();
		
		return new ContainerLayout(
			texture.getXPos() + dialog.getX() - jei.getX() - 1,
			texture.getYPos() + dialog.getY() - jei.getY() - 1,
			texture.getXSpacing(), texture.getYSpacing(),
			texture.getRows(), texture.getCols()
		);		
	}
	
	public ContainerLayout getJeiInputLayout() {
		return getJeiOffset(this.getLayout(0));
	}
	
	public ContainerLayout getJeiOutputLayout() {
		return getJeiOffset(this.getLayout(1));
	}
	
	public ContainerLayout getJeiSecondaryLayout() {
		return getJeiOffset(this.getLayout(2));
	}
}
