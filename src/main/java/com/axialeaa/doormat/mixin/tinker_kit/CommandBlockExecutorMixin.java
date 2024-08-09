package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.fake.CommandBlockWorldTimeChecker;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.CommandBlockExecutor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CommandBlockExecutor.class)
public class CommandBlockExecutorMixin implements CommandBlockWorldTimeChecker {

    @Unique private boolean shouldCheckTime = true;

    @Override
    public void setShouldCheckTime(boolean shouldCheckTime) {
        this.shouldCheckTime = shouldCheckTime;
    }

    @ModifyExpressionValue(method = "execute", at = @At(value = "FIELD", target = "Lnet/minecraft/world/CommandBlockExecutor;lastExecution:J", ordinal = 0))
    private long bypassLastExecution(long lastExecution) {
        if (!this.shouldCheckTime)
            lastExecution = -1;

        return lastExecution;
    }

}
