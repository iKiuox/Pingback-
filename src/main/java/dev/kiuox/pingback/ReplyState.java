package dev.kiuox.pingback;

public final class ReplyState {
    private static volatile StoredMessage target;

    private ReplyState() {}

    public static void setTarget(StoredMessage message) {
        target = message;
        PingbackText.rebuild();
    }

    public static StoredMessage target() {
        return target;
    }

    public static StoredMessage consume() {
        StoredMessage t = target;
        target = null;
        PingbackText.rebuild();
        return t;
    }

    public static void cancel() {
        target = null;
        PingbackText.rebuild();
    }

    public static void reset() {
        target = null;
        MessageStore.clear();
        PingbackText.rebuild();
    }
}
