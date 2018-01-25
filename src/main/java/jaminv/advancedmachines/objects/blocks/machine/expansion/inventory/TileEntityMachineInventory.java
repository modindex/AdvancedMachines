package jaminv.advancedmachines.objects.blocks.machine.expansion.inventory;

import jaminv.advancedmachines.objects.blocks.inventory.TileEntityInventory;
import jaminv.advancedmachines.objects.blocks.machine.MachineEnergyStorage;
import jaminv.advancedmachines.objects.items.ItemStackHandlerObservable;
import jaminv.advancedmachines.util.interfaces.IHasGui;
import jaminv.advancedmachines.util.recipe.RecipeInput;
import jaminv.advancedmachines.util.recipe.RecipeOutput;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileEntityMachineInventory extends TileEntityInventory implements IHasGui {
	
	public final int SIZE = 27;
	@Override
	public int getInventorySize() {
		return SIZE;
	}
	
	@Override
	public Container createContainer(IInventory inventory) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public GuiContainer createGui(IInventory inventory) {
		// TODO Auto-generated method stub
		return null;
	}
}
