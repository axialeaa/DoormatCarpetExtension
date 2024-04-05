package com.axialeaa.doormat.mixin.rule.jukeboxDiscProgressSignal;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.inventory.SingleStackInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JukeboxBlockEntity.class)
public abstract class JukeboxBlockEntityMixin implements SingleStackInventory {

    @Shadow public abstract ItemStack getStack();
    @Shadow protected abstract boolean isSongFinished(MusicDiscItem disc);

    /**
     * Updates adjacent comparators every tick for as long as the rule is enabled and the jukebox is playing a disc.
     * @implNote Unfortunately this is necessary because the lerp function in {@link JukeboxBlockMixin} updates out-of-phase from the internal seconds counter, and vanilla only updates comparators on disc entry and exit.
     */
    @Inject(method = "tick(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/JukeboxBlockEntity;isPlayingRecord()Z"))
    private void updateComparatorsEachTick(World world, BlockPos pos, BlockState state, CallbackInfo ci) {
        Item item = this.getStack().getItem();
        boolean finishedPlaying = item instanceof MusicDiscItem disc && isSongFinished(disc);
        if (DoormatSettings.jukeboxDiscProgressSignal && !finishedPlaying)
            world.updateComparators(pos, state.getBlock());
    }

}
