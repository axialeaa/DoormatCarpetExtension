package com.axialeaa.doormat.mixin.rule.jukeboxDiscProgressSignal;

import net.minecraft.block.entity.JukeboxBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(JukeboxBlockEntity.class)
public interface JukeboxBlockEntityAccessor {

    @Accessor("tickCount")
    long getTickCount();

    @Accessor("recordStartTick")
    long getRecordStartTick();

}
