package com.axialeaa.doormat.mixin.rule.endPlatformSillyString;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.axialeaa.doormat.setting.enum_option.EndPlatformSillyStringMode;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.gen.feature.EndPlatformFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EndPlatformFeature.class)
public class EndPlatformFeatureMixin {

    @WrapWithCondition(method = "generate(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/util/math/BlockPos;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/ServerWorldAccess;breakBlock(Lnet/minecraft/util/math/BlockPos;ZLnet/minecraft/entity/Entity;)Z"))
    private static boolean dropStacks(ServerWorldAccess instance, BlockPos blockPos, boolean b, Entity entity) {
        if (!DoormatSettings.endPlatformSillyString.isEnabled())
            return true;

        if (DoormatSettings.endPlatformSillyString == EndPlatformSillyStringMode.TRUE)
            breakBlocks((World) instance, blockPos);

        return false;
    }

    @Unique
    private static void breakBlocks(World world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();

        if (!(block instanceof AbstractFireBlock))
            world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, pos, Block.getRawIdFromState(blockState));

        BlockEntity blockEntity = blockState.hasBlockEntity() ? world.getBlockEntity(pos) : null;
        Block.dropStacks(blockState, world, pos, blockEntity);

        world.emitGameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Emitter.of(blockState));
    }

}
