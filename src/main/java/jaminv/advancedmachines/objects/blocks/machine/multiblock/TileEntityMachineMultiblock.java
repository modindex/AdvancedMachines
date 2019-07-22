package jaminv.advancedmachines.objects.blocks.machine.multiblock;

import org.apache.logging.log4j.Level;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgrade;
import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgrade.UpgradeType;
import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgradeTileEntity;
import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgradeTool;
import jaminv.advancedmachines.objects.blocks.machine.expansion.TileEntityMachineExpansionBase;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.MultiblockState.MultiblockNull;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.ICanHaveMachineFace;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.MachineFace;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.MachineParent;
import jaminv.advancedmachines.util.ModConfig;
import jaminv.advancedmachines.util.helper.BlockHelper;
import jaminv.advancedmachines.util.helper.BlockHelper.BlockChecker;
import jaminv.advancedmachines.util.helper.BlockHelper.ScanResult;
import jaminv.advancedmachines.util.recipe.IRecipeManager;
import jaminv.advancedmachines.util.recipe.RecipeBase;
import jaminv.advancedmachines.util.recipe.RecipeInput;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;

public abstract class TileEntityMachineMultiblock extends TileEntityMachineBase implements IMachineUpgradeTileEntity, ICanHaveMachineFace {

	public TileEntityMachineMultiblock(IRecipeManager recipeManager) {
		super(recipeManager);
	}
	
	protected MultiblockBorders borders = new MultiblockBorders();
	
	public void setBorders(MultiblockBorders borders) {
		this.borders = borders;
	}
	
	public MultiblockBorders getBorders() {
		return borders; 
	}
	
	public abstract MachineParent getMachineType();
	
	protected MachineFace face = MachineFace.NONE;

	@Override
	public void setMachineFace(MachineFace face, MachineParent parent, EnumFacing facing) {
		this.face = face;		
	}
	
	public MachineFace getMachineFace() { return face; }

	protected MultiblockState multiblockState = new MultiblockNull();
	public String getMultiblockString() { 
		if (multiblockState instanceof MultiblockNull) {
			scanMultiblock();
		}
		return multiblockState.toString(); 
	}
	
	protected UpgradeManager upgrades = new UpgradeManager();
	protected BlockPos multiblockMin = null, multiblockMax = null;

	public static class MultiblockChecker extends BlockChecker {
		@Override
		public Action checkBlock(World world, BlockPos pos) {
			Block block = world.getBlockState(pos).getBlock();
			if (block instanceof BlockMachineMultiblock) { return Action.END; }
			if (block instanceof IMachineUpgrade) { return Action.SCAN; }
			return Action.SKIP;
		}
	}	
	
	public void scanMultiblock() {
		scanMultiblock(false);
	}
	
	protected void reset() {
		if (multiblockMin != null && multiblockMax != null) {
			setMultiblock(multiblockMin, multiblockMax, false, world, this.getPos());
			world.markBlockRangeForRenderUpdate(multiblockMin, multiblockMax);
			
			multiblockMin = null; multiblockMax = null;
		}
		
		this.upgrades.reset();
	}
	
