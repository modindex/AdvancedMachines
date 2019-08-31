package jaminv.advancedmachines.proxy;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.client.textureset.FileHandlerTextureSet;
import jaminv.advancedmachines.lib.render.BakedModelLoader;
import jaminv.advancedmachines.lib.util.parser.DataParser;
import jaminv.advancedmachines.proxy.handlers.EventHandlerClient;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		MinecraftForge.EVENT_BUS.register(EventHandlerClient.INSTANCE);
		
		Main.logger.info("Loading Baked Models");
		ModelLoaderRegistry.registerLoader(new BakedModelLoader());
		Main.logger.info("Completed - Loading Baked Models");
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
		
		DataParser.parseFolder("data/texturesets", new FileHandlerTextureSet());		
	}

	@Override
	public World getMessageWorld(MessageContext ctx) {
		return Minecraft.getMinecraft().world;
	}
}
