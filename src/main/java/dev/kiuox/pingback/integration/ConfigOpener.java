package dev.kiuox.pingback.integration;

import dev.kiuox.pingback.util.Log;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public final class ConfigOpener {
    private static final String YACL_MOD_ID = "yet_another_config_lib_v3";
    private static final String YACL_CLASS = "dev.isxander.yacl3.api.YetAnotherConfigLib";

    private static volatile boolean pending = false;

    private ConfigOpener() {}

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!pending || client.currentScreen != null) return;
            pending = false;
            try {
                client.setScreen(screen(null));
            } catch (Throwable t) {
                Log.err("openConfig", t);
            }
        });
    }

    public static void request() {
        pending = true;
    }

    public static boolean yaclAvailable() {
        if (FabricLoader.getInstance().isModLoaded(YACL_MOD_ID)) return true;
        try {
            Class.forName(YACL_CLASS, false, ConfigOpener.class.getClassLoader());
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    public static Screen screen(Screen parent) {
        if (!yaclAvailable()) {
            return failure(parent, "Pingback: config screen unavailable",
                    "Yet Another Config Lib (YACL) is not installed. Edit config/pingback.json instead.");
        }
        try {
            return PingbackConfigScreen.create(parent);
        } catch (Throwable t) {
            Log.err("configScreen", t);
            return failure(parent, "Pingback: config screen failed", String.valueOf(t));
        }
    }

    private static Screen failure(Screen parent, String title, String message) {
        return new ConfirmScreen(accepted -> MinecraftClient.getInstance().setScreen(parent),
                Text.literal(title), Text.literal(message));
    }
}
