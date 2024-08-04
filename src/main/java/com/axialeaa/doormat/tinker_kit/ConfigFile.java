package com.axialeaa.doormat.tinker_kit;

import com.axialeaa.doormat.DoormatServer;
import com.axialeaa.doormat.util.UpdateType;
import com.google.gson.*;
import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.ApiStatus;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static com.axialeaa.doormat.tinker_kit.TinkerKit.*;

/**
 * Controls the saving and loading of the doormat.json file, which is used to keep the quasi-connectivity and update type settings persistent on relog. This should never be mixed into or called from in any other mod than <b>Doormat</b>.
 */
@ApiStatus.Internal
public class ConfigFile {

    public static final String FILE_NAME = DoormatServer.MOD_ID + ".json";

    private static File getWorldDirectory(MinecraftServer server) {
        return server.getSavePath(WorldSavePath.ROOT).toFile();
    }

    public static boolean updateFile(MinecraftServer server) {
        File dir = getWorldDirectory(server);

        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs()) {
            JsonObject root = new JsonObject();

            root.addProperty("mod_version", DoormatServer.MOD_VERSION.getFriendlyString());
            root.addProperty("notice", "This file is used for saving values across server restarts. If you delete an entry here (or set it to something invalid), it will reset to the default value the next time you enter the world!");

            for (Type type : Type.values()) {
                JsonObject obj = new JsonObject();
                root.add(type.name, obj);

                for (Block block : type.getBlocks().toList()) {
                    // Saves the value when, and only when, it has been modified from default.
                    //  This helps prevent unnecessary bloat, since the default values are already hardcoded.
                    if (!type.isDefaultValue(block)) {
                        var value = type.getModifiedValue(block);

                        obj.add(getKey(block), switch (type) {
                            case QC -> new JsonPrimitive((int) value);
                            case UPDATE_TYPE -> new JsonPrimitive(value.toString());
                        });
                    }
                }
            }

            return writeToFile(root, new File(dir, FILE_NAME));
        }

        return false;
    }

    /**
     * This is pretty much copied from Masa's Malilib. It writes new modifications to a temporary file which then
     * overwrites the old one. This helps to prevent losing your data if the server crashes when changing a value.
     */
    private static boolean writeToFile(JsonObject root, File file) {
        File temp = new File(file.getParentFile(), file.getName() + ".tmp");

        if (temp.exists())
            temp = new File(file.getParentFile(), UUID.randomUUID() + ".tmp");

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(temp), StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            writer.write(gson.toJson(root));
            writer.close();

            if (file.exists() && file.isFile() && !file.delete())
                DoormatServer.LOGGER.warn("Could not delete the file at {}!", file.getAbsolutePath());

            if (!temp.renameTo(file))
                throw new Exception();
        }
        catch (Exception e) {
            DoormatServer.LOGGER.warn("Could not write data to file at {}!", temp.getAbsolutePath(), e);
            return false;
        }

        return true;
    }

    /**
     * Sets the quasi-connectivity and update type hashmap values to those stored in the json file, if they exist. Otherwise, they will be set to the default values.
     */
    public static boolean loadFromFile(MinecraftServer server) {
        File file = new File(getWorldDirectory(server), FILE_NAME);

        if (file.exists() && file.isFile() && file.canRead()) {
            JsonElement elem;

            try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
                elem = JsonParser.parseReader(reader);
            }
            catch (IOException e) {
                DoormatServer.LOGGER.warn("Failed to parse the file at {}!", file.getAbsolutePath(), e);
                return false;
            }

            if (elem == null || !elem.isJsonObject())
                return false;

            JsonObject root = elem.getAsJsonObject();
            boolean changed = false;

            for (Type type : Type.values()) {
                String name = type.name;

                if (!root.has(name))
                    continue;

                JsonObject obj = root.get(name).getAsJsonObject();

                for (Block block : type.getBlocks().toList()) {
                    if (!obj.has(getKey(block)))
                        continue;

                    try {
                        type.set(block, switch (type) {
                            case QC -> MathHelper.clamp(JsonHelper.getInt(obj, getKey(block)), 0, DoormatServer.MAX_QC_RANGE);
                            case UPDATE_TYPE -> UpdateType.valueOf(JsonHelper.getString(obj, getKey(block)).toUpperCase());
                        });
                        changed = true;
                    }
                    catch (Exception e) {
                        DoormatServer.LOGGER.warn("Failed to overwrite the default {} value ({}) for block {}!", name, type.getDefaultValue(block), getTranslatedName(block), e);
                    }
                }
            }

            return changed;
        }

        return false;
    }

}
