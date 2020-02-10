package net.journey;

import java.util.ArrayList;

import net.journey.misc.EnchantmentHotTouch;
import net.journey.misc.EnchantmentWaterWalk;
import net.journey.proxy.CommonProxy;
import net.journey.util.recipes.JourneyRecipes;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.slayer.api.SlayerAPI;

@Mod(name = SlayerAPI.MOD_NAME, modid = SlayerAPI.MOD_ID, version = SlayerAPI.MOD_VERSION)
public class JITL {

	@Instance(SlayerAPI.MOD_ID)
	public static JITL instance;

	@SidedProxy(clientSide = "net.journey.proxy.ClientProxy", serverSide = "net.journey.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static final Enchantment hotTouch = new EnchantmentHotTouch();
	public static final Enchantment waterWalk = new EnchantmentWaterWalk();
			
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
		proxy.registerClient();
		proxy.clientPreInit();
		ArrayList<String> author = new ArrayList<String>();
		author.add("The_SlayerMC");
		event.getModMetadata().autogenerated = false;
		event.getModMetadata().credits = 
				SlayerAPI.Colour.AQUA +		 "The_SlayerMC - Owner/Creator/Developer/Code/Assets " + 
				SlayerAPI.Colour.GREEN +	 "Dash - Code Helper/Gen Tweaker" + 
				SlayerAPI.Colour.BLUE + 	 "Dizzlepop12 - Artist/Models/Code/Assistant Developer, ";
		event.getModMetadata().description = "A full progressional experience that adds Weapons, Mobs, Dimensions, Bosses, and much more to your game.";
		event.getModMetadata().modId = SlayerAPI.MOD_ID;
		event.getModMetadata().url = "slayermods.net";
		event.getModMetadata().name = SlayerAPI.MOD_NAME;
		event.getModMetadata().version = SlayerAPI.MOD_VERSION;
		event.getModMetadata().logoFile = "assets/journey/textures/logo.png";
		event.getModMetadata().authorList = author;
	}

	@EventHandler
	public static void init(FMLInitializationEvent event) {
		proxy.init(event);
		proxy.clientInit(event);
		JourneyRecipes recipe = new JourneyRecipes();
	}

	@EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
		proxy.registerSounds();
	}

	@EventHandler
	public static void serverStarting(FMLServerStartingEvent event) {
		SlayerAPI.logger.info("Starting server...");
		proxy.serverStarting(event);
	}
}
