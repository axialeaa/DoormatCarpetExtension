package com.axialeaa.doormat.helper;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.Dismounting;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.CollisionView;
import net.minecraft.world.World;

import java.util.Optional;

public class CampfireRespawningHelper {

    /**
     * Constructs a list of positions forming a 3x3 ring around a campfire.
     * <pre>
     *       N
     *   [5][1][6]
     * W [2] C [4] E
     *   [7][3][8]
     *       S </pre>
     */
    private static final ImmutableList<Vec3i> DONUT = ImmutableList.of(
        new Vec3i( 0, 0, -1), // 1
        new Vec3i(-1, 0,  0), // 2
        new Vec3i( 0, 0,  1), // 3
        new Vec3i( 1, 0,  0), // 4
        new Vec3i(-1, 0, -1), // 5
        new Vec3i( 1, 0, -1), // 6
        new Vec3i(-1, 0,  1), // 7
        new Vec3i( 1, 0,  1)  // 8
    );

    /**
     * Constructs a 3x3x3 cube with a 3-block tall hole through the middle, centred on the campfire. This protects the player from burning on the campfire when respawning.
     */
    private static final ImmutableList<Vec3i> SPAWN_OFFSETS = new ImmutableList.Builder<Vec3i>()
        .addAll(DONUT)
        .addAll(DONUT.stream().map(Vec3i::down).iterator())
        .addAll(DONUT.stream().map(Vec3i::up).iterator())
        .build();

    public static Optional<Vec3d> findRespawnPosition(EntityType<?> entity, CollisionView world, BlockPos pos) {
        Optional<Vec3d> optional = findRespawnPosition(entity, world, pos, true);
        return optional.isPresent() ? optional : findRespawnPosition(entity, world, pos, false);
    }

    private static Optional<Vec3d> findRespawnPosition(EntityType<?> entity, CollisionView world, BlockPos pos, boolean ignoreInvalidPos) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for(Vec3i offset : SPAWN_OFFSETS) {
            mutable.set(pos).move(offset);
            Vec3d vec3d = Dismounting.findRespawnPos(entity, world, mutable, ignoreInvalidPos);

            if (vec3d != null)
                return Optional.of(vec3d);
        }

        return Optional.empty();
    }

    public static ItemActionResult setSpawnAt(World world, BlockPos pos, BlockState state, PlayerEntity player, ItemStack stack) {
        if (!DoormatSettings.campfireRespawning || !shouldAcceptCampfireInteraction(world, state, player, stack) || !(player instanceof ServerPlayerEntity serverPlayer))
            return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if (pos != serverPlayer.getSpawnPointPosition()) {
            serverPlayer.setSpawnPoint(World.OVERWORLD, pos, 0.0f, false, true);
            return ItemActionResult.SUCCESS;
        }

        return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    private static boolean shouldAcceptCampfireInteraction(World world, BlockState state, PlayerEntity player, ItemStack stack) {
        return stack.isEmpty() && player.isSneaking() && world.getRegistryKey() == World.OVERWORLD && CampfireBlock.isLitCampfire(state);
    }

    public static void sendSoundPacket(ServerWorld world, BlockPos pos, ServerPlayerEntity player) {
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();

        player.networkHandler.sendPacket(new PlaySoundS2CPacket(
            RegistryEntry.of(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE),
            SoundCategory.BLOCKS,
            x, y, z,
            1.0F,
            1.0F,
            world.getRandom().nextLong()
        ));
    }

}
