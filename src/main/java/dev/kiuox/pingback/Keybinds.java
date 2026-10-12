package dev.kiuox.pingback;

import dev.kiuox.pingback.config.ConfigManager;
import dev.kiuox.pingback.util.Feedback;
import dev.kiuox.pingback.util.Log;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public final class Keybinds {
    private static KeyBinding replyKey;
    private static KeyBinding cycleKey;
    private static KeyBinding cancelKey;
    private static KeyBinding toggleKey;

    private Keybinds() {}

    public static void init() {
        replyKey = reg("key.pingback.reply", GLFW.GLFW_KEY_R);
        cycleKey = reg("key.pingback.cycle", GLFW.GLFW_KEY_J);
        cancelKey = reg("key.pingback.cancel", GLFW.GLFW_KEY_B);
        toggleKey = reg("key.pingback.toggle", GLFW.GLFW_KEY_K);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            try {
                if (client.player == null) return;
                while (replyKey.wasPressed()) if (client.currentScreen == null) ReplyActions.replyToLast();
                while (cycleKey.wasPressed()) if (client.currentScreen == null) ReplyActions.cycleTarget();
                while (cancelKey.wasPressed()) {
                    ReplyState.cancel();
                    Feedback.info(PingbackText.get("pingback.status.cancelled"));
                }
                while (toggleKey.wasPressed()) {
                    boolean now = !ConfigManager.get().enabled;
                    ConfigManager.get().enabled = now;
                    ConfigManager.save();
                    PingbackText.rebuild();
                    Feedback.status(PingbackText.get(now ? "pingback.status.enabled" : "pingback.status.disabled"));
                }
            } catch (Throwable t) {
                Log.err("keyTick", t);
            }
        });
    }

    private static KeyBinding reg(String id, int code) {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding(id, InputUtil.Type.KEYSYM, code, "category.pingback"));
    }
}
