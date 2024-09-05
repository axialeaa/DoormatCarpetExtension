package com.axialeaa.doormat.util;

import com.axialeaa.doormat.mixin.tinker_kit.CommandBlockExecutorMixin;
import com.axialeaa.doormat.mixin.tinker_kit.CommandBlockMixin;
import net.minecraft.world.World;

/**
 * Implements a single method used for bypassing the {@code lastExecution} check in {@link net.minecraft.world.CommandBlockExecutor#execute(World)}.
 * @see CommandBlockMixin
 * @see CommandBlockExecutorMixin
 */
public interface CommandBlockTimeChecker {

    void set(boolean shouldCheckTime);

}
