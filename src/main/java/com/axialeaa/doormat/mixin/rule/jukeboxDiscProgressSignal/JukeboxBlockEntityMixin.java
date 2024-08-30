package com.axialeaa.doormat.mixin.rule.jukeboxDiscProgressSignal;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.axialeaa.doormat.helper.JukeboxSignalHelper;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.block.jukebox.JukeboxManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JukeboxBlockEntity.class)
public abstract class JukeboxBlockEntityMixin extends BlockEntity {

    @Shadow public abstract BlockEntity asBlockEntity();
    @Shadow public abstract ItemStack getStack();

    @Unique private final JukeboxBlockEntity jukeboxBlockEntity = (JukeboxBlockEntity) this.asBlockEntity();

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
        int output = JukeboxSignalHelper.getOutput(jukeboxBlockEntity);
        return DoormatSettings.jukeboxDiscProgressSignal ? output : original;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private static void updateComparatorsOnPlayTick(World world, BlockPos pos, BlockState state, JukeboxBlockEntity blockEntity, CallbackInfo ci) {
        JukeboxManager manager = blockEntity.getManager();

        if (DoormatSettings.jukeboxDiscProgressSignal && manager.isPlaying()) {
            Block block = state.getBlock();
            world.updateComparators(pos, block);
        }
    }

    @Inject(method = "onRecordStackChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;emitGameEvent(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/event/GameEvent$Emitter;)V", shift = At.Shift.AFTER))
    private void updateComparatorsOnStackChanged(boolean hasRecord, CallbackInfo ci) {
        JukeboxManager manager = jukeboxBlockEntity.getManager();
        World world = this.getWorld();

        if (DoormatSettings.jukeboxDiscProgressSignal && world != null && manager.isPlaying()) {
            BlockPos blockPos = this.getPos();
            BlockState blockState = this.getCachedState();

            world.updateComparators(blockPos, blockState.getBlock());
        }
    }

}