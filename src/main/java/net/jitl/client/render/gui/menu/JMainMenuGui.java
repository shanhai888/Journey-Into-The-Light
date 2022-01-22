package net.jitl.client.render.gui.menu;

import com.google.common.util.concurrent.Runnables;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Vector3f;
import com.mojang.realmsclient.RealmsMainScreen;
import com.mojang.realmsclient.gui.screens.RealmsNotificationsScreen;
import net.jitl.client.render.BlurredCubeMap;
import net.jitl.client.render.gui.button.JButton;
import net.jitl.client.render.gui.button.JImageButton;
import net.jitl.core.JITL;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.SafetyScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.LevelSummary;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.ModListScreen;
import net.minecraftforge.client.gui.NotificationModUpdateScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.function.Consumer;

//FIXME fix buttons
@OnlyIn(Dist.CLIENT)
public class JMainMenuGui extends TitleScreen {
	private static final Logger LOGGER = LogManager.getLogger();
	public static final BlurredCubeMap CUBE_MAP = new BlurredCubeMap(JITL.rl("textures/gui/title/background/panorama"));
	private static final ResourceLocation PANORAMA_OVERLAY = JITL.rl("textures/gui/title/background/panorama_overlay.png");
	private static final ResourceLocation ACCESSIBILITY_TEXTURE = new ResourceLocation("textures/gui/accessibility.png");
	private static final ResourceLocation LANGUAGE_TEXTURE = JITL.rl("textures/gui/title/language_button.png");
	@Nullable
	private String splash;
	private Button resetDemoButton;
	private static final ResourceLocation MINECRAFT_LOGO = JITL.rl("textures/gui/title/logo.png");
	private net.minecraftforge.client.gui.NotificationModUpdateScreen modUpdateNotification;

	/**
	 * Has the check for a realms notification screen been performed?
	 */
	private boolean realmsNotificationsInitialized;
	/**
	 * A screen generated by realms for notifications; drawn in adition to the main menu (buttons and such from both are
	 * drawn at the same time). May be null.
	 */
	private Screen realmsNotificationsScreen;
	private int copyrightWidth;
	private int copyrightX;
	//private final PanoramaRenderer panorama = new PanoramaRenderer(CUBE_MAP);
	private final boolean fading;
	private long fadeInStart;

	public JMainMenuGui() {
		this(true);
	}

	public JMainMenuGui(boolean fadeIn) {
		this.fading = fadeIn;
	}

	/**
	 * Is there currently a realms notification screen, and are realms notifications enabled?
	 */
	private boolean realmsNotificationsEnabled() {
		return this.minecraft.options.realmsNotifications && this.realmsNotificationsScreen != null;
	}

	public void tick() {
		if (this.realmsNotificationsEnabled()) {
			this.realmsNotificationsScreen.tick();
		}

	}

	public boolean isPauseScreen() {
		return false;
	}

	public boolean shouldCloseOnEsc() {
		return false;
	}

	protected void init() {
		if (this.splash == null) {
			this.splash = this.minecraft.getSplashManager().getSplash();
		}

		this.copyrightWidth = this.font.width("Copyright Mojang AB. Do not distribute!");
		this.copyrightX = this.width - this.copyrightWidth - 2;
		int j = this.height / 4 + 48;
		JButton modButton = null;
		if (this.minecraft.isDemo()) {
			this.createDemoMenuOptions(j, 24);
		} else {
			this.createNormalMenuOptions(j);
			modButton = this.addRenderableWidget(new JButton(this.width / 2 - 206, 30 + j - 15, 200, 20, new TranslatableComponent("fml.menu.mods"), button -> {
				this.minecraft.setScreen(new ModListScreen(this));
			}, false));
		}
		modUpdateNotification = NotificationModUpdateScreen.init(this, modButton);

		this.addRenderableWidget(new JImageButton(this.width / 2 - 206, j + 75, 20, 20, 0, 0, 20, LANGUAGE_TEXTURE, 20, 40, (button9_) -> {
			this.minecraft.setScreen(new LanguageSelectScreen(this, this.minecraft.options, this.minecraft.getLanguageManager()));
		}, new TranslatableComponent("narrator.button.language")));
		this.addRenderableWidget(new JButton(this.width / 2 - 206, j + 72 + 12 - 39, 200, 20, new TranslatableComponent("menu.options"), (button8_) -> {
			this.minecraft.setScreen(new OptionsScreen(this, this.minecraft.options));
		}, false));
		this.addRenderableWidget(new JButton(this.width / 2 + 5, j + 72 + 12 - 39, 200, 20, new TranslatableComponent("menu.quit"), (button7_) -> {
			this.minecraft.stop();
		}, true));
		this.addRenderableWidget(new JImageButton(this.width / 2 + 185, j + 75, 20, 20, 0, 0, 20, ACCESSIBILITY_TEXTURE, 32, 64, (button6_) -> {
			this.minecraft.setScreen(new AccessibilityOptionsScreen(this, this.minecraft.options));
		}, new TranslatableComponent("narrator.button.accessibility")));
		this.minecraft.setConnectedToRealms(false);
		if (this.minecraft.options.realmsNotifications && !this.realmsNotificationsInitialized) {
			this.realmsNotificationsScreen = new RealmsNotificationsScreen();
			this.realmsNotificationsInitialized = true;
		}

		if (this.realmsNotificationsEnabled()) {
			this.realmsNotificationsScreen.init(this.minecraft, this.width, this.height);
		}
	}

