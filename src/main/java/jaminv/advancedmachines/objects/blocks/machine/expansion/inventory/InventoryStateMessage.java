package jaminv.advancedmachines.objects.blocks.machine.expansion.inventory;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class InventoryStateMessage implements IMessage {
	
	  public InventoryStateMessage() {}

	  private BlockPos pos;
	  private boolean state;
	  
	  public InventoryStateMessage(BlockPos pos, boolean state) {
		  this.pos = pos;
		  this.state = state;
	  }

	  @Override 
	  public void toBytes(ByteBuf buf) {
		  buf.writeLong(pos.toLong());
		  buf.writeBoolean(state);
	  }

	  @Override
	  public void fromBytes(ByteBuf buf) {
		  pos = BlockPos.fromLong(buf.readLong());
		  state = buf.readBoolean();
	  }
	  
	  public static class InventoryStateMessageHandler implements IMessageHandler<InventoryStateMessage, IMessage> {

		  @Override
		  public IMessage onMessage(InventoryStateMessage message, MessageContext ctx) {
			  EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
			  WorldServer world = ctx.getServerHandler().player.getServerWorld();
			  
			  BlockPos pos = message.pos;
			  boolean state = message.state;
			  
			  if (!world.isBlockLoaded(pos)) { return null; }
			  
			  world.addScheduledTask(() -> {
				  TileEntity te = world.getTileEntity(pos);
				  if (te instanceof TileEntityMachineInventory) {
					  ((TileEntityMachineInventory)te).setInputState(state);
				  }
			  });
			  return null;
		  }
	  }
}
