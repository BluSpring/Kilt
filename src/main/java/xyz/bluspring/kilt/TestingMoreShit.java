package xyz.bluspring.kilt;

import com.google.common.collect.Lists;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import xyz.bluspring.kilt.injections.client.render.block.model.ItemTransformInjection;
import xyz.bluspring.kilt.injections.world.item.enchantment.EnchantmentCategoryInjection;
import xyz.bluspring.kilt.injections.world.level.block.PoweredRailBlockInjection;
import xyz.bluspring.kilt.mixin.EnchantmentCategoryAccessor;

import java.util.List;
import java.util.function.Supplier;

// I use this class to just throw stuff into and
// see how the bytecode looks when it's built.
// I know ASM-ifier exists, but this is a better
// learning experience for me, and is definitely
// not because the ASM-ifier doesn't actually work
// for me.
public class TestingMoreShit {
    private static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "fuck");

    private static DeferredRegister<Block> BLOCKS2;

    private ForgeRegistry<Block> block;

    public TestingMoreShit(ForgeRegistry<Block> registry) {
        block = registry;
    }

    public boolean moreRandomShit(Item item) {
        var quack = new Item(new Item.Properties().durability(0));
        //return ((EnchantmentCategoryInjection) (Object) this).getDelegate().test(item);
        return quack.equals("a");
    }

    static {
        try {
            BLOCKS2 = DeferredRegister.create((ForgeRegistry<Block>) Class.forName("net.minecraftforge.registries.ForgeRegistries").getDeclaredField("BLOCKS").get(null), "fuck");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static class TestingDoubleTime extends TestingMoreShit {
        public TestingDoubleTime() {
            super(ForgeRegistries.BLOCKS);
        }
    }

    public static class TestingTripleTime extends Block {
        private final FlowingFluid bruh;
        private final List<?> test;
        private final Supplier<? extends FlowingFluid> supplier;

        public TestingTripleTime(Supplier<? extends FlowingFluid> fluid, BlockBehaviour.Properties properties) {
            super(properties);

            this.bruh = null;
            this.test = Lists.newArrayList();
            this.registerDefaultState(this.stateDefinition.any().setValue(FlowingFluid.LEVEL, 0));
            this.supplier = fluid;
        }
    }

    public static abstract class TestingQuadrupleTime extends ItemTransform {
        public TestingQuadrupleTime(Vector3f vector3f, Vector3f vector3f2, Vector3f vector3f3, Vector3f rightRotation) {
            super(vector3f, vector3f2, vector3f3);
            ((ItemTransformInjection) this).setRightRotation(rightRotation);
        }
    }

    public enum MoreTestingBullshit {
        WELL, THEN;

        public static TestingDoubleTime quack() {
            return new TestingDoubleTime();
        }

        public boolean wow(Item item) {
            return ((EnchantmentCategoryInjection) (Object) this).getDelegate() != null && ((EnchantmentCategoryInjection) (Object) this).getDelegate().test(item);
        }
    }
}
