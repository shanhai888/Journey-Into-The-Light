package net.jitl.common.block.crop;

import net.jitl.common.block.base.JCropBlock;
import net.jitl.core.init.JItems;
import net.minecraft.world.item.Item;

public class FloroCropBlock extends JCropBlock {

    @Override
    public Item getSeedItem() {
        return JItems.FLORO_SEEDS;
    }

    @Override
    public int getMaxAge() {
        return 5;
    }
}
