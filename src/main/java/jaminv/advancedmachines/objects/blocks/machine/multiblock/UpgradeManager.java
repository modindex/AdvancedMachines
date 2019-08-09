package jaminv.advancedmachines.objects.blocks.machine.multiblock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.management.NotificationBroadcaster;

import jaminv.advancedmachines.lib.recipe.RecipeInput;
import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgrade;
import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgradeTileEntity;
import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgradeTool;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

public class UpgradeManager implements INBTSerializable<NBTTagCompound> {
	protected Map<IMachineUpgrade.UpgradeType, Integer> upgrades;
	protected List<BlockPos> tools;
	
	public static class ToolCompare implements Comparator<BlockPos> {
		World world;
		
		public ToolCompare(World world) {
			this.world = world;
		}
		
		@Override
		public int compare(BlockPos arg0, BlockPos arg1) {
			TileEntity te0 = world.getTileEntity(arg0);
			if (!(te0 instanceof IMachineUpgradeTool)) { return 1; }
			TileEntity te1 = world.getTileEntity(arg1);
			if (!(te1 instanceof IMachineUpgradeTool)) { return -1; }
			
			return ((IMachineUpgradeTool)te1).getPriority() - ((IMachineUpgradeTool)te0).getPriority();
		}		
	}	
	
	protected void reset() {
		upgrades = new EnumMap<IMachineUpgrade.UpgradeType, Integer>(IMachineUpgrade.UpgradeType.class);
		tools = new ArrayList<BlockPos>();
	}
	
	public boolean isValid() {
		return (upgrades != null && upgrades.size() > 0) || (tools != null && tools.size() > 0);
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
	
	public void addTool(BlockPos pos, World world) {
		if (tools == null) { tools = new ArrayList<BlockPos>(); }
		tools.add(new BlockPos(pos));
		sortTools(world);
	}
	
	public void sortTools(World world) {
		if (tools == null) { return; }
		
		BlockPos[] t = tools.toArray(new BlockPos[0]);
		Arrays.sort(t, new ToolCompare(world));
		tools = new ArrayList<BlockPos>(Arrays.asList(t));
	}
	
	public BlockPos[] getTools() {
		BlockPos[] def = new BlockPos[0];
		if (tools == null) { return def; }
		
		return tools.toArray(def);
	}
	
    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();

        if (upgrades != null) {
        	NBTTagList nbtTagList = new NBTTagList();
        	int count = 0;
        	for (IMachineUpgrade.UpgradeType type : IMachineUpgrade.UpgradeType.values()) {
        		Integer qty = upgrades.get(type);
        		if (qty != null) {
        			NBTTagCompound upgradeTag = new NBTTagCompound();
        			upgradeTag.setString("type", type.getName());
        			upgradeTag.setInteger("qty", qty);
        			nbtTagList.appendTag(upgradeTag);
        			count++;
        		}
            }

            nbt.setTag("upgrades", nbtTagList);
            nbt.setInteger("upgrade_count", count);
        }
        if (tools != null) {
        	NBTTagList nbtTagList = new NBTTagList();
        	for (BlockPos pos : tools) {
        		nbtTagList.appendTag(NBTUtil.createPosTag(pos));
        	}
        	nbt.setTag("tools", nbtTagList);
        	nbt.setInteger("tool_count", tools.size());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
    	this.reset();
    	if (nbt.hasKey("upgrade_count")) {
    		int count = nbt.getInteger("upgrade_count");
            NBTTagList tagList = nbt.getTagList("upgrades", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < count; i++) {
                NBTTagCompound upgradeTag = tagList.getCompoundTagAt(i);
                String type = upgradeTag.getString("type");
                int qty = upgradeTag.getInteger("qty");
                upgrades.put(IMachineUpgrade.UpgradeType.getType(type), qty);            	
            }
    	}
    	if (nbt.hasKey("tool_count")) {
    		int count = nbt.getInteger("tool_count");
    		NBTTagList tagList = nbt.getTagList("tools", Constants.NBT.TAG_COMPOUND);
    		for (int i = 0; i < count; i++) {
    			NBTTagCompound toolTag = tagList.getCompoundTagAt(i);
    			tools.add(NBTUtil.getPosFromTag(toolTag));
    		}
    	}
    }
}
