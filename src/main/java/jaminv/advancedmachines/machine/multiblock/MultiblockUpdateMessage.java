package jaminv.advancedmachines.machine.multiblock;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MultiblockUpdateMessage implements IMessage {
	
	  public MultiblockUpdateMessage() {}

	  BlockPos pos;
	  BlockPos min;
	  BlockPos max;
	  
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
}
