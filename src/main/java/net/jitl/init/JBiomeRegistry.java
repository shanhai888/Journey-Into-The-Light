package net.jitl.init;

import net.jitl.JITL;
import net.jitl.common.dimension.JBiomeProvider;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class JBiomeRegistry {

    public static Biome EUCA_PLAINS;
    public static Biome EUCA_GOLDITE_GRAINS;
    public static Biome EUCA_SILVER_PLAINS;

    public static Biome FROZEN_WASTES;
    public static Biome FROZEN_DYING_FOREST;
    public static Biome FROZEN_BITTERWOOD_FOREST;

    private static RegistryKey<Biome> registerBiome(String name) {
        return RegistryKey.create(Registry.BIOME_REGISTRY, JITL.rl(name));
    }

    @SubscribeEvent
    public static void registerBiomes(RegistryEvent.Register<Biome> event) {
        EUCA_PLAINS.setRegistryName(JITL.rl("euca/euca_plains"));
        event.getRegistry().register(EUCA_PLAINS);

        EUCA_GOLDITE_GRAINS.setRegistryName(JITL.rl("euca/euca_goldite_grains"));
        event.getRegistry().register(EUCA_GOLDITE_GRAINS);

        EUCA_SILVER_PLAINS.setRegistryName(JITL.rl("euca/euca_silver_plains"));
        event.getRegistry().register(EUCA_SILVER_PLAINS);

        FROZEN_WASTES.setRegistryName(JITL.rl("frozen/frozen_wastes"));
        event.getRegistry().register(FROZEN_WASTES);

        FROZEN_DYING_FOREST.setRegistryName(JITL.rl("frozen/dying_forest"));
        event.getRegistry().register(FROZEN_DYING_FOREST);

        FROZEN_BITTERWOOD_FOREST.setRegistryName(JITL.rl("frozen/bitterwood_forest"));
        event.getRegistry().register(FROZEN_BITTERWOOD_FOREST);
    }

    public static void registerProviders() {
        Registry.register(Registry.BIOME_SOURCE, JITL.rl("base_biome_provider"), JBiomeProvider.CODEC);
        //Registry.register(Registry.BIOME_SOURCE, JITL.rl("frozen_provider"), FrozenBiomeProvider.CODEC);
    }
}
