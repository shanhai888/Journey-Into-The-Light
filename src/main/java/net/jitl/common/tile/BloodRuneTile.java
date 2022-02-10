package net.jitl.common.tile;

import net.jitl.common.tile.base.BaseBloodRuneTile;
import net.jitl.core.init.JTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BloodRuneTile extends BaseBloodRuneTile {

    public BloodRuneTile(BlockPos pos, BlockState state) {
        super(JTiles.BLOOD_RUNE, pos, state);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return new TranslatableComponent("jitl.tile.blood_rune");
    }
}