package net.jitl.client.eventhandler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.jitl.JITL;
import net.jitl.client.render.gui.button.ToggleMenuButton;
import net.jitl.client.render.gui.menu.JMainMenuGui;
import net.jitl.client.util.RenderUtils;
import net.jitl.common.capability.player.JPlayer;
import net.jitl.common.capability.player.data.Essence;
import net.jitl.config.JClientConfig;
import net.jitl.config.JConfigs;
import net.jitl.util.IEssenceItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = JITL.MODID, value = Dist.CLIENT)
public class GuiEventHandler {
	public static float maxEssence = 10F;

	public static float essence = 10F;

	private static float transparency;

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void overrideMainMenu(GuiOpenEvent event) {
		if (JConfigs.CLIENT.GUI_CATEGORY.isJITLMenuEnabled()) {
			if (event.getGui() instanceof MainMenuScreen) {
				event.setGui(new JMainMenuGui());
			}
		}
	}

	@SubscribeEvent()
	public static void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event) {
		JClientConfig.GuiCategory guiConfig = JConfigs.CLIENT.GUI_CATEGORY;
		Minecraft minecraft = Minecraft.getInstance();
		int x = event.getGui().width / 1024;

		ToggleMenuButton buttonToggleMenu = new ToggleMenuButton(x, 0, (action) -> {
			guiConfig.setJITLMenu(!guiConfig.isJITLMenuEnabled());
			if (!guiConfig.isJITLMenuEnabled()) {
				minecraft.setScreen(new MainMenuScreen());
			} else {
				minecraft.setScreen(new JMainMenuGui());
			}
		});
		if (event.getGui() instanceof MainMenuScreen) {
			if (guiConfig.isToggleMenuButtonEnabled()) {
				event.addWidget(buttonToggleMenu);
			}
		}
	}

	@SubscribeEvent()
	public static void renderEssenceBar(RenderGameOverlayEvent.Post event) {
		if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
			Minecraft minecraft = Minecraft.getInstance();
			PlayerEntity player = minecraft.player;
			if (!player.isCreative()) {
				MatrixStack matrixStack = event.getMatrixStack();
				JPlayer cap = JPlayer.from(player);
				if (cap != null) {
					float currentEssence = cap.essence.get().getCurrentEssence();
					float maxEssence = Essence.getMaxEssence(player);

					boolean isEssenceUsed = currentEssence < maxEssence;

					if (instanceOfEssenceItem(player.getMainHandItem().getItem()) || isEssenceUsed && transparency <= 1.0) {
						transparency += .02;
					} else if (transparency > 0) {
						transparency -= .02;
					}
					if (!minecraft.options.hideGui) {
						int l = event.getWindow().getGuiScaledHeight() - 32 + 3;
						int w = event.getWindow().getGuiScaledWidth() / 2 - 91;

						RenderSystem.color4f(1.0F, 1.0F, 1.0F, transparency);
						minecraft.getTextureManager().bind(JITL.tl("gui/essence.png").fullLocation());
						RenderUtils.blit(matrixStack, w, l, 0, 5, 81, 5, 81, 10);

						int i = (int) ((currentEssence / maxEssence) * 81);
						RenderUtils.blit(matrixStack, w, l, 0, 0, i, 5, 81, 10);
					}
				}
			}
		}
	}

	public static boolean instanceOfEssenceItem(Item isEssence) {
		return isEssence instanceof IEssenceItem;
	}
}
