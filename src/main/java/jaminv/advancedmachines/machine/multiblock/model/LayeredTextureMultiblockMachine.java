package jaminv.advancedmachines.machine.multiblock.model;

import jaminv.advancedmachines.client.RawTextures;
import jaminv.advancedmachines.init.property.Properties;
import jaminv.advancedmachines.machine.multiblock.face.SidedTexture;
import jaminv.advancedmachines.machine.multiblock.face.MachineFace;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.common.property.IExtendedBlockState;

public class LayeredTextureMultiblockMachine extends LayeredTextureMultiblock {

	public LayeredTextureMultiblockMachine(IBlockState state, String texture) {
		super(state, texture);
	}

	@Override
	protected TextureAtlasSprite getBaseTexture(String variant) {
		IExtendedBlockState ext = (IExtendedBlockState)getState();
		MachineFace face = ext.getValue(Properties.MACHINE_FACE);
		String active = ext.getValue(Properties.ACTIVE) ? "active" : "inactive";
		
		return RawTextures.get(getTexture(), active, variant, face == MachineFace.NONE ? "base" : face.getName());
	}
}
