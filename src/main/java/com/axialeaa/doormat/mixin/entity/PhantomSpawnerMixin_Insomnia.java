package com.axialeaa.doormat.mixin.entity;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.world.spawner.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin_Insomnia {

    @ModifyConstant(method = "spawn", constant = @Constant(intValue = 72000))
    private int spawn(int constant) {
        return DoormatSettings.insomniaDaysSinceSlept * 24000 /* number of ticks in a day */ ;
    }
    // time since last slept constant. vanilla is 72000 ticks/1 hour/3 days

}
