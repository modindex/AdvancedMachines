package jaminv.advancedmachines.util.dialog;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.util.dialog.container.EmptyContainer;
import jaminv.advancedmachines.util.dialog.container.IContainerUpdate;
import jaminv.advancedmachines.util.dialog.gui.GuiContainerObservable;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

abstract public class DialogModal extends DialogBase {

	protected GuiContainerObservable gui;
	protected IContainerUpdate container;
	
	public DialogModal(String background, int xpos, int ypos, int width, int height, IContainerUpdate container) {
		super(background, xpos, ypos, width, height);
		
		this.container = container;		
		this.gui = new GuiContainerObservable(new EmptyContainer(container), width, height);
	}
	
	protected IModalObserver observer;
	
	abstract int getGuid();
	
	public void render(IModalObserver observer) {
		this.observer = observer;
		
		Minecraft mc = Minecraft.getMinecraft();
		
		if (!mc.world.isRemote) {
			mc.displayGuiScreen(this.gui);
		}
	}
}
