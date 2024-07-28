package com.axialeaa.doormat.mixin.command;

import com.axialeaa.doormat.command.RandomTickCommand;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {

    @Shadow public abstract ServerWorld toServerWorld();

    @Inject(method = "tickChunk", at = @At(value = "CONSTANT", args = "stringValue=tickBlocks", shift = At.Shift.AFTER))
    private void onExecuteRandomTickCommand(CallbackInfo ci) {
        RandomTickCommand.sendRandomTicks(this.toServerWorld());
    }

}
