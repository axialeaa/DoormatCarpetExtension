package com.axialeaa.doormat.mixin.rules.deepslateDungeons;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.DungeonFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DungeonFeature.class)
public class DungeonFeatureMixin {

    /**
     * @param state the blockstate looked for across all invocations of setBlockStateIf()
     * @param pos local capture of the variable that acts as context.getOrigin()
     * @return cobbled deepslate if the rule is enabled, the structure was generated below y = 0 and the block in question is cobblestone or mossy cobblestone. Otherwise, the block in question.
     */
    @ModifyArg(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/DungeonFeature;setBlockStateIf(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Ljava/util/function/Predicate;)V"))
    private BlockState replaceWithDeepslate(BlockState state, @Local(ordinal = 0) BlockPos pos) {
        if (pos.getY() > 0 || !DoormatSettings.deepslateDungeons)
            return state;
        return state.isOf(Blocks.COBBLESTONE) || state.isOf(Blocks.MOSSY_COBBLESTONE) ?
            Blocks.COBBLED_DEEPSLATE.getDefaultState() : state;
    }

}