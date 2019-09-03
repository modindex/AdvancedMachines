package jaminv.advancedmachines;

import org.apache.logging.log4j.Logger;

import jaminv.advancedmachines.init.GuiProxy;
import jaminv.advancedmachines.init.InitProxy;
import jaminv.advancedmachines.init.handlers.EventHandlerCommon;
import jaminv.advancedmachines.init.init.RecipeInit;
import jaminv.advancedmachines.lib.parser.DataParser;
import jaminv.advancedmachines.lib.parser.FileHandlerOreDictionary;
import jaminv.advancedmachines.util.network.MessageRegistry;
import jaminv.advancedmachines.world.gen.WorldGenCustomOres;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, dependencies = "required-after:forge@[14.23.2.2638,)", useMetadata = true)
public class AdvancedMachines {
	
	@Instance
	public static AdvancedMachines instance;
	
	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID);
	
	@SidedProxy(clientSide = Reference.CLIENT, serverSide = Reference.SERVER)
	public static InitProxy proxy;
	// %1.13%:
	//public static InitProxy proxy = DistExecutor.runForDist(() -> InitProxyClient::new, () -> InitProxyServer::new);
	
	public static Logger logger;
	
	static {
		FluidRegistry.enableUniversalBucket();
	}
	
	@EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("Preinit");
        
		MinecraftForge.EVENT_BUS.register(EventHandlerCommon.class);
		
		logger.info("Network Registry");
		MessageRegistry.register();
		
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
    	logger.info("Init");

		AdvancedMachines.logger.info("WorldGen Registry");
		GameRegistry.registerWorldGenerator(new WorldGenCustomOres(), 0);	
    	
    	logger.info("Parse Constants");
		DataParser.parseConstants();
		logger.info("Ore Dictionary Registry");
		DataParser.parseFolder("data/ore_dictionary", new FileHandlerOreDictionary());
		
		NetworkRegistry.INSTANCE.registerGuiHandler(AdvancedMachines.instance, new GuiProxy());

		proxy.init(e);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
		logger.info("Recipe Initialization");
		RecipeInit.init();    	
    	
        proxy.postInit(e);
    }
}
