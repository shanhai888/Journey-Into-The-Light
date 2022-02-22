package net.jitl.client.render.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.jitl.common.capability.player.JPlayer;
import net.jitl.common.capability.player.data.Knowledge;
import net.jitl.common.container.EmptyContainer;
import net.jitl.common.helper.ArgbColor;
import net.jitl.common.helper.EnumKnowledgeType;
import net.jitl.core.JITL;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import ru.timeconqueror.timecore.api.util.client.ClientProxy;

public class PlayerStatsScreen extends AbstractContainerScreen<EmptyContainer> {

    private PageButton nextButton;
    private PageButton previousButton;
    private final ResourceLocation KNOWLEDGE_SPRITE = JITL.rl("textures/gui/knowledge/knowledge_sprites.png");
    private final ResourceLocation BACKGROUND = JITL.rl("textures/gui/stats.png");
    public int pageNumber = 0;
    private final JPlayer jPlayer;

    public PlayerStatsScreen(Inventory playerInventory) {
        super(new EmptyContainer(), playerInventory, new TranslatableComponent("jitl.stats"));
        this.imageWidth = 242;
        this.imageHeight = 197;
        this.jPlayer = JPlayer.from(ClientProxy.player());
    }

    @Override
    protected void init() {
        super.init();
        int w = (this.width - this.imageWidth) / 2;
        int h = (this.height - this.imageHeight) / 2;
        int xPos = w + 95;
        int yPos = h + 177;
        this.nextButton = this.addRenderableWidget(new PageButton(xPos + 32, yPos, true, (button) -> {
            this.flipPage(true);
        }, true));
        this.previousButton = this.addRenderableWidget(new PageButton(xPos, yPos, false, (button) -> {
            this.flipPage(false);
        }, true));
        this.updateButtonVisibility();
    }

    private void updateButtonVisibility() {
        this.nextButton.visible = true;
        this.previousButton.visible = true;
        this.nextButton.active = pageNumber != 1;
        this.previousButton.active = pageNumber != 0;
    }

    protected void flipPage(boolean forward) {
        if(forward) this.pageNumber++;
        else this.pageNumber--;
        this.updateButtonVisibility();
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        this.renderBackground(poseStack);//Dims around the GUI for a more vanilla look
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        poseStack.pushPose();
        RenderSystem.setShader(GameRenderer::getPositionTexColorNormalShader);
        RenderSystem.setShaderTexture(0, this.BACKGROUND);
        blit(poseStack, x, y, 0, 0, this.imageWidth, this.imageHeight);//Draws the main Background

        switch(pageNumber) {
            case 0 -> page1(poseStack);
            case 1 -> page2(poseStack);
            default -> {
            }
        }
        poseStack.popPose();
        RenderSystem.enableDepthTest();
    }

    public void page1(PoseStack stack) {
        int height = 43;
        int x = 9;
        int h = 9;
        drawSprite(stack, x, h, 0, 74, "Sentacoins:");
        drawKnowledgeSprite(stack, 126, h, EnumKnowledgeType.OVERWORLD, "Overworld");

        h += height;

        drawKnowledgeSprite(stack, x, h, EnumKnowledgeType.END, "End");
        drawKnowledgeSprite(stack, 126, h, EnumKnowledgeType.FROZEN, "Frozen Lands");

        h += height;
        drawKnowledgeSprite(stack, x, h, EnumKnowledgeType.NETHER, "The Nether");
        drawKnowledgeSprite(stack, 126, h, EnumKnowledgeType.BOIL, "Boiling Point");

        h += height;
        drawKnowledgeSprite(stack, x, h, EnumKnowledgeType.EUCA, "Euca");
        drawKnowledgeSprite(stack, 126, h, EnumKnowledgeType.DEPTHS, "The Depths");
    }

