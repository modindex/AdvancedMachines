package jaminv.advancedmachines.objects.blocks.machine.instance.melter;

import org.apache.logging.log4j.Level;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.lib.recipe.IRecipeManager;
import jaminv.advancedmachines.lib.recipe.RecipeBase;
import jaminv.advancedmachines.objects.blocks.machine.ContainerMachine;
import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgrade.UpgradeType;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.TileEntityMachineMultiblock;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.TileEntityMachineMultiblockFluid;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.util.ModConfig;
import jaminv.advancedmachines.util.recipe.machine.grinder.GrinderManager;
import jaminv.advancedmachines.util.recipe.machine.melter.MelterManager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;

public class TileEntityMachineMelter extends TileEntityMachineMultiblockFluid {

	@Override
	public int getInputCount() { return 1; }
	
	@Override
	public int getOutputCount() { return 1;	}
	
	@Override 
	public int getSecondaryCount() { return 0; }

	public TileEntityMachineMelter() {
		super(MelterManager.getRecipeManager());
		this.getTank().setCanFill(false);
		this.getInventory().setCanExtract(false);
	}

	@Override
	public MachineType getMachineType() {
		return MachineType.MELTER;
	}

	@Override
	public ContainerMachine createContainer(IInventory inventory) {
		return new ContainerMachine(inventory, DialogMachineMelter.layout, this, MelterManager.getRecipeManager());
	}
	
	@Override
	public GuiContainer createGui(IInventory inventory) {
		return new DialogMachineMelter(createContainer(inventory), this);
	}

	@Override
	protected boolean preProcess() {
		boolean didSomething = super.preProcess();
		
		int i = this.getFirstOutputSlot();
		ItemStack stack = this.getInventory().getStackInSlot(i);
		if (stack != null) {
			FluidActionResult result = null;
			result = FluidUtil.tryFillContainer(stack, this.getTank(), 1000, null, true);
			
			if (result != null && result.success) {
				this.getInventory().extractItem(i, stack.getCount(), false);
				this.getInventory().insertItem(i, result.getResult(), false);
				didSomething = true;
			}
		}
		
		return didSomething;
	}
}
