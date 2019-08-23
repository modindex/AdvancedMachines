package jaminv.advancedmachines.util.network;

import io.netty.buffer.ByteBuf;
import jaminv.advancedmachines.lib.machine.IRedstoneControlled;
import jaminv.advancedmachines.lib.machine.IRedstoneControlled.RedstoneState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RedstoneStateMessage implements IMessage {
	
	  public RedstoneStateMessage() {}

	  private BlockPos pos;
	  private RedstoneState state;
	  
	  public RedstoneStateMessage(BlockPos pos, RedstoneState state) {
		  this.pos = pos;
		  this.state = state;
	  }

	  @Override 
	  public void toBytes(ByteBuf buf) {
		  buf.writeLong(pos.toLong());
		  buf.writeInt(state.getValue());
	  }

	  @Override
	  public void fromBytes(ByteBuf buf) {
		  pos = BlockPos.fromLong(buf.readLong());
		  state = RedstoneState.fromValue(buf.readInt());
	  }
	  
	  public static class RedstoneStateMessageHandler implements IMessageHandler<RedstoneStateMessage, IMessage> {

		  @Override
		  public IMessage onMessage(RedstoneStateMessage message, MessageContext ctx) {
			  EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
			  WorldServer world = ctx.getServerHandler().player.getServerWorld();
			  
			  BlockPos pos = message.pos;
			  RedstoneState state = message.state;
			  
			  if (!world.isBlockLoaded(pos)) { return null; }
			  
			  world.addScheduledTask(() -> {
				  TileEntity te = world.getTileEntity(pos);
				  if (te instanceof IRedstoneControlled) {
					  ((IRedstoneControlled)te).setRedstoneState(state);
				  }
			  });
			  return null;
		  }
	  }
}
