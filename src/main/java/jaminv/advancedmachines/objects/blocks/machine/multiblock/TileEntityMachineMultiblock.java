package jaminv.advancedmachines.objects.blocks.machine.multiblock;

import org.apache.logging.log4j.Level;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgrade;
import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgrade.UpgradeType;
import jaminv.advancedmachines.objects.blocks.machine.expansion.inventory.BlockMachineInventory;
import jaminv.advancedmachines.objects.blocks.machine.expansion.inventory.TileEntityMachineInventory;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.MultiblockState.MultiblockSimple;
import jaminv.advancedmachines.util.Config;
import jaminv.advancedmachines.util.helper.BlockHelper;
import jaminv.advancedmachines.util.helper.BlockHelper.BlockChecker;
import jaminv.advancedmachines.util.helper.BlockHelper.ScanResult;
import jaminv.advancedmachines.util.recipe.IRecipeManager;
import jaminv.advancedmachines.util.recipe.RecipeBase;
import jaminv.advancedmachines.util.recipe.RecipeInput;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;

public abstract class TileEntityMachineMultiblock extends TileEntityMachineBase {

	public TileEntityMachineMultiblock(IRecipeManager recipeManager) {
		super(recipeManager);
	}

	protected MultiblockState multiblockState = new MultiblockSimple("message.multiblock.absent");
	public String getMultiblockString() { return multiblockState.toString(); }
	
	UpgradeManager upgrades = new UpgradeManager();

	public static class MultiblockChecker extends BlockChecker {
		@Override
		public Action checkBlock(World world, BlockPos pos) {
			Block block = world.getBlockState(pos).getBlock();
			if (block instanceof IMachineUpgrade) { return Action.SCAN; }
			if (block instanceof BlockMachineMultiblock) { return Action.END; }
			return Action.SKIP;
		}
	}	
	
	public void scanMultiblock() {
		this.upgrades.reset();
		
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
						
						if (block instanceof BlockMachineInventory) {
							TileEntity te = world.getTileEntity(check);
							if (te instanceof TileEntityMachineInventory) {
								TileEntityMachineInventory inv = (TileEntityMachineInventory)te;
								if (inv.getInputState()) { upgrades.addInventoryInput(check); }
								else { upgrades.addInventoryOutput(check); }
							}
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
		
		// Tell all the upgrades that they are now part of a multiblock
		MutableBlockPos upgrade = new MutableBlockPos();
		for (int x = min.getX(); x <= max.getX(); x++) {
			for (int y = min.getY(); y <= max.getY(); y++) {
				for (int z = min.getZ(); z <= max.getZ(); z++) {
					upgrade.setPos(x, y, z);
					if (pos.equals(upgrade)) { continue; }
					Block block = world.getBlockState(check).getBlock();
					if (block instanceof IMachineUpgrade) { 
						((IMachineUpgrade) block).setMultiblock(world, upgrade, this.getPos(), new MultiblockBorders(upgrade, min, max));
					}
				}
			}
		}
		
		world.markBlockRangeForRenderUpdate(min, max);
		return;
	}
	
	protected int qtyProcessing = 0;
	
	@Override
	protected int beginProcess(RecipeBase recipe, RecipeInput[] input) {
		IRecipeManager mgr = getRecipeManager();
		qtyProcessing = Math.min(Math.min(mgr.getRecipeQty(recipe, input), mgr.getOutputQty(recipe, this.getOutputStacks())), upgrades.get(UpgradeType.MULTIPLY) + 1) ;
		return Config.processTimeBasic;
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
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("upgrades")) {
			upgrades.deserializeNBT(compound.getCompoundTag("upgrades"));
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("upgrades",  upgrades.serializeNBT());
		return compound;
	}
}
