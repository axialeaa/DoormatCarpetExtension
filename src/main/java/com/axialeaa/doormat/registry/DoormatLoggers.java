package com.axialeaa.doormat.registry;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import java.lang.reflect.Field;

public class DoormatLoggers {

    public static boolean randomTicks;

    public static void register() {
        LoggerRegistry.registerLogger("randomTicks", create("randomTicks", "brief", new String[]{ "brief", "full" }, true));
    }

    private static Logger create(String logName, String defaultOption, String[] options, boolean strictOptions) {
        Field field = null;

        try {
            field = DoormatLoggers.class.getField(logName);
        }
        catch (NoSuchFieldException ignored) {
            // Will never get reached if the register method is filled out correctly.
        }

        return new Logger(field, logName, defaultOption, options, strictOptions);
    }

}
