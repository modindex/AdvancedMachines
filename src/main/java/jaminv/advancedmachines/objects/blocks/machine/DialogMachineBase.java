package jaminv.advancedmachines.objects.blocks.machine;

import jaminv.advancedmachines.objects.blocks.inventory.DialogInventory;
import jaminv.advancedmachines.util.dialog.struct.DialogArea;
import net.minecraft.util.ResourceLocation;

public abstract class DialogMachineBase extends DialogInventory {
	
	public DialogMachineBase(String background, int xpos, int ypos, int width, int height) {
		super(background, xpos, ypos, width, height);
	}
	
	public ResourceLocation getJeiBackground() { return this.getBackground(); }
	public abstract DialogArea getJeiTexture();
	public abstract DialogArea getJeiTarget();
	
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
