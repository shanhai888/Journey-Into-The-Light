package net.journey.eventhandler;

import baubles.api.BaublesApi;
import net.journey.JITL;
import net.journey.init.items.JourneyItems;
import net.journey.util.PotionEffects;
import net.journey.util.RandHelper;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

@Mod.EventBusSubscriber(modid = JITL.MOD_ID)
public class BaublesEventHandler {

    @SubscribeEvent
    public static void onPlayerAttacked(LivingHurtEvent event) {
        if (!event.getEntity().world.isRemote) {
            if (event.getEntity() instanceof EntityPlayer) {
                if (BaublesApi.isBaubleEquipped((EntityPlayer) event.getEntity(), JourneyItems.skullOfDecay) != -1) {
                    if (event.getSource().getTrueSource() instanceof EntityLiving) {
                        EntityLiving entity = (EntityLiving) event.getSource().getTrueSource();
                        entity.addPotionEffect(PotionEffects.setPotionEffect(PotionEffects.wither, 400, 1));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBlockHarvested(BlockEvent.HarvestDropsEvent event) {
        EntityPlayer player = event.getHarvester();
        IBlockState state = event.getState();
        List<ItemStack> drops = event.getDrops();

        if (player != null && !player.world.isRemote) {
            if (BaublesApi.isBaubleEquipped(player, JourneyItems.luckyCharm) != -1) {
                if (RandHelper.RANDOM.nextInt(2) == 0) {
                    if (state.getBlock() instanceof IGrowable) {
                        drops.add(new ItemStack(state.getBlock().getItemDropped(state, RandHelper.RANDOM, 2), 1));
                    }
                }
            }
        }
    }

    /* @SubscribeEvent
    public static void onPotionEffect(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;

        if (player != null && !player.world.isRemote) {
            if (BaublesApi.isBaubleEquipped(player, JourneyItems.slownessRing) != -1) {
                Potion potion = MobEffects.SLOWNESS;
                if (player.isPotionActive(potion)) {
                    player.removePotionEffect(potion);
                }
            }
        }
    } */
}
