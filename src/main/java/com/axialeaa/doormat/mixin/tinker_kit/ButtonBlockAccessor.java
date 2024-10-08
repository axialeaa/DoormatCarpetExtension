package com.axialeaa.doormat.mixin.tinker_kit;

import net.minecraft.block.ButtonBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ButtonBlock.class)
public interface ButtonBlockAccessor {

    @Accessor("pressTicks")
    int getPressTicks();

}
