package jaminv.advancedmachines.proxy;

import jaminv.advancedmachines.objects.blocks.machine.purifier.ContainerMachinePurifier;
import jaminv.advancedmachines.objects.blocks.machine.purifier.GuiMachinePurifier;
import jaminv.advancedmachines.objects.blocks.machine.purifier.TileEntityMachinePurifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityMachinePurifier) {
            return new ContainerMachinePurifier(player.inventory, (TileEntityMachinePurifier) te);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityMachinePurifier) {
        	TileEntityMachinePurifier tileEntity = (TileEntityMachinePurifier) te;
            return new GuiMachinePurifier(tileEntity, new ContainerMachinePurifier(player.inventory, tileEntity));
        }
        return null;
    }
}