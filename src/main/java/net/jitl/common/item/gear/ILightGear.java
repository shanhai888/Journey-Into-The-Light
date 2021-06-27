package net.jitl.common.item.gear;

import net.jitl.common.helper.TooltipFiller;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import java.util.List;

public interface ILightGear {
    default void repair(Entity entity, ItemStack stack) {
        CompoundNBT tag = stack.hasTag() ? stack.getTag() : new CompoundNBT();
        float value = tag.getFloat("cooldown");
        if (stack.getDamageValue() > 0 || value < 100) {
            if (value == 0) {
                tag.putFloat("cooldown", 100);
                stack.setDamageValue(stack.getDamageValue() - 1);
            } else {
                tag.putFloat("cooldown", Math.max(value - entity.getBrightness(), 0));
            }
            System.out.println(tag.getFloat("cooldown"));
            stack.setTag(tag);
        }
    }

    default void lightTooltip(List<ITextComponent> tooltip) {
        TooltipFiller filler = new TooltipFiller(tooltip, "lunium_gear");
        filler.addOverview();
        filler.addDrawback();
    }

    default double scaleWithDarkness(Entity entity, double original) {
        return original * (1 - entity.getBrightness());
    }

    default boolean animateIgnoreNBT(ItemStack oldStack, ItemStack newStack) {
        if (oldStack.equals(newStack)) return false;
        if (oldStack.sameItem(newStack)) {
            int durability = newStack.getDamageValue() - oldStack.getDamageValue();
            return durability != 0 && durability != 1; //for repair
        }
        return true;
    }
}