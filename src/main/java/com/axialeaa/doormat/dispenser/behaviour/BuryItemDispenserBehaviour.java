package com.axialeaa.doormat.dispenser.behaviour;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.axialeaa.doormat.util.SingleStackSetter;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import java.util.Map;

public class BuryItemDispenserBehaviour extends FallibleItemDispenserBehavior {

    private final BlockState susState;

    public static final Map<Block, Block> MAP = ImmutableMap.of(
        Blocks.SAND, Blocks.SUSPICIOUS_SAND,
        Blocks.GRAVEL, Blocks.SUSPICIOUS_GRAVEL
    );

    public BuryItemDispenserBehaviour(Block block) {
        Block susBlock = MAP.get(block);
        this.susState = susBlock.getDefaultState();
    }

    @Override
    protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
        this.setSuccess(false);

        if (stack.isEmpty())
            return stack;

        ServerWorld serverWorld = pointer.world();

        Direction facingDir = pointer.state().get(DispenserBlock.FACING);
        BlockPos blockPos = pointer.pos().offset(facingDir);

        if (!serverWorld.setBlockState(blockPos, this.susState, Block.NOTIFY_ALL))
            return stack;

        BlockEntity blockEntity = serverWorld.getBlockEntity(blockPos);

        if (blockEntity instanceof SingleStackSetter singleStackSetter) {
            singleStackSetter.setStack(DoormatSettings.suspiciousLootAmountFix ? stack.copyAndEmpty() : stack.split(10));
            this.setSuccess(true);
        }

        return stack;
    }

}
