package com.axialeaa.doormat.entity;

import com.axialeaa.doormat.MainEntrypoint;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class DoormatDamageTypes {
    public static final RegistryKey<DamageType> ROSE_BUSH = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(MainEntrypoint.MODID, "rose_bush"));
}