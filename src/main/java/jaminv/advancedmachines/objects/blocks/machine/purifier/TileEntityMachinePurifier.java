package jaminv.advancedmachines.objects.blocks.machine.purifier;

import org.apache.logging.log4j.Level;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachineBase;
import jaminv.advancedmachines.util.Config;
import jaminv.advancedmachines.util.recipe.IRecipeManager;
import jaminv.advancedmachines.util.recipe.RecipeInput;
import jaminv.advancedmachines.util.recipe.machine.PurifierManager;
import jaminv.advancedmachines.util.recipe.machine.PurifierManager.PurifierRecipe;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileEntityMachinePurifier extends TileEntityMachineBase {

	@Override
	public int getInputCount() { return 1; }
	
	@Override
	public int getOutputCount() { return 1;	}
	
	@Override
	public int getSecondaryCount() { return 9; }
	
	public TileEntityMachinePurifier() {
		super(PurifierManager.getRecipeManager());
	}

	@Override
	public Class<? extends Container> getContainerClass() {
		return ContainerMachinePurifier.class;
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return GuiMachinePurifier.class;
	}


}
