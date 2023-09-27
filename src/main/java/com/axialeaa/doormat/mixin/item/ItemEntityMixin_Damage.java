package com.axialeaa.doormat.mixin.item;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.util.DoormatTags;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin_Damage {

    @Shadow public abstract ItemStack getStack();

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (DoormatSettings.consistentItemExplosionDamage && this.getStack().isIn(DoormatTags.EXPLOSION_IMMUNE_ITEMS) && source.isIn(DamageTypeTags.IS_EXPLOSION))
            cir.setReturnValue(false);
    }

}
