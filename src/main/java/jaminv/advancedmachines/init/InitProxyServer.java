package jaminv.advancedmachines.init;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class InitProxyServer implements InitProxy {

	@Override public void preInit(FMLPreInitializationEvent e) {}
	@Override public void init(FMLInitializationEvent e) {}
	@Override public void postInit(FMLPostInitializationEvent e) {}
	
	@Override
	public World getMessageWorld(MessageContext ctx) {
		return ctx.getServerHandler().player.getServerWorld();
	}	
}
