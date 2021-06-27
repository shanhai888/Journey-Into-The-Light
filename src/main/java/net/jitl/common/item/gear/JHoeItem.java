package net.jitl.common.item.gear;

import net.jitl.common.helper.JToolTiers;
import net.jitl.init.JTabs;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class JHoeItem extends HoeItem implements JGear {
	IAbility ability;

	public JHoeItem(JToolTiers tier, IAbility hoeAbility) {
		super(tier, 1, tier.getAttackSpeed(), new Item.Properties().tab(JTabs.TOOLS));
		ability = hoeAbility;
	}

	@Override
	public IAbility getAbility() {
		return ability;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		ability.fillTooltips(stack, tooltip);
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return ability.animate(super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged), oldStack, newStack, slotChanged);
	}

	@Override
	public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
		return ability.resetBreak(super.shouldCauseBlockBreakReset(oldStack, newStack), oldStack, newStack);
	}
}