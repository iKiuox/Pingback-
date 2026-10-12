package dev.kiuox.pingback.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.kiuox.pingback.util.Log;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("pingback.json");

    private static PingbackConfig config = new PingbackConfig();

    private ConfigManager() {}

    public static PingbackConfig get() {
        return config;
    }

    public static void load() {
        try {
            if (Files.exists(PATH)) {
                PingbackConfig loaded = GSON.fromJson(Files.readString(PATH), PingbackConfig.class);
                if (loaded != null) config = loaded;
                migrate();
                Log.info("Config loaded from " + PATH);
            } else {
                save();
                Log.info("Default config written to " + PATH);
            }
        } catch (Throwable t) {
            Log.err("configLoad", t);
            config = new PingbackConfig();
        }
        config.quoteLength = clamp(config.quoteLength, 8, 40);
        config.maxMessageLength = clamp(config.maxMessageLength, 40, 250);
        if (config.quoteStyle == null) config.quoteStyle = PingbackConfig.QuoteStyle.BRACKET;
        if (config.mentionMode == null) config.mentionMode = PingbackConfig.MentionMode.LEADING_ONLY;
        if (config.indicatorMode == null) config.indicatorMode = PingbackConfig.IndicatorMode.ACTIONBAR;
        if (config.emptyQuoteMode == null) config.emptyQuoteMode = PingbackConfig.EmptyQuoteMode.OMIT;
        if (config.arrow == null) config.arrow = "--->";
    }

    private static void migrate() {
        if (config.configVersion < PingbackConfig.CURRENT_CONFIG_VERSION) {
            Log.info("Migrating config v" + config.configVersion + " -> v" + PingbackConfig.CURRENT_CONFIG_VERSION);
            config.configVersion = PingbackConfig.CURRENT_CONFIG_VERSION;
            save();
        }
    }

    public static void save() {
        try {
            Files.createDirectories(PATH.getParent());
            Files.writeString(PATH, GSON.toJson(config));
        } catch (Throwable t) {
            Log.err("configSave", t);
        }
    }

    private static int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }
}
