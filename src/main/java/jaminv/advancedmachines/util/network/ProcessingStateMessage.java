package jaminv.advancedmachines.util.network;

import io.netty.buffer.ByteBuf;
import jaminv.advancedmachines.AdvancedMachines;
import jaminv.advancedmachines.lib.machine.ICanProcess;
import jaminv.advancedmachines.lib.util.helper.BlockIterator;
import jaminv.advancedmachines.machine.multiblock.face.MachineFaceTile;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ProcessingStateMessage implements IMessage {
	
	public ProcessingStateMessage() {}
	
	private BlockPos min, max;
	private boolean state;
	 
	public ProcessingStateMessage(BlockPos min, BlockPos max, boolean state) {
		this.min = min; this.max = max;
		this.state = state;
	}
	
	@Override 
	public void toBytes(ByteBuf buf) {
		buf.writeLong(min.toLong());
		buf.writeLong(max.toLong());
		buf.writeBoolean(state);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		min = BlockPos.fromLong(buf.readLong());
		max = BlockPos.fromLong(buf.readLong());
		state = buf.readBoolean();
	}
	  
	public static class ProcessingStateMessageHandler implements IMessageHandler<ProcessingStateMessage, IMessage> {
	
		@Override
		public IMessage onMessage(ProcessingStateMessage message, MessageContext ctx) {
			World world = AdvancedMachines.proxy.getMessageWorld(ctx);
			  
			BlockPos min = message.min;
			BlockPos max = message.max;
			boolean state = message.state;
				  
			if (!world.isBlockLoaded(min) && !world.isBlockLoaded(max)) { return null; }
				  
			Minecraft.getMinecraft().addScheduledTask(() -> {
				BlockIterator.iterateBlocks(world, min, max, (worldIn, pos) -> {
					TileEntity te = worldIn.getTileEntity(pos);
					if (te instanceof ICanProcess) {
						((ICanProcess)te).setProcessingState(state);
					}
					if (te instanceof MachineFaceTile) {
						((MachineFaceTile)te).setActive(state);
					}					
				});
				world.markBlockRangeForRenderUpdate(min, max);
			});
			return null;
		}
	}
}
