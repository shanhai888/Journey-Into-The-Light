package net.jitl.init;

import net.jitl.JITL;
import net.jitl.common.world.gen.features.CaveVinesFeature;
import net.jitl.common.world.gen.features.RuinsFeature;
import net.jitl.common.world.gen.features.euca.EucaBotSpawner;
import net.jitl.common.world.gen.features.euca.EucaTreeFeature;
import net.jitl.common.world.gen.features.featureconfig.EucaSpawnerFeatureConfig;
import net.jitl.common.world.gen.features.featureconfig.EucaTreeFeatureConfig;
import net.jitl.common.world.gen.features.featureconfig.RuinsFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import ru.timeconqueror.timecore.api.registry.SimpleForgeRegister;
import ru.timeconqueror.timecore.api.registry.util.AutoRegistrable;

public class JFeatures {
    @AutoRegistrable
    private static final SimpleForgeRegister<Feature<?>> REGISTER = new SimpleForgeRegister<>(ForgeRegistries.FEATURES, JITL.MODID);

    public static final RegistryObject<Feature<RuinsFeatureConfig>> RUINS = REGISTER.register("ruins", () -> new RuinsFeature(RuinsFeatureConfig.CODEC));
    public static final RegistryObject<Feature<NoFeatureConfig>> CAVE_VINES = REGISTER.register("cave_vines", () -> new CaveVinesFeature(NoFeatureConfig.CODEC));
    public static final RegistryObject<Feature<EucaTreeFeatureConfig>> EUCA_TREE = REGISTER.register("euca_tree", () -> new EucaTreeFeature(EucaTreeFeatureConfig.CODEC));

    public static final RegistryObject<Feature<EucaSpawnerFeatureConfig>> EUCA_BOT_SPAWNER = REGISTER.register("euca_bot_spawner", () -> new EucaBotSpawner(EucaSpawnerFeatureConfig.CODEC));

}
