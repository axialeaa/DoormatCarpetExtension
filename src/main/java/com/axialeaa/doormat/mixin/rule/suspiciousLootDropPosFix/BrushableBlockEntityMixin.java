package com.axialeaa.doormat.mixin.rule.suspiciousLootDropPosFix;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.BrushableBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BrushableBlockEntity.class)
public class BrushableBlockEntityMixin extends BlockEntity {

    public BrushableBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @WrapOperation(method = "spawnItem", at = @At(value = "NEW", target = "(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/ItemEntity;"))
    private ItemEntity modifyItemSpawnPos(World world, double x, double y, double z, ItemStack stack, Operation<ItemEntity> original, @Local Direction direction) {
        if (!DoormatSettings.suspiciousLootDropPosFix)
            return original.call(world, x, y, z, stack);

        Vec3d vec3d = this.pos.toCenterPos()
            .offset(Direction.DOWN, EntityType.ITEM.getHeight() / 2)
            .offset(direction, 0.5 + (direction.getAxis().isVertical() ? EntityType.ITEM.getHeight() : EntityType.ITEM.getWidth()) / 2);

        return original.call(world, vec3d.getX(), vec3d.getY(), vec3d.getZ(), stack);
    }

}
