package com.axialeaa.doormat.mixin.rules.dustTravelDownGlass;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.interfaces.BlockDustBehaviourInterface;
import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractGlassBlock.class)
public class AbstractGlassBlockMixin implements BlockDustBehaviourInterface {

    @Override
    public boolean doormat$dustCanDescend(World world, BlockPos pos, BlockState state, Direction direction) {
        return DoormatSettings.dustTravelDownGlass;
    }

}
