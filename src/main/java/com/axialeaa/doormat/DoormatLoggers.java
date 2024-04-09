package com.axialeaa.doormat;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;

public class DoormatLoggers {

    public static boolean __randomTicks;

    public static void register() {
        LoggerRegistry.registerLogger("randomTicks", createLogger("randomTicks", "chat", new String[]{ "chat", "visual", "both" }, true));
    }

    static Logger createLogger(String logName, String defaultOption, String [] options, boolean strictOptions) {
        try {
            return new Logger(DoormatLoggers.class.getField("__" + logName), logName, defaultOption, options, strictOptions);
        }
        catch (NoSuchFieldException e) {
            throw new RuntimeException("Failed to create logger " + logName);
        }
    }

}
