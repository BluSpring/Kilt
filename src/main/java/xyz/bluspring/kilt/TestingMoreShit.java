package xyz.bluspring.kilt;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

public class TestingMoreShit {
    private static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "fuck");

    private static DeferredRegister<Block> BLOCKS2;

    private ForgeRegistry<Block> block;

    public TestingMoreShit(ForgeRegistry<Block> registry) {
        block = registry;
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

    public static class TestingTripleTime extends TestingMoreShit {
        public TestingTripleTime() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
            super((ForgeRegistry<Block>) Class.forName("net.minecraftforge.registries.ForgeRegistries").getDeclaredField("BLOCKS").get(null));
        }
    }
}