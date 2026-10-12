package dev.kiuox.pingback;

import dev.kiuox.pingback.util.Log;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public final class ClientLifecycle {
    private ClientLifecycle() {}

    public static void init() {
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            try {
                ReplyState.reset();
                Log.debug("state reset on disconnect");
            } catch (Throwable t) {
                Log.err("disconnect", t);
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            try {
                if (client.world == null && (ReplyState.target() != null || MessageStore.size() > 0)) {
                    ReplyState.reset();
                }
            } catch (Throwable t) {
                Log.err("tickReset", t);
            }
        });
    }
}
