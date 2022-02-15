package net.jitl.common.item;

import net.jitl.common.capability.dialog.DialogManager;
import net.jitl.core.init.JDialogs;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class
DebugItem extends Item {
    public DebugItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        if (!worldIn.isClientSide()) {
            DialogManager.of(((ServerPlayer) playerIn)).startDialog(JDialogs.test());
//            ItemStack scrollStack = new ItemStack(JItems.LORE_SCROLL);
//            LoreScrollItem.bindScrollEntry(scrollStack, ScrollEntries.TEST, EnumKnowledgeType.SENTERIAN, 50F);
//            playerIn.addItem(scrollStack);
//
//            if (worldIn instanceof ServerLevel serverLevel) {
//                var advancement = serverLevel.getServer().getServerResources().getAdvancements().getAdvancement(new ResourceLocation("minecraft:advancements/story/root"));
//                if (playerIn instanceof ServerPlayer serverPlayer) {
//                    if (advancement != null) {
//                        boolean isComplete = serverPlayer.getAdvancements().getOrStartProgress(advancement).isDone();
//                        if (isComplete) {
//                            JITL.LOGGER.info("Player has advancement");
//                        }
//                    }
//                }
//            }

           /* List<ItemStack> loot = new ArrayList<>();
            loot.add(new ItemStack(JItems.LUNIUM_POWDER, 5));
            loot.add(new ItemStack(Items.DIAMOND, 5));
            loot.add(new ItemStack(JItems.BLOOD, 5));
            BossCrystalEntity.create(worldIn, playerIn.position(), BossCrystalEntity.Type.CORBA, loot);
            return InteractionResultHolder.success(playerIn.getItemInHand(handIn));*/
        }
        return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
    }


    public boolean isInMainHand(Player player) {
        return player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == this;
    }
}
