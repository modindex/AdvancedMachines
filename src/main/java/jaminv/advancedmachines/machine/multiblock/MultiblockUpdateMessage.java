package jaminv.advancedmachines.machine.multiblock;

import io.netty.buffer.ByteBuf;
import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.machine.TileMachineMultiblock;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MultiblockUpdateMessage implements IMessage {
	
	  public MultiblockUpdateMessage() {}

	  private BlockPos pos, min, max;
	  
	  public MultiblockUpdateMessage(BlockPos pos, BlockPos min, BlockPos max) {
		  this.pos = pos;
		  this.min = min;
		  this.max = max;
	  }

	  @Override 
	  public void toBytes(ByteBuf buf) {
		  buf.writeLong(pos.toLong());
		  buf.writeLong(min.toLong());
		  buf.writeLong(max.toLong());
	  }

	  @Override
	  public void fromBytes(ByteBuf buf) {
		  pos = BlockPos.fromLong(buf.readLong());
		  min = BlockPos.fromLong(buf.readLong());
		  max = BlockPos.fromLong(buf.readLong());
	  }
	  
	  public static class MultiblockUpdateMessageHandler implements IMessageHandler<MultiblockUpdateMessage, IMessage> {

		  @Override
		  public IMessage onMessage(MultiblockUpdateMessage message, MessageContext ctx) {
			  World world = Main.proxy.getMessageWorld(ctx);
			  
			  BlockPos pos = message.pos;
			  BlockPos min = message.min;
			  BlockPos max = message.max;
			  
			  if (!world.isBlockLoaded(pos)) { return null; }
			  
			  Minecraft.getMinecraft().addScheduledTask(() -> {
				  TileMachineMultiblock.setMultiblock(min, max, false, world, pos);
				  world.markBlockRangeForRenderUpdate(min, max);
			  });
			  return null;
		  }
	  }
}
