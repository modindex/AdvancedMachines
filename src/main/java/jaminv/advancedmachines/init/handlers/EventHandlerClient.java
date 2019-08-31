package jaminv.advancedmachines.init.handlers;

import jaminv.advancedmachines.lib.util.registry.RegistryHelper;
import jaminv.advancedmachines.render.RawTextures;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerClient {
	@SubscribeEvent
	public static void handleTextureStitchPreEvent(TextureStitchEvent.Pre event) {
		RawTextures.registerTextures(event.getMap(), "blocks/machine");
	}
	
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		RegistryHelper.registerModels();
	}	
}
