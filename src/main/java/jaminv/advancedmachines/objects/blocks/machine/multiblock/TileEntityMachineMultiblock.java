package jaminv.advancedmachines.objects.blocks.machine.multiblock;

import org.apache.logging.log4j.Level;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.lib.recipe.IRecipeManager;
import jaminv.advancedmachines.lib.recipe.RecipeBase;
import jaminv.advancedmachines.objects.blocks.BlockMaterial;
import jaminv.advancedmachines.objects.blocks.machine.BlockMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgrade;
import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgrade.UpgradeType;
import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgradeTileEntity;
import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgradeTool;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.MultiblockState.MultiblockNull;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.IMachineFaceTE;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.MachineFace;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.objects.material.MaterialBase;
import jaminv.advancedmachines.util.ModConfig;
import jaminv.advancedmachines.util.helper.BlockHelper;
import jaminv.advancedmachines.util.helper.BlockHelper.BlockChecker;
import jaminv.advancedmachines.util.helper.BlockHelper.ScanResult;
import jaminv.advancedmachines.util.message.ProcessingStateMessage;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;

public abstract class TileEntityMachineMultiblock extends TileEntityMachineBase implements IMachineUpgradeTileEntity, IMachineFaceTE {

	public TileEntityMachineMultiblock(IRecipeManager recipeManager) {
		super(recipeManager);
	}
	
	protected MultiblockBorders borders = new MultiblockBorders();
	
	public void setBorders(World world, MultiblockBorders borders) {
		this.borders = borders;
	}
	
	public MultiblockBorders getBorders() {
		return borders; 
	}
	
	public abstract MachineType getMachineType();
	
	protected MachineFace face = MachineFace.NONE;
	protected BlockPos facemin, facemax;

	@Override
	public void setMachineFace(MachineFace face, MachineType parent, EnumFacing facing, BlockPos pos) {
		this.face = face;
		if (face == MachineFace.NONE) {
			facemin = pos; facemax = pos;
		}
	}
	
	@Override
	public void setActive(boolean active) {
		; // no op
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
		if (destroy && multiblockMin != null && multiblockMax != null) {
			Main.NETWORK.sendToAll(new MultiblockUpdateMessage(this.getPos(), multiblockMin, multiblockMax));
		}
		
		this.wake();
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
		
		// Add the upgrade for the machine itself
		this.upgrades.add(UpgradeType.MULTIPLY, this.getMultiplier());
		
		this.multiblockState = new MultiblockState.MultiblockComplete(this.upgrades);
		multiblockMin = min; multiblockMax = max;
		
		// Tell all the upgrades that they are now part of a multiblock
		setMultiblock(min, max, true, world, this.getPos());
		
		scanFace();
		
		world.markBlockRangeForRenderUpdate(min, max);
		return;
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
							if (te instanceof IMachineFaceTE) {
								((IMachineFaceTE)te).setMachineFace(MachineFace.NONE, MachineType.NONE, EnumFacing.UP, null);
							}
						} else { bord = new MultiblockBorders(world, upgrade, min, max); }
						
						((IMachineUpgrade) block).setMultiblock(world, upgrade, pos, bord);
					}
				}
			}
		}
	}
	
	/* ============== *
	 *  Machine Face  *
	 * ============== */
	
	protected void scanFace() {
		EnumFacing dir = facing.rotateAround(Axis.Y);
		for (int x = 0; x <= 2; x++) {
			for (int y = 0; y <= 2; y++) {
				BlockPos pos = this.getPos().offset(dir, x).offset(EnumFacing.UP, y);
				if (scanFaceAt(pos, 3, dir)) { buildFace(pos, 3, dir); }
				if (x != 2 && y != 2 && scanFaceAt(pos, 2, dir)) { buildFace(pos, 2, dir); }				
			}
		}
	}
	
	protected boolean scanFaceAt(BlockPos pos, int count, EnumFacing dir) {
		World world = this.getWorld();
		MaterialBase variant = world.getBlockState(getPos()).getValue(BlockMaterial.EXPANSION_VARIANT);		
		
		for (int x = -count; x < 2; x++) {
			for (int y = -count; y < 2; y++) {
				BlockPos check = pos.offset(dir, x).offset(EnumFacing.UP, y);
				
				boolean eval;
				TileEntity te = world.getTileEntity(check);
				
				if (te != null) {
					eval = (te instanceof IMachineFaceTE);
					Block block = world.getBlockState(check).getBlock();
					if (eval) { eval = world.getBlockState(check).getValue(BlockMachineBase.EXPANSION_VARIANT) == variant; }
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
				TileEntity te = world.getTileEntity(pos.offset(dir, x).offset(EnumFacing.UP, y));
				
				if (te instanceof IMachineFaceTE) {
					((IMachineFaceTE)te).setMachineFace(MachineFace.build(count, -x, -y), this.getMachineType(), facing, this.getPos());
				}
			}
		}
		
		this.facemin = pos.offset(dir, -count+1).offset(EnumFacing.UP, -count+1);
		this.facemax = pos;
	}	
	
	/* ============ *
	 *  Processing  *
	 * ============ */
	
	protected int qtyProcessing = 0;

	@Override
	public void onInputChanged(int slot, RecipeBase recipe) {
		int qty = this.getRecipeQty(recipe);
		if (qty <= 0) { haltProcess(); }
		if (qty < qtyProcessing) { qtyProcessing = qty; }
	}

	public int getQtyProcessing() { return qtyProcessing; }
	
	@Override
	protected boolean preProcess() {
		boolean didSomething = super.preProcess();
		
		BlockPos[] tools = upgrades.getTools();
		for (BlockPos tool : tools) {
			TileEntity te = world.getTileEntity(tool);
			if (te instanceof IMachineUpgradeTool) {
				didSomething = ((IMachineUpgradeTool)te).tickUpdate(this) || didSomething;
			}
		}
		
		return didSomething;
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
	protected boolean beginProcess(RecipeBase recipe) {
		qtyProcessing = Math.min(getRecipeQty(recipe), Math.max(upgrades.get(UpgradeType.MULTIPLY), getMultiplier()));
		return qtyProcessing > 0;
	}
	
	protected int getRecipeQty(RecipeBase recipe) {
		return recipe.getRecipeQty(getInput(), getOutput());
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
		
		qtyProcessing = 0;
	}
	
	@Override
	protected void sendProcessingMesssage(boolean isProcessing) {
		if (facemin != null && facemax != null) {
			Main.NETWORK.sendToAll(new ProcessingStateMessage(facemin, facemax, isProcessing));
		} else {
			super.sendProcessingMesssage(isProcessing);
		}
	}

	@Override
	protected void haltProcess() {
		super.haltProcess();
		if (!world.isRemote) { qtyProcessing = 0; }
	}
	
	/* ================= *
	 *  Network and NBT  *
	 * ================= */
	
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
    	if (compound.hasKey("facemin")) {
    		facemin = NBTUtil.getPosFromTag(compound.getCompoundTag("facemin"));
    	}
    	if (compound.hasKey("facemax")) {
    		facemax = NBTUtil.getPosFromTag(compound.getCompoundTag("facemax"));
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
        if (facemin != null) { compound.setTag("facemin", NBTUtil.createPosTag(facemin)); }
        if (facemax != null) { compound.setTag("facemax", NBTUtil.createPosTag(facemax)); }
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
}
