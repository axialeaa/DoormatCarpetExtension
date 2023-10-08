package com.axialeaa.doormat.mixin.rule.renewableSporeBlossoms_mossSpreadTo;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.SporeBlossomBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SporeBlossomBlock.class)
public class SporeBlossomBlockMixin_Duplicate implements Fertilizable {

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return DoormatSettings.renewableSporeBlossoms == DoormatSettings.RenewableSporeBlossomsMode.SELF;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return DoormatSettings.renewableSporeBlossoms == DoormatSettings.RenewableSporeBlossomsMode.SELF;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        Block.dropStack(world, pos, new ItemStack(Items.SPORE_BLOSSOM));
    }

}
