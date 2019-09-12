package jaminv.advancedmachines.machine.expansion.tank;

import javax.annotation.Nullable;

import jaminv.advancedmachines.AdvancedMachines;
import jaminv.advancedmachines.ModConfig;
import jaminv.advancedmachines.init.HasGui;
import jaminv.advancedmachines.lib.container.ContainerInventory;
import jaminv.advancedmachines.lib.container.layout.ILayoutManager;
import jaminv.advancedmachines.lib.container.layout.LayoutManager;
import jaminv.advancedmachines.lib.container.layout.impl.BucketLayout;
import jaminv.advancedmachines.lib.dialog.fluid.DialogBucketToggle;
import jaminv.advancedmachines.lib.fluid.BucketHandler;
import jaminv.advancedmachines.lib.fluid.FluidTankAdvanced;
import jaminv.advancedmachines.lib.fluid.IFluidObservable;
import jaminv.advancedmachines.lib.inventory.IItemObservable;
import jaminv.advancedmachines.lib.inventory.ItemStackHandlerObservable;
import jaminv.advancedmachines.lib.machine.IMachineController;
import jaminv.advancedmachines.lib.util.blocks.HasItemNBT;
import jaminv.advancedmachines.lib.util.helper.HasFacing;
import jaminv.advancedmachines.machine.dialog.DialogIOToggle;
import jaminv.advancedmachines.machine.expansion.TileMachineExpansion;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import jaminv.advancedmachines.util.network.BucketStateMessage;
import jaminv.advancedmachines.util.network.IOStateMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class TileMachineTank extends TileMachineExpansion implements ITickable, HasGui, HasFacing, HasItemNBT,
		IMachineController.ISubController, IItemObservable.IObserver, IFluidObservable.IObserver, DialogIOToggle.ISwitchableIO,
		DialogBucketToggle.Provider {
	
	public static final ILayoutManager layout = new LayoutManager()
		.addLayout(new BucketLayout(152, 59))
		.setInventoryLayout(8, 84)
		.setHotbarLayout(8, 142);	
	
	protected EnumFacing facing = EnumFacing.NORTH;
	protected boolean inputState = true;
	protected int priority = 0;
	protected IMachineController controller;
	protected BucketHandler bucketHandler = new BucketHandler().setCallback(state -> {
		if (controller != null) { controller.wake(); }		
		if (world.isRemote) {
			AdvancedMachines.NETWORK.sendToServer(new BucketStateMessage(this.getPos(), state));
		}
	});
	
	public BucketHandler getBucketToggle() { return bucketHandler; }
	
	protected ItemStackHandlerObservable inventory = new ItemStackHandlerObservable(1) {
		@Override protected int getStackLimit(int slot, ItemStack stack) {
			if (FluidUtil.getFluidHandler(stack) != null) {	return 1; }
			return super.getStackLimit(slot, stack);
		}
	};
	
	protected FluidTankAdvanced tank = new FluidTankAdvanced(ModConfig.general.defaultMachineFluidCapacity * VariantExpansion.maxMultiplier,
		ModConfig.general.defaultMachineFluidTransfer * VariantExpansion.maxMultiplier);
	public IFluidTank getTank() { return tank; }
	
	public TileMachineTank() {
		tank.addObserver(this);
		inventory.addObserver(this);
	}
	
	public boolean onBlockActivated(EntityPlayer player, EnumHand hand) {
		return bucketHandler.onBlockActivate(player, hand, tank);
	}
	
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
    }

    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }
    
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		handleUpdateTag(pkt.getNbtCompound());
	}	
	
	@Override
	public Container createContainer(IInventory playerInventory) {
		return new ContainerInventory(layout, inventory, playerInventory);
	}
	
	public void setFacing(EnumFacing facing) {
		this.facing = facing;
	}
	
	public EnumFacing getFacing() {
		return facing;
	}
	
	public boolean getInputState() {
		return inputState;
	}
	    
	@Override
	public void setVariant(VariantExpansion variant) {
		super.setVariant(variant);
		tank.setCapacity(ModConfig.general.defaultMachineFluidCapacity * this.getMultiplier());
	}
	
	public void setInputState(boolean state) {
		this.inputState = state;
		world.markBlockRangeForRenderUpdate(this.pos, this.pos);
		
		if (controller != null) { controller.wake(); }
		
		if (world.isRemote) {
			AdvancedMachines.NETWORK.sendToServer(new IOStateMessage(this.getPos(), state, priority));
		}
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
				
		if (world.isRemote) {
			AdvancedMachines.NETWORK.sendToServer(new IOStateMessage(this.getPos(), inputState, priority));
		}
		
		if (controller != null) { controller.sortSubControllers(); }
	}
	
	boolean allowInput = false;
	boolean allowOutput = false;
	
	@Override public boolean canInput() { return allowInput; }
	@Override public boolean canOutput() { return allowOutput; }
	@Override public boolean hasController() { return controller != null; }

	@Override
	public void setController(IMachineController controller) {
		this.controller = controller;
		if (controller != null) {
			// TODO: Machine Input/Output determination
			allowInput = true; //inv.canInsert();
			allowOutput = true; //inv.canExtract();
			
			if (allowInput && !allowOutput) { this.setInputState(true); }
			if (allowOutput && !allowInput) { this.setInputState(false); }
		} else {
			allowInput = false;
			allowOutput = false;
		}
	}
	
	@Override
	public int getPriority() {
		return priority;
	}
	
	boolean sleep = true;
	
	private int tick;
		
	@Override
	public void update() {
		if (sleep) { return; } // Return quickly if there's nothing to do

		tick++;
		if (tick < ModConfig.general.tickUpdate) { return; }
		tick = 0;
		
		if (!bucketHandler.handleBucket(inventory, 0, tank)) { sleep = false; }
	}
	
	@Override
	public boolean preProcess(IMachineController controller) {
		boolean didSomething = bucketHandler.handleBucket(inventory, 0, tank);
		
		if (inputState) {
			return moveInput(controller) > 0 || didSomething;
		} else {
			return moveOutput(controller) > 0 || didSomething;
		}
	}
	
	protected int moveInput(IMachineController controller) {
		FluidStack stack = tank.drain(Integer.MAX_VALUE, false);
		int amount = controller.getFluidTank().fill(stack, false);
		
		if (amount > 0) {
			stack = tank.drain(amount, true);
			controller.getFluidTank().fill(stack, true);
			return amount;
		}
		return 0;
	}
	
	protected int moveOutput(IMachineController controller) {
		FluidStack stack = controller.getFluidTank().drain(Integer.MAX_VALUE, false);
		int amount = tank.fill(stack, false);
		
		if (amount > 0) {
			stack = controller.getFluidTank().drain(amount, true);
			tank.fill(stack, true);
			return amount;
		}			
		return 0;
	}	
	
	@Override
	public void onInventoryContentsChanged(int slot) {
		sleep = false;
	}
	
	@Override
	public void onTankContentsChanged() {
		this.markDirty();
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
		world.markBlockRangeForRenderUpdate(pos, pos);
		
		sleep = false;
		if (controller != null) { controller.wake(); }
	}
	
	public boolean canInteractWith(EntityPlayer playerIn) {
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
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
		if (compound.hasKey("tank")) {
			tank.deserializeNBT(compound.getCompoundTag("tank"));
		}
		if (compound.hasKey("facing")) {
			facing = EnumFacing.byName(compound.getString("facing"));
		}
		inputState = compound.getBoolean("inputState");
		priority = compound.getInteger("priority");
		allowInput = compound.getBoolean("allowInput");
		allowOutput = compound.getBoolean("allowOutput");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("tank", tank.serializeNBT());
		compound.setString("facing", facing.getName());
		compound.setBoolean("inputState", inputState);
		compound.setInteger("priority", priority);
		compound.setBoolean("allowInput", allowInput);
		compound.setBoolean("allowOutput", allowOutput);		
		return compound;
	}

	@Override
	public void readItemNBT(NBTTagCompound compound) {
		if (compound.hasKey("tank")) {
			tank.deserializeNBT(compound.getCompoundTag("tank"));
		}	
	}

	@Override
	public NBTTagCompound writeItemNBT(NBTTagCompound compound) {
		compound.setTag("tank", tank.serializeNBT());
		return compound;
	}
}
