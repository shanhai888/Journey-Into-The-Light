package net.jitl.common.world.gen.structures;

import com.mojang.serialization.Codec;
import net.jitl.common.world.gen.util.GenHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class GuardianTowerStructure extends Structure<NoFeatureConfig> {
    public GuardianTowerStructure(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return Start::new;
    }

    @Override
    public GenerationStage.Decoration step() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    public static class Start extends StructureStart<NoFeatureConfig> {
        public Start(Structure<NoFeatureConfig> structure, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox_, int references, long seed) {
            super(structure, chunkX, chunkZ, mutableBoundingBox_, references, seed);
        }

        public void generatePieces(DynamicRegistries dynamicRegistries_, ChunkGenerator chunkGenerator_, TemplateManager templateManager_, int chunkX, int chunkZ, Biome biome_, NoFeatureConfig featureConfig_) {
            int x = chunkX * 16;
            int z = chunkZ * 16;

            int height = GenHelper.getAveragePlacementHeight(chunkGenerator_, x, z, x + GuardianTowerPieces.BB_WIDTH, z + GuardianTowerPieces.BB_WIDTH);

            pieces.add(new GuardianTowerPieces.Floor(this.random, x, height, z));
            this.calculateBoundingBox();
        }
    }
}
