package jaminv.advancedmachines.proxy.handlers;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.lib.util.registry.RegistryHelper;
import jaminv.advancedmachines.machine.expansion.BlockMachineProductivity;
import jaminv.advancedmachines.machine.expansion.BlockMachineSpeed;
import jaminv.advancedmachines.machine.expansion.TileMachineExpansion;
import jaminv.advancedmachines.machine.expansion.energy.BlockMachineEnergy;
import jaminv.advancedmachines.machine.expansion.energy.TileMachineEnergy;
import jaminv.advancedmachines.machine.expansion.inventory.BlockMachineInventory;
import jaminv.advancedmachines.machine.expansion.inventory.TileMachineInventory;
import jaminv.advancedmachines.machine.expansion.multiply.BlockMachineMultiply;
import jaminv.advancedmachines.machine.expansion.multiply.TileMachineMultiply;
import jaminv.advancedmachines.machine.expansion.redstone.BlockMachineRedstone;
import jaminv.advancedmachines.machine.expansion.redstone.TileMachineRedstone;
import jaminv.advancedmachines.machine.expansion.tank.BlockMachineTank;
import jaminv.advancedmachines.machine.expansion.tank.TileMachineTank;
import jaminv.advancedmachines.machine.instance.alloy.BlockMachineAlloy;
import jaminv.advancedmachines.machine.instance.alloy.TileMachineAlloy;
import jaminv.advancedmachines.machine.instance.furnace.BlockMachineFurnace;
import jaminv.advancedmachines.machine.instance.furnace.TileMachineFurnace;
import jaminv.advancedmachines.machine.instance.grinder.BlockMachineGrinder;
import jaminv.advancedmachines.machine.instance.grinder.TileMachineGrinder;
import jaminv.advancedmachines.machine.instance.injector.BlockMachineInjector;
import jaminv.advancedmachines.machine.instance.injector.TileMachineInjector;
import jaminv.advancedmachines.machine.instance.melter.BlockMachineMelter;
import jaminv.advancedmachines.machine.instance.melter.TileMachineMelter;
import jaminv.advancedmachines.machine.instance.purifier.BlockMachinePurifier;
import jaminv.advancedmachines.machine.instance.purifier.TileMachinePurifier;
import jaminv.advancedmachines.machine.instance.stabilizer.BlockMachineStabilizer;
import jaminv.advancedmachines.machine.instance.stabilizer.TileMachineStabilizer;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import jaminv.advancedmachines.util.Reference;
import jaminv.advancedmachines.util.network.MessageRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
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
		//event.getRegistry().registerAll(ItemInit.ITEMS.toArray(new Item[0]));
		RegistryHelper.registerItems(event);
	}
	
	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event) {
		/*FluidInit.registerFluids();
		event.getRegistry().registerAll(BlockInit.BLOCKS.toArray(new Block[0]));
		
		for (Block block : BlockInit.BLOCKS) {
			if (block instanceof IHasTileEntity) {
				GameRegistry.registerTileEntity(((IHasTileEntity)block).getTileEntityClass(), block.getUnlocalizedName());
			}
		}*/
		
		for (VariantExpansion variant : VariantExpansion.values()) {
			RegistryHelper.addBlockWithBakedModel(new BlockMachineMultiply(variant), "machine_expansion_" + variant.getName());
			RegistryHelper.addBlockWithBakedModel(new BlockMachineSpeed(variant), "machine_speed_" + variant.getName());
			RegistryHelper.addBlockWithBakedModel(new BlockMachineProductivity(variant), "machine_productivity_" + variant.getName());
			RegistryHelper.addBlockWithBakedModel(new BlockMachineInventory(variant), "machine_inventory_" + variant.getName());
			RegistryHelper.addBlockWithBakedModel(new BlockMachineEnergy(variant), "machine_energy_" + variant.getName());
			RegistryHelper.addBlockWithBakedModel(new BlockMachineRedstone(variant), "machine_redstone_" + variant.getName());
			RegistryHelper.addBlockWithBakedModel(new BlockMachineTank(variant), "machine_tank_" + variant.getName());
			
			RegistryHelper.addBlockWithBakedModel(new BlockMachineFurnace(variant), "machine_furnace_" + variant.getName());
			RegistryHelper.addBlockWithBakedModel(new BlockMachineGrinder(variant), "machine_grinder_" + variant.getName());
			RegistryHelper.addBlockWithBakedModel(new BlockMachineAlloy(variant), "machine_alloy_" + variant.getName());
			RegistryHelper.addBlockWithBakedModel(new BlockMachinePurifier(variant), "machine_purifier_" + variant.getName());
			RegistryHelper.addBlockWithBakedModel(new BlockMachineMelter(variant), "machine_melter_" + variant.getName());
			RegistryHelper.addBlockWithBakedModel(new BlockMachineStabilizer(variant), "machine_stabilizer_" + variant.getName());
			RegistryHelper.addBlockWithBakedModel(new BlockMachineInjector(variant), "machine_injector_" + variant.getName());
		}
		GameRegistry.registerTileEntity(TileMachineMultiply.class, new ResourceLocation("tile_machine_expansion"));
		GameRegistry.registerTileEntity(TileMachineExpansion.class, new ResourceLocation("tile_machine_expansion_type"));
		GameRegistry.registerTileEntity(TileMachineInventory.class, new ResourceLocation("tile_machine_inventory"));
		GameRegistry.registerTileEntity(TileMachineEnergy.class, new ResourceLocation("tile_machine_energy"));
		GameRegistry.registerTileEntity(TileMachineRedstone.class, new ResourceLocation("tile_machine_redstone"));
		GameRegistry.registerTileEntity(TileMachineTank.class, new ResourceLocation("tile_machine_tank"));
		
		GameRegistry.registerTileEntity(TileMachineFurnace.class, new ResourceLocation("tile_machine_furnace"));
		GameRegistry.registerTileEntity(TileMachineGrinder.class, new ResourceLocation("tile_machine_grinder"));
		GameRegistry.registerTileEntity(TileMachineAlloy.class, new ResourceLocation("tile_machine_alloy"));
		GameRegistry.registerTileEntity(TileMachinePurifier.class, new ResourceLocation("tile_machine_purifier"));
		GameRegistry.registerTileEntity(TileMachineMelter.class, new ResourceLocation("tile_machine_melter"));
		GameRegistry.registerTileEntity(TileMachineStabilizer.class, new ResourceLocation("tile_machine_stabilizer"));
		GameRegistry.registerTileEntity(TileMachineInjector.class, new ResourceLocation("tile_machine_injector"));
		
		RegistryHelper.registerBlocks(event);
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onModelRegister(ModelRegistryEvent event) {
		/*for (Item item : ItemInit.ITEMS) {
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
		}*/
		
		RegistryHelper.registerModels();
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