	public void scanMultiblock(boolean destroy) { 
		if (destroy) {
			Main.NETWORK.sendToAll(new MultiblockUpdateMessage(this.getPos(), multiblockMin, multiblockMax));
		}
		
		this.reset();		
		
		if (destroy) { return; }
		
		BlockPos pos = this.getPos();
		
		ScanResult result = BlockHelper.scanBlocks(world, pos, new MultiblockChecker());
		
		BlockPos end = result.getEnd();
		if (end != null) {
			this.multiblockState = new MultiblockState.MultiblockIllegal("message.multiblock.connected_machine", BlockHelper.getBlockName(world, end), end);
			return;
		}
		
		BlockPos min = result.getMin(), max = result.getMax();
		
		if (pos.equals(min) && pos.equals(max)) {
			this.multiblockState = new MultiblockState.MultiblockSimple("message.multiblock.absent");
			return;
		}
		
		MutableBlockPos check = new MutableBlockPos();
		for (int x = min.getX(); x <= max.getX(); x++) {
			for (int y = min.getY(); y <= max.getY(); y++) {
				for (int z = min.getZ(); z <= max.getZ(); z++) {
					check.setPos(x, y, z);
					if (pos.equals(check)) { continue; }
					Block block = world.getBlockState(check).getBlock();
					if (block instanceof IMachineUpgrade) {
						IMachineUpgrade upgrade = (IMachineUpgrade)block;
						this.upgrades.add(upgrade.getUpgradeType(), upgrade.getUpgradeQty(world, check));

						TileEntity te = world.getTileEntity(check);
						if (te instanceof IMachineUpgradeTool) {
							((IMachineUpgradeTool)te).setParent(pos);
							upgrades.addTool(check, world);
						}
					} else {
						this.multiblockState = new MultiblockState.MultiblockIllegal("message.multiblock.illegal", block.getLocalizedName(), check.toImmutable());
						this.upgrades.reset();
						return;
					} 
				}
			}
		}
		
		this.multiblockState = new MultiblockState.MultiblockComplete(this.upgrades);
		multiblockMin = min; multiblockMax = max;
		
		// Tell all the upgrades that they are now part of a multiblock
		setMultiblock(min, max, true, world, this.getPos());
		
		scanFace();
		
		world.markBlockRangeForRenderUpdate(min, max);
		return;
	}
	
	protected int qtyProcessing = 0;
	public int getQtyProcessing() { return qtyProcessing; }
	
	@Override
	protected void tickUpdate() {
		super.tickUpdate();
		
		BlockPos[] tools = upgrades.getTools();
		for (BlockPos tool : tools) {
			TileEntity te = world.getTileEntity(tool);
			if (te instanceof IMachineUpgradeTool) {
				((IMachineUpgradeTool)te).tickUpdate(this);
			}
		}
	}
	
	public void sortTools() {
		upgrades.sortTools(world);
	}
	
	public void setRedstone(boolean redstone) {
		if (redstone) {
			this.redstone = true;
		}
	}
		
	@Override
	protected int beginProcess(RecipeBase recipe, RecipeInput[] input) {
		IRecipeManager mgr = getRecipeManager();
		qtyProcessing = Math.min(Math.min(mgr.getRecipeQty(recipe, input), mgr.getOutputQty(recipe, this.getOutputStacks())), upgrades.get(UpgradeType.MULTIPLY) + 1) ;
		return ModConfig.general.processTimeBasic;
	}
	
	@Override
	protected void endProcess(RecipeBase recipe) {
		for (int i = 0; i < recipe.getInputCount(); i++) {
			if (recipe.getInput(i).isEmpty()) { continue; }
			if (!removeInput(recipe.getInput(i).multiply(qtyProcessing))) {
				// Some kind of strange error
				Main.logger.log(Level.ERROR,  "error.machine.process.cannot_input");
				haltProcess();
				return;
			}
		}
		
		for (int i = 0; i < recipe.getOutputCount(); i++) {
			if (recipe.getOutput(i).isEmpty()) { continue; }
			if (!outputItem(recipe.getOutput(i).multiply(qtyProcessing), false)) {
				// Some kind of strange error
				Main.logger.log(Level.ERROR, "error.machine.process.cannot_output");
				haltProcess();
				return;
			}
		}
		
		for (int i = 0; i < qtyProcessing; i++) {
			outputSecondary(recipe.getSecondary());
		}
	}
	
	@Override
	protected void haltProcess() {
		super.haltProcess();
		qtyProcessing = 0;
	}
	
