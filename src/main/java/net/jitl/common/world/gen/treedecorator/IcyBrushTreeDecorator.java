package net.jitl.common.world.gen.treedecorator;

import com.mojang.serialization.Codec;
import net.jitl.init.JBlocks;
import net.jitl.init.world.JTreeDecorators;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.BiConsumer;

public class IcyBrushTreeDecorator extends TreeDecorator {
    public static final Codec<IcyBrushTreeDecorator> CODEC;
    public static final IcyBrushTreeDecorator INSTANCE = new IcyBrushTreeDecorator();

    protected TreeDecoratorType<?> type() {
        return JTreeDecorators.ICY_BRUSH_TREE_DECORATOR.get();
    }

    @Override
    public void place(LevelSimulatedReader level_, BiConsumer<BlockPos, BlockState> blockSetter_, Random random_, List<BlockPos> logPositions_, List<BlockPos> leafPositions_) {

    }

    public void place(WorldGenLevel seedReader_, Random random_, List<BlockPos> list_, List<BlockPos> list1_, Set<BlockPos> set_, BoundingBox mutableBoundingBox_) {
        list1_.forEach((blockPos1_) -> {
            int chance = 1;
            if (random_.nextInt(chance) == 0) {
                BlockPos blockpos = blockPos1_.west();
                if (Feature.isAir(seedReader_, blockpos)) {
                    this.addHangingVine(seedReader_, random_, blockpos, VineBlock.EAST, set_, mutableBoundingBox_);
                }
            }

            if (random_.nextInt(chance) == 0) {
                BlockPos blockpos1 = blockPos1_.east();
                if (Feature.isAir(seedReader_, blockpos1)) {
                    this.addHangingVine(seedReader_, random_, blockpos1, VineBlock.WEST, set_, mutableBoundingBox_);
                }
            }

            if (random_.nextInt(chance) == 0) {
                BlockPos blockpos2 = blockPos1_.north();
                if (Feature.isAir(seedReader_, blockpos2)) {
                    this.addHangingVine(seedReader_, random_, blockpos2, VineBlock.SOUTH, set_, mutableBoundingBox_);
                }
            }

            if (random_.nextInt(chance) == 0) {
                BlockPos blockpos3 = blockPos1_.south();
                if (Feature.isAir(seedReader_, blockpos3)) {
                    this.addHangingVine(seedReader_, random_, blockpos3, VineBlock.NORTH, set_, mutableBoundingBox_);
                }
            }
        });
    }

    private void addHangingVine(LevelSimulatedRW worldGenerationReader_, Random random, BlockPos blockPos_, BooleanProperty booleanProperty_, Set<BlockPos> set_, BoundingBox mutableBoundingBox_) {
        this.placeBrush(worldGenerationReader_, blockPos_, booleanProperty_, set_, mutableBoundingBox_);
        int i = random.nextInt(4) + 3;

        for (BlockPos blockpos = blockPos_.below(); Feature.isAir(worldGenerationReader_, blockpos) && i > 0; --i) {
            this.placeBrush(worldGenerationReader_, blockpos, booleanProperty_, set_, mutableBoundingBox_);
            blockpos = blockpos.below();
        }
    }

    protected void placeBrush(LevelWriter worldWriter_, BlockPos blockPos_, BooleanProperty booleanProperty_, Set<BlockPos> set_, BoundingBox mutableBoundingBox_) {
        //FIXMEthis.setBlock(worldWriter_, blockPos_, JBlocks.ICY_BRUSH.defaultBlockState().setValue(booleanProperty_, true), set_, mutableBoundingBox_);
    }

    static {
        CODEC = Codec.unit(() -> INSTANCE);
    }
}