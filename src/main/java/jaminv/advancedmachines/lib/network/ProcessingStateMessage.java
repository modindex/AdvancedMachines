package jaminv.advancedmachines.lib.network;

import io.netty.buffer.ByteBuf;
import jaminv.advancedmachines.lib.machine.ICanProcess;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.IMachineFaceTE;
import jaminv.advancedmachines.util.helper.BlockHelper;
import jaminv.advancedmachines.util.helper.BlockHelper.BlockCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * TODO: Needs cleanup. Accesses non-lib objects.
 */
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
			WorldClient world = Minecraft.getMinecraft().world;
			  
			BlockPos min = message.min;
			BlockPos max = message.max;
			boolean state = message.state;
			  
			BlockCallback callback = new BlockCallback() {
				@Override
				public void callback(World world, BlockPos pos) {
					TileEntity te = world.getTileEntity(pos);
					if (te instanceof ICanProcess) {
						((ICanProcess)te).setProcessingState(state);
					}
					if (te instanceof IMachineFaceTE) {
						((IMachineFaceTE)te).setActive(state);
					}
				}
			};			  
				  
			if (!world.isBlockLoaded(min) && !world.isBlockLoaded(max)) { return null; }
				  
			Minecraft.getMinecraft().addScheduledTask(() -> {
				BlockHelper.iterateBlocks(world, min, max, callback);
				world.markBlockRangeForRenderUpdate(min, max);
			});
			return null;
		}
	}
}
