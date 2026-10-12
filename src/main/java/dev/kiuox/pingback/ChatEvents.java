package dev.kiuox.pingback;

import dev.kiuox.pingback.config.ConfigManager;
import dev.kiuox.pingback.util.Log;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.client.MinecraftClient;

public final class ChatEvents {
    private ChatEvents() {}

    public static void init() {
        ClientReceiveMessageEvents.ALLOW_CHAT.register((message, signed, sender, params, reception) -> {
            try {
                if (sender != null && message != null) {
                    MinecraftClient mc = MinecraftClient.getInstance();
                    boolean own = mc.player != null && sender.getId() != null
                            && sender.getId().equals(mc.player.getUuid());
                    String content = message.getString();
                    if (!own && !content.isBlank()) MessageStore.add(sender.getName(), content);
                }
            } catch (Throwable t) {
                Log.err("receiveChat", t);
            }
            return true;
        });

        ClientSendMessageEvents.MODIFY_CHAT.register(ChatEvents::modifyOutgoing);
    }

    private static String modifyOutgoing(String message) {
        try {
            if (!ConfigManager.get().enabled) return message;

            StoredMessage target = ReplyState.consume();
            if (target == null) return message;

            String out = ReplyComposer.build(target, message);

            Log.debug("outgoing reply -> " + out);

            if (out.length() > 256) {
                Log.warn("reply is " + out.length() + " chars and may be rejected by the server");
            }
            return out;
        } catch (Throwable t) {
            Log.err("modifyChat", t);
            return message;
        }
    }
}
