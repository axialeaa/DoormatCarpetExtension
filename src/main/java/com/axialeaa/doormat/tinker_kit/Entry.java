package com.axialeaa.doormat.tinker_kit;

import com.axialeaa.doormat.util.UpdateType;
import net.minecraft.world.tick.TickPriority;
import org.jetbrains.annotations.Nullable;

public record Entry(@Nullable Integer quasiConnectivity, @Nullable Integer delay, @Nullable UpdateType updateType, @Nullable TickPriority tickPriority) { }
