package dev.kiuox.pingback.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public final class Feedback {
    private Feedback() {}

    public static void preview(String text) {
        send(text, 0xFFFF55);
    }

    public static void info(String text) {
        send(text, 0xAAAAAA);
    }

    public static void status(String text) {
        send(text, 0x55FF55);
    }

    public static void error(String text) {
        send(text, 0xFF5555);
    }

    private static void send(String text, int color) {
        try {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc == null || mc.player == null) return;
            mc.player.sendMessage(Text.literal(text).styled(s -> s.withColor(color)), true);
        } catch (Throwable t) {
            Log.err("feedback", t);
        }
    }
}
