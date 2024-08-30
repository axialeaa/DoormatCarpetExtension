package com.axialeaa.doormat.mixin.rule.itemMagnet;

import com.axialeaa.doormat.setting.DoormatSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends Entity {

    public PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void attractItemsTowardPlayer(CallbackInfo ci) {
        World world = this.getWorld();

        if (DoormatSettings.itemMagnetRange == 0 || world.isClient() || this.noClip)
            return;

        Box box = this.getBoundingBox().expand(DoormatSettings.itemMagnetRange);

        List<ItemEntity> itemEntities = world.getEntitiesByClass(ItemEntity.class, box, itemEntity -> {
            RaycastContext ctx = new RaycastContext(itemEntity.getEyePos(), this.getEyePos(), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this);
            BlockHitResult hitResult = world.raycast(ctx);

            return hitResult.getType() != HitResult.Type.BLOCK && !itemEntity.cannotPickup();
        });

        for (ItemEntity itemEntity : itemEntities) {
            Vec3d vec3d = this.getPos().subtract(itemEntity.getPos());
            itemEntity.addVelocity(vec3d.multiply(DoormatSettings.itemMagnetVelocity / itemEntity.squaredDistanceTo(this)));
        }
    }

}
