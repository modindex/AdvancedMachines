package jaminv.advancedmachines.proxy;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.client.textureset.FileHandlerTextureSet;
import jaminv.advancedmachines.lib.render.BakedModelLoader;
import jaminv.advancedmachines.proxy.handlers.EventHandlerClient;
import jaminv.advancedmachines.util.Reference;
import jaminv.advancedmachines.util.network.MessageRegistry;
import jaminv.advancedmachines.util.parser.DataParser;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
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
		
		MessageRegistry.registerClient();
	}
	
	
	
	@Override
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
		
		DataParser.parseFolder("data/texturesets", new FileHandlerTextureSet());		
	}

	@Override
	public void registerItemRenderer(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
	}
	
	@Override
	public void registerVariantRenderer(Item item, int meta, String filename, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(Reference.MODID, filename), id));
	}
}
