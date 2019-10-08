package jaminv.advancedmachines.util.network;

import jaminv.advancedmachines.AdvancedMachines;
import jaminv.advancedmachines.machine.ProcessingStateMessage;
import jaminv.advancedmachines.machine.ProcessingStateMessage.ProcessingStateMessageHandler;
import jaminv.advancedmachines.machine.multiblock.network.MultiblockDestroyMessage;
import jaminv.advancedmachines.machine.multiblock.network.MultiblockDestroyMessage.MultiblockDestroyMessageHandler;
import jaminv.advancedmachines.machine.multiblock.network.MultiblockUpdateMessage;
import jaminv.advancedmachines.machine.multiblock.network.MultiblockUpdateMessage.MultiblockUpdateMessageHandler;
import jaminv.advancedmachines.util.network.BucketStateMessage.BucketStateMessageHandler;
import jaminv.advancedmachines.util.network.IOStateMessage.IOMessageHandler;
import jaminv.advancedmachines.util.network.RedstoneStateMessage.RedstoneStateMessageHandler;
import net.minecraftforge.fml.relauncher.Side;

public class MessageRegistry {
	static int discriminator = 0;

	public static void register() {
		AdvancedMachines.NETWORK.registerMessage(IOMessageHandler.class, IOStateMessage.class, discriminator++, Side.SERVER);
		AdvancedMachines.NETWORK.registerMessage(RedstoneStateMessageHandler.class, RedstoneStateMessage.class, discriminator++, Side.SERVER);
		AdvancedMachines.NETWORK.registerMessage(BucketStateMessageHandler.class, BucketStateMessage.class, discriminator++, Side.SERVER);
		AdvancedMachines.NETWORK.registerMessage(MultiblockUpdateMessageHandler.class, MultiblockUpdateMessage.class, discriminator++, Side.CLIENT);
		AdvancedMachines.NETWORK.registerMessage(MultiblockDestroyMessageHandler.class, MultiblockDestroyMessage.class, discriminator++, Side.CLIENT);
		AdvancedMachines.NETWORK.registerMessage(ProcessingStateMessageHandler.class, ProcessingStateMessage.class, discriminator++, Side.CLIENT);
	}
}
