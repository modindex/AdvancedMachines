package jaminv.advancedmachines.objects.blocks.machine.multiblock.model;

import jaminv.advancedmachines.client.RawTextures;
import jaminv.advancedmachines.init.property.Properties;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.MachineFace;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.MachineType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.common.property.IExtendedBlockState;

public class LayeredTextureMultiblockMachine extends LayeredTextureMultiblockBase {

	public LayeredTextureMultiblockMachine(IBlockState state, String base) {
		super(state, base);
	}

	@Override
	protected TextureAtlasSprite getBaseTexture(String variant) {
		IExtendedBlockState ext = (IExtendedBlockState)getState();
		MachineFace face = ext.getValue(Properties.MACHINE_FACE);
		String active = ext.getValue(Properties.ACTIVE) ? "active" : "inactive";
		
		return RawTextures.get(getBase(), active, variant, face == MachineFace.NONE ? "base" : face.getName());
	}
}