	/**
	 * Adds Singleplayer and Multiplayer buttons on Main Menu for players who have bought the game.
	 */
	private void createNormalMenuOptions(int yIn) {
		this.addRenderableWidget(new JButton(this.width / 2 - 206, yIn - 15, 200, 20, new TranslatableComponent("menu.singleplayer"), (button5_) -> {
			assert this.minecraft != null;
			this.minecraft.setScreen(new SelectWorldScreen(this));
		}, false));
		assert this.minecraft != null;
		boolean flag = this.minecraft.allowsMultiplayer();
		Button.OnTooltip button$itooltip = flag ? Button.NO_TOOLTIP : new Button.OnTooltip() {
			private final Component text = new TranslatableComponent("title.multiplayer.disabled");

			public void onTooltip(Button button10_, PoseStack poseStack3_, int int_, int int1_) {
				if (!button10_.active) {
					JMainMenuGui.this.renderTooltip(poseStack3_, JMainMenuGui.this.minecraft.font.split(this.text, Math.max(JMainMenuGui.this.width / 2 - 43, 170)), int_, int1_);
				}

			}

			public void narrateTooltip(Consumer<Component> consumer_) {
				consumer_.accept(this.text);
			}
		};
		(this.addRenderableWidget(new JButton(this.width / 2 + 5, yIn - 15, 200, 20, new TranslatableComponent("menu.multiplayer"), (button4_) -> {
			Screen screen = this.minecraft.options.skipMultiplayerWarning ? new JoinMultiplayerScreen(this) : new SafetyScreen(this);
			this.minecraft.setScreen(screen);
		}, button$itooltip, true))).active = flag;
		(this.addRenderableWidget(new JButton(this.width / 2 + 5, yIn + 15, 200, 20, new TranslatableComponent("menu.online"), (button3_) -> {
			this.realmsButtonClicked();
		}, button$itooltip, true))).active = flag;
	}

	/**
	 * Adds Demo buttons on Main Menu for players who are playing Demo.
	 */
	private void createDemoMenuOptions(int yIn, int rowHeightIn) {
		boolean flag = this.checkDemoWorldPresence();
		this.addRenderableWidget(new Button(this.width / 2 - 100, yIn, 200, 20, new TranslatableComponent("menu.playdemo"), (button2_) -> {
			if (flag) {
				this.minecraft.loadLevel("Demo_World");
			} else {
				RegistryAccess.RegistryHolder dynamicregistries$impl = RegistryAccess.builtin();
				this.minecraft.createLevel("Demo_World", MinecraftServer.DEMO_SETTINGS, dynamicregistries$impl, WorldGenSettings.demoSettings(dynamicregistries$impl));
			}

		}));
		this.resetDemoButton = this.addRenderableWidget(new Button(this.width / 2 - 100, yIn + rowHeightIn * 1, 200, 20, new TranslatableComponent("menu.resetdemo"), (button_) -> {
			LevelStorageSource levelstoragesource = this.minecraft.getLevelSource();

			try {
				LevelStorageSource.LevelStorageAccess levelstoragesource$levelstorageaccess = levelstoragesource.createAccess("Demo_World");

				try {
					LevelSummary levelsummary = levelstoragesource$levelstorageaccess.getSummary();
					if (levelsummary != null) {
						this.minecraft.setScreen(new ConfirmScreen(this::confirmDemo, new TranslatableComponent("selectWorld.deleteQuestion"), new TranslatableComponent("selectWorld.deleteWarning", levelsummary.getLevelName()), new TranslatableComponent("selectWorld.deleteButton"), CommonComponents.GUI_CANCEL));
					}
				} catch (Throwable throwable1) {
					if (levelstoragesource$levelstorageaccess != null) {
						try {
							levelstoragesource$levelstorageaccess.close();
						} catch (Throwable throwable) {
							throwable1.addSuppressed(throwable);
						}
					}

					throw throwable1;
				}

				if (levelstoragesource$levelstorageaccess != null) {
					levelstoragesource$levelstorageaccess.close();
				}
			} catch (IOException ioexception) {
				SystemToast.onWorldAccessFailure(this.minecraft, "Demo_World");
				LOGGER.warn("Failed to access demo world", ioexception);
			}

		}));
		this.resetDemoButton.active = flag;
	}

