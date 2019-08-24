package jaminv.advancedmachines.proxy.handlers;

import jaminv.advancedmachines.client.RawTextures;
import jaminv.advancedmachines.lib.render.BakedModelLoader;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerClient {
	public static final EventHandlerClient INSTANCE = new EventHandlerClient();

	@SubscribeEvent
	public void handleTextureStitchPreEvent(TextureStitchEvent.Pre event) {
		RawTextures.registerTextures(event.getMap(), "blocks/machine");
	}
}
