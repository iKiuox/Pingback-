package dev.kiuox.pingback.util;

import dev.kiuox.pingback.config.ConfigManager;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalTime;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class Log {
    private static final Logger LOG = LoggerFactory.getLogger("pingback");
    private static final Set<String> REPORTED = ConcurrentHashMap.newKeySet();
    private static Path debugPath;

    private Log() {}

    public static void info(String msg) {
        LOG.info("[Pingback] {}", msg);
        file("INFO  " + msg);
    }

    public static void warn(String msg) {
        LOG.warn("[Pingback] {}", msg);
        file("WARN  " + msg);
    }

    public static void err(String where, Throwable t) {
        if (REPORTED.add(where)) {
            LOG.error("[Pingback] failure in '{}'", where, t);
        }
        file("ERROR " + where + " :: " + t);
    }

    public static void debug(String msg) {
        if (!ConfigManager.get().debug) return;
        LOG.info("[Pingback/debug] {}", msg);
        file("DEBUG " + msg);
    }

    private static void file(String line) {
        try {
            if (!ConfigManager.get().debug) return;
            if (debugPath == null) {
                debugPath = FabricLoader.getInstance().getGameDir().resolve("logs").resolve("pingback-debug.log");
            }
            Files.writeString(debugPath,
                    "[" + LocalTime.now() + "] " + line + System.lineSeparator(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Throwable ignored) {
        }
    }
}
