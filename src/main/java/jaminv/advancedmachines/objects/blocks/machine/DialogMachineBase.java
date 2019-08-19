package jaminv.advancedmachines.objects.blocks.machine;

import java.util.ArrayList;

import jaminv.advancedmachines.integration.jei.element.JeiElement;
import jaminv.advancedmachines.lib.container.ContainerMachine;
import jaminv.advancedmachines.lib.container.layout.ILayout;
import jaminv.advancedmachines.lib.container.layout.Layout;
import jaminv.advancedmachines.lib.container.layout.LayoutManager;
import jaminv.advancedmachines.util.dialog.DialogBase;
import jaminv.advancedmachines.util.dialog.struct.DialogArea;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public abstract class DialogMachineBase extends DialogBase {
	
	private ContainerMachine container;
	protected ContainerMachine getContainer() { return container; }
	
	public DialogMachineBase(Container container, String background, int xpos, int ypos, int width, int height) {
		super(container, background, xpos, ypos, width, height); 
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
	
	protected Layout getJeiOffset(ILayout iLayout) {
		DialogArea dialog = this.getDialogArea();
		DialogArea jei = this.getJeiTexture();
		
		return new Layout(
			iLayout.getXPos() + dialog.getX() - jei.getX() - 1,
			iLayout.getYPos() + dialog.getY() - jei.getY() - 1,
			iLayout.getXSpacing(), iLayout.getYSpacing(),
			iLayout.getRows(), iLayout.getCols()
		);		
	}
	
	protected abstract LayoutManager getLayout();
	
	public Layout getJeiInputLayout() {
		return getJeiOffset(getLayout().getLayout(0));
	}
	
	public Layout getJeiOutputLayout() {
		return getJeiOffset(getLayout().getLayout(1));
	}
	
	public Layout getJeiSecondaryLayout() {
		return getJeiOffset(getLayout().getLayout(2));
	}
}
