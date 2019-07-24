package jaminv.advancedmachines.client;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.util.Reference;
import net.minecraft.util.ResourceLocation;

public class MultiblockTextures {
	
	public final static String[] machines = { "alloy", "furnace", "grinder", "purifier" }; 
	public final static String[] materials = { "basic", "compressed", "quad", "improbable" };
	public final static String[] files = { "all", "bottom", "bottom_left", "bottom_left_right", "bottom_right", "left", "left_right", "none", 
			"right", "top", "top_bottom", "top_bottom_left", "top_bottom_right", "top_left", "top_left_right", "top_right"
		};
	public final static String[] states = { "active", "inactive" };
	public final static ImmutableMap<String, ResourceLocation> resources;
	public final static ImmutableSet<ResourceLocation> textures;
	
	static {
		Main.logger.info("Loading static textures");
		
		ImmutableMap.Builder<String, ResourceLocation> map = ImmutableMap.<String, ResourceLocation>builder();
		ImmutableSet.Builder<ResourceLocation> set = ImmutableSet.<ResourceLocation>builder();
		
		for (String mat : materials) {
			for (String file : files) {
				ResourceLocation res = new ResourceLocation(Reference.MODID, "blocks/machine/expansion/" + mat + "/" + file);
				map.put("expansion." + mat + "." + file, res);
				set.add(res);
			}
		}
		
		for (String machine : machines) {
			for (String state : states) {
				for (String material : materials) {
					for (String file : files) {
						ResourceLocation res = new ResourceLocation(Reference.MODID, "blocks/machine/instance/" + machine + "/" + state + "/" + material + "/" + file);
						map.put(machine + "." + state + "." + material + "." + file, res);
						set.add(res);
					}

					if (machine.equals("furnace")) {
						for (int x = 0; x < 3; x++) {
							for (int y = 0; y < 3; y++) {
								ResourceLocation res = new ResourceLocation(Reference.MODID, "blocks/machine/instance/" + machine + "/" + state + "/" + material + "/f3x3p" + x + y);
								map.put(machine + "." + state + "." + material + "." + "f3x3p" + Integer.toString(x) + Integer.toString(y), res);
								set.add(res);
							}
						}
						
						for (int x = 0; x < 2; x++) {
							for (int y = 0; y < 2; y++) {
								ResourceLocation res = new ResourceLocation(Reference.MODID, "blocks/machine/instance/" + machine + "/" + state + "/" + material + "/f2x2p" + x + y);
								map.put(machine + "." + state + "." + material + "." + "f2x2p" + Integer.toString(x) + Integer.toString(y), res);
								set.add(res);
							}
						}
					}
				}
			}
		}
		
		resources = map.build();
		textures = set.build();

		Main.logger.info("Completed - Loading static textures");
	};	
}
