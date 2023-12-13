package com.axialeaa.doormat.util;

import com.axialeaa.doormat.DoormatServer;
import com.axialeaa.doormat.command.QuasiConnectivityCommand;
import com.axialeaa.doormat.command.UpdateTypeCommand;
import com.google.gson.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Controls the saving and loading of the doormat.json file, which is used to keep the quasi-connectivity and update type settings persistent on relog.
 */
public class ConfigFile {

    private static final String FILE_NAME = DoormatServer.MOD_ID + ".json";

    private static File getWorldDirectory(MinecraftServer server) {
        return server.getSavePath(WorldSavePath.ROOT).toFile();
    }

    public static void save(MinecraftServer server) {
        File directory = getWorldDirectory(server);

        if ((directory.exists() && directory.isDirectory()) || directory.mkdirs()) {
            JsonObject root = new JsonObject();
            JsonObject qcObj = new JsonObject();
            JsonObject updateTypeObj = new JsonObject();

            root.addProperty("notice", "This file is used for saving values across server restarts. If you delete an entry here, it will reset to the default value the next time you enter the world!");

            root.add(QuasiConnectivityCommand.ALIAS, qcObj);
            root.add(UpdateTypeCommand.ALIAS, updateTypeObj);
            // Reuse the command aliases for the object names because why not? It saves writing another string xd

            for (QuasiConnectivityRules component : QuasiConnectivityRules.values())
                if (QuasiConnectivityRules.ruleValues.get(component) != component.getDefaultValue())
                    qcObj.add(component.getKey(), new JsonPrimitive(QuasiConnectivityRules.ruleValues.get(component)));

            for (UpdateTypeRules component : UpdateTypeRules.values())
                if (UpdateTypeRules.ruleValues.get(component) != component.getDefaultValue())
                    updateTypeObj.add(component.getKey(), new JsonPrimitive(UpdateTypeRules.ruleValues.get(component).getKey()));
            // Saves the value when, and only when, it has been modified from default.
            //  This helps prevent unnecessary bloat, since the default values are provided in the enum classes anyway.

            write(root, new File(directory, FILE_NAME));
        }
    }

    /**
     * This is pretty much copied from Masa's Malilib. It writes new modifications to a temporary file which then overwrites the old one. This helps to prevent losing your data if the server crashes when changing a value.
     */
    private static boolean write(JsonObject root, File configFile) {
        File tempFile = new File(configFile.getParentFile(), configFile.getName() + ".tmp");

        if (tempFile.exists())
            tempFile = new File(configFile.getParentFile(), UUID.randomUUID() + ".tmp");

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(tempFile), StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            writer.write(gson.toJson(root));
            writer.close();

            if (configFile.exists() && configFile.isFile() && !configFile.delete())
                DoormatServer.LOGGER.warn("Could not delete the file: {}", configFile.getAbsolutePath());

            return tempFile.renameTo(configFile);
        }
        catch (Exception exception) {
            DoormatServer.LOGGER.warn("Could not write data to file: {}", tempFile.getAbsolutePath(), exception);
        }
        return false;
    }

    /**
     * Sets the quasi-connectivity and update type hashmap values to default if there's no config file or json entry for the component, otherwise the value in the json file.
     */
    public static void load(MinecraftServer server) {
        File configFile = new File(getWorldDirectory(server), FILE_NAME);

        if (!configFile.exists()) {
            for (QuasiConnectivityRules component : QuasiConnectivityRules.values())
                QuasiConnectivityRules.ruleValues.put(component, component.getDefaultValue());

            for (UpdateTypeRules component : UpdateTypeRules.values())
                UpdateTypeRules.ruleValues.put(component, component.getDefaultValue());
        }
        else if (configFile.isFile() && configFile.canRead()) {
            JsonElement parseElement = null;
            try (InputStreamReader reader = new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8)) {
                parseElement = JsonParser.parseReader(reader);
            } catch (IOException exception) {
                DoormatServer.LOGGER.warn("Could not parse the file at: {}", configFile.getAbsolutePath(), exception);
            }

            if (parseElement != null && parseElement.isJsonObject()) {
                JsonObject root = parseElement.getAsJsonObject();

                JsonObject qcObj = root.get(QuasiConnectivityCommand.ALIAS).getAsJsonObject();
                JsonObject updateTypeObj = root.get(UpdateTypeCommand.ALIAS).getAsJsonObject();

                for (QuasiConnectivityRules component : QuasiConnectivityRules.values()) {
                    String key = component.getKey();
                    JsonElement keyElement = qcObj.get(key);
                    boolean keyExists = qcObj.has(key) && keyElement.isJsonPrimitive();

                    QuasiConnectivityRules.ruleValues.put(component, keyExists ?
                        keyElement.getAsBoolean() : component.getDefaultValue());
                }
                // One for quasi-connectivity...

                for (UpdateTypeRules component : UpdateTypeRules.values()) {
                    String key = component.getKey();
                    JsonElement keyElement = updateTypeObj.get(key);
                    boolean keyExists = updateTypeObj.has(key) && keyElement.isJsonPrimitive();

                    UpdateTypeRules.ruleValues.put(component, keyExists ?
                        UpdateTypeRules.UpdateTypes.keys.get(keyElement.getAsString()) :
                        component.getDefaultValue());
                }
                // and one for update type!
            }
        }
        else DoormatServer.LOGGER.warn("Could not read the file at: {}", configFile.getAbsolutePath());
    }

}
