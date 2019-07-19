package jaminv.advancedmachines;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

import jaminv.advancedmachines.proxy.CommonProxy;
import jaminv.advancedmachines.util.Reference;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, dependencies = "required-after:forge@[11.16.0.1865,)", useMetadata = true)
public class Main {
	
	@Instance
	public static Main instance;
	
	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID);
	
	@SidedProxy(clientSide = Reference.CLIENT, serverSide = Reference.COMMON)
	public static CommonProxy proxy;
	
	public static Logger logger;
	
	@EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        
/*        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        builder.setStatusLevel(Level.ALL);
        builder.setConfigurationName(Reference.MODID + ".parser");
        AppenderComponentBuilder appenderBuilder = builder.newAppender("parser", "File")
        	.addAttribute("fileName",  "logs/" + Reference.FILENAME + ".parser.log")
        	.addAttribute("append",  true);
        builder.add(appenderBuilder);
        builder.add(builder.newLogger(Reference.MODID + ".parser", Level.TRACE)
        	.add(builder.newAppenderRef("parser"))
        	.addAttribute("additivity", false));
        Configurator.initialize(builder.build()); */
        
        //LogManager.getLogger();
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }
}
