package dev.kiuox.pingback;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.kiuox.pingback.config.ConfigManager;
import dev.kiuox.pingback.integration.ConfigOpener;
import dev.kiuox.pingback.util.Feedback;
import dev.kiuox.pingback.util.Log;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public final class ClientCommand {
    private ClientCommand() {}

    public static void init() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registry) -> {
            try {
                LiteralArgumentBuilder<FabricClientCommandSource> root = ClientCommandManager.literal("pingback");

                root.executes(ctx -> {
                    Feedback.info("Pingback " + (ConfigManager.get().enabled ? "ON" : "OFF")
                            + ", stored: " + MessageStore.size());
                    return 1;
                });

                root.then(ClientCommandManager.literal("status").executes(ctx -> {
                    Feedback.info("Pingback " + (ConfigManager.get().enabled ? "ON" : "OFF")
                            + " | quoteLength " + ConfigManager.get().quoteLength
                            + " | stored " + MessageStore.size());
                    return 1;
                }));

                root.then(ClientCommandManager.literal("toggle").executes(ctx -> {
                    boolean now = !ConfigManager.get().enabled;
                    ConfigManager.get().enabled = now;
                    ConfigManager.save();
                    PingbackText.rebuild();
                    Feedback.status(PingbackText.get(now ? "pingback.status.enabled" : "pingback.status.disabled"));
                    return 1;
                }));

                root.then(ClientCommandManager.literal("config").executes(ctx -> {
                    ConfigOpener.request();
                    return 1;
                }));

                root.then(ClientCommandManager.literal("reload").executes(ctx -> {
                    ConfigManager.load();
                    PingbackText.rebuild();
                    Feedback.status(PingbackText.get("pingback.status.reloaded"));
                    return 1;
                }));

                root.then(ClientCommandManager.literal("reply")
                        .then(ClientCommandManager.literal("last").executes(ctx -> {
                            ReplyActions.replyToLast();
                            return 1;
                        })));

                dispatcher.register(root);
            } catch (Throwable t) {
                Log.err("commandRegister", t);
            }
        });
    }
}
