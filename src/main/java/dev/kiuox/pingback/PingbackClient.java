package dev.kiuox.pingback;

import dev.kiuox.pingback.config.ConfigManager;
import dev.kiuox.pingback.integration.ConfigOpener;
import dev.kiuox.pingback.util.Log;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class PingbackClient implements ClientModInitializer {
    public static final String MOD_ID = "pingback";

    @Override
    public void onInitializeClient() {
        ConfigManager.load();
        PingbackText.rebuild();

        safely("keybinds", Keybinds::init);
        safely("chatEvents", ChatEvents::init);
        safely("lifecycle", ClientLifecycle::init);
        safely("indicator", Indicator::init);
        safely("command", ClientCommand::init);
        safely("configOpener", ConfigOpener::init);

        Log.info("Pingback v" + version() + " initialised");
    }

    public static String version() {
        return FabricLoader.getInstance().getModContainer(MOD_ID)
                .map(container -> container.getMetadata().getVersion().getFriendlyString())
                .orElse("dev");
    }

    private static void safely(String name, Runnable task) {
        try {
            task.run();
        } catch (Throwable t) {
            Log.err(name, t);
        }
    }
}
