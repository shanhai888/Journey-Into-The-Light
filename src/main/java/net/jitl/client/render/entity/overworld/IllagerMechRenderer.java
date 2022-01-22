package net.jitl.client.render.entity.overworld;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.jitl.common.entity.overworld.IllagerMechEntity;
import net.jitl.core.JITL;
import net.jitl.core.init.client.JsonModels;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import ru.timeconqueror.timecore.animation.renderer.AnimatedLivingEntityRenderer;
import ru.timeconqueror.timecore.animation.renderer.ModelConfiguration;
import ru.timeconqueror.timecore.client.render.model.TimeEntityModel;
import ru.timeconqueror.timecore.client.render.model.TimeModelPart;

import java.util.Map;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class IllagerMechRenderer extends AnimatedLivingEntityRenderer<IllagerMechEntity, TimeEntityModel<IllagerMechEntity>> {
    private static final Map<IllagerMechEntity.Cracks, ResourceLocation> resourceLocations = ImmutableMap.of(
            IllagerMechEntity.Cracks.LOW, JITL.rl("textures/entity/overworld/illager_mech_cracked_low.png"),
            IllagerMechEntity.Cracks.MEDIUM, JITL.rl("textures/entity/overworld/illager_mech_cracked_medium.png"),
            IllagerMechEntity.Cracks.HIGH, JITL.rl("textures/entity/overworld/illager_mech_cracked_high.png"));

    public IllagerMechRenderer(EntityRendererProvider.Context context) {
        super(context, new TimeEntityModel<>(ModelConfiguration.builder(JsonModels.ILLAGER_MECH).build()), 0.5F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull IllagerMechEntity entity) {
        IllagerMechEntity.Cracks cracks = entity.getCrackiness();
        if (cracks != IllagerMechEntity.Cracks.NONE) {
            return resourceLocations.get(cracks);
        } else {
            return JITL.rl("textures/entity/overworld/illager_mech.png");
        }
    }

    @Override
    protected void setupRotations(@NotNull IllagerMechEntity entityLiving, @NotNull PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
        float f = Mth.rotLerp(partialTicks, entityLiving.yBodyRotO, entityLiving.yBodyRot);
        float f1 = Mth.rotLerp(partialTicks, entityLiving.yHeadRotO, entityLiving.yHeadRot);
        float f2 = f1 - f;

        TimeModelPart headPiece = Objects.requireNonNull(model.getPart("head"));

        Objects.requireNonNull(headPiece).yRot = f2 * ((float) Math.PI / 270F);
        Objects.requireNonNull(headPiece).xRot = Mth.lerp(partialTicks, entityLiving.xRotO, entityLiving.getXRot()) * ((float) Math.PI / 270F);

        if (!((double) entityLiving.animationSpeed < 0.01D)) {
            float v1 = entityLiving.animationPosition - entityLiving.animationSpeed * (1.0F - partialTicks) + 6.0F;
            float v2 = (Math.abs(v1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
            matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(6.5F * v2));
        }
    }
}