	private boolean checkDemoWorldPresence() {
		try {
			LevelStorageSource.LevelStorageAccess levelstoragesource$levelstorageaccess = this.minecraft.getLevelSource().createAccess("Demo_World");

			boolean flag;
			try {
				flag = levelstoragesource$levelstorageaccess.getSummary() != null;
			} catch (Throwable throwable1) {
				if (levelstoragesource$levelstorageaccess != null) {
					try {
						levelstoragesource$levelstorageaccess.close();
					} catch (Throwable throwable) {
						throwable1.addSuppressed(throwable);
					}
				}

				throw throwable1;
			}

			if (levelstoragesource$levelstorageaccess != null) {
				levelstoragesource$levelstorageaccess.close();
			}

			return flag;
		} catch (IOException ioexception) {
			SystemToast.onWorldAccessFailure(this.minecraft, "Demo_World");
			LOGGER.warn("Failed to read demo world data", ioexception);
			return false;
		}
	}

	private void realmsButtonClicked() {
		this.minecraft.setScreen(new RealmsMainScreen(this));
	}

	@Override
	public void render(@NotNull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		if (this.fadeInStart == 0L && this.fading) {
			this.fadeInStart = Util.getMillis();
		}

		float f = this.fading ? (float) (Util.getMillis() - this.fadeInStart) / 1000.0F : 1.0F;
		//fill(matrixStack, 0, 0, this.width, this.height, -1);
		CUBE_MAP.renderSkybox(mouseX, mouseY, partialTicks);

		this.fillGradient(matrixStack, 0, 0, this.width, this.height, -2130706433, 16777215);
		this.fillGradient(matrixStack, 0, 0, this.width, this.height, 0, Integer.MIN_VALUE);

		int j = this.width / 2 - 137;
		assert this.minecraft != null;
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, PANORAMA_OVERLAY);
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.fading ? (float) Mth.ceil(Mth.clamp(f, 0.0F, 1.0F)) : 1.0F);
		blit(matrixStack, 0, 0, this.width, this.height, 0.0F, 0.0F, 16, 128, 16, 128);
		float f1 = this.fading ? Mth.clamp(f - 1.0F, 0.0F, 1.0F) : 1.0F;
		int l = Mth.ceil(f1 * 255.0F) << 24;
		if ((l & -67108864) != 0) {

			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderTexture(0, MINECRAFT_LOGO);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, f1);
			blit(matrixStack, j + 23, 36, 0.0F, 0.0F, 227, 55, 227, 55);

			net.minecraftforge.client.ForgeHooksClient.renderMainMenu(this, matrixStack, this.font, this.width, this.height, l);
			if (this.splash != null) {
				matrixStack.pushPose();
				matrixStack.translate((float) (this.width / 2 + 80), 45.0F, 0.0F);
				matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-20.0F));
				float f2 = 1.8F - Mth.abs(Mth.sin((float) (Util.getMillis() % 1000L) / 1000.0F * ((float) Math.PI * 2F)) * 0.1F);
				f2 = f2 * 100.0F / (float) (this.font.width(this.splash) + 32);
				matrixStack.scale(f2, f2, f2);
				drawCenteredString(matrixStack, this.font, this.splash, 0, -8, 16776960 | l);
				matrixStack.popPose();
			}

			String s = "Minecraft " + SharedConstants.getCurrentVersion().getName();
			if (this.minecraft.isDemo()) {
				s = s + " Demo";
			} else {
				s = s + ("release".equalsIgnoreCase(this.minecraft.getVersionType()) ? "" : "/" + this.minecraft.getVersionType());
			}

			if (Minecraft.checkModStatus().shouldReportAsModified()) {
				s = s + I18n.get("menu.modded");
			}

			net.minecraftforge.internal.BrandingControl.forEachLine(true, true, (brdline, brd) ->
					drawString(matrixStack, this.font, brd, 2, this.height - (10 + brdline * (this.font.lineHeight + 1)), 16777215 | l)
			);

			net.minecraftforge.internal.BrandingControl.forEachAboveCopyrightLine((brdline, brd) ->
					drawString(matrixStack, this.font, brd, this.width - font.width(brd), this.height - (10 + (brdline + 1) * (this.font.lineHeight + 1)), 16777215 | l)
			);

			drawString(matrixStack, this.font, "Copyright Mojang AB. Do not distribute!", this.copyrightX, this.height - 10, 16777215 | l);
			if (mouseX > this.copyrightX && mouseX < this.copyrightX + this.copyrightWidth && mouseY > this.height - 10 && mouseY < this.height) {
				fill(matrixStack, this.copyrightX, this.height - 1, this.copyrightX + this.copyrightWidth, this.height, 16777215 | l);
			}

			for(GuiEventListener guieventlistener : this.children()) {
				if (guieventlistener instanceof AbstractWidget) {
					((AbstractWidget)guieventlistener).setAlpha(f1);
				}
			}

			//FIXME
			/*int buttonSize;
			for (buttonSize = 0; buttonSize < this.buttons.size(); ++buttonSize) {
				(this.buttons.get(buttonSize)).renderButton(matrixStack, mouseX, mouseY, partialTicks);
			}

			for (buttonSize = 0; buttonSize < this.buttons.size(); ++buttonSize) {
				(this.buttons.get(buttonSize)).renderToolTip(matrixStack, mouseX, mouseY);
			}*/

			//super.render(matrixStack, mouseX, mouseY, partialTicks);

			for (Widget widget : this.renderables) {
				widget.render(matrixStack, mouseX, mouseY, partialTicks);
			}

			if (this.realmsNotificationsEnabled() && f1 >= 1.0F) {
				this.realmsNotificationsScreen.render(matrixStack, mouseX, mouseY, partialTicks);
			}
			modUpdateNotification.render(matrixStack, mouseX, mouseY, partialTicks);
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int buttonIn) {
		if (super.mouseClicked(mouseX, mouseY, buttonIn)) {
			return true;
		} else if (this.realmsNotificationsEnabled() && this.realmsNotificationsScreen.mouseClicked(mouseX, mouseY, buttonIn)) {
			return true;
		} else {
			if (mouseX > (double) this.copyrightX && mouseX < (double) (this.copyrightX + this.copyrightWidth) && mouseY > (double) (this.height - 10) && mouseY < (double) this.height) {
				this.minecraft.setScreen(new WinScreen(false, Runnables.doNothing()));
			}

			return false;
		}
	}

	@Override
	public void removed() {
		if (this.realmsNotificationsScreen != null) {
			this.realmsNotificationsScreen.removed();
		}
	}

	private void confirmDemo(boolean delete_) {
		if (delete_) {
			try {
				LevelStorageSource.LevelStorageAccess levelstoragesource$levelstorageaccess = this.minecraft.getLevelSource().createAccess("Demo_World");

				try {
					levelstoragesource$levelstorageaccess.deleteLevel();
				} catch (Throwable throwable1) {
					if (levelstoragesource$levelstorageaccess != null) {
						try {
							levelstoragesource$levelstorageaccess.close();
						} catch (Throwable throwable) {
							throwable1.addSuppressed(throwable);
						}
					}

					throw throwable1;
				}

				if (levelstoragesource$levelstorageaccess != null) {
					levelstoragesource$levelstorageaccess.close();
				}
			} catch (IOException ioexception) {
				SystemToast.onWorldDeleteFailure(this.minecraft, "Demo_World");
				LOGGER.warn("Failed to delete demo world", ioexception);
			}
		}

		this.minecraft.setScreen(this);
	}

	private void blurPanorama(Minecraft mc) {
		RenderSystem.viewport(0, 0, mc.getWindow().getWidth(), mc.getWindow().getHeight());
		float f = 120.0F / (float) (Math.max(this.width, this.height));
		float f1 = (float) this.height * f / 256.0F;
		float f2 = (float) this.width * f / 256.0F;
		int i = this.width;
		int j = this.height;
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuilder();
		bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
		assert mc.screen != null;
		bufferbuilder.vertex(0.0D, j, mc.screen.getBlitOffset()).uv(0.5F - f1, 0.5F + f2)
				.color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		bufferbuilder.vertex(i, j, mc.screen.getBlitOffset()).uv(0.5F - f1, 0.5F - f2)
				.color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		bufferbuilder.vertex(i, 0.0D, mc.screen.getBlitOffset()).uv(0.5F + f1, 0.5F - f2)
				.color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		bufferbuilder.vertex(0.0D, 0.0D, mc.screen.getBlitOffset()).uv(0.5F + f1, 0.5F + f2)
				.color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
	}
}
