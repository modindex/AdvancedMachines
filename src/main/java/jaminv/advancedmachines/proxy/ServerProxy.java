package jaminv.advancedmachines.proxy;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerProxy extends CommonProxy {

	@Override
	public World getMessageWorld(MessageContext ctx) {
		return ctx.getServerHandler().player.getServerWorld();
	}
}
