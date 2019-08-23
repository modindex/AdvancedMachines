package jaminv.advancedmachines.util.network;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.machine.multiblock.MultiblockUpdateMessage;
import jaminv.advancedmachines.machine.multiblock.MultiblockUpdateMessage.MultiblockUpdateMessageHandler;
import jaminv.advancedmachines.util.network.BucketStateMessage.BucketStateMessageHandler;
import jaminv.advancedmachines.util.network.IOStateMessage.IOMessageHandler;
import jaminv.advancedmachines.util.network.ProcessingStateMessage.ProcessingStateMessageHandler;
import jaminv.advancedmachines.util.network.RedstoneStateMessage.RedstoneStateMessageHandler;
import net.minecraftforge.fml.relauncher.Side;

public class MessageRegistry {
	public static void register() {
		int discriminator = 0;
		Main.NETWORK.registerMessage(IOMessageHandler.class, IOStateMessage.class, discriminator++, Side.SERVER);
		Main.NETWORK.registerMessage(MultiblockUpdateMessageHandler.class, MultiblockUpdateMessage.class, discriminator++, Side.CLIENT);
		Main.NETWORK.registerMessage(RedstoneStateMessageHandler.class, RedstoneStateMessage.class, discriminator++, Side.SERVER);
		Main.NETWORK.registerMessage(ProcessingStateMessageHandler.class, ProcessingStateMessage.class, discriminator++, Side.CLIENT);
		Main.NETWORK.registerMessage(BucketStateMessageHandler.class, BucketStateMessage.class, discriminator++, Side.SERVER);
	}
}
