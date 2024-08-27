package com.axialeaa.doormat.dispenser.behaviour;

import com.axialeaa.doormat.settings.DoormatSettings;
import com.axialeaa.doormat.fake.SingleStackSetter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class BuryItemDispenserBehaviour extends FallibleItemDispenserBehavior {

    @Override
    protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
        this.setSuccess(true);

        ServerWorld serverWorld = pointer.world();
        Direction facingDir = pointer.state().get(DispenserBlock.FACING);
        BlockPos blockPos = pointer.pos().offset(facingDir);
        BlockState blockState = serverWorld.getBlockState(blockPos);

        Block block = null;

        if (blockState.isOf(Blocks.SAND))
            block = Blocks.SUSPICIOUS_SAND;
        else if (blockState.isOf(Blocks.GRAVEL))
            block = Blocks.SUSPICIOUS_GRAVEL;

        if (block != null && !stack.isEmpty()) {
            serverWorld.setBlockState(blockPos, block.getDefaultState(), Block.NOTIFY_ALL);
            MinecraftClient.getInstance().particleManager.addBlockBreakParticles(blockPos, block.getDefaultState());

            if (serverWorld.getBlockEntity(blockPos) instanceof SingleStackSetter singleStackSetter)
                singleStackSetter.setStack(DoormatSettings.suspiciousLootAmountFix ? stack.copyAndEmpty() : stack.split(10));

            return stack;
        }

        this.setSuccess(false);

        return stack;
    }

}
