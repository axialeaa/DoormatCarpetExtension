package com.axialeaa.doormat.mixin.rule.huskWashing;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.entity.mob.HuskEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HuskEntity.class)
public class HuskEntityMixin extends ZombieEntity {

    public HuskEntityMixin(World world) {
        super(world);
    }

    @Inject(method = "convertInWater", at = @At("HEAD"))
    private void onConvertInWater(CallbackInfo info) {
        if (DoormatSettings.huskWashing && this.getWorld().getGameRules().getBoolean(GameRules.DO_MOB_LOOT) && !this.isBaby())
            dropStack(new ItemStack(Items.SAND, random.nextBetween(1, 3)));
    }

}
