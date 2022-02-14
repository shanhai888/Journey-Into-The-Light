package net.jitl.common.tile.spawner;

import net.jitl.core.init.JBlocks;
import net.jitl.core.init.JTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class GoldBotSpawnerTile extends BlockEntity {

    private final BaseSpawner spawner = new BaseSpawner() {

        @Override
        public void broadcastEvent(@NotNull Level level_, @NotNull BlockPos pos_, int int_) {
            GoldBotSpawnerTile.this.level.blockEvent(GoldBotSpawnerTile.this.worldPosition, JBlocks.GOLD_BOT_SPAWNER, int_, 0);
        }

        public Level getLevel() {
            return GoldBotSpawnerTile.this.level;
        }

        public BlockPos getPos() {
            return GoldBotSpawnerTile.this.worldPosition;
        }

        @Override
        public void setNextSpawnData(Level level, BlockPos pos, @NotNull SpawnData nextSpawnData) {
            super.setNextSpawnData(level, pos, nextSpawnData);
            if (this.getLevel() != null) {
                BlockState blockstate = this.getLevel().getBlockState(this.getPos());
                this.getLevel().sendBlockUpdated(GoldBotSpawnerTile.this.worldPosition, blockstate, blockstate, 4);
            }
        }
    };

    public GoldBotSpawnerTile(BlockPos pos, BlockState state) {
        super(JTiles.GOLD_BOT_SPAWNER, pos, state);
    }

    @Override
    public void load(@NotNull CompoundTag tag_) {
        super.load(tag_);
        this.spawner.load(this.level, this.worldPosition, tag_);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag_) {
        super.saveAdditional(tag_);
        this.spawner.save(tag_);
    }


    public static void clientTick(Level level_, BlockPos pos_, BlockState state_, GoldBotSpawnerTile blockEntity_) {
        blockEntity_.spawner.clientTick(level_, pos_);
    }

    public static void serverTick(Level level_, BlockPos pos_, BlockState state_, GoldBotSpawnerTile blockEntity_) {
        blockEntity_.spawner.serverTick((ServerLevel) level_, pos_);
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag compoundnbt = this.saveWithoutMetadata();
        compoundnbt.remove("SpawnPotentials");
        return compoundnbt;
    }

    @Override
    public boolean triggerEvent(int id_, int type_) {
        return this.spawner.onEventTriggered(this.level, id_) || super.triggerEvent(id_, type_);
    }

    @Override
    public boolean onlyOpCanSetNbt() {
        return true;
    }

    public BaseSpawner getSpawner() {
        return this.spawner;
    }
}