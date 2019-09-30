package jaminv.advancedmachines.objects.blocks.tank;

import jaminv.advancedmachines.ModConfig;
import jaminv.advancedmachines.lib.fluid.FluidCapabilityProvider;
import jaminv.advancedmachines.lib.fluid.FluidContainerItem;
import jaminv.advancedmachines.lib.fluid.FluidTankDefault;
import jaminv.advancedmachines.lib.fluid.FluidTankProperties;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class ItemBlockTank extends ItemBlock implements FluidContainerItem {

	public ItemBlockTank(Block block) {
		super(block);
		this.setMaxStackSize(1);
	}
	
	protected FluidTankDefault getTank(ItemStack stack) { 
		FluidTankDefault tank = new FluidTankDefault(ModConfig.general.defaultMachineFluidCapacity, ModConfig.general.defaultMachineFluidTransfer);
		if (stack != null && stack.hasTagCompound()) {
			tank.deserializeNBT(stack.getTagCompound().getCompoundTag("tank"));
		}
		return tank;
	}
	
	protected void setTag(ItemStack stack, FluidTankDefault tank) {
		if (tank.isEmpty()) { stack.setTagCompound(null); return; }
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("tank", tank.serializeNBT());
		stack.setTagCompound(nbt);
		return;
	}
	
	@Override
	public IFluidTankProperties[] getTankProperties(ItemStack stack) {
		return new IFluidTankProperties[] { new FluidTankProperties(getTank(stack)) };
	}

	@Override
	public int fill(ItemStack stack, FluidStack resource, boolean doFill) {
		FluidTankDefault tank = getTank(stack);
		int ret = tank.fill(resource, doFill);
		if (doFill) { setTag(stack, tank); }
		return ret;
	}
	
	@Override
	public FluidStack drain(ItemStack stack, FluidStack resource, boolean doDrain) {
		FluidTankDefault tank = getTank(stack);
		FluidStack ret = tank.drain(resource, doDrain);
		if (doDrain) { setTag(stack, tank); }
		return ret;
	}

	@Override
	public FluidStack drain(ItemStack stack, int maxDrain, boolean doDrain) {
		FluidTankDefault tank = getTank(stack);
		FluidStack ret = tank.drain(maxDrain, doDrain);
		if (doDrain) { setTag(stack, tank); }
		return ret;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new FluidCapabilityProvider(stack, this);
	}
}
