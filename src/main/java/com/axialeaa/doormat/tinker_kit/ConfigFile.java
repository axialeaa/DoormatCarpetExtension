package com.axialeaa.doormat.tinker_kit;

import com.axialeaa.doormat.Doormat;
import com.axialeaa.doormat.registry.DoormatTinkerTypes;
import com.google.gson.*;
import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;
import org.jetbrains.annotations.ApiStatus;
import java.io.*;
import java.lang.constant.ConstantDesc;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import static com.axialeaa.doormat.tinker_kit.TinkerKitUtils.getKey;

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

        for (TinkerType<?, ?> type : DoormatTinkerTypes.LIST)
            saveValuesForType(type, root);

        return writeToFile(root, new File(dir, FILE_NAME));
    }

    private static <W extends ConstantDesc, R> void saveValuesForType(TinkerType<W, R> type, JsonObject root) {
        if (type.transientValues.isEmpty())
            return;

        JsonObject obj = new JsonObject();
        type.transientValues.forEach((block, r) -> obj.add(getKey(block), type.getPrimitiveFromR(r)));

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

        JsonElement element;

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            element = JsonParser.parseReader(reader);
        }
        catch (IOException e) {
            Doormat.LOGGER.warn("Failed to parse the file at {}!", file.getAbsolutePath(), e);
            return false;
        }

        if (element == null || !element.isJsonObject())
            return false;

        JsonObject root = element.getAsJsonObject();

        for (TinkerType<?, ?> type : DoormatTinkerTypes.LIST) {
            String name = type.name;

            if (!root.has(name))
                continue;

            loadValuesForType(type, root);
        }

        return true;
    }

    private static <R> void loadValuesForType(TinkerType<?, R> type, JsonObject root) {
        String name = type.name;
        JsonElement elem = root.get(name);

        if (elem == null)
            return;

        JsonObject obj = elem.getAsJsonObject();

        for (Block block : type.getBlocks()) {
            String key = getKey(block);

            if (!obj.has(key))
                continue;

            JsonPrimitive primitive = obj.getAsJsonPrimitive(key);
            R value = type.getRFromElement(primitive);

            if (value == null)
                continue;

            type.set(block, value);
        }
    }

}
