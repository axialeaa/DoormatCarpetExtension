package com.axialeaa.doormat.mixin.rules.peacefulMonsterSpawning.block;

import com.axialeaa.doormat.helpers.PeacefulSpawningHelper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.WitherSkullBlock;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WitherSkullBlock.class)
public class WitherSkullBlockMixin {

    /**
     * Bypasses the peaceful check for building withers with soul sand and skulls.
     */
    @ModifyExpressionValue(method = {"onPlaced(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/SkullBlockEntity;)V", "canDispense"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getDifficulty()Lnet/minecraft/world/Difficulty;"))
    private static Difficulty bypassPeacefulDespawnCheck(Difficulty original) {
        return PeacefulSpawningHelper.bypassCheck(original);
    }

}
