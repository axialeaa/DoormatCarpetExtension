package com.axialeaa.doormat.mixin.rule.barrelItemDumping;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.*;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BarrelBlock.class)
public abstract class BarrelBlockMixin extends BlockWithEntity implements BlockEntityProvider {

    protected BarrelBlockMixin(Settings settings) {
        super(settings);
    }

    /**
     * Iterates through all items in the barrel's inventory, ejecting them into the world instantly.
     */
    @Unique
    private static void dumpItems(World world, BlockPos pos, BlockState state, BarrelBlockEntity blockEntity) {
        Direction down = Direction.DOWN;

        if (state.get(BarrelBlock.FACING) != down || Block.hasTopRim(world, pos.down()))
            return;

        for (ItemStack stack : ((BarrelBlockEntityAccessor) blockEntity).getInventory()) {
            if (stack.isEmpty())
                return;

            stack = stack.split(stack.getCount());
            ItemDispenserBehavior.spawnItem(world, stack, 0, down, Vec3d.ofCenter(pos).offset(down, 0.7));
        }
    }

    /**
     * Ticks the barrel every tick to dump its items when the rule is enabled and the barrel is open and facing down.
     */
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return DoormatSettings.barrelItemDumping && state.get(BarrelBlock.OPEN) && state.get(BarrelBlock.FACING) == Direction.DOWN ?
            BarrelBlock.validateTicker(type, BlockEntityType.BARREL, BarrelBlockMixin::dumpItems) :
            null;
    }

}
