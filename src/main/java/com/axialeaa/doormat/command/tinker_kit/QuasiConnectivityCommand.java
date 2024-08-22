package com.axialeaa.doormat.command.tinker_kit;

import com.axialeaa.doormat.DoormatServer;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import static com.axialeaa.doormat.tinker_kit.TinkerKit.Type;

public class QuasiConnectivityCommand extends AbstractTinkerKitCommand<Integer> {

    @Override
    public Type getType() {
        return Type.QC;
    }

    @Override
    public ArgumentType<Integer> getArgumentType() {
        return IntegerArgumentType.integer(0);
    }

    @Override
    public Class<Integer> getObjectClass() {
        return Integer.class;
    }

    @Override
    public Object getInputValue(Integer argument) {
        return argument > DoormatServer.MAX_QC_RANGE ? null : argument;
    }

}
