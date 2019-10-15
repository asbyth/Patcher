package dev.asbyth.patcher;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.asbyth.patcher.command.PatcherCommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

import static dev.asbyth.patcher.config.Settings.*;

@Mod(modid = Patcher.MODID, name = Patcher.NAME, version = Patcher.VERSION)
public class Patcher {

    public static final String MODID = "patcher_mod";
    public static final String NAME = "Patcher";
    public static final String VERSION = "1.1";

    public static final Logger LOGGER = LogManager.getLogger("Patcher");

    private File configFile = null;

    @Mod.Instance(MODID)
    public static Patcher INSTANCE;

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        configFile = event.getSuggestedConfigurationFile();
        loadConfig();
        Runtime.getRuntime().addShutdownHook(new Thread(this::saveConfig));
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new PatcherCommand());
    }

    private void loadConfig() {
        try {
            JsonObject object = new JsonParser().parse(FileUtils.readFileToString(configFile)).getAsJsonObject();
            if (object.has("ASYNC_SCREENSHOTS")) ASYNC_SCREENSHOTS = object.get("ASYNC_SCREENSHOTS").getAsBoolean();
            if (object.has("CLOSING_CHAT")) CLOSING_CHAT = object.get("CLOSING_CHAT").getAsBoolean();
            if (object.has("COMMAND_HANDLER")) COMMAND_HANDLER = object.get("COMMAND_HANDLER").getAsBoolean();
            if (object.has("DEATH_SCREEN")) DEATH_SCREEN = object.get("DEATH_SCREEN").getAsBoolean();
            if (object.has("FULLBRIGHT")) FULLBRIGHT = object.get("FULLBRIGHT").getAsBoolean();
            if (object.has("FULLSCREEN")) FULLSCREEN = object.get("FULLSCREEN").getAsBoolean();
            if (object.has("INTERNAL_ERROR")) INTERNAL_ERROR = object.get("INTERNAL_ERROR").getAsBoolean();
            if (object.has("INVENTORY_POSITION")) INVENTORY_POSITION = object.get("INVENTORY_POSITION").getAsBoolean();
            if (object.has("MOUSE_DELAY")) MOUSE_DELAY = object.get("MOUSE_DELAY").getAsBoolean();
            if (object.has("PLAYER_ARM")) PLAYER_ARM = object.get("PLAYER_ARM").getAsBoolean();
            if (object.has("VOID_FLICKERING")) VOID_FLICKERING = object.get("VOID_FLICKERING").getAsBoolean();
            if (object.has("WORLD_SWITCHING")) WORLD_SWITCHING = object.get("WORLD_SWITCHING").getAsBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("ASYNC_SCREENSHOTS", ASYNC_SCREENSHOTS);
            jsonObject.addProperty("CLOSING_CHAT", CLOSING_CHAT);
            jsonObject.addProperty("COMMAND_HANDLER", COMMAND_HANDLER);
            jsonObject.addProperty("DEATH_SCREEN", DEATH_SCREEN);
            jsonObject.addProperty("FULLBRIGHT", FULLBRIGHT);
            jsonObject.addProperty("FULLSCREEN", FULLSCREEN);
            jsonObject.addProperty("INTERNAL_ERROR", INTERNAL_ERROR);
            jsonObject.addProperty("INVENTORY_POSITION", INVENTORY_POSITION);
            jsonObject.addProperty("MOUSE_DELAY", MOUSE_DELAY);
            jsonObject.addProperty("PLAYER_ARM", PLAYER_ARM);
            jsonObject.addProperty("VOID_FLICKERING", VOID_FLICKERING);
            jsonObject.addProperty("WORLD_SWITCHING", WORLD_SWITCHING);
            FileUtils.writeStringToFile(configFile, jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
