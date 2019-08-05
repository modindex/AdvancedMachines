package jaminv.advancedmachines.objects.blocks.machine.expansion.tank;

import javax.annotation.Nullable;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.objects.blocks.fluid.FluidTankObservable;
import jaminv.advancedmachines.objects.blocks.fluid.TileEntityFluid;
import jaminv.advancedmachines.objects.blocks.inventory.ContainerInventory;
import jaminv.advancedmachines.objects.blocks.machine.expansion.BlockMachineExpansionBase;
import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgradeTool;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.TileEntityMachineMultiblock;
import jaminv.advancedmachines.objects.material.MaterialExpansion;
import jaminv.advancedmachines.util.ModConfig;
import jaminv.advancedmachines.util.interfaces.IDirectional;
import jaminv.advancedmachines.util.interfaces.IHasGui;
import jaminv.advancedmachines.util.interfaces.IHasMetadata;
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
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;

public class TileEntityMachineTank extends TileEntityFluid implements ITickable, IHasGui, IMachineUpgradeTool, IHasMetadata, IDirectional, ISwitchableIO {
	
	protected EnumFacing facing = EnumFacing.NORTH;
	protected boolean inputState = true;
	protected int priority = 0;
	protected MultiblockBorders borders = new MultiblockBorders();
	protected BlockPos parent = null;
	
	@Override
	public ContainerInventory createContainer(IInventory inventory) {
		return new ContainerInventory(inventory, DialogMachineTank.layout, this);
	}
	
	@Override
	public GuiContainer createGui(IInventory inventory) {
		return new DialogMachineTank(createContainer(inventory), this);
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
	
	private MaterialExpansion material;
	    
	@Override
	public void setMeta(int meta) {
		material = MaterialExpansion.byMetadata(meta);
		this.getTank().setCapacity(ModConfig.general.defaultMachineFluidCapacity * material.getMultiplier());
	}

	@Override
	public FluidTankObservable createTank() { return new FluidTankObservable(ModConfig.general.defaultMachineFluidCapacity * MaterialExpansion.maxMultiplier); }
	
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
	
	boolean doNothing = true;
	
	private int tick;
		
	@Override
	public void update() {
		if (doNothing) { return; }		// Return quickly if there's nothing to do

		tick++;
		if (tick < ModConfig.general.tickUpdate) { return; }
		tick = 0;
		
		boolean didSomething = false;
		
		for (int i = 0; i < this.getInventorySize(); i++) {
			ItemStack stack = this.getInventory().getStackInSlot(i);
			if (stack == null) { continue; }

			FluidActionResult result = null;
			if (i == 0) { 
				result = FluidUtil.tryEmptyContainer(stack, this.getTank(), 1000, null, true);
			} else if (i == 1) {
				result = FluidUtil.tryFillContainer(stack, this.getTank(), 1000, null, true);
			}
			
			if (result != null && result.success) {
				this.getInventory().extractItem(i, stack.getCount(), false);
				this.getInventory().insertItem(i, result.getResult(), false);
				didSomething = true;
			}
		}
		
		//Fluid f = this.getTank().getFluid().getFluid();
		//String name = FluidRegistry.getFluidName(f);
		//String name2 = FluidRegistry.getFluidName(this.getTank().getFluid());
		
		if (!didSomething) { doNothing = true; }
	}
	
	@Override
	public boolean tickUpdate(TileEntityMachineMultiblock te) {
		boolean didSomething = false;
		
		if (inputState) {
			//moveInput(te);
		} else {
			//moveOutput(te);
		}
		
		return didSomething;
	}
	
	@Override
	public void onInventoryContentsChanged(int slot) {
		super.onInventoryContentsChanged(slot);
		doNothing = false;

		if (parent == null) { return; }
		TileEntity te = world.getTileEntity(parent);
		if (te instanceof TileEntityMachineMultiblock) {
			((TileEntityMachineMultiblock)te).doSomething();
		}		
	}
	
	@Override
	public void onTankContentsChanged() {
		super.onTankContentsChanged();
		doNothing = false;
		
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
		world.markBlockRangeForRenderUpdate(pos, pos);
		
		if (parent == null) { return; }
		TileEntity te = world.getTileEntity(parent);
		if (te instanceof TileEntityMachineMultiblock) {
			((TileEntityMachineMultiblock)te).doSomething();
		}		
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

	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("meta")) {
			setMeta(compound.getInteger("meta"));
		}
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
		compound.setInteger("meta", material.getMeta());
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
