package net.jitl.common.world.gen.foliageplacer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.jitl.init.JFoliagePlacers;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FoliagePlacerType;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.Set;

public class SphericalFoliagePlacer extends BlobFoliagePlacer {
    public static final Codec<SphericalFoliagePlacer> CODEC = RecordCodecBuilder.create((instance_) -> {
        return blobParts(instance_).apply(instance_, SphericalFoliagePlacer::new);
    });

    public SphericalFoliagePlacer(FeatureSpread featureSpread_, FeatureSpread featureSpread1_, int int_) {
        super(featureSpread_, featureSpread1_, int_);
    }

    @Override
    protected @NotNull FoliagePlacerType<?> type() {
        return JFoliagePlacers.SPHERICAL_FOLIAGE_PLACER.get();
    }

    @Override
    protected void createFoliage(@NotNull IWorldGenerationReader reader, @NotNull Random rand, @NotNull BaseTreeFeatureConfig baseTreeFeatureConfig_, int int_, Foliage foliage_, int int1_, int int2_, Set<BlockPos> set_, int int3_, MutableBoundingBox mutableBoundingBox_) {
        int size = int2_ + foliage_.radiusOffset();
        BlockPos pos = foliage_.foliagePos().above(int3_);
        pos = pos.offset(Direction.UP.getNormal());
        for (byte x = 0; x <= size; x++) {
            for (byte y = 0; y <= size; y++) {
                for (byte z = 0; z <= size; z++) {

                    int distance;

                    if (x >= y & x >= z) {
                        distance = x + (Math.max(y, z) >> 1) + (Math.min(y, z) >> 1);
                    } else if (y >= x & y >= z) {
                        distance = y + (Math.max(x, z) >> 1) + (Math.min(x, z) >> 1);
                    } else {
                        distance = z + (Math.max(x, y) >> 1) + (Math.min(x, y) >> 1);
                    }

                    if (distance <= size) {
                        placeLeavesRow(reader, rand, baseTreeFeatureConfig_, pos.offset(+x, +y, +z), 0, set_, -1 - int1_, false, mutableBoundingBox_);
                        placeLeavesRow(reader, rand, baseTreeFeatureConfig_, pos.offset(+x, +y, -z), 0, set_, -1 - int1_, false, mutableBoundingBox_);
                        placeLeavesRow(reader, rand, baseTreeFeatureConfig_, pos.offset(-x, +y, +z), 0, set_, -1 - int1_, false, mutableBoundingBox_);
                        placeLeavesRow(reader, rand, baseTreeFeatureConfig_, pos.offset(-x, +y, -z), 0, set_, -1 - int1_, false, mutableBoundingBox_);
                        placeLeavesRow(reader, rand, baseTreeFeatureConfig_, pos.offset(+x, -y, +z), 0, set_, -1 - int1_, false, mutableBoundingBox_);
                        placeLeavesRow(reader, rand, baseTreeFeatureConfig_, pos.offset(+x, -y, -z), 0, set_, -1 - int1_, false, mutableBoundingBox_);
                        placeLeavesRow(reader, rand, baseTreeFeatureConfig_, pos.offset(-x, -y, +z), 0, set_, -1 - int1_, false, mutableBoundingBox_);
                        placeLeavesRow(reader, rand, baseTreeFeatureConfig_, pos.offset(-x, -y, -z), 0, set_, -1 - int1_, false, mutableBoundingBox_);
                    }
                }
            }
        }
    }

    @Override
    public int foliageHeight(@NotNull Random random_, int int_, @NotNull BaseTreeFeatureConfig baseTreeFeatureConfig_) {
        return 0;
    }

    @Override
    protected boolean shouldSkipLocation(@NotNull Random random_, int int_, int int1_, int int2_, int int3_, boolean boolean_) {
        if (int1_ == 0) {
            return (int_ > 1 || int2_ > 1) && int_ != 0 && int2_ != 0;
        } else {
            return int_ == int3_ && int2_ == int3_ && int3_ > 0;
        }
    }
}