package com.axialeaa.doormat.mixin.rule.dispensersBuryItems;

import com.axialeaa.doormat.util.SingleStackSetter;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.BrushableBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BrushableBlockEntity.class)
public class BrushableBlockEntityMixin extends BlockEntity implements SingleStackSetter {

    @Shadow private ItemStack item;

    public BrushableBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void setStack(ItemStack stack) {
        this.item = stack;
        this.markDirty();
    }

}
