package net.jitl.core.init.world;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.jitl.common.world.gen.features.featureconfig.FrostyIceClusterFeatureConfig;
import net.jitl.common.world.gen.features.featureconfig.IcicleFeatureConfig;
import net.jitl.common.world.gen.features.featureconfig.LargeIcicleFeatureConfig;
import net.jitl.common.world.gen.features.featureconfig.RuinsFeatureConfig;
import net.jitl.common.world.gen.foliageplacer.SphericalFoliagePlacer;
import net.jitl.common.world.gen.treedecorator.*;
import net.jitl.core.JITL;
import net.jitl.core.init.JBlocks;
import net.jitl.core.init.JTags;
import net.jitl.core.util.JRuleTests;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.PineFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.SpruceFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.ForkingTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.GiantTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import ru.timeconqueror.timecore.api.registry.SimpleVanillaRegister;
import ru.timeconqueror.timecore.api.registry.util.AutoRegistrable;
import ru.timeconqueror.timecore.api.registry.util.Promised;

import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("Convert2MethodRef")
public class JConfiguredFeatures {
    public static final RuleTest STONE_ORE_REPLACEABLES = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
    public static final RuleTest DEEPSLATE_ORE_REPLACEABLES = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
    public static final RuleTest EUCA_ORE_REPLACEABLES = new TagMatchTest(JTags.EUCA_STONE_ORE_REPLACEABLES);
    public static final RuleTest BOIL_ORE_REPLACEABLES = new TagMatchTest(JTags.BOIL_STONE_ORE_REPLACEABLES);
    public static final RuleTest FROZEN_ORE_REPLACEABLES = new TagMatchTest(JTags.FROZEN_STONE_ORE_REPLACEABLES);

    //FIXME lunium ore is null
    /*public static final List<OreConfiguration.TargetBlockState> ORE_LUNIUM_TARGET_LIST = List.of(
            OreConfiguration.target(STONE_ORE_REPLACEABLES, JBlocks.LUNIUM_ORE.defaultBlockState()),
            OreConfiguration.target(DEEPSLATE_ORE_REPLACEABLES, JBlocks.DEEPSLATE_LUNIUM_ORE.defaultBlockState()));*/

    @AutoRegistrable
    private static final SimpleVanillaRegister<ConfiguredFeature<?, ?>> REGISTER = new SimpleVanillaRegister<>(JITL.MODID, BuiltinRegistries.CONFIGURED_FEATURE);

    public static final Promised<? extends ConfiguredFeature<?, ?>> TARTBERRY_BUSH =
            REGISTER.register("tartberry_bush", surfacePatchFeature(() -> JBlocks.TARTBERRY_BUSH.defaultBlockState()));

    public static final Promised<? extends ConfiguredFeature<?, ?>> FROST_CRYSTAL =
            REGISTER.register("frost_crystal", surfacePatchFeature(() -> JBlocks.FROST_CRYSTAL_LARGE.defaultBlockState()));

    public static final Promised<? extends ConfiguredFeature<?, ?>> REDCURRANT_BUSH =
            REGISTER.register("redcurrant_bush", surfacePatchFeature(() -> JBlocks.REDCURRANT_BUSH.defaultBlockState()));

