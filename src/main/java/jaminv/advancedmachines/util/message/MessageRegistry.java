package jaminv.advancedmachines.util.message;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.MultiblockUpdateMessage;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.MultiblockUpdateMessage.MultiblockUpdateMessageHandler;
import jaminv.advancedmachines.util.message.IOStateMessage.IOMessageHandler;
import jaminv.advancedmachines.util.message.ProcessingStateMessage.ProcessingStateMessageHandler;
import jaminv.advancedmachines.util.message.RedstoneStateMessage.RedstoneStateMessageHandler;
import net.minecraftforge.fml.relauncher.Side;

public class MessageRegistry {
	public static void register() {
		int discriminator = 0;
		Main.NETWORK.registerMessage(IOMessageHandler.class, IOStateMessage.class, discriminator++, Side.SERVER);
		Main.NETWORK.registerMessage(MultiblockUpdateMessageHandler.class, MultiblockUpdateMessage.class, discriminator++, Side.CLIENT);
		Main.NETWORK.registerMessage(RedstoneStateMessageHandler.class, RedstoneStateMessage.class, discriminator++, Side.SERVER);
		Main.NETWORK.registerMessage(ProcessingStateMessageHandler.class, ProcessingStateMessage.class, discriminator++, Side.CLIENT);
	}
}
