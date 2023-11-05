package com.axialeaa.doormat.mixin.rules.zoglinsSpawnInPortals;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetherPortalBlock.class)
public class NetherPortalBlockMixin {

    @Inject(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;", ordinal = 0), cancellable = true)
    private void spawnZoglins(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (DoormatSettings.zoglinsSpawnInPortals && random.nextFloat() < 0.1 && world.getBlockState(pos).allowsSpawning(world, pos, EntityType.ZOGLIN)) {
            // if the rules is enabled, a random number between 0 and 1 is less than 0.1 (1 in 10 chance) and the block at the current pos allows for zoglin spawning...
            ci.cancel(); // cancel zombified piglin spawning
            Entity entity = EntityType.ZOGLIN.spawn(world, pos.up(), SpawnReason.STRUCTURE); // spawn a zoglin
            if (entity != null)
                entity.resetPortalCooldown(); // and reset the entity's portal cooldown if the entity exists
        }
    }

}
