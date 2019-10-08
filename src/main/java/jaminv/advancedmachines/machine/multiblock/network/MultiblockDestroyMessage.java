package jaminv.advancedmachines.machine.multiblock.network;

import io.netty.buffer.ByteBuf;
import jaminv.advancedmachines.AdvancedMachines;
import jaminv.advancedmachines.machine.multiblock.MultiblockBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Called by TileMachineMultiblock whenever a machine is destroyed.
 * By the time the message is received by the client, the tile entity no longer exists,
 * therefore a normal MultiblockUpdateMessage won't work.
 * Instead, this message just passes the data required to destroy any existing multiblock.
 * @author Jamin VanderBerg
 */
public class MultiblockDestroyMessage implements IMessage {
	
	  public MultiblockDestroyMessage() {}
	  
	  private BlockPos min, max;
	  
	  public MultiblockDestroyMessage(BlockPos min, BlockPos max) {
		  this.min = min;
		  this.max = max;
	  }

	  @Override 
	  public void toBytes(ByteBuf buf) {
		  buf.writeLong(min.toLong());
		  buf.writeLong(max.toLong());
	  }

	  @Override
	  public void fromBytes(ByteBuf buf) {
		  min = BlockPos.fromLong(buf.readLong());
		  max = BlockPos.fromLong(buf.readLong());
	  }
	  
	  public static class MultiblockDestroyMessageHandler implements IMessageHandler<MultiblockDestroyMessage, IMessage> {

		  @Override
		  public IMessage onMessage(MultiblockDestroyMessage message, MessageContext ctx) {
			  World world = AdvancedMachines.proxy.getMessageWorld(ctx);
			  
			  MultiblockBuilder.setMultiblock(message.min, message.max, false, world);
			  return null;
		  }
	  }
}
