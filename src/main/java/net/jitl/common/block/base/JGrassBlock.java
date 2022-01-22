package net.jitl.common.block.base;

import net.jitl.core.init.JBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.lighting.LayerLightEngine;
import net.minecraftforge.common.IPlantable;

import java.util.List;
import java.util.Random;

public class JGrassBlock extends Block implements BonemealableBlock {

    private final Block dirt;

    public JGrassBlock(Properties properties, Block dirt) {
        super(properties);
        this.dirt = dirt;
    }

    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
        return true;
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter level, BlockPos pos, BlockState state, boolean isClient_) {
        return level.getBlockState(pos.above()).isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level level, Random rand, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, Random rand, BlockPos pos, BlockState state) {
        BlockPos blockpos = pos.above();
        BlockState blockstate = this.defaultBlockState();
        label46:
        for(int i = 0; i < 128; ++i) {
            BlockPos blockpos1 = blockpos;
            for(int j = 0; j < i / 16; ++j) {
                blockpos1 = blockpos1.offset(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);
                if (!level.getBlockState(blockpos1.below()).is(this) || level.getBlockState(blockpos1).isCollisionShapeFullBlock(level, blockpos1)) {
                    continue label46;
                }
            }
            BlockState blockstate1 = level.getBlockState(blockpos1);
            if (blockstate1.is(blockstate.getBlock()) && rand.nextInt(10) == 0) {
                ((BonemealableBlock)blockstate.getBlock()).performBonemeal(level, rand, blockpos1, blockstate1);
            }

            if (blockstate1.isAir()) {
                PlacedFeature placedfeature = null;
                if (rand.nextInt(8) == 0) {
                    List<ConfiguredFeature<?, ?>> list = level.getBiome(blockpos1).getGenerationSettings().getFlowerFeatures();
                    if(list.isEmpty()) {
                        continue;
                    }
                    placedfeature = ((RandomPatchConfiguration)list.get(0).config()).feature().get();
                } else {
                    //FIXME
                    if(blockstate.getBlock() == JBlocks.EUCA_GOLD_GRASS_BLOCK)
                        placedfeature = VegetationPlacements.GRASS_BONEMEAL;
                    if(blockstate.getBlock() == JBlocks.GOLDITE_GRASS_BLOCK) {
                        placedfeature = VegetationPlacements.GRASS_BONEMEAL;
                    }
                }
                if(placedfeature != null)
                    placedfeature.place(level, level.getChunkSource().getGenerator(), rand, blockpos1);
            }
        }
    }

    private static boolean canBeGrass(BlockState state, LevelReader levelReader, BlockPos pos) {
        BlockPos blockpos = pos.above();
        BlockState blockstate = levelReader.getBlockState(blockpos);
        return true;
    }

    private static boolean canPropagate(BlockState state, LevelReader level, BlockPos pos) {
        return canBeGrass(state, level, pos);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random_) {
        if(!canBeGrass(state, level, pos)) {
            if(!level.isAreaLoaded(pos, 1)) return;
            level.setBlockAndUpdate(pos, this.dirt.defaultBlockState());
        } else {
            if (!level.isAreaLoaded(pos, 3)) return;
            if (level.getMaxLocalRawBrightness(pos.above()) >= 9) {
                BlockState blockstate = this.defaultBlockState();
                for(int i = 0; i < 4; ++i) {
                    BlockPos blockpos = pos.offset(random_.nextInt(3) - 1, random_.nextInt(5) - 3, random_.nextInt(3) - 1);
                    if (level.getBlockState(blockpos).is(this.dirt) && canPropagate(blockstate, level, blockpos)) {
                        level.setBlockAndUpdate(blockpos, blockstate);
                    }
                }
            }
        }
    }
}