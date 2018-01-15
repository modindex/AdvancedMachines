package jaminv.advancedmachines.objects.items.material;

import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.alloy.TileEntityMachineAlloy;
import jaminv.advancedmachines.objects.blocks.machine.purifier.TileEntityMachinePurifier;

public abstract class MaterialMachine extends MaterialBase {

	private static MaterialType TYPE = MaterialType.MACHINE;
	
	public static final MaterialMachine PURIFIER = new MaterialMachine(0, "purifier") {
		public TileEntityMachineBase createNewTileEntity() { return new TileEntityMachinePurifier(); }
	};
	public static final MaterialMachine ALLOY = new MaterialMachine(1, "copper") {
		public TileEntityMachineBase createNewTileEntity() { return new TileEntityMachineAlloy(); }
	};
	
	public static MaterialBase[] values() {
		return MaterialBase.values(TYPE, new MaterialBase[0]);
	}
	
	public static MaterialBaseOre byMetadata(int meta) {
		return (MaterialBaseOre)(MaterialBase.byMetadata(TYPE, meta));
	}
	
	private MaterialMachine(int meta, String name) {
		super(TYPE, meta, name);
	}

	@Override
	public boolean doInclude(String oredictType) {
		return true;
	}
	
	public abstract TileEntityMachineBase createNewTileEntity();
}
