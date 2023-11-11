package com.axialeaa.doormat.entity;

import com.axialeaa.doormat.DoormatServer;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

/**
 * Stores all custom damage types in Doormat (currently only rose_bush).
 */
public class DoormatDamageTypes {

    public static final RegistryKey<DamageType> ROSE_BUSH = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(DoormatServer.MODID, "rose_bush"));

}