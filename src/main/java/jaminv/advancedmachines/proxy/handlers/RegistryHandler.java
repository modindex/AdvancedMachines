package jaminv.advancedmachines.proxy.handlers;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.init.BlockInit;
import jaminv.advancedmachines.init.FluidInit;
import jaminv.advancedmachines.init.ItemInit;
import jaminv.advancedmachines.lib.render.BakedModelLoader;
import jaminv.advancedmachines.lib.render.ModelBakeryProvider;
import jaminv.advancedmachines.util.Reference;
import jaminv.advancedmachines.util.interfaces.IHasModel;
import jaminv.advancedmachines.util.interfaces.IHasTileEntity;
import jaminv.advancedmachines.util.network.MessageRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber(modid = Reference.MODID)
public class RegistryHandler {
	protected static class CustomStateMapper extends StateMapperBase {
		protected ModelResourceLocation loc;
		public CustomStateMapper(ModelResourceLocation resource) {
			this.loc = resource;			
		}

		@Override
		protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
			return loc;
		}
	}	

	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(ItemInit.ITEMS.toArray(new Item[0]));
	}
	
	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event) {
		FluidInit.registerFluids();
		event.getRegistry().registerAll(BlockInit.BLOCKS.toArray(new Block[0]));
		
		for (Block block : BlockInit.BLOCKS) {
			if (block instanceof IHasTileEntity) {
				GameRegistry.registerTileEntity(((IHasTileEntity)block).getTileEntityClass(), block.getUnlocalizedName());
			}
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onModelRegister(ModelRegistryEvent event) {
		for (Item item : ItemInit.ITEMS) {
			if (item instanceof IHasModel) {
				((IHasModel)item).registerModels();
			}
		}
		
		for (Block block : BlockInit.BLOCKS) {
			if (block instanceof IHasModel) {
				((IHasModel)block).registerModels();
			}
			
			if (block instanceof ModelBakeryProvider) {
				ModelResourceLocation resource = new ModelResourceLocation(block.getRegistryName(), "normal");
				ModelLoader.setCustomStateMapper(block, new CustomStateMapper(resource));
				BakedModelLoader.register(resource, ((ModelBakeryProvider)block).getModelBakery());				
			}
		}
	}
	
	public static void otherRegistries() {
		Main.logger.info("Message Registry");
		MessageRegistry.register();
		Main.logger.info("Complete - Message Registry");

		Main.logger.info("World Generation Registry");
		//GameRegistry.registerWorldGenerator(new WorldGenCustomOres(), 0);
		Main.logger.info("Complete - World Generation Registry");
	}
}
