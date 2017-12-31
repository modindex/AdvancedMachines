package mcfactory.mod;

import org.apache.logging.log4j.Logger;

import mcfactory.mod.proxy.CommonProxy;
import mcfactory.mod.util.Reference;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, dependencies = "required-after:forge@[11.16.0.1865,)", useMetadata = true)
public class Main {
	
	@Instance
	public static Main instance;
	
	@SidedProxy(clientSide = Reference.CLIENT, serverSide = Reference.COMMON)
	public static CommonProxy proxy;
	
	public static Logger logger;
	
	@EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        //proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
        //proxy.init(e);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        //proxy.postInit(e);
    }
}
