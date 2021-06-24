package net.jitl.common.item.gear.lunium;

import net.jitl.common.helper.JToolTiers;
import net.jitl.common.item.gear.ILightGear;
import net.jitl.common.item.gear.JTieredItemAbility;
import net.jitl.common.item.gear.base.JHoeItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LuniumHoeItem extends JHoeItem implements ILightGear, JTieredItemAbility {
    public LuniumHoeItem(JToolTiers tier) {
        super(tier);
    }

    @Override
    public void tick(LivingEntity entity, World world, ItemStack stack) {
        if (!world.isClientSide()) repair(entity, stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!worldIn.isClientSide()) {
            LivingEntity entity = (LivingEntity) entityIn;
            if (entity.getMainHandItem() != stack && entity.getOffhandItem() != stack) repair(entityIn, stack);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        lightTooltip(tooltip);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return animateIgnoreNBT(oldStack, newStack);
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        return animateIgnoreNBT(oldStack, newStack);
    }
}
