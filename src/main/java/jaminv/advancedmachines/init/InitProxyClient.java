package jaminv.advancedmachines.init;

import jaminv.advancedmachines.AdvancedMachines;
import jaminv.advancedmachines.ModReference;
import jaminv.advancedmachines.init.handlers.EventHandlerClient;
import jaminv.advancedmachines.lib.parser.DataParser;
import jaminv.advancedmachines.lib.render.BakedModelLoader;
import jaminv.advancedmachines.render.textureset.FileHandlerTextureSet;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class InitProxyClient implements InitProxy {
	@Override
	public void preInit(FMLPreInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register(EventHandlerClient.class);
		
		AdvancedMachines.logger.info("Loading Baked Models");
		ModelLoaderRegistry.registerLoader(new BakedModelLoader());
	}
	
	@Override
	public void init(FMLInitializationEvent e) {}

	@Override
	public void postInit(FMLPostInitializationEvent e) {
		AdvancedMachines.logger.info("Loading Texture Sets");
		DataParser.parseFolder(ModReference.MODID, "data/texturesets", new FileHandlerTextureSet());		
	}

	@Override
	public World getMessageWorld(MessageContext ctx) {
		return Minecraft.getMinecraft().world;
	}
}
