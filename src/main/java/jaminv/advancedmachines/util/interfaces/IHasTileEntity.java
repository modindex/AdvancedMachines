package jaminv.advancedmachines.util.interfaces;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;

public interface IHasTileEntity extends ITileEntityProvider {
	public Class<? extends TileEntity> getTileEntityClass();
}