    public void drawSprite(PoseStack matrixStack, int x, int y, int spriteX, int spriteY, String s) {
        int k = (width - imageWidth) / 2;
        int l = (height - imageHeight) / 2;
        matrixStack.pushPose();
        RenderSystem.setShader(GameRenderer::getPositionTexColorNormalShader);
        RenderSystem.setShaderTexture(0, this.BACKGROUND);

        blit(matrixStack, k + x - 4, l + y - 4, 0, 216, 115, 40);//Draws the yellow rectangle bg for the sprites
        matrixStack.popPose();

        matrixStack.pushPose();
        RenderSystem.setShader(GameRenderer::getPositionTexColorNormalShader);
        RenderSystem.setShaderTexture(0, this.KNOWLEDGE_SPRITE);

        blit(matrixStack, k + x, l + y, spriteX, spriteY, 32, 32); //Draws the knowledge sprite
        font.draw(matrixStack, s, k + x + 35, l + y + 5, 4210752); //Draws the sprite name

        if(s.contains("Sentacoins"))
            font.draw(matrixStack, "" + jPlayer.sentacoins.getAmount(), k + x + 35, l + y + 15, 4210752);

        matrixStack.popPose();
        RenderSystem.enableDepthTest();
    }

    public void drawKnowledgeSprite(PoseStack matrixStack, int x, int y, EnumKnowledgeType type, String s) {
        drawSprite(matrixStack, x, y, type.getSpriteX(), type.getSpriteY(), s);
        int progressBarSize = 65;
        int k = (width - imageWidth) / 2;
        int l = (height - imageHeight) / 2;
        matrixStack.pushPose();
        RenderSystem.setShader(GameRenderer::getPositionTexColorNormalShader);
        RenderSystem.setShaderTexture(0, this.KNOWLEDGE_SPRITE);
        Knowledge knowledge = jPlayer.knowledge;

        float percents = knowledge.getXP(type) / knowledge.getLevelCapacity(knowledge.getLevel(type));
        int width = (int) (percents * progressBarSize);

        int progressBarX = k + x + 35, progressBarY = l + y + 19;
        blit(matrixStack, progressBarX, progressBarY, 0, 5, progressBarSize, 5);
        blit(matrixStack, progressBarX, progressBarY, 0, 0, width, 5);

        int lvX = progressBarX + 29, lvY = progressBarY - 1;

        int getLevelCount = knowledge.getLevel(type);

        font.drawShadow(matrixStack, "" + getLevelCount, getLevelCount > 10 ? lvX - 2 : getLevelCount > 100 ? lvX - 4 : lvX, lvY, ArgbColor.from(ChatFormatting.WHITE));
        matrixStack.popPose();
        RenderSystem.enableDepthTest();
    }

    public void page2(PoseStack stack) {
        int height = 43;
        int x = 9;
        int h = 9;

        drawKnowledgeSprite(stack, x, h,  EnumKnowledgeType.CORBA, "Corba");
        drawKnowledgeSprite(stack, 126, h,  EnumKnowledgeType.TERRANIA, "Terrania");

        h += height;

        drawKnowledgeSprite(stack, x, h,  EnumKnowledgeType.CLOUDIA, "Cloudia");
        drawKnowledgeSprite(stack, 126, h,  EnumKnowledgeType.SENTERIAN, "Senterain");
        //h += height;
        //drawKnowledgeSprite(x, h, 128, 10, 0.30F, E"SDHFSDH");
        //drawKnowledgeSprite(126, h, 160, 10, 0.75F, "Frozen Lands");
        //h += height - 2;
        //drawKnowledgeSprite(x, h, 192, 10, 0.10F, "Euca");
        //drawKnowledgeSprite(126, h, 224, 10, 0.60F, "The Depths");
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.nextButton.active = pageNumber < 1;
        this.previousButton.active = pageNumber > 0;
    }

    @Override
    protected void renderLabels(@NotNull PoseStack matrixStack, int x, int y) {

    }
}