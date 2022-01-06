package net.jitl.common.block;

import net.jitl.api.block.GroundPredicate;
import net.jitl.common.block.base.JDoublePlantBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import org.jetbrains.annotations.NotNull;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

import VoxelShape;

public class TallGlowshroomBlock extends JDoublePlantBlock {

    private static final VoxelShape HITBOX = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 14.0D, 13.0D);

    public TallGlowshroomBlock(Properties properties) {
        super(properties);
        setGroundPredicate(GroundPredicate.UNDERGROUND);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return HITBOX;
    }
}