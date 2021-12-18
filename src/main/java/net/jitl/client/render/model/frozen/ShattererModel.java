package net.jitl.client.render.model.frozen;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public class ShattererModel <T extends Entity> extends SegmentedModel<T> {

    private final ModelRenderer eyes;
    private final ModelRenderer spike1;
    private final ModelRenderer spike2;
    private final ModelRenderer arm1;
    private final ModelRenderer arm2;

    public ShattererModel() {
        this.texWidth = 64;
        this.texHeight = 64;

        eyes = new ModelRenderer(this);
        eyes.setPos(0.0F, 4.0F, 1.5F);
        eyes.texOffs(0, 0).addBox(-5.0F, -5.0F, -1.5F, 10.0F, 10.0F, 3.0F, 0.0F, false);
        eyes.texOffs(16, 13).addBox(-19.0F, -3.0F, -0.5F, 6.0F, 6.0F, 1.0F, 0.0F, false);
        eyes.texOffs(16, 13).addBox(-3.0F, 13.0F, -0.5F, 6.0F, 6.0F, 1.0F, 0.0F, false);
        eyes.texOffs(16, 13).addBox(-3.0F, -19.0F, -0.5F, 6.0F, 6.0F, 1.0F, 0.0F, false);
        eyes.texOffs(16, 13).addBox(13.0F, -3.0F, -0.5F, 6.0F, 6.0F, 1.0F, 0.0F, false);

        spike1 = new ModelRenderer(this);
        spike1.setPos(0.1612F, 4.0F, 1.5F);
        setRotationAngle(spike1, 0.0F, 0.0F, -0.7854F);
        spike1.texOffs(16, 32).addBox(-12.5459F, -1.0F, -0.5F, 8.0F, 2.0F, 1.0F, 0.0F, false);
        spike1.texOffs(16, 32).addBox(4.5459F, -1.0F, -0.5F, 8.0F, 2.0F, 1.0F, 0.0F, false);

        spike2 = new ModelRenderer(this);
        spike2.setPos(0.0F, 4.0F, 1.5F);
        setRotationAngle(spike2, 0.0F, 0.0F, 0.7854F);
        spike2.texOffs(16, 32).addBox(4.7071F, -1.0F, -0.5F, 8.0F, 2.0F, 1.0F, 0.0F, false);
        spike2.texOffs(16, 32).addBox(-12.3848F, -1.0F, -0.5F, 8.0F, 2.0F, 1.0F, 0.0F, false);

        arm1 = new ModelRenderer(this);
        arm1.setPos(0.0F, 4.0F, 1.5F);
        arm1.texOffs(28, 35).addBox(-13.0F, -1.0F, -0.5F, 8.0F, 2.0F, 1.0F, 0.0F, false);
        arm1.texOffs(28, 35).addBox(5.0F, -1.0F, -0.5F, 8.0F, 2.0F, 1.0F, 0.0F, false);

        arm2 = new ModelRenderer(this);
        arm2.setPos(0.0F, 4.0F, 1.5F);
        setRotationAngle(arm2, 0.0F, 0.0F, 1.5708F);
        arm2.texOffs(28, 35).addBox(-13.0F, -1.0F, -0.5F, 8.0F, 2.0F, 1.0F, 0.0F, false);
        arm2.texOffs(28, 35).addBox(5.0F, -1.0F, -0.5F, 8.0F, 2.0F, 1.0F, 0.0F, false);
    }

    @Override
    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of(eyes, spike1, spike2, arm1, arm2);
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float f = 8;
        this.eyes.zRot = ageInTicks / f;
        this.spike1.zRot = -2.4F + ageInTicks / f;
        this.spike2.zRot = 2.4F + ageInTicks / f;
        this.arm1.zRot = ageInTicks / f;
        this.arm2.zRot = 4.7F + ageInTicks / f;
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}