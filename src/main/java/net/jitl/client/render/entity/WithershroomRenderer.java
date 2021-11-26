package net.jitl.client.render.entity;

import net.jitl.JITL;
import net.jitl.client.render.model.HongoModel;
import net.jitl.common.entity.overworld.WithershroomEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class WithershroomRenderer extends MobRenderer<WithershroomEntity, HongoModel<WithershroomEntity>> {
    public WithershroomRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new HongoModel<>(), 0.7F);
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull WithershroomEntity entityIn) {
        return JITL.rl("textures/entity/overworld/withershroom.png");
    }
}
