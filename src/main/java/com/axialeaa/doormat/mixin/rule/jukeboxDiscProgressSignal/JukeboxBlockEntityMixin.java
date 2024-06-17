package com.axialeaa.doormat.mixin.rule.jukeboxDiscProgressSignal;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helper.JukeboxSignalHelper;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JukeboxBlockEntity.class)
public abstract class JukeboxBlockEntityMixin extends BlockEntity {

    @Shadow public abstract BlockEntity asBlockEntity();
    @Shadow public abstract ItemStack getStack();

    public JukeboxBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    /**
     * @return for as long as the rule is enabled, an integer between 1 and 15 based on the fraction of the disc
     * played, otherwise the disc index as normal.
     * @implNote Note from happy Axia: "this is so FREAKING smart i'm so happy eeeheeehee"
     */
    @ModifyReturnValue(method = "getComparatorOutput", at = @At("RETURN"))
    private int modifyComparatorOutput(int original) {
        int output = JukeboxSignalHelper.getOutput((JukeboxBlockEntity) this.asBlockEntity());

        return DoormatSettings.jukeboxDiscProgressSignal ? output : original;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private static void updateComparatorsOnPlayTick(World world, BlockPos pos, BlockState state, JukeboxBlockEntity blockEntity, CallbackInfo ci) {
        if (DoormatSettings.jukeboxDiscProgressSignal && blockEntity.getManager().isPlaying())
            world.updateComparators(pos, state.getBlock());
    }

    @Inject(method = "onRecordStackChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;emitGameEvent(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/event/GameEvent$Emitter;)V", shift = At.Shift.AFTER))
    private void updateComparatorsOnStackChanged(boolean hasRecord, CallbackInfo ci) {
        if (DoormatSettings.jukeboxDiscProgressSignal && this.getWorld() != null && ((JukeboxBlockEntity) this.asBlockEntity()).getManager().isPlaying())
            this.getWorld().updateComparators(this.getPos(), this.getCachedState().getBlock());
    }

}