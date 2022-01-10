package net.jitl.common.world.gen.jigsaw;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.jitl.JITL;
import net.jitl.init.world.JProcessorLists;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;

//TODO: remove?
public class JigsawStarters {
    public static final StructureTemplatePool ESKIMO_CAMP_START = Pools.register(
            new StructureTemplatePool(JITL.rl("frozen/eskimo_camp/starting_well"),
                    new ResourceLocation("empty"),
                    ImmutableList.of(
                            Pair.of(StructurePoolElement.single(JITL.MODID + ":frozen/eskimo_camp/eskimo_starting_well", JProcessorLists.IGLOO_PROCESSOR), 1)),
                    StructureTemplatePool.Projection.RIGID));
}