package jaminv.advancedmachines.machine;

import io.netty.buffer.ByteBuf;
import jaminv.advancedmachines.AdvancedMachines;
import jaminv.advancedmachines.lib.machine.CanProcess;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ProcessingStateMessage implements IMessage {
	
	public ProcessingStateMessage() {}
	
	private BlockPos pos;
	private boolean state;
	 
	public ProcessingStateMessage(BlockPos pos, boolean state) {
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
	  
	public static class ProcessingStateMessageHandler implements IMessageHandler<ProcessingStateMessage, IMessage> {
	
		@Override
		public IMessage onMessage(ProcessingStateMessage message, MessageContext ctx) {
			World world = AdvancedMachines.proxy.getMessageWorld(ctx);
			  
			BlockPos pos = message.pos;
			boolean state = message.state;
				  
			if (!world.isBlockLoaded(pos)) { return null; }
				  
			Minecraft.getMinecraft().addScheduledTask(() -> {
				TileEntity te = world.getTileEntity(pos);
				if (te instanceof CanProcess) {
					((CanProcess)te).setProcessingState(state);
				}
			});
			return null;
		}
	}
}
