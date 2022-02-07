package net.jitl.core.init.world;

import net.jitl.common.world.gen.treedecorator.*;
import net.jitl.core.JITL;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.timeconqueror.timecore.api.registry.SimpleForgeRegister;
import ru.timeconqueror.timecore.api.registry.util.AutoRegistrable;

public class JTreeDecorators {
    @AutoRegistrable
    private static final SimpleForgeRegister<TreeDecoratorType<?>> REGISTER = new SimpleForgeRegister<>(ForgeRegistries.TREE_DECORATOR_TYPES, JITL.MODID);

    public static final RegistryObject<TreeDecoratorType<FrozenTreeDecorator>> FROZEN_DECORATOR =
            REGISTER.register("frozen_tree_decorator", () -> new TreeDecoratorType<>(FrozenTreeDecorator.CODEC));

    public static final RegistryObject<TreeDecoratorType<CrystalFruitTreeDecorator>> CRYSTAL_FRUIT_DECORATOR = REGISTER.register("crystal_fruit_tree_decorator", () -> new TreeDecoratorType<>(CrystalFruitTreeDecorator.CODEC));

    public static final RegistryObject<TreeDecoratorType<GlimmerRootTreeDecorator>> GLIMMER_ROOT_DECORATOR = REGISTER.register("glimmer_root_tree_decorator", () -> new TreeDecoratorType<>(GlimmerRootTreeDecorator.CODEC));

    public static final RegistryObject<TreeDecoratorType<IceShroomTreeDecorator>> ICE_SHROOM_TREE_DECORATOR = REGISTER.register("ice_shroom_tree_decorator", () -> new TreeDecoratorType<>(IceShroomTreeDecorator.CODEC));

    public static final RegistryObject<TreeDecoratorType<IcyBrushTreeDecorator>> ICY_BRUSH_TREE_DECORATOR = REGISTER.register("icy_brush_tree_decorator", () -> new TreeDecoratorType<>(IcyBrushTreeDecorator.CODEC));

    public static final RegistryObject<TreeDecoratorType<CharredBrushTreeDecorator>> CHARRED_BRUSH_TREE_DECORATOR = REGISTER.register("charred_brush_tree_decorator", () -> new TreeDecoratorType<>(CharredBrushTreeDecorator.CODEC));
}
