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
	  private int priority;
	  
	  public InventoryStateMessage(BlockPos pos, boolean state, int priority) {
		  this.pos = pos;
		  this.state = state;
		  this.priority = priority;
	  }

	  @Override 
	  public void toBytes(ByteBuf buf) {
		  buf.writeLong(pos.toLong());
		  buf.writeBoolean(state);
		  buf.writeInt(priority);
	  }

	  @Override
	  public void fromBytes(ByteBuf buf) {
		  pos = BlockPos.fromLong(buf.readLong());
		  state = buf.readBoolean();
		  priority = buf.readInt();
	  }
	  
	  public static class InventoryStateMessageHandler implements IMessageHandler<InventoryStateMessage, IMessage> {

		  @Override
		  public IMessage onMessage(InventoryStateMessage message, MessageContext ctx) {
			  EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
			  WorldServer world = ctx.getServerHandler().player.getServerWorld();
			  
			  BlockPos pos = message.pos;
			  boolean state = message.state;
			  int priority = message.priority;
			  
			  if (!world.isBlockLoaded(pos)) { return null; }
			  
			  world.addScheduledTask(() -> {
				  TileEntity te = world.getTileEntity(pos);
				  if (te instanceof TileEntityMachineInventory) {
					  ((TileEntityMachineInventory)te).setInputState(state);
					  ((TileEntityMachineInventory)te).setPriority(priority);
				  }
			  });
			  return null;
		  }
	  }
}
