package com.axialeaa.doormat.helper;

import com.axialeaa.doormat.settings.DoormatSettings;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BarrelItemDumpingHelper {

    public static void dumpItemStacks(World world, BlockPos pos, BlockState state, BarrelBlockEntity blockEntity) {
        if (!DoormatSettings.barrelItemDumping || Block.hasTopRim(world, pos.down()) || blockEntity.isEmpty())
            return;

        if (!(state.getBlock() instanceof BarrelBlock) || state.get(BarrelBlock.FACING) != Direction.DOWN)
            return;

        for (int i = 0; i < blockEntity.size(); i++) {
            ItemStack itemStack = blockEntity.getStack(i);

            if (itemStack.isEmpty())
                continue;

            Vec3d vec3d = Vec3d.ofCenter(pos).offset(Direction.DOWN, 0.5 + EntityType.ITEM.getHeight() / 2);
            ItemDispenserBehavior.spawnItem(world, itemStack.copyAndEmpty(), 0, Direction.DOWN, vec3d);
        }
    }

}
