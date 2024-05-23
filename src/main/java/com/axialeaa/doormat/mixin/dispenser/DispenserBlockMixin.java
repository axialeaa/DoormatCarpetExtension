package com.axialeaa.doormat.mixin.dispenser;

import com.axialeaa.doormat.dispenser.DoormatDispenserBehaviours;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DispenserBlock.class)
public class DispenserBlockMixin {

    @WrapOperation(method = "dispense", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/DispenserBlock;getBehaviorForItem(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/block/dispenser/DispenserBehavior;"))
    private DispenserBehavior dispenseCustomBehaviour(DispenserBlock instance, World world, ItemStack stack, Operation<DispenserBehavior> original, @Local(argsOnly = true) BlockPos pos, @Local BlockPointer blockPointer) {
        DispenserBehavior behaviour = DoormatDispenserBehaviours.getCustom((ServerWorld) world, pos, blockPointer);

        return behaviour == null ? original.call(instance, world, stack) : behaviour;
    }

}
