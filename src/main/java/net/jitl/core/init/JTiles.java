package net.jitl.core.init;

import com.google.common.collect.Lists;
import net.jitl.client.render.tile.*;
import net.jitl.common.tile.*;
import net.jitl.common.tile.base.JFurnaceTile;
import net.jitl.common.tile.spawner.GoldBotSpawnerTile;
import net.jitl.common.tile.spawner.MiniGhastSpawnerTile;
import net.jitl.core.JITL;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ObjectHolder;
import ru.timeconqueror.timecore.api.registry.TileEntityRegister;
import ru.timeconqueror.timecore.api.registry.util.AutoRegistrable;

import static ru.timeconqueror.timecore.api.util.Hacks.promise;

@ObjectHolder(JITL.MODID)
public class JTiles {

    public static final BlockEntityType<LaserEmitterTile> LASER_EMITTER = promise();
    public static final BlockEntityType<GoldBotSpawnerTile> GOLD_BOT_SPAWNER = promise();
    public static final BlockEntityType<MiniGhastSpawnerTile> MINI_GHAST_SPAWNER = promise();
    public static final BlockEntityType<EssenciaAltarTile> ESSENCIA_ALTAR = promise();
    public static final BlockEntityType<GuardianTowerBrainTile> GUARDIAN_TOWER_BRAIN = promise();
    public static final BlockEntityType<PedestalTile> PEDESTAL = promise();
    public static final BlockEntityType<ClayPotTile> CLAY_POTTERY = promise();
    public static final BlockEntityType<ClayVaseTile> CLAY_VASE = promise();
    public static final BlockEntityType<ObeliskTile> OBELISK = promise();
    public static final BlockEntityType<JFurnaceTile> JFURNACE = promise();
    public static final BlockEntityType<JChestBlockEntity> JCHEST = promise();

    public static final BlockEntityType<BloodRuneTile> BLOOD_RUNE = promise();
    public static final BlockEntityType<BloodRuneFleshTile> BLOOD_RUNE_FLESH = promise();
    public static final BlockEntityType<BloodRuneSoulTile> BLOOD_RUNE_SOUL = promise();
    public static final BlockEntityType<BloodRuneLifeTile> BLOOD_RUNE_LIFE = promise();
    public static final BlockEntityType<BloodRuneDeathTile> BLOOD_RUNE_DEATH = promise();

    private static class Registrator {
        @AutoRegistrable
        private static final TileEntityRegister REGISTER = new TileEntityRegister(JITL.MODID);

        @AutoRegistrable.Init
        private static void register() {
            REGISTER.register("jfurnace", JFurnaceTile::new, () -> Lists.newArrayList(JBlocks.GOLDITE_FURNACE, JBlocks.PERMAFROST_FURNACE));
            REGISTER.register("jchest", JChestBlockEntity::new, () -> Lists.newArrayList(JBlocks.JOURNEY_CHEST, JBlocks.EUCA_CHEST, JBlocks.BOIL_CHEST, JBlocks.FROZEN_CHEST, JBlocks.NETHER_CHEST)).regCustomRenderer(() -> JChestTER::new);

            REGISTER.registerSingleBound("laser_emitter", LaserEmitterTile::new, () -> JBlocks.LASER_EMITTER).regCustomRenderer(() -> LaserEmitterTER::new);
            REGISTER.registerSingleBound("gold_bot_spawner", GoldBotSpawnerTile::new, () -> JBlocks.GOLD_BOT_SPAWNER);
            REGISTER.registerSingleBound("mini_ghast_spawner", MiniGhastSpawnerTile::new, () -> JBlocks.MINI_GHAST_SPAWNER);

            REGISTER.registerSingleBound("essencia_altar", EssenciaAltarTile::new, () -> JBlocks.ESSENCIA_ALTAR).regCustomRenderer(() -> EssenciaAltarTER::new);
            REGISTER.registerSingleBound("guardian_tower_brain", GuardianTowerBrainTile::new, () -> JBlocks.GUARDIAN_TOWER_BRAIN);
            REGISTER.registerSingleBound("pedestal", PedestalTile::new, () -> JBlocks.FROZEN_PEDESTAL).regCustomRenderer(() -> PedestalTER::new);
            REGISTER.registerSingleBound("clay_pottery", ClayPotTile::new, () -> JBlocks.CLAY_POTTERY);
            REGISTER.registerSingleBound("clay_vase", ClayVaseTile::new, () -> JBlocks.CLAY_VASE);
            REGISTER.registerSingleBound("obelisk", ObeliskTile::new, () -> JBlocks.ANCIENT_OBELISK).regCustomRenderer(() -> ObeliskTER::new);

            REGISTER.registerSingleBound("blood_rune", BloodRuneTile::new, () -> JBlocks.EMPTY_BLOOD_RUNE).regCustomRenderer(() -> BloodRuneTER::new);
            REGISTER.registerSingleBound("blood_rune_flesh", BloodRuneFleshTile::new, () -> JBlocks.BLOOD_RUNE_FLESH).regCustomRenderer(() -> BloodRuneTER::new);
            REGISTER.registerSingleBound("blood_rune_soul", BloodRuneSoulTile::new, () -> JBlocks.BLOOD_RUNE_SOUL).regCustomRenderer(() -> BloodRuneTER::new);
            REGISTER.registerSingleBound("blood_rune_life", BloodRuneLifeTile::new, () -> JBlocks.BLOOD_RUNE_LIFE).regCustomRenderer(() -> BloodRuneTER::new);
            REGISTER.registerSingleBound("blood_rune_death", BloodRuneDeathTile::new, () -> JBlocks.BLOOD_RUNE_DEATH).regCustomRenderer(() -> BloodRuneTER::new);
        }
    }
}
