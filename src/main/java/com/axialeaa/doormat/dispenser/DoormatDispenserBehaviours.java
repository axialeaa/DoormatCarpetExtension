package com.axialeaa.doormat.dispenser;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.axialeaa.doormat.dispenser.behaviour.BuryItemDispenserBehaviour;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class DoormatDispenserBehaviours {

    public static final DispenserBehavior BURY_ITEM = new BuryItemDispenserBehaviour();

    public static DispenserBehavior getCustom(ServerWorld world, BlockPos pos, BlockPointer pointer) {
        Direction facingDir = pointer.state().get(DispenserBlock.FACING);
        BlockPos blockPos = pos.offset(facingDir);
        BlockState blockState = world.getBlockState(blockPos);

        if (DoormatSettings.dispensersBuryItems && (blockState.isOf(Blocks.SAND) || blockState.isOf(Blocks.GRAVEL)))
            return BURY_ITEM;

        return null;
    }

}
