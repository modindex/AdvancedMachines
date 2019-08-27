package jaminv.advancedmachines.machine.multiblock;

import jaminv.advancedmachines.machine.TileEntityMachineMultiblock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MultiblockUpdateMessageHandler implements IMessageHandler<MultiblockUpdateMessage, IMessage> {

	  @Override
	  public IMessage onMessage(MultiblockUpdateMessage message, MessageContext ctx) {
		  WorldClient world = Minecraft.getMinecraft().world;
		  
		  BlockPos pos = message.pos;
		  BlockPos min = message.min;
		  BlockPos max = message.max;
		  
		  if (!world.isBlockLoaded(pos)) { return null; }
		  
		  Minecraft.getMinecraft().addScheduledTask(() -> {
			  TileEntityMachineMultiblock.setMultiblock(min, max, false, world, pos);
			  world.markBlockRangeForRenderUpdate(min, max);
		  });
		  return null;
	  }
}