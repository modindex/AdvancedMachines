package jaminv.advancedmachines.objects.blocks.machine.expansion.tank;

import javax.annotation.Nullable;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.objects.blocks.fluid.TileEntityFluid;
import jaminv.advancedmachines.objects.blocks.inventory.ContainerInventory;
import jaminv.advancedmachines.objects.blocks.machine.expansion.BlockMachineExpansionBase;
import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgradeTool;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.TileEntityMachineMultiblock;
import jaminv.advancedmachines.util.dialog.gui.GuiContainerObservable;
import jaminv.advancedmachines.util.interfaces.IDirectional;
import jaminv.advancedmachines.util.interfaces.IHasGui;
import jaminv.advancedmachines.util.interfaces.ISwitchableIO;
import jaminv.advancedmachines.util.message.IOStateMessage;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;

public class TileEntityMachineTank extends TileEntityFluid implements IHasGui, IMachineUpgradeTool, IDirectional, ISwitchableIO {
	
	protected EnumFacing facing = EnumFacing.NORTH;
	protected boolean inputState = true;
	protected int priority = 0;
	protected MultiblockBorders borders = new MultiblockBorders();
	protected BlockPos parent = null;
	
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
	public int getCapacity() {
		return 4000;
	}
	
	public void setInputState(boolean state) {
		this.inputState = state;
		BlockMachineExpansionBase.scanMultiblock(world, this.getPos());
		world.markBlockRangeForRenderUpdate(this.pos, this.pos);
		
		if (world.isRemote) {
			Main.NETWORK.sendToServer(new IOStateMessage(this.getPos(), state, priority));
		}
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
				
		if (world.isRemote) {
			Main.NETWORK.sendToServer(new IOStateMessage(this.getPos(), inputState, priority));
		}
		
		if (parent == null) { return; }
		TileEntity te = world.getTileEntity(parent);
		if (te instanceof TileEntityMachineMultiblock) {
			((TileEntityMachineMultiblock)te).sortTools();
		}
	}
	
	@Override
	public void setParent(BlockPos pos) {
		parent = pos;		
	}	
	
	@Override
	public int getPriority() {
		return priority;
	}
	
	@Override
	public void tickUpdate(TileEntityMachineMultiblock te) {
		if (inputState) {
			//moveInput(te);
		} else {
			//moveOutput(te);
		}
	}
	
	boolean inChangedEvent = false; // Prevent inventory changes in below event from triggering more events
	
	@Override
	public void onContentsChanged(int slot) {
		if (inChangedEvent) { return; }
		inChangedEvent = true;

		for (int i = 0; i <= 1; i++) {
			ItemStack stack = this.getInventory().getStackInSlot(slot);
			if (stack == null) { break; }

			FluidActionResult result = null;
			if (slot == 0) { 
				result = FluidUtil.tryEmptyContainer(stack, this.getTank(), this.getCapacity(), null, true);
			} else if (slot == 1) {
				result = FluidUtil.tryFillContainer(stack, this.getTank(), this.getCapacity(), null, true);
			}
			
			if (result != null && result.success) {
				this.getInventory().extractItem(slot, stack.getCount(), false);
				this.getInventory().insertItem(slot, result.getResult(), false);
			} else { break; } // Prevent infinite loop
			
			slot = (slot == 0) ? 1 : 0;
		}
		
		super.onContentsChanged(slot);
		inChangedEvent = false;
	}
	
	
	public void setBorders(World world, MultiblockBorders borders) {
		this.borders = borders;
	}
	
	public MultiblockBorders getBorders() {
		return borders; 
	}	
	
	@Override
	public int getInventorySize() {
		return 2;
	}
	
	private final DialogMachineTank dialog = new DialogMachineTank(this);
	
	public class GuiMachineTank extends GuiContainerObservable {
		public GuiMachineTank(ContainerInventory container, DialogMachineTank dialog) {
			super(container, dialog.getW(), dialog.getH());
			this.addObserver(dialog);
		}
	}
	
	@Override
	public ContainerInventory createContainer(IInventory inventory) {
		return new ContainerInventory(inventory, this, dialog);
	}
	
	@Override
	public GuiContainer createGui(IInventory inventory) {
		return new GuiMachineTank(createContainer(inventory), dialog);
	}
	
	
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("facing")) {
			facing = EnumFacing.byName(compound.getString("facing"));
		}
		if (compound.hasKey("inputState")) {
			inputState = compound.getBoolean("inputState");
		}
		if (compound.hasKey("priority")) {
			priority = compound.getInteger("priority");
		}
		if (compound.hasKey("borders")) {
			borders.deserializeNBT(compound.getCompoundTag("borders"));
		}
		if (compound.hasKey("parent")) {
			parent = NBTUtil.getPosFromTag(compound.getCompoundTag("parent"));
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("facing", facing.getName());
		compound.setBoolean("inputState", inputState);
		compound.setInteger("priority", priority);
		compound.setTag("borders",  borders.serializeNBT());	
		if (parent != null) {
			compound.setTag("parent", NBTUtil.createPosTag(parent));
		}
		return compound;
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
}
