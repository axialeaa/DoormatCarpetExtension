package com.axialeaa.doormat.mixin.rule.dustTravel;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.fakes.BlockDustBehaviourInterface;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(StairsBlock.class)
public class StairBlockMixin implements BlockDustBehaviourInterface {

    @Override
    public boolean dustCanDescend(World world, BlockPos pos, BlockState state, Direction direction) {
        return DoormatSettings.stairDiodes;
    }

}
