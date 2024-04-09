package com.axialeaa.doormat.mixin.rule.thornyRoseBush;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DamageSources.class)
public interface DamageSourcesAccessor {

    @Invoker("create")
    DamageSource invokeCreate(RegistryKey<DamageType> key);

}