	public static void setMultiblock(BlockPos min, BlockPos max, boolean isMultiblock, World world, BlockPos pos) {
		MutableBlockPos upgrade = new MutableBlockPos();
		for (int x = min.getX(); x <= max.getX(); x++) {
			for (int y = min.getY(); y <= max.getY(); y++) {
				for (int z = min.getZ(); z <= max.getZ(); z++) {
					upgrade.setPos(x, y, z);
					//if (pos.equals(upgrade)) { continue; }
					Block block = world.getBlockState(upgrade).getBlock();
					if (block instanceof IMachineUpgrade) { 
						MultiblockBorders bord;
						if (!isMultiblock) { 
							bord = MultiblockBorders.DEFAULT;
							
							TileEntity te = world.getTileEntity(upgrade);
							if (te instanceof IMachineUpgradeTool) {
								((IMachineUpgradeTool)te).setParent(null);
							}
							if (te instanceof ICanHaveMachineFace) {
								((ICanHaveMachineFace)te).setMachineFace(MachineFace.NONE, MachineParent.NONE, EnumFacing.NORTH);
							}
						} else { bord = new MultiblockBorders(world, upgrade, min, max); }
						
						((IMachineUpgrade) block).setMultiblock(world, upgrade, pos, bord);
					}
				}
			}
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("borders")) {
			borders.deserializeNBT(compound.getCompoundTag("borders"));
		}		
		if (compound.hasKey("upgrades")) {
			upgrades.deserializeNBT(compound.getCompoundTag("upgrades"));
		}
    	if (compound.hasKey("multiblockMin")) {
    		multiblockMin = NBTUtil.getPosFromTag(compound.getCompoundTag("multiblockMin"));
    	}
    	if (compound.hasKey("multiblockMax")) {
    		multiblockMax = NBTUtil.getPosFromTag(compound.getCompoundTag("multiblockMax"));
    	}		
    	if (compound.hasKey("face")) {
    		face = MachineFace.lookup(compound.getString("face"));
    	}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("borders",  borders.serializeNBT());		
		compound.setTag("upgrades",  upgrades.serializeNBT());
        if (multiblockMin != null) { compound.setTag("multiblockMin", NBTUtil.createPosTag(multiblockMin)); }
        if (multiblockMax != null) { compound.setTag("multiblockMax", NBTUtil.createPosTag(multiblockMax)); }
        compound.setString("face", face.getName());
		return compound;
	}
	
	public int getFieldCount() { return super.getFieldCount() + 1; }
	public int getField(int id) {
		int s = super.getFieldCount();
		if (id == s) { 
			return qtyProcessing;
		}
		return super.getField(id);
	}
	
	public void setField(int id, int value) {
		int s = super.getFieldCount();
		if (id == s) {
			qtyProcessing = value; return;
		}
		super.setField(id, value);
	}
	
	protected void scanFace() {
		EnumFacing dir = facing.rotateAround(Axis.Y);
		for (int i = 0; i <= 1; i++) {
			BlockPos pos = this.getPos().offset(dir, i).offset(EnumFacing.UP, i);
			if (scanFaceAt(pos, 2, dir)) { buildFace(pos, 2, dir); }
			if (scanFaceAt(pos, 3, dir)) { buildFace(pos, 3, dir); }
		}
		
		BlockPos pos = this.getPos().offset(dir, 2).offset(EnumFacing.UP, 2);
		if (scanFaceAt(pos, 3, dir)) { buildFace(pos, 3, dir); }
	}
	
	protected boolean scanFaceAt(BlockPos pos, int count, EnumFacing dir) {
		for (int x = -count; x < 2; x++) {
			for (int y = -count; y < 2; y++) {
				BlockPos check = pos.offset(dir, x).offset(EnumFacing.UP, y);
				
				boolean eval;
				TileEntity te = world.getTileEntity(check);
				
				if (te != null) {
					eval = (te instanceof ICanHaveMachineFace);
					if (eval) { ; }
				} else { eval = false; }
				
				if (x == -count || y == -count || x == 1 || y == 1) {
					if (eval) { return false; }
				} else {
					if (!eval) { return false; }
				}
			}
		}
		return true;
	}
	
	protected void buildFace(BlockPos pos, int count, EnumFacing dir) {
		int i = 0;
		for (int x = -count+1; x < 1; x++) {
			for (int y = -count+1; y < 1; y++) {
				int face;
				if (count == 2) { face = MachineFace.F2x2P00.getIndex(); } else { face = MachineFace.F3x3P00.getIndex(); }
				face += (-x) + (-y * count);
				
				TileEntity te = world.getTileEntity(pos.offset(dir, x).offset(EnumFacing.UP, y));
				
				if (te instanceof ICanHaveMachineFace) {
					((ICanHaveMachineFace)te).setMachineFace(MachineFace.values()[face], this.getMachineType(), dir);
				}
			}
		}
	}
}
