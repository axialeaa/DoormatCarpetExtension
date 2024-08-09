package com.axialeaa.doormat.fake;

import com.axialeaa.doormat.mixin.tinker_kit.CommandBlockExecutorMixin;
import com.axialeaa.doormat.mixin.tinker_kit.CommandBlockMixin;
import net.minecraft.world.World;

/**
 * Implements a single method used for bypassing the {@code lastExecution} check in {@link net.minecraft.world.CommandBlockExecutor#execute(World)}.
 * @see CommandBlockMixin
 * @see CommandBlockExecutorMixin
 */
public interface CommandBlockWorldTimeChecker {

    void setShouldCheckTime(boolean shouldCheckTime);

}
