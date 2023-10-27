package com.axialeaa.doormat.mixin.rule.consistentItemExplosionDamage;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Shadow public abstract ItemStack getStack();

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void disableExplosionDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Item item = this.getStack().getItem();
        Block block = ((BlockItem)item).getBlock();
        float blastResistance = block.getBlastResistance();
        if (DoormatSettings.consistentItemExplosionDamage && source.isIn(DamageTypeTags.IS_EXPLOSION)) {
            if ((source.getSource() instanceof FireballEntity || source.getSource() instanceof WitherSkullEntity) && blastResistance > 3)
                cir.setReturnValue(false);
            cir.setReturnValue(blastResistance > 9);
        }
    }

}
