package xyz.bluspring.kilt.forgeinjects.client.color.block;

import net.minecraft.client.color.block.BlockColors;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.fml.ModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockColors.class)
public class BlockColorsInject {
    @Inject(at = @At("RETURN"), method = "createDefault")
    private static void kilt$initForgeBlockColors(CallbackInfoReturnable<BlockColors> cir) {
        // ForgeHooksClient.onBlockColorsInit
        ModLoader.get().postEvent(new RegisterColorHandlersEvent.Block(cir.getReturnValue()));
    }
}
