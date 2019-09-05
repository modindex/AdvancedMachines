package jaminv.advancedmachines.machine.multiblock;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import jaminv.advancedmachines.machine.expansion.MachineUpgrade;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

public interface MultiblockUpgrades extends INBTSerializable<NBTTagCompound> {
	
	public static MultiblockUpgrades EMPTY = new Builder();
	
	public boolean isValid();

	public int get(MachineUpgrade.UpgradeType type);

	public int getToolCount();
	public BlockPos getTool(int index);
	
	public static class Builder implements MultiblockUpgrades {
		protected Map<MachineUpgrade.UpgradeType, Integer> upgrades;
		protected List<BlockPos> tools = new ArrayList<BlockPos>();
		
		public Builder() {
			this.reset();
		}
	
		public void reset() {
			upgrades = new EnumMap<MachineUpgrade.UpgradeType, Integer>(MachineUpgrade.UpgradeType.class);
			tools = new ArrayList<BlockPos>();
		}
		
		public boolean isValid() {
			return (upgrades != null && upgrades.size() > 0);
		}
		
		public void add(MachineUpgrade.UpgradeType type, int add) {
			if (upgrades == null) { reset(); }
			Integer qty = upgrades.get(type);
			if (qty == null) { qty = 0; }
			upgrades.put(type, qty + add);
		}
		
		public int get(MachineUpgrade.UpgradeType type) {
			if (upgrades == null) { return 0; }
			Integer qty = upgrades.get(type);
			if (qty == null) { return 0; }
			return qty;
		}
		
		public void addTool(BlockPos pos) {	tools.add(pos); }	
		public int getToolCount() { return tools.size(); }
		public BlockPos getTool(int index) { return tools.get(index); }
		
	    @Override
	    public NBTTagCompound serializeNBT()
	    {
	        NBTTagCompound nbt = new NBTTagCompound();
	
	        if (upgrades != null) {
	        	NBTTagList nbtUpgrades = new NBTTagList();
	        	int count = 0;
	        	for (MachineUpgrade.UpgradeType type : MachineUpgrade.UpgradeType.values()) {
	        		Integer qty = upgrades.get(type);
	        		if (qty != null) {
	        			NBTTagCompound upgradeTag = new NBTTagCompound();
	        			upgradeTag.setString("type", type.getName());
	        			upgradeTag.setInteger("qty", qty);
	        			nbtUpgrades.appendTag(upgradeTag);
	        			count++;
	        		}
	            }
	        	
	            nbt.setTag("upgrades", nbtUpgrades);
	            nbt.setInteger("upgrade_count", count);
	        }
	        
	    	if (tools != null && tools.size() > 0) {
		        NBTTagList nbtTools = new NBTTagList();
		    	for (BlockPos pos : tools) {
		    		nbtTools.appendTag(NBTUtil.createPosTag(pos));
		    	}
		    	
		    	nbt.setTag("tools", nbtTools);
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
	                upgrades.put(MachineUpgrade.UpgradeType.getType(type), qty);            	
	            }
	    	}
	    	
	    	if (nbt.hasKey("tool_count")) {
	    		int count = nbt.getInteger("tool_count");
	    		NBTTagList tagList = nbt.getTagList("tools", Constants.NBT.TAG_COMPOUND);
	    		for (int i = 0; i < count; i++) {
	    			tools.add(NBTUtil.getPosFromTag(tagList.getCompoundTagAt(i)));
	    		}
	    	}
	    }
	}
}
