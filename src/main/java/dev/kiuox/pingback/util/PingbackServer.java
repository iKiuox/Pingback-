package dev.kiuox.pingback.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;

public final class PingbackServer {
    private PingbackServer() {}

    public static boolean isRealPlayer(String name) {
        try {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc == null || mc.getNetworkHandler() == null) return true;
            for (PlayerListEntry entry : mc.getNetworkHandler().getPlayerList()) {
                if (entry.getProfile() != null && entry.getProfile().getName().equalsIgnoreCase(name)) {
                    return true;
                }
            }
            return false;
        } catch (Throwable t) {
            return true;
        }
    }

}
