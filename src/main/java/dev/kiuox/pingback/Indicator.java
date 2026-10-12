package dev.kiuox.pingback;

import dev.kiuox.pingback.config.ConfigManager;
import dev.kiuox.pingback.config.PingbackConfig;
import dev.kiuox.pingback.util.Feedback;
import dev.kiuox.pingback.util.Log;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public final class Indicator {
    private static final int ACTIONBAR_REPEAT_TICKS = 40;
    private static int ticks = 0;

    private Indicator() {}

    public static void init() {
        HudRenderCallback.EVENT.register((context, tickCounter) -> {
            try {
                render(context);
            } catch (Throwable t) {
                Log.err("hudRender", t);
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            try {
                tickActionBar(client);
            } catch (Throwable t) {
                Log.err("actionbarTick", t);
            }
        });
    }

    private static void tickActionBar(MinecraftClient mc) {
        PingbackConfig c = ConfigManager.get();
        StoredMessage target = ReplyState.target();
        if (!c.enabled || c.indicatorMode != PingbackConfig.IndicatorMode.ACTIONBAR || mc.player == null || target == null) {
            ticks = 0;
            return;
        }
        if (ticks++ % ACTIONBAR_REPEAT_TICKS == 0) {
            Feedback.preview(PingbackText.get("pingback.status.target", target.sender()));
        }
    }

    private static void render(DrawContext context) {
        PingbackConfig c = ConfigManager.get();
        if (!c.enabled || c.indicatorMode != PingbackConfig.IndicatorMode.HUD) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.currentScreen != null) return;

        StoredMessage target = ReplyState.target();
        String label = target == null ? "Pingback: ON" : "Pingback @ " + target.sender();
        int color = target == null ? 0xAAAAAA : 0x55FF55;

        int y = Math.max(2, context.getScaledWindowHeight() - 14);
        context.drawTextWithShadow(mc.textRenderer, Text.literal(label), 4, y, color);
    }
}
