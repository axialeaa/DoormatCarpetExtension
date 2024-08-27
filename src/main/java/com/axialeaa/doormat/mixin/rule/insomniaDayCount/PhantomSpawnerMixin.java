package com.axialeaa.doormat.mixin.rule.insomniaDayCount;

import com.axialeaa.doormat.settings.DoormatSettings;
import net.minecraft.world.spawner.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {

    @ModifyConstant(method = "spawn", constant = @Constant(intValue = 72000))
    private int modifySpawnTime(int original) {
        return DoormatSettings.insomniaDayCount * 24000; // number of ticks in a day
    }

}
