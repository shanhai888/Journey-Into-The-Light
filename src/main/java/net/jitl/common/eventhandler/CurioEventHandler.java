package net.jitl.common.eventhandler;

import net.jitl.JITL;
import net.jitl.init.JItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

@Mod.EventBusSubscriber(modid = JITL.MODID)
public class CurioEventHandler {

    @SubscribeEvent
    public static void onPlayerAttacked(LivingHurtEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (!entity.level.isClientSide()) {
            if (entity instanceof PlayerEntity) {
                if (CuriosApi.getCuriosHelper().findEquippedCurio(JItems.SKULL_OF_DECAY, entity).isPresent()) {
                    if (event.getSource().getDirectEntity() instanceof LivingEntity) {
                        LivingEntity attacker = (LivingEntity) event.getSource().getDirectEntity();
                        attacker.addEffect(new EffectInstance(Effects.WITHER, 100, 1));
                    }
                }
            }
        }
    }

    public static void onKeyPressed(PlayerEntity player) {

    }
}
