package net.jitl.init.internal;

import net.jitl.JITL;
import net.jitl.common.block.JOreBlock;
import net.jitl.common.helper.EnumHarvestLevel;
import net.jitl.init.JTabs;
import net.jitl.util.JBlockProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import ru.timeconqueror.timecore.api.client.resource.location.BlockModelLocation;
import ru.timeconqueror.timecore.registry.AutoRegistrable;
import ru.timeconqueror.timecore.registry.newreg.BlockRegister;

public class BlockRegistrator {

    @AutoRegistrable
    private static final BlockRegister REGISTER = new BlockRegister(JITL.MODID);

    @AutoRegistrable.InitMethod
    private static void register() {
        registerOreBlock("sapphire_ore", "Sapphire Ore", EnumHarvestLevel.DIAMOND, 3);
        registerEmissiveOreBlock("lunium_ore", "Lunium Ore", EnumHarvestLevel.DIAMOND, 0);
        registerOreBlock("shadium_ore", "Shadium Ore", EnumHarvestLevel.DIAMOND, 0);
        registerOreBlock("iridium_ore", "Iridium Ore", EnumHarvestLevel.IRON, 3);
        registerOreBlock("bloodcrust_ore", "Bloodcrust Ore", EnumHarvestLevel.DIAMOND, 0);
        registerOreBlock("ashual_ore", "Ashual Ore", EnumHarvestLevel.DIAMOND, 4);
        registerOreBlock("blazium_ore", "Blazium Ore", EnumHarvestLevel.DIAMOND, 4);
        registerOreBlock("mekyum_ore", "Mekyum Ore", EnumHarvestLevel.DIAMOND, 0);
        registerOreBlock("celestium_ore", "Celestium Ore", EnumHarvestLevel.DIAMOND, 0);
        registerOreBlock("storon_ore", "Storon Ore", EnumHarvestLevel.DIAMOND, 0);
        registerOreBlock("flairium_ore", "Flairium Ore", EnumHarvestLevel.DIAMOND, 0);
        registerOreBlock("des_ore", "Des Ore", EnumHarvestLevel.DIAMOND, 0);
        registerOreBlock("enderillium_ore", "Enderillium Ore", EnumHarvestLevel.DIAMOND, 4);
        registerOreBlock("gorbite_ore", "Gorbite Ore", EnumHarvestLevel.DIAMOND, 4);
        registerOreBlock("orbadite_ore", "Orbadite Ore", EnumHarvestLevel.DIAMOND, 0);
        registerOreBlock("lunite_ore", "Lunite Ore", EnumHarvestLevel.DIAMOND, 6);
        registerOreBlock("firestone_ore", "Firestone Ore", EnumHarvestLevel.DIAMOND, 3);

        registerDefaultBlock("lava_rock", "Lava Rock");
        registerDefaultBlock("sapphire_block", "Sapphire Block");
        registerDefaultBlock("lunium_block", "Lunium Block");
        registerDefaultBlock("shadium_block", "Shadium Block");
        registerDefaultBlock("iridium_block", "Iridium Block");
        registerDefaultBlock("bloodcrust_block", "Bloodcrust Block");
        registerDefaultBlock("ashual_block", "Ashual Block");
        registerDefaultBlock("blazium_block", "Blazium Block");
        registerDefaultBlock("mekyum_block", "Mekyum Block");
        registerDefaultBlock("celestium_block", "Celestium Block");
        registerDefaultBlock("storon_block", "Storon Block");
        registerDefaultBlock("flairium_block", "Flairium Block");
        registerDefaultBlock("des_block", "Des Block");
        registerDefaultBlock("enderillium_block", "Enderillium Block");
        registerDefaultBlock("gorbite_block", "Gorbite Block");
        registerDefaultBlock("orbadite_block", "Orbadite Block");
        registerDefaultBlock("lunite_block", "Lunite Block");
        registerDefaultBlock("nethic_gemstone_block", "Nethic Gemstone Block");
        registerDefaultBlock("frost_gem_block", "Frost Gems Block");

        registerDefaultBlock("dungeon_bricks", "Dungeon Bricks");
        registerDefaultBlock("dungeon_bricks_carved", "Carved Dungeon Bricks");
        registerDefaultBlock("dungeon_bricks_chiseled", "Chiseled Dungeon Bricks");
        registerDefaultBlock("dungeon_bricks_cracked", "Cracked Dungeon Bricks");
        registerDefaultBlock("dungeon_lamp", "Dungeon Lamp");
        registerDefaultBlock("common_gems", "Common Gems");
        registerDefaultBlock("rare_gems", "Rare Gems");

        registerDefaultBlock("euca_brick", "Euca Brick");
    }

    private static void registerDefaultBlock(String name, String enName) {
        REGISTER.register(name, () -> new Block
                (JBlockProperties.STONE_PROPS.create()))
                .genLangEntry(enName)
                .regDefaultBlockItem(JTabs.BLOCKS)
                .genDefaultStateAndModel();
    }

    private static void registerOreBlock(String name, String enName, EnumHarvestLevel harvestLevel, int minExp) {
        REGISTER.register(name, () -> new JOreBlock
                (JBlockProperties.ORE_PROPS.create()
                        .harvestLevel(harvestLevel.getInt()))
                .setExpDrop(minExp))
                .genLangEntry(enName)
                .regDefaultBlockItem(JTabs.BLOCKS)
                .genDefaultStateAndModel();
    }

    //test
    private static void registerEmissiveOreBlock(String name, String enName, EnumHarvestLevel harvestLevel, int minExp) {
        REGISTER.register(name, () -> new JOreBlock
                (JBlockProperties.ORE_PROPS.create()
                        .harvestLevel(harvestLevel.getInt())
//                        .emissiveRendering(BlockRegistrator::always))
                )
                .setExpDrop(minExp))
                .genLangEntry(enName)
                .regDefaultBlockItem(JTabs.BLOCKS)
                .genDefaultState(new BlockModelLocation(JITL.MODID, "block/" + name));
    }

    private static boolean always(BlockState blockState, IBlockReader blockReader, BlockPos blockPos) {
        return true;
    }
}
