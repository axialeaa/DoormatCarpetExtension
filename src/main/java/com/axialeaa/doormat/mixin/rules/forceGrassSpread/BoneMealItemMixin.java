package com.axialeaa.doormat.mixin.rules.forceGrassSpread;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SpreadableBlock;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mixin(BoneMealItem.class)
public class BoneMealItemMixin {

    /**
     * Future-proof implementation of what is essentially {@link net.minecraft.block.NetherrackBlock#grow(ServerWorld, Random, BlockPos, BlockState) nylium logic}. Instead of defining new booleans for each instanceof {@link SpreadableBlock}, just create a list of all {@link SpreadableBlock}s around the target dirt, eliminate duplicate list entries, and pick a random list index. This should comfortably scale with any new {@link SpreadableBlock} types Mojang may or may not decide to add in the future.
     */
    @Inject(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isSideSolidFullSquare(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z"), cancellable = true)
    private void convertDirtOnUse(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir, @Local World world, @Local(ordinal = 0) BlockPos blockPos, @Local BlockState blockState) {
        if (DoormatSettings.forceGrassSpread && blockState.isOf(Blocks.DIRT)) {
            List<Block> blocks = new ArrayList<>();

            for (BlockPos adjacentPos : BlockPos.iterate(blockPos.add(-1, -1, -1), blockPos.add(1, 1, 1))) {
                Block adjacentBlock = world.getBlockState(adjacentPos).getBlock();
                if (adjacentBlock instanceof SpreadableBlock)
                    blocks.add(adjacentBlock);
            }

            List<Block> uniqueBlocks = blocks.stream().distinct().toList();
            if (!uniqueBlocks.isEmpty() && SpreadableBlock.canSpread(blockState, world, blockPos)) {
                if (!world.isClient()) {
                    Block randomBlock = uniqueBlocks.get(world.getRandom().nextInt(uniqueBlocks.size()));
                    boolean isSnowAbove = world.getBlockState(blockPos.up()).isOf(Blocks.SNOW);

                    world.setBlockState(blockPos, randomBlock.getDefaultState().with(SpreadableBlock.SNOWY, isSnowAbove));
                    Objects.requireNonNull(context.getPlayer()).emitGameEvent(GameEvent.ITEM_INTERACT_FINISH);
                    world.syncWorldEvent(WorldEvents.BONE_MEAL_USED, blockPos, 0);
                }
                cir.setReturnValue(ActionResult.success(world.isClient()));
            }
        }
    }

}
