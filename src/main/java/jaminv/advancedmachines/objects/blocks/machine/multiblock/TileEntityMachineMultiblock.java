package jaminv.advancedmachines.objects.blocks.machine.multiblock;

import java.util.EnumMap;
import java.util.Map;

import org.apache.logging.log4j.Level;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgrade;
import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgrade.UpgradeType;
import jaminv.advancedmachines.util.Config;
import jaminv.advancedmachines.util.helper.BlockHelper;
import jaminv.advancedmachines.util.helper.BlockHelper.BlockChecker;
import jaminv.advancedmachines.util.helper.BlockHelper.ScanResult;
import jaminv.advancedmachines.util.recipe.IRecipeManager;
import jaminv.advancedmachines.util.recipe.RecipeBase;
import jaminv.advancedmachines.util.recipe.RecipeInput;
import jaminv.advancedmachines.util.recipe.RecipeOutput;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;

public abstract class TileEntityMachineMultiblock extends TileEntityMachineBase {
	
	protected static class UpgradeManager {
		Map<IMachineUpgrade.UpgradeType, Integer> upgrades;
		
		protected void reset() {
			upgrades = new EnumMap<IMachineUpgrade.UpgradeType, Integer>(IMachineUpgrade.UpgradeType.class);
		}
		
		protected void add(IMachineUpgrade.UpgradeType type, int add) {
			if (upgrades == null) { reset(); }
			Integer qty = upgrades.get(type);
			if (qty == null) { qty = 0; }
			upgrades.put(type, qty + add);
		}
		
		public int get(IMachineUpgrade.UpgradeType type) {
			if (upgrades == null) { return 0; }
			Integer qty = upgrades.get(type);
			if (qty == null) { return 0; }
			return qty;
		}
	}

	public TileEntityMachineMultiblock(IRecipeManager recipeManager) {
		super(recipeManager);
	}

	public static abstract class MultiblockState {
		public abstract String toString();
	}
	public static class MultiblockSimple extends MultiblockState {
		protected final String message;
		public MultiblockSimple(String message) {
			this.message = message;
		}
		
		@Override
		public String toString() {
			return I18n.format(message);
		}
	}
	public static class MultiblockIllegal extends MultiblockState {
		protected final String message;
		protected final String name;
		protected final BlockPos pos;
		public MultiblockIllegal(String message, String name, BlockPos pos) {
			this.message = message;
			this.name = name;
			this.pos = pos;
		}
		
		@Override
		public String toString() {
			return I18n.format(message, name, pos.getX(), pos.getY(), pos.getZ());
		}
	}
	
	public static class MultiblockComplete extends MultiblockState {
		protected UpgradeManager upgrades;
		public MultiblockComplete(UpgradeManager upgrades) {
			this.upgrades = upgrades;
		}
		
		@Override
		public String toString() {
			return I18n.format("message.multiblock.complete", 
				upgrades.get(IMachineUpgrade.UpgradeType.MULTIPLY),
				upgrades.get(IMachineUpgrade.UpgradeType.SPEED)
			);
		}		
	}
	
	public static class MultiblockChecker extends BlockChecker {
		@Override
		public Action checkBlock(World world, BlockPos pos) {
			Block block = world.getBlockState(pos).getBlock();
			if (block instanceof IMachineUpgrade) { return Action.SCAN; }
			if (block instanceof BlockMachineMultiblock) { return Action.END; }
			return Action.SKIP;
		}
	}
	
	protected MultiblockState multiblockState = new MultiblockSimple("message.multiblock.absent");
	public String getMultiblockString() { return multiblockState.toString(); }
	
	UpgradeManager upgrades = new UpgradeManager();
	
	public void scanMultiblock() {
		this.upgrades.reset();
		
		BlockPos pos = this.getPos();
		
		ScanResult result = BlockHelper.scanBlocks(world, pos, new MultiblockChecker());
		
		BlockPos end = result.getEnd();
		if (end != null) {
			this.multiblockState = new MultiblockIllegal("message.multiblock.connected_machine", BlockHelper.getBlockName(world, end), end);
			return;
		}
		
		BlockPos min = result.getMin(), max = result.getMax();
		
		if (pos.equals(min) && pos.equals(max)) {
			this.multiblockState = new MultiblockSimple("message.multiblock.absent");
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
					} else {
						this.multiblockState = new MultiblockIllegal("message.multiblock.illegal", block.getLocalizedName(), check.toImmutable());
						this.upgrades.reset();
						return;
					} 
				}
			}
		}
		
		this.multiblockState = new MultiblockComplete(this.upgrades);
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
}
