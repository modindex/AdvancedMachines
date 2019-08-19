package jaminv.advancedmachines.objects.blocks.machine.multiblock;

import io.netty.buffer.ByteBuf;
import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachineMultiblock;
import jaminv.advancedmachines.objects.blocks.machine.expansion.inventory.TileEntityMachineInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
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
}