    public static final Promised<? extends ConfiguredFeature<?, ?>> DEFAULT_OVERWORLD_RUINS =
            REGISTER.register("default_overworld_ruins",
                    () -> JFeatures.RUINS.get()
                            .configured(new RuinsFeatureConfig(
                                    JRuleTests.GRASS,
                                    new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                                            .add(Blocks.STONE_BRICKS.defaultBlockState(), 6)
                                            .add(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 5)
                                            .add(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 4)
                                            .add(Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 3)
                                            .add(Blocks.COBBLESTONE.defaultBlockState(), 4)
                                            .add(Blocks.CHISELED_STONE_BRICKS.defaultBlockState(), 2)
                                            .add(Blocks.INFESTED_COBBLESTONE.defaultBlockState(), 1)
                                            .add(Blocks.INFESTED_STONE_BRICKS.defaultBlockState(), 1)
                                            .add(Blocks.INFESTED_MOSSY_STONE_BRICKS.defaultBlockState(), 1)
                                            .add(Blocks.INFESTED_CRACKED_STONE_BRICKS.defaultBlockState(), 1)),
                                    5,
                                    5,
                                    8,
                                    BuiltInLootTables.ABANDONED_MINESHAFT)));

    public static final Promised<? extends ConfiguredFeature<?, ?>> DESERT_OVERWORLD_RUINS =
            REGISTER.register("desert_overworld_ruins",
                    () -> JFeatures.RUINS.get()
                            .configured(new RuinsFeatureConfig(
                                    JRuleTests.SAND,
                                    new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                                            .add(Blocks.SANDSTONE.defaultBlockState(), 3)
                                            .add(Blocks.CHISELED_SANDSTONE.defaultBlockState(), 1)
                                            .add(Blocks.CUT_SANDSTONE.defaultBlockState(), 2)),
                                    5,
                                    5,
                                    8,
                                    BuiltInLootTables.DESERT_PYRAMID)));

    public static final Promised<? extends ConfiguredFeature<BlockColumnConfiguration, ?>> DEEPVINE_VEG =
            REGISTER.register("deepvine_veg",
                    () -> Feature.BLOCK_COLUMN.configured(
                            new BlockColumnConfiguration(List.of(
                                    BlockColumnConfiguration.layer(
                                            new WeightedListInt(
                                                    SimpleWeightedRandomList.<IntProvider>builder()
                                                            .add(UniformInt.of(0, 3), 5)
                                                            .add(UniformInt.of(1, 7), 1)
                                                            .build()),
                                            BlockStateProvider.simple(JBlocks.DEEPVINE_PLANT.defaultBlockState())),
                                    BlockColumnConfiguration.layer(
                                            ConstantInt.of(1),
                                            BlockStateProvider.simple(JBlocks.DEEPVINE.defaultBlockState()))),
                                    Direction.DOWN,
                                    BlockPredicate.ONLY_IN_AIR_PREDICATE,
                                    true)));

    public static final Promised<? extends ConfiguredFeature<?, ?>> DEEPVINE =
            REGISTER.register("deepvine",
                    () -> Feature.VEGETATION_PATCH.configured(
                            new VegetationPatchConfiguration(
                                    BlockTags.MOSS_REPLACEABLE.getName(),
                                    BlockStateProvider.simple(Blocks.DEEPSLATE),
                                    () -> DEEPVINE_VEG.get().placed(),
                                    CaveSurface.CEILING,
                                    UniformInt.of(1, 2),
                                    0.0F,
                                    5,
                                    0.08F,
                                    UniformInt.of(4, 7),
                                    0.3F)));

    public static final Promised<? extends ConfiguredFeature<?, ?>> TALL_GLOWSHROOMS_VEG =
            REGISTER.register("tall_glowshrooms_veg",
                    () -> Feature.RANDOM_PATCH.configured(
                            FeatureUtils.simplePatchConfiguration(
                                    Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(
                                            new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                                                    .add(JBlocks.BLUE_GLOWSHROOM.defaultBlockState(), 3)
                                                    .add(JBlocks.GREEN_GLOWSHROOM.defaultBlockState(), 4)
                                                    .add(JBlocks.RED_GLOWSHROOM.defaultBlockState(), 2)
                                                    .add(JBlocks.TALL_BLUE_GLOWSHROOM.defaultBlockState(), 3)
                                                    .add(JBlocks.TALL_GREEN_GLOWSHROOM.defaultBlockState(), 4)
                                                    .add(JBlocks.TALL_RED_GLOWSHROOM.defaultBlockState(), 2)))))));

    public static final Promised<? extends ConfiguredFeature<?, ?>> TALL_GLOWSHROOMS =
            REGISTER.register("tall_glowshrooms",
                    () -> Feature.VEGETATION_PATCH.configured(
                            new VegetationPatchConfiguration(
                                    BlockTags.MOSS_REPLACEABLE.getName(),
                                    BlockStateProvider.simple(JBlocks.DEEP_MYCELIUM),
                                    () -> TALL_GLOWSHROOMS_VEG.get().placed(),
                                    CaveSurface.FLOOR,
                                    ConstantInt.of(1),
                                    0.0F,
                                    1,
                                    0.05F,
                                    UniformInt.of(4, 7),
                                    0.1F)));

    public static final Promised<? extends ConfiguredFeature<?, ?>> SMALL_GLOWSHROOMS_VEG =
            REGISTER.register("small_glowshrooms_veg",
                    () -> Feature.RANDOM_PATCH.configured(
                            FeatureUtils.simplePatchConfiguration(
                                    Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(
                                            new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                                                    .add(JBlocks.BLUE_GLOWSHROOM.defaultBlockState(), 3)
                                                    .add(JBlocks.GREEN_GLOWSHROOM.defaultBlockState(), 4)
                                                    .add(JBlocks.RED_GLOWSHROOM.defaultBlockState(), 2)))))));

    public static final Promised<? extends ConfiguredFeature<?, ?>> SMALL_GLOWSHROOMS =
            REGISTER.register("small_glowshrooms",
                    () -> Feature.VEGETATION_PATCH.configured(
                            new VegetationPatchConfiguration(
                                    BlockTags.MOSS_REPLACEABLE.getName(),
                                    BlockStateProvider.simple(JBlocks.DEEP_MYCELIUM),
                                    () -> SMALL_GLOWSHROOMS_VEG.get().placed(),
                                    CaveSurface.FLOOR,
                                    ConstantInt.of(1),
                                    0.0F,
                                    1,
                                    0.05F,
                                    UniformInt.of(4, 7),
                                    0.1F)));

    public static final Promised<? extends ConfiguredFeature<?, ?>> LUNIUM_ORE =
            REGISTER.register("lunium_ore", () -> Feature.ORE.configured(new OreConfiguration(List.of(
                    OreConfiguration.target(STONE_ORE_REPLACEABLES, JBlocks.LUNIUM_ORE.defaultBlockState()),
                    OreConfiguration.target(DEEPSLATE_ORE_REPLACEABLES, JBlocks.DEEPSLATE_LUNIUM_ORE.defaultBlockState())),
                    9)));

    public static final Promised<? extends ConfiguredFeature<?, ?>> LUNIUM_ORE_BURIED =
            REGISTER.register("lunium_ore_buried", () -> Feature.ORE.configured(new OreConfiguration(List.of(
                    OreConfiguration.target(STONE_ORE_REPLACEABLES, JBlocks.LUNIUM_ORE.defaultBlockState()),
                    OreConfiguration.target(DEEPSLATE_ORE_REPLACEABLES, JBlocks.DEEPSLATE_LUNIUM_ORE.defaultBlockState())),
                    9,
                    0.5F)));

    public static final Promised<? extends ConfiguredFeature<?, ?>> SAPPHIRE_ORE =
            REGISTER.register("sapphire_ore", () -> Feature.ORE.configured(new OreConfiguration(List.of(
                    OreConfiguration.target(STONE_ORE_REPLACEABLES, JBlocks.SAPPHIRE_ORE.defaultBlockState()),
                    OreConfiguration.target(DEEPSLATE_ORE_REPLACEABLES, JBlocks.DEEPSLATE_SAPPHIRE_ORE.defaultBlockState())),
                    9)));

    public static final Promised<? extends ConfiguredFeature<?, ?>> SAPPHIRE_ORE_BURIED =
            REGISTER.register("sapphire_ore_buried", () -> Feature.ORE.configured(new OreConfiguration(List.of(
                    OreConfiguration.target(STONE_ORE_REPLACEABLES, JBlocks.SAPPHIRE_ORE.defaultBlockState()),
                    OreConfiguration.target(DEEPSLATE_ORE_REPLACEABLES, JBlocks.DEEPSLATE_SAPPHIRE_ORE.defaultBlockState())),
                    9,
                    0.5F)));

    public static final Promised<? extends ConfiguredFeature<?, ?>> SHADIUM_ORE =
            REGISTER.register("shadium_ore", () -> Feature.ORE.configured(new OreConfiguration(List.of(
                    OreConfiguration.target(STONE_ORE_REPLACEABLES, JBlocks.SHADIUM_ORE.defaultBlockState()),
                    OreConfiguration.target(DEEPSLATE_ORE_REPLACEABLES, JBlocks.DEEPSLATE_SHADIUM_ORE.defaultBlockState())),
                    9)));

    public static final Promised<? extends ConfiguredFeature<?, ?>> SHADIUM_ORE_BURIED =
            REGISTER.register("shadium_ore_buried", () -> Feature.ORE.configured(new OreConfiguration(List.of(
                    OreConfiguration.target(STONE_ORE_REPLACEABLES, JBlocks.SHADIUM_ORE.defaultBlockState()),
                    OreConfiguration.target(DEEPSLATE_ORE_REPLACEABLES, JBlocks.DEEPSLATE_SHADIUM_ORE.defaultBlockState())),
                    9,
                    0.5F)));

    public static final Promised<? extends ConfiguredFeature<?, ?>> IRIDIUM_ORE =
            REGISTER.register("iridium_ore", () -> Feature.ORE.configured(new OreConfiguration(List.of(
                    OreConfiguration.target(STONE_ORE_REPLACEABLES, JBlocks.IRIDIUM_ORE.defaultBlockState()),
                    OreConfiguration.target(DEEPSLATE_ORE_REPLACEABLES, JBlocks.DEEPSLATE_IRIDIUM_ORE.defaultBlockState())),
                    9)));

    public static final Promised<? extends ConfiguredFeature<?, ?>> IRIDIUM_ORE_BURIED =
            REGISTER.register("iridium_ore_buried", () -> Feature.ORE.configured(new OreConfiguration(List.of(
                    OreConfiguration.target(STONE_ORE_REPLACEABLES, JBlocks.IRIDIUM_ORE.defaultBlockState()),
                    OreConfiguration.target(DEEPSLATE_ORE_REPLACEABLES, JBlocks.DEEPSLATE_IRIDIUM_ORE.defaultBlockState())),
                    9,
                    0.5F)));

    public static final Promised<? extends ConfiguredFeature<?, ?>> SCORCHED_STALAGMITE =
            REGISTER.register("scorched_stalagmite", () -> JFeatures.SCORCHED_STALAGMITE.get().configured(FeatureConfiguration.NONE));

    public static final Promised<? extends ConfiguredFeature<?, ?>> SCORCHED_CACTUS =
            REGISTER.register("scorched_cactus",
                    () -> Feature.RANDOM_PATCH.configured(
                            FeatureUtils.simpleRandomPatchConfiguration(7,
                                    Feature.BLOCK_COLUMN.configured(
                                            BlockColumnConfiguration.simple(BiasedToBottomInt.of(1, 5),
                                                    BlockStateProvider.simple(JBlocks.SCORCHED_CACTUS))).placed(
                                            BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE,
                                                    BlockPredicate.wouldSurvive(JBlocks.SCORCHED_CACTUS.defaultBlockState(), BlockPos.ZERO)))))));

    public static final Promised<? extends ConfiguredFeature<?, ?>> SULPHUR_DEPOSIT =
            REGISTER.register("sulpur_deposit", () -> JFeatures.SULPHUR_DEPOSIT.get().configured(new BlockStateConfiguration(JBlocks.SULPHUR_ROCK.defaultBlockState())));

    public static final Promised<? extends ConfiguredFeature<?, ?>> EUCA_WATER =
            REGISTER.register("euca_water", () -> JFeatures.EUCA_WATER_GEN.get().configured((new SpringConfiguration(Fluids.WATER.defaultFluidState(), false, 4, 1, ImmutableSet.of(JBlocks.GOLDITE_STONE, JBlocks.GOLDITE_DIRT)))));


    public static final Promised<? extends ConfiguredFeature<?, ?>> SULPHUR_CRYSTAL =
            REGISTER.register("sulphar_crystal", () -> JFeatures.SULPHUR_CRYSTAL.get().configured(FeatureConfiguration.NONE));

    public static final Promised<? extends ConfiguredFeature<?, ?>> ICE_SPIKE =
            REGISTER.register("ice_spike", () -> JFeatures.FROZEN_ICE_SPIKE.get().configured(FeatureConfiguration.NONE));

    public static final Promised<? extends ConfiguredFeature<?, ?>> BOILING_FIRE =
            REGISTER.register("boiling_fire", surfacePatchFeature(() -> Blocks.FIRE.defaultBlockState()));


    public static final Promised<? extends ConfiguredFeature<?, ?>> BOIL_SANDS_VEG =
            REGISTER.register("boil_sands_veg",
                    () -> Feature.RANDOM_PATCH.configured(
                            FeatureUtils.simplePatchConfiguration(
                                    Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(
                                            new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                                                    .add(JBlocks.TALL_MOLTEN_PLANT.defaultBlockState(), 1)
                                                    .add(JBlocks.LAVA_BLOOM.defaultBlockState(), 2)
                                                    .add(JBlocks.CRUMBLING_PINE.defaultBlockState(), 8)
                                                    .add(JBlocks.TALL_CRUMBLING_PINE.defaultBlockState(), 2)))))));

    public static final Promised<? extends ConfiguredFeature<?, ?>> BOIL_VEG =
            REGISTER.register("boil_veg",
                    () -> Feature.RANDOM_PATCH.configured(
                            FeatureUtils.simplePatchConfiguration(
                                    Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(
                                            new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                                                    .add(JBlocks.INFERNO_BUSH.defaultBlockState(), 3)
                                                    .add(JBlocks.FLAME_POD.defaultBlockState(), 4)
                                                    .add(JBlocks.CRISP_GRASS.defaultBlockState(), 2)))))));

    public static final Promised<? extends ConfiguredFeature<?, ?>> CHARRED_FIELDS_VEG =
            REGISTER.register("charred_fields_veg",
                    () -> Feature.RANDOM_PATCH.configured(
                            FeatureUtils.simplePatchConfiguration(
                                    Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(
                                            new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                                                    .add(JBlocks.CHARRED_WEEDS.defaultBlockState(), 3)
                                                    .add(JBlocks.CHARRED_SHORT_GRASS.defaultBlockState(), 3)
                                                    .add(JBlocks.CHARRED_TALL_GRASS.defaultBlockState(), 2)))))));

    public static final Promised<? extends ConfiguredFeature<?, ?>> FLAME_BULB =
            REGISTER.register("flame_bulb",
                    () -> JFeatures.FLAME_BULB.get().configured(FeatureConfiguration.NONE));

    public static final Promised<? extends ConfiguredFeature<?, ?>> LARGE_CHARRED_TREE =
            REGISTER.register("large_charred_tree",
                    () -> Feature.TREE.configured(
                            new TreeConfiguration.TreeConfigurationBuilder(
                                    BlockStateProvider.simple(JBlocks.BURNED_BARK),
                                    new ForkingTrunkPlacer(5, 5, 5),
                                    BlockStateProvider.simple(JBlocks.CHARRED_LEAVES),
                                    new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1), 2),
                                    new TwoLayersFeatureSize(1, 1, 2)).ignoreVines()
                                    .forceDirt()
                                    .dirt(BlockStateProvider.simple(JBlocks.CHARRED_GRASS))
                                    .decorators(List.of(CharredBrushTreeDecorator.INSTANCE))
                                    .build()));

    public static final Promised<? extends ConfiguredFeature<?, ?>> DYING_BURNED_TREE =
            REGISTER.register("dying_burned_tree",
                    () -> Feature.TREE.configured(
                            new TreeConfiguration.TreeConfigurationBuilder(
                                    BlockStateProvider.simple(JBlocks.BURNED_BARK),
                                    new ForkingTrunkPlacer(2, 1, 1),
                                    BlockStateProvider.simple(JBlocks.CHARRED_LEAVES),
                                    new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1), 2),
                                    new TwoLayersFeatureSize(1, 1, 2))
                                    .forceDirt()
                                    .dirt(BlockStateProvider.simple(JBlocks.VOLCANIC_SAND))
                                    .build()));

    public static final Promised<? extends ConfiguredFeature<?, ?>> MEDIUM_BURNED_TREE =
            REGISTER.register("medium_burned_tree",
                    () -> Feature.TREE.configured(
                            new TreeConfiguration.TreeConfigurationBuilder(
                                    BlockStateProvider.simple(JBlocks.BURNED_BARK),
                                    new ForkingTrunkPlacer(4, 4, 4),
                                    BlockStateProvider.simple(JBlocks.CHARRED_LEAVES),
                                    new FancyFoliagePlacer(ConstantInt.of(3), ConstantInt.of(1), 2),
                                    new TwoLayersFeatureSize(1, 1, 2)).ignoreVines()
                                    .decorators(ImmutableList.of(CharredBrushTreeDecorator.INSTANCE))
                                    .forceDirt()
                                    .dirt(BlockStateProvider.simple(JBlocks.CHARRED_GRASS))
                                    .build()));

    public static final Promised<? extends ConfiguredFeature<?, ?>> SMALL_BURNED_TREE =
            REGISTER.register("small_burned_tree",
                    () -> Feature.TREE.configured(
                            new TreeConfiguration.TreeConfigurationBuilder(
                                    BlockStateProvider.simple(JBlocks.BURNED_BARK),
                                    new ForkingTrunkPlacer(3, 3, 3),
                                    BlockStateProvider.simple(JBlocks.CHARRED_LEAVES),
                                    new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1), 2),
                                    new TwoLayersFeatureSize(1, 1, 2)).ignoreVines()
                                    .decorators(ImmutableList.of(CharredBrushTreeDecorator.INSTANCE))
                                    .forceDirt()
                                    .dirt(BlockStateProvider.simple(JBlocks.CHARRED_GRASS))
                                    .build()));

    public static final Promised<? extends ConfiguredFeature<?, ?>> SMALL_FROZEN_TREE =
            REGISTER.register("small_frozen_tree",
                    () -> Feature.TREE.configured(
                            new TreeConfiguration.TreeConfigurationBuilder(
                                    BlockStateProvider.simple(JBlocks.FROZEN_LOG.defaultBlockState()),
                                    new ForkingTrunkPlacer(2, 1, 3),
                                    BlockStateProvider.simple(JBlocks.FROZEN_LEAVES.defaultBlockState()),
                                    new PineFoliagePlacer(ConstantInt.of(3), ConstantInt.of(1), ConstantInt.of(2)),
                                    new TwoLayersFeatureSize(1, 1, 2)).ignoreVines()
                                    .decorators(ImmutableList.of(IcyBrushTreeDecorator.INSTANCE, new IceShroomTreeDecorator(0.2F)))
                                    .forceDirt()
                                    .dirt(BlockStateProvider.simple(JBlocks.GRASSY_PERMAFROST))
                                    .build()));

    public static final Promised<? extends ConfiguredFeature<?, ?>> MEDIUM_FROZEN_TREE =
            REGISTER.register("medium_frozen_tree",
                    () -> Feature.TREE.configured(
                            new TreeConfiguration.TreeConfigurationBuilder(
                                    BlockStateProvider.simple(JBlocks.FROZEN_LOG.defaultBlockState()),
                                    new FancyTrunkPlacer(10, 5, 5),
                                    BlockStateProvider.simple(JBlocks.FROZEN_LEAVES.defaultBlockState()),
                                    new PineFoliagePlacer(ConstantInt.of(3), ConstantInt.of(1), ConstantInt.of(2)),
                                    new TwoLayersFeatureSize(1, 1, 2)).ignoreVines()
                                    .decorators(ImmutableList.of(
                                            IcyBrushTreeDecorator.INSTANCE,
                                            new IceShroomTreeDecorator(0.2F),
                                            new CrystalFruitTreeDecorator(4)))
                                    .forceDirt()
                                    .dirt(BlockStateProvider.simple(JBlocks.GRASSY_PERMAFROST))
                                    .build()));

    public static final Promised<? extends ConfiguredFeature<?, ?>> LARGE_FROZEN_TREE =
            REGISTER.register("large_frozen_tree",
                    () -> Feature.TREE.configured(
                            new TreeConfiguration.TreeConfigurationBuilder(
                                    BlockStateProvider.simple(JBlocks.FROZEN_LOG.defaultBlockState()),
                                    new FancyTrunkPlacer(15, 7, 7),
                                    BlockStateProvider.simple(JBlocks.FROZEN_LEAVES.defaultBlockState()),
                                    new PineFoliagePlacer(ConstantInt.of(3), ConstantInt.of(1), ConstantInt.of(2)),
                                    new TwoLayersFeatureSize(1, 1, 2)).ignoreVines()
                                    .decorators(ImmutableList.of(
                                            IcyBrushTreeDecorator.INSTANCE,
                                            new IceShroomTreeDecorator(0.2F),
                                            new CrystalFruitTreeDecorator(4)))
                                    .forceDirt()
                                    .dirt(BlockStateProvider.simple(JBlocks.CRUMBLED_PERMAFROST))
                                    .build()));

    public static final Promised<? extends ConfiguredFeature<?, ?>> LARGE_FROZEN_BITTERWOOOD_TREE =
            REGISTER.register("large_frozen_bitterwood_tree",
                    () -> Feature.TREE.configured(
                            new TreeConfiguration.TreeConfigurationBuilder(
                                    BlockStateProvider.simple(JBlocks.FROZEN_LOG.defaultBlockState()),
                                    new GiantTrunkPlacer(15, 7, 7),
                                    BlockStateProvider.simple(JBlocks.FROZEN_LEAVES.defaultBlockState()),
                                    new SpruceFoliagePlacer(ConstantInt.of(3), ConstantInt.of(1), ConstantInt.of(2)),
                                    new TwoLayersFeatureSize(1, 1, 2)).ignoreVines()
                                    .decorators(ImmutableList.of(new FrozenTreeDecorator(0.01F)))
                                    .forceDirt()
                                    .dirt(BlockStateProvider.simple(JBlocks.CRUMBLED_PERMAFROST))
                                    .build()));

    public static final Promised<? extends ConfiguredFeature<?, ?>> MEDIUM_FROZEN_BITTERWOOOD_TREE =
            REGISTER.register("medium_frozen_bitterwood_tree",
                    () -> Feature.TREE.configured(
                            new TreeConfiguration.TreeConfigurationBuilder(
                                    BlockStateProvider.simple(JBlocks.FROZEN_LOG.defaultBlockState()),
                                    new GiantTrunkPlacer(10, 7, 7),
                                    BlockStateProvider.simple(JBlocks.FROZEN_LEAVES.defaultBlockState()),
                                    new SpruceFoliagePlacer(ConstantInt.of(3), ConstantInt.of(1), ConstantInt.of(2)),
                                    new TwoLayersFeatureSize(1, 1, 2)).ignoreVines()
                                    .decorators(ImmutableList.of(new FrozenTreeDecorator(0.01F)))
                                    .forceDirt()
                                    .dirt(BlockStateProvider.simple(JBlocks.CRUMBLED_PERMAFROST))
                                    .build()));

    public static final Promised<? extends ConfiguredFeature<?, ?>> SMALL_FROZEN_BITTERWOOOD_TREE =
            REGISTER.register("small_frozen_bitterwood_tree",
                    () -> Feature.TREE.configured(
                            new TreeConfiguration.TreeConfigurationBuilder(
                                    BlockStateProvider.simple(JBlocks.FROZEN_LOG.defaultBlockState()),
                                    new StraightTrunkPlacer(4, 2, 3),
                                    BlockStateProvider.simple(JBlocks.FROZEN_LEAVES.defaultBlockState()),
                                    new SpruceFoliagePlacer(ConstantInt.of(3), ConstantInt.of(1), ConstantInt.of(2)),
                                    new TwoLayersFeatureSize(1, 1, 2)).ignoreVines()
                                    .decorators(ImmutableList.of(new FrozenTreeDecorator(0.01F)))
                                    .forceDirt()
                                    .dirt(BlockStateProvider.simple(JBlocks.GRASSY_PERMAFROST))
                                    .build()));

    public static final Promised<? extends ConfiguredFeature<?, ?>> FROZEN_ICICLE =
            REGISTER.register("frozen_icicle",
                    () -> Feature.SIMPLE_RANDOM_SELECTOR.configured(new SimpleRandomFeatureConfiguration(ImmutableList.of(() ->
                            JFeatures.FROZEN_ICICLE.get().configured(
                                            new IcicleFeatureConfig(
                                                    0.2F,
                                                    0.7F,
                                                    0.5F,
                                                    0.5F))
                                    .placed(
                                            EnvironmentScanPlacement.scanningFor(
                                                    Direction.DOWN,
                                                    BlockPredicate.solid(),
                                                    BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE,
                                                    12),
                                            RandomOffsetPlacement.vertical(
                                                    ConstantInt.of(1))), () ->
                            JFeatures.FROZEN_ICICLE.get().configured(
                                            new IcicleFeatureConfig(
                                                    0.2F,
                                                    0.7F,
                                                    0.5F,
                                                    0.5F))
                                    .placed(
                                            EnvironmentScanPlacement.scanningFor(
                                                    Direction.UP,
                                                    BlockPredicate.solid(),
                                                    BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE,
                                                    12),
                                            RandomOffsetPlacement.vertical(
                                                    ConstantInt.of(-1)))))));

    public static final Promised<? extends ConfiguredFeature<?, ?>> FROSTY_ICE_CLUSTER =
            REGISTER.register("frosty_ice_cluster",
                    () -> JFeatures.FROSTY_ICE_CLUSTER.get().configured(
                            new FrostyIceClusterFeatureConfig(
                                    12,
                                    UniformInt.of(3, 6),
                                    UniformInt.of(2, 8),
                                    1,
                                    3,
                                    UniformInt.of(2, 4),
                                    UniformFloat.of(0.3F, 0.7F),
                                    ClampedNormalFloat.of(
                                            0.1F,
                                            0.3F,
                                            0.1F,
                                            0.9F),
                                    0.1F,
                                    3,
                                    8)));

    public static final Promised<? extends ConfiguredFeature<?, ?>> LARGE_ICICLE =
            REGISTER.register("large_icicle",
                    () -> JFeatures.LARGE_ICICLE.get().configured(
                            new LargeIcicleFeatureConfig(
                                    30,
                                    UniformInt.of(3, 19),
                                    UniformFloat.of(0.4F, 2.0F),
                                    0.33F,
                                    UniformFloat.of(0.3F, 0.9F),
                                    UniformFloat.of(0.4F, 1.0F),
                                    UniformFloat.of(0.0F, 0.3F),
                                    4,
                                    0.6F)));

    public static final Promised<? extends ConfiguredFeature<?, ?>> FROZEN_VEG =
            REGISTER.register("frozen_veg",
                    () -> Feature.RANDOM_PATCH.configured(
                            FeatureUtils.simplePatchConfiguration(
                                    Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(
                                            new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                                                    .add(JBlocks.ICE_BUSH.defaultBlockState(), 1)
                                                    .add(JBlocks.FROSTBERRY_THORN.defaultBlockState(), 5)))))));

    public static final Promised<? extends ConfiguredFeature<?, ?>> FROZEN_FLOWERS =
            REGISTER.register("frozen_flowers",
                    () -> Feature.RANDOM_PATCH.configured(
                            FeatureUtils.simplePatchConfiguration(
                                    Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(
                                            new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                                                    .add(JBlocks.CICLEBLOOM.defaultBlockState(), 1)
                                                    .add(JBlocks.FROZEN_BLOOM.defaultBlockState(), 15)
                                                    .add(JBlocks.ICE_BUD.defaultBlockState(), 10)))))));

    public static final Promised<? extends ConfiguredFeature<?, ?>> GOLDITE_VEG =
            REGISTER.register("goldite_veg",
                    () -> Feature.RANDOM_PATCH.configured(
                            FeatureUtils.simplePatchConfiguration(
                                    Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(
                                            new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                                                    .add(JBlocks.GOLDITE_TALL_GRASS.defaultBlockState(), 2)
                                                    .add(JBlocks.GOLDITE_STALKS.defaultBlockState(), 10)
                                                    .add(JBlocks.GOLDITE_FLOWER.defaultBlockState(), 4)
                                                    .add(JBlocks.GOLDITE_BULB.defaultBlockState(), 4)))))));

    public static final Promised<? extends ConfiguredFeature<?, ?>> GOLD_VEG =
            REGISTER.register("gold_veg",
                    () -> Feature.RANDOM_PATCH.configured(
                            FeatureUtils.simplePatchConfiguration(
                                    Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(
                                            new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                                                    .add(JBlocks.EUCA_SILVER_GOLD_FLOWER.defaultBlockState(), 2)
                                                    .add(JBlocks.EUCA_TALL_FLOWERS.defaultBlockState(), 1)
                                                    .add(JBlocks.EUCA_TALL_GRASS.defaultBlockState(), 5)))))));

    public static final Promised<? extends ConfiguredFeature<?, ?>> EUCA_GOLD_TREES =
            REGISTER.register("euca_gold_tree",
                    () -> Feature.TREE.configured(
                            new TreeConfiguration.TreeConfigurationBuilder(
                                    BlockStateProvider.simple(JBlocks.EUCA_GOLD_LOG.defaultBlockState()),
                                    new ForkingTrunkPlacer(4, 1, 6),
                                    BlockStateProvider.simple(JBlocks.EUCA_GOLD_LEAVES.defaultBlockState()),
                                    new SphericalFoliagePlacer(ConstantInt.of(3), ConstantInt.of(1), 1),
                                    new TwoLayersFeatureSize(1, 1, 2))
                                    .ignoreVines()
                                    .dirt(BlockStateProvider.simple(JBlocks.GOLDITE_DIRT))
                                    .build()));

    public static final Promised<? extends ConfiguredFeature<?, ?>> EUCA_GREEN_TREES =
            REGISTER.register("euca_green_tree",
                    () -> Feature.TREE.configured(
                            new TreeConfiguration.TreeConfigurationBuilder(
                                    BlockStateProvider.simple(JBlocks.EUCA_BROWN_LOG.defaultBlockState()),
                                    new ForkingTrunkPlacer(4, 1, 6),
                                    BlockStateProvider.simple(JBlocks.EUCA_GREEN_LEAVES.defaultBlockState()),
                                    new SphericalFoliagePlacer(ConstantInt.of(3), ConstantInt.of(1), 1),
                                    new TwoLayersFeatureSize(1, 1, 2))
                                    .decorators(ImmutableList.of(new GlimmerRootTreeDecorator(4)))
                                    .ignoreVines()
                                    .dirt(BlockStateProvider.simple(JBlocks.GOLDITE_DIRT))
                                    .build()));

    public static final Promised<? extends ConfiguredFeature<?, ?>> EUCA_BOULDER =
            REGISTER.register("euca_boulder",
                    () -> JFeatures.BOULDER.get().configured(
                            new BlockStateConfiguration(
                                    JBlocks.GOLDITE_STONE.defaultBlockState())));

    public static final Promised<? extends ConfiguredFeature<?, ?>> MEKYUM_ORE =
            REGISTER.register("mekyum_ore", () -> Feature.ORE.configured(new OreConfiguration(List.of(
                    OreConfiguration.target(EUCA_ORE_REPLACEABLES, JBlocks.MEKYUM_ORE.defaultBlockState())),
                    12)));

    public static final Promised<? extends ConfiguredFeature<?, ?>> STORON_ORE =
            REGISTER.register("storon_ore", () -> Feature.ORE.configured(new OreConfiguration(List.of(
                    OreConfiguration.target(EUCA_ORE_REPLACEABLES, JBlocks.STORON_ORE.defaultBlockState())),
                    12)));

    public static final Promised<? extends ConfiguredFeature<?, ?>> CELESTIUM_ORE =
            REGISTER.register("celestium_ore", () -> Feature.ORE.configured(new OreConfiguration(List.of(
                    OreConfiguration.target(EUCA_ORE_REPLACEABLES, JBlocks.CELESTIUM_ORE.defaultBlockState())),
                    12)));

    public static final Promised<? extends ConfiguredFeature<?, ?>> KORITE_ORE =
            REGISTER.register("korite_ore", () -> Feature.ORE.configured(new OreConfiguration(List.of(
                    OreConfiguration.target(EUCA_ORE_REPLACEABLES, JBlocks.KORITE_ORE.defaultBlockState())),
                    12)));

    public static final Promised<? extends ConfiguredFeature<?, ?>> ASHUAL_ORE =
            REGISTER.register("ashual_ore", () -> Feature.ORE.configured(new OreConfiguration(List.of(
                    OreConfiguration.target(BOIL_ORE_REPLACEABLES, JBlocks.ASHUAL_ORE.defaultBlockState())),
                    7)));

    public static final Promised<? extends ConfiguredFeature<?, ?>> BLAZIUM_ORE =
            REGISTER.register("blazium_ore", () -> Feature.ORE.configured(new OreConfiguration(List.of(
                    OreConfiguration.target(BOIL_ORE_REPLACEABLES, JBlocks.BLAZIUM_ORE.defaultBlockState())),
                    7)));

    public static final Promised<? extends ConfiguredFeature<?, ?>> RIMESTONE_ORE =
            REGISTER.register("rimestone_ore", () -> Feature.ORE.configured(new OreConfiguration(List.of(
                    OreConfiguration.target(FROZEN_ORE_REPLACEABLES, JBlocks.RIMESTONE_ORE.defaultBlockState())),
                    7)));

    public static final Promised<? extends ConfiguredFeature<?, ?>> PERIDOT_ORE =
            REGISTER.register("peridot_ore", () -> Feature.ORE.configured(new OreConfiguration(List.of(
                    OreConfiguration.target(FROZEN_ORE_REPLACEABLES, JBlocks.PERIDOT_ORE.defaultBlockState())),
                    7)));

    public static final Promised<? extends ConfiguredFeature<?, ?>> GLOWING_FUNGI =
            REGISTER.register("glowing_fungi",
                    () -> Feature.RANDOM_PATCH.configured(
                            FeatureUtils.simplePatchConfiguration(
                                    Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(
                                            new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                                                    .add(JBlocks.TALL_FUNGI.defaultBlockState(), 4)))))));

    public static final Promised<? extends ConfiguredFeature<?, ?>> EUCA_GOLDBOT_SAPAWNER =
            REGISTER.register("euca_goldbot_spawner",
                            () -> JFeatures.EUCA_BOT_SPAWNER.get().configured(NoneFeatureConfiguration.NONE));

    //FIXME port
   /*

    public static final Promised<? extends ConfiguredFeature<?, ?>> BLOODCRUST_ORE =
            REGISTER.register("bloodcrust_ore",
                    Decoration.UNDERGROUND_ORES,
                    netherOreFeature(() -> JBlocks.BLOODCRUST_ORE.defaultBlockState(), JRuleTests.NETHERRACK, 10, 10))
                    .setBiomePredicate(IN_NETHER)
                    .asPromise();

    public static final Promised<? extends ConfiguredFeature<?, ?>> FIRESTONE_ORE =
            REGISTER.register("firestone_ore",
                    Decoration.UNDERGROUND_ORES,
                    netherOreFeature(() -> JBlocks.FIRESTONE_ORE.defaultBlockState(), JRuleTests.BASALT, 10, 24))
                    .setBiomePredicate(IN_NETHER)
                    .asPromise();

    public static final Promised<? extends ConfiguredFeature<?, ?>> LAVA_ROCK_CLUMP =
            REGISTER.register("lava_rock_clump",
                    Decoration.UNDERGROUND_ORES,
                    netherOreFeature(() -> JBlocks.BLOOD_ROCK.defaultBlockState(), JRuleTests.NETHERRACK, 10, 24))
                    .setBiomePredicate(IN_NETHER)
                    .asPromise();

    public static final Promised<? extends ConfiguredFeature<?, ?>> CRIMSON_QUARTZ_ORE =
            REGISTER.register("crimson_quartz_ore",
                    Decoration.UNDERGROUND_ORES,
                    netherOreFeature(() -> JBlocks.CRIMSON_QUARTZ_ORE.defaultBlockState(), JRuleTests.NETHERRACK, 10, 10))
                    .setBiomePredicate(IN_CRIMSON_FOREST)
                    .asPromise();

    public static final Promised<? extends ConfiguredFeature<?, ?>> WARPED_QUARTZ_ORE =
            REGISTER.register("crimson_quartz_ore",
                    Decoration.UNDERGROUND_ORES,
                    netherOreFeature(() -> JBlocks.WARPED_QUARTZ_ORE.defaultBlockState(), JRuleTests.NETHERRACK, 10, 10))
                    .setBiomePredicate(IN_WARPED_FOREST)
                    .asPromise();

    public static final Promised<? extends ConfiguredFeature<?, ?>> ENDERILLIUM_ORE =
            REGISTER.register("enderillium_ore",
                    Decoration.UNDERGROUND_ORES,
                    defaultOreFeature(() -> JBlocks.ENDERILLIUM_ORE.defaultBlockState(), JRuleTests.END_STONE, 12, 128, 20))
                    .setBiomePredicate(IN_END)
                    .asPromise();


    public static final Promised<? extends ConfiguredFeature<?, ?>> PERIDOT_ORE = //TODO: Tweak rarity and quantity
            REGISTER.register("peridot_ore",
                    Decoration.UNDERGROUND_ORES,
                    defaultOreFeature(() -> JBlocks.PERIDOT_ORE.defaultBlockState(), JRuleTests.STONE_FROZEN, 8, 64, 10))
                    .setBiomePredicate(FROZEN_DYING_FORST.or(FROZEN_BITTERWOOD_FORST).or(FROZEN_WASTES))
                    .asPromise();


    public static final Promised<? extends ConfiguredFeature<?, ?>> RIMESTONE_ORE = //TODO: Tweak rarity and quantity
            REGISTER.register("rimestone_ore",
                    Decoration.UNDERGROUND_ORES,
                    defaultOreFeature(() -> JBlocks.RIMESTONE_ORE.defaultBlockState(), JRuleTests.STONE_FROZEN, 4, 64, 5))
                    .setBiomePredicate(FROZEN_DYING_FORST.or(FROZEN_BITTERWOOD_FORST).or(FROZEN_WASTES))
                    .asPromise();

    public static final Promised<? extends ConfiguredFeature<?, ?>> MUD_DISK =
            REGISTER.register("mud_disk",
                    Decoration.UNDERGROUND_ORES,
                    defaultDiskFeature(() -> JBlocks.BLOCK_OF_MUD.defaultBlockState(), Blocks.DIRT.defaultBlockState(), 2, 4, 1))
                    .setBiomePredicate(COMMON_BIOMES)
                    .asPromise();

    public static final Promised<? extends ConfiguredFeature<?, ?>> FROZEN_ICE_SPIKE =
            REGISTER.register("frozen_ice_spike",
                    Decoration.SURFACE_STRUCTURES,
                    () -> JFeatures.FROZEN_ICE_SPIKE.get()
                            .configured(FeatureConfiguration.NONE)
                            .range(5)
                            .squared()
                            .chance(70)
                            .countRandom(32)
                            .decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).squared())
                    .setBiomePredicate(FROZEN_WASTES)
                    .asPromise();

    public static final Promised<? extends ConfiguredFeature<?, ?>> FROZEN_PLANTS =
            REGISTER.register("frozen_plants", Decoration.VEGETAL_DECORATION, () -> Feature.RANDOM_PATCH
                    .configured((new RandomPatchConfiguration.GrassConfigurationBuilder(
                            new WeightedStateProvider()
                                    .add(JBlocks.ICE_BUSH.defaultBlockState(), 1)
                                    .add(JBlocks.FROSTBERRY_THORN.defaultBlockState(), 5),
                            new SimpleBlockPlacer()))
                            .tries(26)
                            .xspread(8)
                            .zspread(8)
                            .whitelist(ImmutableSet.of(
                                    JBlocks.GRASSY_PERMAFROST))
                            .noProjection()
                            .build())
                    .decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).squared()
                    .range(250)
                    .count(64))
                    .setBiomePredicate(FROZEN_BIOMES)
                    .asPromise();

    public static final Promised<? extends ConfiguredFeature<?, ?>> FROZEN_FLOWERS =
            REGISTER.register("frozen_flowers", Decoration.VEGETAL_DECORATION, () -> Feature.RANDOM_PATCH
                    .configured((new RandomPatchConfiguration.GrassConfigurationBuilder(
                            new WeightedStateProvider()
                                    .add(JBlocks.FROZEN_BLOOM.defaultBlockState(), 5)
                                    .add(JBlocks.ICE_BUD.defaultBlockState(), 2),
                            new SimpleBlockPlacer()))
                            .tries(3)
                            .xspread(8)
                            .zspread(8)
                            .whitelist(ImmutableSet.of(
                                    JBlocks.GRASSY_PERMAFROST))
                            .noProjection()
                            .build())
                    .decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).squared()
                    .range(250)
                    .count(50))
                    .setBiomePredicate(FROZEN_BIOMES)
                    .asPromise();

    public static final Promised<? extends ConfiguredFeature<?, ?>> CICLEBLOOM =
            REGISTER.register("ciclebloom", Decoration.VEGETAL_DECORATION, () -> Feature.RANDOM_PATCH
                    .configured((new RandomPatchConfiguration.GrassConfigurationBuilder(
                            new SimpleStateProvider(JBlocks.CICLEBLOOM.defaultBlockState()),
                            new SimpleBlockPlacer()))
                            .tries(2)
                            .xspread(10)
                            .zspread(10)
                            .whitelist(ImmutableSet.of(
                                    JBlocks.GRASSY_PERMAFROST))
                            .noProjection()
                            .build())
                            .decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).squared()
                            .range(250)
                            .count(5))
                    .setBiomePredicate(FROZEN_DYING_FORST.or(FROZEN_BITTERWOOD_FORST))
                    .asPromise();

    public static final Promised<? extends ConfiguredFeature<?, ?>> REDCURRANT_BUSH =
            REGISTER.register("redcurrant_bush", Decoration.VEGETAL_DECORATION, () -> Feature.RANDOM_PATCH
                            .configured((new RandomPatchConfiguration.GrassConfigurationBuilder(
                                    new SimpleStateProvider(JBlocks.REDCURRANT_BUSH.defaultBlockState().setValue(JBerryBushBlock.AGE, 3)),
                                    new SimpleBlockPlacer()))
                                    .tries(1)
                                    .xspread(3)
                                    .zspread(3)
                                    .whitelist(ImmutableSet.of(
                                            JBlocks.GRASSY_PERMAFROST))
                                    .noProjection()
                                    .build())
                            .decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).squared()
                            .range(250)
                            .count(1))
                    .setBiomePredicate(FROZEN_DYING_FORST.or(FROZEN_BITTERWOOD_FORST))
                    .asPromise();

    public static final Promised<? extends ConfiguredFeature<?, ?>> TALL_BOIL_UNDERGROWTH =
            REGISTER.register("tall_boil_undergrowth",
                            Decoration.UNDERGROUND_DECORATION,
                            () -> Feature.RANDOM_PATCH
                                    .configured((new RandomPatchConfiguration.GrassConfigurationBuilder(
                                            new WeightedStateProvider()
                                                    .add(JBlocks.TALL_SIZZLESHROOM.defaultBlockState(), 1),
                                            new DoublePlantPlacer()))
                                            .tries(10)
                                            .xspread(6)
                                            .zspread(6)
                                            .noProjection()
                                            .build())
                                    .range(60)
                                    .count(10))
                    .setBiomePredicate(BOIL_FIRE_BIOMES)
                    .asPromise();

    public static final Promised<? extends ConfiguredFeature<?, ?>> BOILING_UNDERGROWTH =
            REGISTER.register("boiling_undergrowth", Decoration.VEGETAL_DECORATION, () -> Feature.RANDOM_PATCH
                            .configured((new RandomPatchConfiguration.GrassConfigurationBuilder(
                                    new WeightedStateProvider()
                                            .add(JBlocks.SIZZLESHROOM.defaultBlockState(), 1),
                                    new SimpleBlockPlacer()))
                                    .tries(10)
                                    .xspread(10)
                                    .zspread(10)
                                    .noProjection()
                                    .build())
                            .range(60)
                            .count(10))
                    .setBiomePredicate(BOIL_FIRE_BIOMES)
                    .asPromise();*/

    /**
     * Creates an ore feature with basic parameters.
     *
     * @param oreSup     The supplier of the ore being generated
     * @param spawnBlock The RuleTest block that the ore can generate in
     * @param size       The maximum size of the ore vein
     * @param range      The maximum height the ore can generate within
     * @param count      (unsure) Possible count per chunk, or possibly extra vein size.
     * @return Returns a new Configured Ore Feature based on the params filled in the method
     */
    private static Supplier<ConfiguredFeature<?, ?>> defaultOreFeature(Supplier<BlockState> oreSup, RuleTest spawnBlock, int size, int range, int count) {
        return () -> Feature.ORE.configured(new OreConfiguration(spawnBlock, oreSup.get(), size));
    }

    private static Supplier<ConfiguredFeature<?, ?>> defaultOreFeature(Supplier<BlockState> oreSup, Supplier<RuleTest> spawnBlock, int size, int range, int count) {
        return () -> Feature.ORE.configured(new OreConfiguration(spawnBlock.get(), oreSup.get(), size));
    }

    private static Supplier<ConfiguredFeature<?, ?>> netherOreFeature(Supplier<BlockState> oreSup, RuleTest spawnBlock, int size, int count) {
        return () -> Feature.ORE.configured(new OreConfiguration(spawnBlock, oreSup.get(), size));
    }

    private static Supplier<ConfiguredFeature<?, ?>> defaultDiskFeature(Supplier<BlockState> oreSup, BlockState spawnBlock, int baseValue, int spread, int halfHeight) {
        return () -> Feature.DISK.configured(
                new DiskConfiguration(oreSup.get(), UniformInt.of(baseValue, spread), halfHeight, ImmutableList.of(spawnBlock)));
    }

    private static Supplier<ConfiguredFeature<?, ?>> surfacePatchFeature(Supplier<BlockState> blockStateSupplier) {
        return () -> Feature.RANDOM_PATCH.configured(FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(BlockStateProvider.simple(blockStateSupplier.get())))));
    }
}