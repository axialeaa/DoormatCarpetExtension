package com.axialeaa.doormat.mixin.entity;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.spawner.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin_Insomnia {

    @ModifyConstant(method = "spawn", constant = @Constant(intValue = 72000))
    private int spawn(int constant) {
        return DoormatSettings.insomniaDaysSinceSlept * 24000; // number of ticks in a day
    } // time since last slept constant. vanilla is 72000 ticks/1 hour/3 days

    @Redirect(method = "spawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;getSeaLevel()I"))
    public int test(ServerWorld world) {
        return DoormatSettings.phantomMinSpawnAltitude;
    }

}
