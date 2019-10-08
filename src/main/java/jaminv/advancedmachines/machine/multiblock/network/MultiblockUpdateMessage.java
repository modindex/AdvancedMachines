package jaminv.advancedmachines.machine.multiblock.network;

import io.netty.buffer.ByteBuf;
import jaminv.advancedmachines.AdvancedMachines;
import jaminv.advancedmachines.machine.TileMachine;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Called by TileMachineMultiblock whenever a block is destroyed.
 * Block.breakBlock() isn't called on the client, so this message is used to tell
 * the client machine to update the multiblock state.
 * @author Jamin VanderBerg
 */
public class MultiblockUpdateMessage implements IMessage {
	
	  public MultiblockUpdateMessage() {}

	  private BlockPos machine, blockDestroyed;
	  
	  public MultiblockUpdateMessage(BlockPos machine, BlockPos blockDestroyed) {
		  this.machine = machine;
		  this.blockDestroyed = blockDestroyed;
	  }

	  @Override 
	  public void toBytes(ByteBuf buf) {
		  buf.writeLong(machine.toLong());
		  buf.writeLong(blockDestroyed.toLong());
	  }

	  @Override
	  public void fromBytes(ByteBuf buf) {
		  machine = BlockPos.fromLong(buf.readLong());
		  blockDestroyed = BlockPos.fromLong(buf.readLong());
	  }
	  
	  public static class MultiblockUpdateMessageHandler implements IMessageHandler<MultiblockUpdateMessage, IMessage> {

		  @Override
		  public IMessage onMessage(MultiblockUpdateMessage message, MessageContext ctx) {
          			  World world = AdvancedMachines.proxy.getMessageWorld(ctx);
			  
			  BlockPos machine = message.machine;
			  BlockPos blockDestroyed = message.blockDestroyed;
			  
			  if (!world.isBlockLoaded(machine)) { return null; }
			  TileEntity te = world.getTileEntity(machine);
			  if (!(te instanceof TileMachine)) { return null; }			  
			  
			  Minecraft.getMinecraft().addScheduledTask(() -> {
				  ((TileMachine)te).scanMultiblock(blockDestroyed);
			  });
			  return null;
		  }
	  }
}
