package com.axialeaa.doormat.tinker_kit;

import com.axialeaa.doormat.DoormatServer;
import com.axialeaa.doormat.command.QuasiConnectivityCommand;
import com.axialeaa.doormat.command.UpdateTypeCommand;
import com.axialeaa.doormat.util.UpdateType;
import com.google.gson.*;
import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.ApiStatus;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.UUID;

import static com.axialeaa.doormat.tinker_kit.TinkerKit.*;

/**
 * Controls the saving and loading of the doormat.json file, which is used to keep the quasi-connectivity and update type settings persistent on relog. This should never be mixed into or called from in any other mod than <b>Doormat</b>.
 */
@ApiStatus.Internal
@ApiStatus.NonExtendable
@ApiStatus.Experimental
public class ConfigFile {

    public static final String FILE_NAME = DoormatServer.MOD_ID + ".json";

    private static File getWorldDirectory(MinecraftServer server) {
        return server.getSavePath(WorldSavePath.ROOT).toFile();
    }

    public static void updateFile(MinecraftServer server) {
        File directory = getWorldDirectory(server);

        if ((directory.exists() && directory.isDirectory()) || directory.mkdirs()) {
            JsonObject root = new JsonObject();

            JsonObject qcObj = new JsonObject();
            JsonObject updateTypeObj = new JsonObject();

            root.addProperty("mod_version", DoormatServer.MOD_VERSION.getFriendlyString());
            root.addProperty("notice", "This file is used for saving values across server restarts. If you delete an entry here (or set it to something invalid), it will reset to the default value the next time you enter the world!");

            root.add(QuasiConnectivityCommand.ALIAS, qcObj);
            root.add(UpdateTypeCommand.ALIAS, updateTypeObj);
            // Reuse the command aliases for the object names because why not? It saves writing another string xd

            for (Block block : Type.QC.getBlocks().toList()) {
                if (!Type.QC.isDefaultValue(block))
                    qcObj.add(getKey(block), new JsonPrimitive((int) Type.QC.getModifiedValue(block)));
            }

            for (Block block : Type.UPDATE_TYPE.getBlocks().toList()) {
                if (!Type.UPDATE_TYPE.isDefaultValue(block))
                    updateTypeObj.add(getKey(block), new JsonPrimitive(((UpdateType) Type.UPDATE_TYPE.getModifiedValue(block)).toString()));
            }
            // Saves the value when, and only when, it has been modified from default.
            //  This helps prevent unnecessary bloat, since the default values are already hardcoded.

            writeToFile(root, new File(directory, FILE_NAME));
        }
    }

    /**
     * This is pretty much copied from Masa's Malilib. It writes new modifications to a temporary file which then
     * overwrites the old one. This helps to prevent losing your data if the server crashes when changing a value.
     */
    private static void writeToFile(JsonObject root, File configFile) {
        File tempFile = new File(configFile.getParentFile(), configFile.getName() + ".tmp");

        if (tempFile.exists())
            tempFile = new File(configFile.getParentFile(), UUID.randomUUID() + ".tmp");

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(tempFile), StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            writer.write(gson.toJson(root));
            writer.close();

            if (configFile.exists() && configFile.isFile() && !configFile.delete())
                DoormatServer.LOGGER.warn("Could not delete the file at {}!", configFile.getAbsolutePath());

            if (!tempFile.renameTo(configFile))
                throw new Exception();
        }
        catch (Exception exception) {
            DoormatServer.LOGGER.warn("Could not write data to file at {}!", tempFile.getAbsolutePath(), exception);
        }
    }

    /**
     * Sets the quasi-connectivity and update type hashmap values to those stored in the json file, if they exist. Otherwise, they will be set to the default values.
     */
    public static void loadFromFile(MinecraftServer server) {
        File configFile = new File(getWorldDirectory(server), FILE_NAME);

        if (configFile.exists() && configFile.isFile() && configFile.canRead()) {
            JsonElement parseElement = null;

            try (InputStreamReader reader = new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8)) {
                parseElement = JsonParser.parseReader(reader);
            }
            catch (IOException exception) {
                DoormatServer.LOGGER.warn("Failed to parse the file at {}!", configFile.getAbsolutePath(), exception);
            }

            if (parseElement != null && parseElement.isJsonObject()) {
                JsonObject root = parseElement.getAsJsonObject();

                putQCValues(root);
                putUpdateTypeValues(root);
            }
        }
    }

    private static void putQCValues(JsonObject root) {
        JsonObject qcObject = root.get(QuasiConnectivityCommand.ALIAS).getAsJsonObject();

        for (Block block : Type.QC.getBlocks().toList()) {
            String key = getKey(block);

            JsonElement qcElement = qcObject.get(key);

            if (qcObject.has(key) && qcElement.isJsonPrimitive()) {
                JsonPrimitive qcPrimitive = qcElement.getAsJsonPrimitive();

                try {
                    if (qcPrimitive.isNumber())
                        Type.QC.set(block, MathHelper.clamp(qcElement.getAsNumber().intValue(), 0, DoormatServer.MAX_QC_RANGE));
                    else if (qcPrimitive.isBoolean()) {
                        DoormatServer.LOGGER.info("{} quasi-connectivity value was written as {}", getTranslatedName(block), qcElement.getAsBoolean() ? "true. Attempting to use quasiConnectivity setting." : "false. Attempting to set to 0.");
                        Type.QC.set(block, qcElement.getAsBoolean() ? 1 : 0);
                    }
                    else throw new Exception();
                }
                catch (Exception e) {
                    DoormatServer.LOGGER.warn("{} quasi-connectivity value failed to overwrite the default value ({}) from json!", getTranslatedName(block), Type.UPDATE_TYPE.getDefaultValue(block));
                }
            }
        }
    }

    private static void putUpdateTypeValues(JsonObject root) {
        JsonObject updateTypeObj = root.get(UpdateTypeCommand.ALIAS).getAsJsonObject();

        for (Block block : Type.UPDATE_TYPE.getBlocks().toList()) {
            String key = getKey(block);

            JsonElement updateTypeElement = updateTypeObj.get(key);

            if (updateTypeObj.has(key) && updateTypeElement.isJsonPrimitive()) {
                JsonPrimitive updateTypePrimitive = updateTypeElement.getAsJsonPrimitive();

                try {
                    if (updateTypePrimitive.isString())
                        Type.UPDATE_TYPE.set(block, UpdateType.valueOf(updateTypeElement.getAsString().toUpperCase(Locale.ROOT)));
                    else if (updateTypePrimitive.isNumber()) {
                        int value = MathHelper.clamp(updateTypeElement.getAsInt(), 0, 3);

                        DoormatServer.LOGGER.info("{} update type value was written as {}. Attempting to set to UpdateType with flags matching {}.", getTranslatedName(block), updateTypeElement.getAsNumber(), value);
                        Type.UPDATE_TYPE.set(block, UpdateType.getFromFlags(value));
                    }
                    else throw new Exception();
                }
                catch (Exception e) {
                    DoormatServer.LOGGER.warn("{} update type failed to overwrite the default value ({}) from json!", getTranslatedName(block), Type.UPDATE_TYPE.getDefaultValue(block));
                }
            }
        }
    }

}
