package com.axialeaa.doormat.mixin.rule.portalSize;

import net.minecraft.world.dimension.NetherPortal;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(NetherPortal.class)
public class NetherPortalMixin {

//    @ModifyConstant(method = { "getHeight", "isValid" }, constant = @Constant(intValue = 3))
//    private int modifyMinHeight(int constant) {
//        return IntegerRangeValidator.getSmall(DoormatSettings.portalHeight);
//    }
//
//    @ModifyConstant(method = { "getLowerCorner", "getHeight", "getPotentialHeight" }, constant = @Constant(intValue = 21))
//    private int modifyMaxHeight(int constant) {
//        return IntegerRangeValidator.getLarge(DoormatSettings.portalHeight);
//    }
//
//    @ModifyConstant(method = "isValid", constant = @Constant(intValue = 21, ordinal = 1))
//    private int modifyMaxHeight_isValid(int constant) {
//        return IntegerRangeValidator.getLarge(DoormatSettings.portalHeight);
//    }
//
//    @ModifyConstant(method = { "getWidth()I", "isValid" }, constant = @Constant(intValue = 2))
//    private int modifyMinWidth(int constant) {
//        return IntegerRangeValidator.getSmall(DoormatSettings.portalWidth);
//    }
//
//    @ModifyConstant(method = "getWidth*", constant = @Constant(intValue = 21))
//    private int modifyMaxWidth(int constant) {
//        return IntegerRangeValidator.getLarge(DoormatSettings.portalWidth);
//    }
//
//    @ModifyConstant(method = "isValid", constant = @Constant(intValue = 21, ordinal = 0))
//    private int modifyMaxWidth_isValid(int constant) {
//        return IntegerRangeValidator.getLarge(DoormatSettings.portalWidth);
//    }

}
