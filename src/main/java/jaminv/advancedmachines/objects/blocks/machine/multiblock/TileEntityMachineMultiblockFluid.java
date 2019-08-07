package jaminv.advancedmachines.objects.blocks.machine.multiblock;

import jaminv.advancedmachines.objects.blocks.fluid.FluidTankObservable;
import jaminv.advancedmachines.objects.blocks.fluid.IFluidHandlerTE;
import jaminv.advancedmachines.objects.blocks.inventory.ItemStackHandlerObservable;
import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgrade.UpgradeType;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.objects.material.MaterialExpansion;
import jaminv.advancedmachines.util.ModConfig;
import jaminv.advancedmachines.util.recipe.IRecipeManager;
import jaminv.advancedmachines.util.recipe.RecipeBase;
import jaminv.advancedmachines.util.recipe.RecipeOutput;
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

public abstract class TileEntityMachineMultiblockFluid extends TileEntityMachineMultiblock implements IFluidHandlerTE, FluidTankObservable.IObserver {
	
	public TileEntityMachineMultiblockFluid(IRecipeManager recipeManager) {
		super(recipeManager);

		tank = new FluidTankObservable(ModConfig.general.defaultMachineFluidCapacity * MaterialExpansion.maxMultiplier);
		tank.addObserver(this);
	}

	private FluidTankObservable tank;
	public FluidTankObservable getTank() { return tank; }
	
	@Override
	public void setMeta(int meta) {
		super.setMeta(meta);
		this.getTank().setCapacity(ModConfig.general.defaultMachineFluidCapacity * getMultiplier());
	}

	@Override
	public void onTankContentsChanged() {
		this.markDirty();
		world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 3);
		world.markBlockRangeForRenderUpdate(pos, pos);		
	}

	public FluidStack getFluid() { return tank.getFluid(); }
	public int getFluidAmount() { return tank.getFluidAmount(); }
	public int getFluidCapacity() { return tank.getCapacity(); }

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
	protected int beginProcess(RecipeBase recipe, ItemStack[] input) {
		IRecipeManager mgr = getRecipeManager();
		
		// TODO: Fluid input
		int maxinput = mgr.getRecipeQty(recipe, input);
		
		int maxoutput = 0;
		if (recipe.getOutput(0).isFluid()) {
			maxoutput = mgr.getOutputQty(recipe, this.getTank());
		} else {
			mgr.getOutputQty(recipe, this.getOutputStacks());
		}

		qtyProcessing = Math.min(Math.min(maxinput, maxoutput), upgrades.get(UpgradeType.MULTIPLY));

		return ModConfig.general.processTimeBasic;
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
