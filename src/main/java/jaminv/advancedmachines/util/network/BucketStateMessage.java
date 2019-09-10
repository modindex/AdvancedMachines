package jaminv.advancedmachines.util.network;

import io.netty.buffer.ByteBuf;
import jaminv.advancedmachines.lib.dialog.control.enums.IOState;
import jaminv.advancedmachines.lib.dialog.fluid.DialogBucketToggle;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BucketStateMessage implements IMessage {
	
	  public BucketStateMessage() {}

	  private BlockPos pos;
	  private IOState state;
	  
	  public BucketStateMessage(BlockPos pos, IOState state) {
		  this.pos = pos;
		  this.state = state;
	  }

	  @Override 
	  public void toBytes(ByteBuf buf) {
		  buf.writeLong(pos.toLong());
		  buf.writeBoolean(state.getState());
	  }

	  @Override
	  public void fromBytes(ByteBuf buf) {
		  pos = BlockPos.fromLong(buf.readLong());
		  state = IOState.find(buf.readBoolean());
	  }
	  
	  public static class BucketStateMessageHandler implements IMessageHandler<BucketStateMessage, IMessage> {

		  @Override
		  public IMessage onMessage(BucketStateMessage message, MessageContext ctx) {
			  EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
			  WorldServer world = ctx.getServerHandler().player.getServerWorld();
			  
			  BlockPos pos = message.pos;
			  IOState state = message.state;
			  
			  if (!world.isBlockLoaded(pos)) { return null; }
			  
			  world.addScheduledTask(() -> {
				  TileEntity te = world.getTileEntity(pos);
				  if (te instanceof DialogBucketToggle.Provider) {
					  ((DialogBucketToggle.Provider)te).getBucketToggle().setBucketState(state);
				  }
			  });
			  return null;
		  }
	  }
}
