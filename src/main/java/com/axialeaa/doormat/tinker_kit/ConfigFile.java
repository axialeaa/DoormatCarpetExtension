package com.axialeaa.doormat.tinker_kit;

import com.axialeaa.doormat.Doormat;
import com.axialeaa.doormat.util.UpdateType;
import com.google.gson.*;
import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.tick.TickPriority;
import org.jetbrains.annotations.ApiStatus;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.axialeaa.doormat.tinker_kit.TinkerKit.Type;
import static com.axialeaa.doormat.tinker_kit.TinkerKit.getKey;

/**
 * Controls the saving and loading of the doormat.json file, which is used to keep the quasi-connectivity and update entries settings persistent on relog. This should never be mixed into or called from in any other mod than <b>Doormat</b>.
 */
@ApiStatus.Internal
public class ConfigFile {

    public static final String FILE_NAME = "%s.json".formatted(Doormat.MOD_ID);

    private static File getWorldDirectory(MinecraftServer server) {
        return server.getSavePath(WorldSavePath.ROOT).toFile();
    }

    public static boolean updateFile(MinecraftServer server) {
        File dir = getWorldDirectory(server);

        if (!((dir.exists() && dir.isDirectory()) || dir.mkdirs()))
            return false;

        JsonObject root = new JsonObject();

        root.addProperty("mod_version", Doormat.MOD_VERSION.getFriendlyString());
        root.addProperty("notice", "This file is used for saving values across server restarts. If you delete an entry here (or set it to something invalid), it will reset to the default value the next time you enter the world!");

        for (Type type : Type.values())
            saveValuesForType(type, root);

        File file = new File(dir, FILE_NAME);

        return writeToFile(root, file);
    }

    private static void saveValuesForType(Type type, JsonObject root) {
        Map<String, JsonPrimitive> values = new HashMap<>();

        for (Block block : type.getBlocks()) {
            var value = type.getTransientValue(block);

            if (value == null)
                continue;

            values.put(getKey(block), switch (type) {
                case QC, DELAY, TICK_PRIORITY -> new JsonPrimitive((int) value);
                case UPDATE_TYPE -> new JsonPrimitive(value.toString());
            });
        }

        if (values.isEmpty())
            return;

        JsonObject obj = new JsonObject();
        values.forEach(obj::add);

        root.add(type.name, obj);
    }

    /**
     * This is pretty much copied from Masa's Malilib. It writes new modifications to a temporary file which then
     * overwrites the old one. This helps to prevent losing your data if the server crashes when changing a value.
     */
    private static boolean writeToFile(JsonObject root, File file) {
        File parent = file.getParentFile();
        File temp = new File(parent, "%s.tmp".formatted(file.getName()));

        if (temp.exists())
            temp = new File(parent, "%s.tmp".formatted(UUID.randomUUID()));

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(temp), StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            writer.write(gson.toJson(root));
            writer.close();

            if (file.exists() && file.isFile() && !file.delete())
                Doormat.LOGGER.warn("Could not delete the file at {}!", file.getAbsolutePath());

            if (!temp.renameTo(file))
                throw new Exception();
        }
        catch (Exception e) {
            Doormat.LOGGER.warn("Could not write data to file at {}!", temp.getAbsolutePath(), e);
            return false;
        }

        return true;
    }

    /**
     * Sets the quasi-connectivity and update entries hashmap values to those stored in the json file, if they exist. Otherwise, they will be set to the default values.
     */
    public static boolean loadFromFile(MinecraftServer server) {
        File file = new File(getWorldDirectory(server), FILE_NAME);

        if (!file.exists() || !file.isFile() && !file.canRead())
            return false;

        JsonElement elem;

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            elem = JsonParser.parseReader(reader);
        }
        catch (IOException e) {
            Doormat.LOGGER.warn("Failed to parse the file at {}!", file.getAbsolutePath(), e);
            return false;
        }

        if (elem == null || !elem.isJsonObject())
            return false;

        JsonObject root = elem.getAsJsonObject();

        for (Type type : Type.values()) {
            String name = type.name;

            if (!root.has(name))
                continue;

            setValuesForType(type, root);
        }

        return true;
    }

    private static void setValuesForType(Type type, JsonObject root) {
        String name = type.name;
        JsonElement elem = root.get(name);

        if (elem == null)
            return;

        JsonObject obj = elem.getAsJsonObject();

        for (Block block : type.getBlocks()) {
            if (!obj.has(getKey(block)))
                continue;

            type.set(block, switch (type) {
                case QC -> clampInt(obj, block, 0, Doormat.MAX_QC_RANGE);
                case DELAY -> clampInt(obj, block, 0, 72000);
                case UPDATE_TYPE -> UpdateType.valueOf(JsonHelper.getString(obj, getKey(block)));
                case TICK_PRIORITY -> clampInt(obj, block, TickPriority.EXTREMELY_HIGH.getIndex(), TickPriority.EXTREMELY_LOW.getIndex());
            });
        }
    }

    private static int clampInt(JsonObject obj, Block block, int min, int max) {
        return MathHelper.clamp(JsonHelper.getInt(obj, getKey(block)), min, max);
    }

}
