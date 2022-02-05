package net.jitl.client.render.overlay.internal;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.jitl.client.util.RenderUtils;
import net.jitl.common.capability.player.JPlayer;
import net.jitl.common.capability.player.data.Essence;
import net.jitl.core.JITL;
import net.jitl.core.config.JClientConfig;
import net.jitl.core.config.JConfigs;
import net.jitl.core.util.IEssenceItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public class EssenceOverlay {
    private static float transparency;
    private static float burnoutTransparency;

    private static final ResourceLocation OVER_EXP_TEXTURE = JITL.tl("gui/essence_over_exp.png").fullLocation();
    private static final ResourceLocation ABOVE_HUNGER_TEXTURE = JITL.tl("gui/essence_over_hunger.png").fullLocation();

    public static void render(PoseStack matrixStack, int height, int width) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player != null && !player.isCreative() && !player.isSpectator()) {
            JPlayer cap = JPlayer.from(player);
            if (cap != null) {
                float currentEssence = cap.essence.getCurrentEssence();
                float maxEssence = Essence.getMaxEssence(player);
                float cooldown = cap.essence.getBurnout();

                boolean isEssenceUsed = currentEssence < maxEssence;

                /*
                 * Handles the transparency of the Essence bar
                 */
                if ((instanceOfEssenceItem(player.getMainHandItem().getItem()) || isEssenceUsed) && transparency <= 1.0) {
                    transparency += .02;
                } else if (transparency > 0) {
                    transparency -= .02;
                }

                boolean cooldownActive = cooldown > 1.0F;

                /*
                 * Handles the transparency of the burnout overlay
                 */
                if (cooldownActive && burnoutTransparency < 1) {
                    burnoutTransparency += .02;
                } else if (burnoutTransparency > 0) {
                    burnoutTransparency -= .02;
                }

                /*
                 * Determines the position of the Essence bar
                 */
                JClientConfig.GuiCategory config = JConfigs.CLIENT.GUI_CATEGORY;
                EssencePosition essencePosition = config.getEssencePosition();
                int yPos = config.getEssenceYPos();
                int xPos = config.getEssenceXPos();

                if (!minecraft.options.hideGui && transparency > 0 && !player.isSpectator()) {
                    int y = height - yPos;
                    int x = width / 2 - xPos;

                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem.setShaderTexture(0, getTextureBasedOnPosition(essencePosition));
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, transparency);

                    /*
                     * Our texture is relative to the configured position.
                     * So, we have to adapt the UV, render height and texture height of the rectangle depending on the configured position.
                     */
                    boolean isOverXPBar = essencePosition == EssencePosition.OVER_EXP_BAR;
                    int barHeight = isOverXPBar ? 5 : 9;
                    int texHeight = isOverXPBar ? 15 : 27;
                    int backgroundVOffset = isOverXPBar ? 5 : 9;
                    int burnoutVOffset = isOverXPBar ? 10 : 18;

                    RenderUtils.blit(matrixStack, x, y, 0, backgroundVOffset, 81, barHeight, 81, texHeight);

                    if (cooldownActive) {
                        /*
                         * Sin function ranging from 0 to 1
                         */
                        float sin = (float) Math.sin((float) player.tickCount / 5F) / 2F + 0.5F;
                        /*
                         * When the cooldown starts getting close to zero, the bar fades out.
                         */
                        float cooldownFade = Math.min(cooldown, 10) / 10;
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, sin * cooldownFade);

                        RenderUtils.blit(matrixStack, x, y, 0, 0, 81, barHeight, 81, texHeight);
                    } else {
                        int i = (int) ((currentEssence / maxEssence) * 81);
                        RenderUtils.blit(matrixStack, x, y, 0, 0, i, barHeight, 81, texHeight);
                    }

                    if (burnoutTransparency > 0) {
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, burnoutTransparency);
                        RenderUtils.blit(matrixStack, x, y, 0, burnoutVOffset, 81, barHeight, 81, texHeight);
                    }
                }
            }
        }
    }

    /**
     * Method used to group all Essence-related items under one umbrella.
     * Allows Essence transparency rendering to be compatible with (hopefully) all Essence-related items without referencing many individual classes/items
     */
    public static boolean instanceOfEssenceItem(Item isEssence) {
        return isEssence instanceof IEssenceItem;
    }


    /**
     * Returns a texture based off of the configured position set by the player.
     */
    private static ResourceLocation getTextureBasedOnPosition(EssencePosition essencePosition) {
        if (essencePosition == EssencePosition.OVER_EXP_BAR) {
            return OVER_EXP_TEXTURE;
        } else {
            return ABOVE_HUNGER_TEXTURE;
        }
    }
}
