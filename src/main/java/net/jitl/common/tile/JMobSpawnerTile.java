package net.jitl.common.tile;

import net.jitl.init.JTiles;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.SpawnData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BaseSpawner;

import javax.annotation.Nullable;

public class JMobSpawnerTile extends BlockEntity implements TickableBlockEntity {

    private final BaseSpawner spawner = new BaseSpawner() {
        public void broadcastEvent(int id) {
            JMobSpawnerTile.this.level.blockEvent(JMobSpawnerTile.this.worldPosition, Blocks.SPAWNER, id, 0);
        }

        public Level getLevel() {
            return JMobSpawnerTile.this.level;
        }

        public BlockPos getPos() {
            return JMobSpawnerTile.this.worldPosition;
        }

        public void setNextSpawnData(SpawnData nextSpawnData) {
            super.setNextSpawnData(nextSpawnData);
            if (this.getLevel() != null) {
                BlockState blockstate = this.getLevel().getBlockState(this.getPos());
                this.getLevel().sendBlockUpdated(JMobSpawnerTile.this.worldPosition, blockstate, blockstate, 4);
            }

        }
    };

    public JMobSpawnerTile() {
        super(JTiles.MOB_SPAWNER);
    }

    @Override
    public void load(BlockState state, CompoundTag nbt) {
        super.load(state, nbt);
        this.spawner.load(nbt);
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        this.spawner.save(compound);
        return compound;
    }

    @Override
    public void tick() {
        this.spawner.tick();
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 1, this.getUpdateTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compoundnbt = this.save(new CompoundTag());
        compoundnbt.remove("SpawnPotentials");
        return compoundnbt;
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        return this.spawner.onEventTriggered(id) || super.triggerEvent(id, type);
    }

    @Override
    public boolean onlyOpCanSetNbt() {
        return true;
    }

    public BaseSpawner getSpawner() {
        return this.spawner;
    }
}
