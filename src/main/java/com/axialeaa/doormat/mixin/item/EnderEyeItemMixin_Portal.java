package com.axialeaa.doormat.mixin.item;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnderEyeItem.class)
public class EnderEyeItemMixin_Portal {

    @Redirect(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/pattern/BlockPattern;searchAround(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/pattern/BlockPattern$Result;"))
    private BlockPattern.Result test(BlockPattern pattern, WorldView world, BlockPos pos) {
        if (DoormatSettings.disableEndPortals)
            return null;
        else
            return EndPortalFrameBlock.getCompletedFramePattern().searchAround(world, pos);
    }

}
