package jaminv.advancedmachines.objects.blocks.machine.multiblock;

import java.util.Collections;

import jaminv.advancedmachines.lib.fluid.FluidTankAdvanced;
import jaminv.advancedmachines.lib.fluid.IFluidHandlerTE;
import jaminv.advancedmachines.lib.inventory.ItemStackHandlerObservable;
import jaminv.advancedmachines.lib.recipe.IRecipeManager;
import jaminv.advancedmachines.lib.recipe.RecipeBase;
import jaminv.advancedmachines.lib.recipe.RecipeOutput;
import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgrade.UpgradeType;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.objects.material.MaterialExpansion;
import jaminv.advancedmachines.util.ModConfig;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public abstract class TileEntityMachineMultiblockFluid extends TileEntityMachineMultiblock implements FluidTankAdvanced.IObserver {
	
	public TileEntityMachineMultiblockFluid(IRecipeManager recipeManager) {
		super(recipeManager);
		
		int capacity = ModConfig.general.defaultMachineFluidCapacity * MaterialExpansion.maxMultiplier;
		
		for (int i = 0; i < getInputTankCount(); i++) {
			inputTank[i] = new FluidTankAdvanced(capacity);
			inputTank[i].addObserver(this);
		}
		for (int i = 0; i < getOutputTankCount(); i++) {
			outputTank[i] = new FluidTankAdvanced(capacity);
			outputTank[i].addObserver(this);
		}
	}
	
	

	@Override
	public void setMeta(int meta) {
		super.setMeta(meta);
		
		int capacity = ModConfig.general.defaultMachineFluidCapacity * getMultiplier();
		for (FluidTankAdvanced input : inputTank) {
			input.setCapacity(capacity);
		}
		for (FluidTankAdvanced output : outputTank) {
			output.setCapacity(capacity);
		}
	}

	@Override
	public void onTankContentsChanged() {
		this.markDirty();
		world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 3);
		world.markBlockRangeForRenderUpdate(pos, pos);		
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
		}
		return super.getCapability(capability, facing);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("fluid")) {
			tank.readFromNBT((NBTTagCompound)compound.getTag("fluid"));
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		
		NBTTagCompound fluid = new NBTTagCompound();
		tank.writeToNBT(fluid);
		compound.setTag("fluid", fluid);
		
		return compound;
	}
	
	/* Processing */
	@Override
	protected int getRecipeQty(RecipeBase recipe) {
		return recipe.getRecipeQty(getInput(), (FluidStack[])null, getOutput(), Collections.singletonList(this.getTank()));
	}

	protected boolean outputItem(RecipeOutput output, boolean simulate) {
		if (!output.isFluid()) { return super.outputItem(output, simulate); }
		
		FluidStack stack = output.toFluidStack();
		if (tank.fillInternal(stack, false) != stack.amount) { return false; }
		
		if (!simulate) { 
			tank.fillInternal(stack, true);
		}
		return true;
	}
}
