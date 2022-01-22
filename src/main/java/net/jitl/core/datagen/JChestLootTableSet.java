package net.jitl.core.datagen;

import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import ru.timeconqueror.timecore.api.devtools.gen.loottable.LootTableSet;

public class JChestLootTableSet extends LootTableSet {
    @Override
    public LootContextParamSet getParameterSet() {
        return LootContextParamSets.CHEST;
    }


    @Override
    public void register() {
        /*registerLootTable(JITL.rl("chests/test"),
                LootTable.lootTable().withPool(LootPool.lootPool()
                        .setRolls(UniformGenerator.between(2.0F, 8.0F))
                        .add(LootItem.lootTableItem(Items.MAP).setWeight(10).apply(ExplorationMapFunction.makeExplorationMap().setDestination(JStructures.ILlAGER_BUNKER.getStructure())
                                .setMapDecoration(MapDecoration.Type.BANNER_LIGHT_GRAY).setZoom((byte) 1).setSkipKnownStructures(false)))));

        registerLootTable(JITL.rl("chests/ancient_ruins"),
                LootTable.lootTable().withPool(LootPool.lootPool()
                        .setRolls(UniformGenerator.between(2.0F, 8.0F))
                        .add(LootItem.lootTableItem(Items.MAP).setWeight(10).apply(ExplorationMapFunction.makeExplorationMap().setDestination(JStructures.ANCIENT_RUINS.getStructure())
                                .setMapDecoration(MapDecoration.Type.BANNER_LIGHT_GRAY).setZoom((byte) 1).setSkipKnownStructures(false)))));*/
    }
}