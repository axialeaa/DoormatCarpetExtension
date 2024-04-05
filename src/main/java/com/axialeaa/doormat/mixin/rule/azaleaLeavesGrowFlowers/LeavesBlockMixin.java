package com.axialeaa.doormat.mixin.rule.azaleaLeavesGrowFlowers;

import net.minecraft.block.LeavesBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LeavesBlock.class)
public class LeavesBlockMixin /*implements Fertilizable*/ {

//    @Unique
//    private boolean isRuleIsLeaves(BlockState state) {
//        return DoormatSettings.azaleaLeavesGrowFlowers && state.isOf(Blocks.AZALEA_LEAVES);
//    }
//
//    @Override
//    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
//        return isRuleIsLeaves(state);
//    }
//
//    @Override
//    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
//        return isRuleIsLeaves(state);
//    }
//
//    /**
//     * Replaces the azalea leaves with flowering azalea leaves when fertilised, conserving the block properties.
//     */
//    @Override
//    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
//        world.setBlockState(pos, Blocks.FLOWERING_AZALEA_LEAVES.getStateWithProperties(state));
//    }

}
