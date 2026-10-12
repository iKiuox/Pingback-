package dev.kiuox.pingback;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public final class MessageStore {
    private static final int MAX = 100;
    private static final Deque<StoredMessage> MESSAGES = new ArrayDeque<>();

    private MessageStore() {}

    public static void add(String sender, String content) {
        if (sender == null || content == null) return;
        String clean = content.strip();
        if (clean.isEmpty()) return;
        MESSAGES.addLast(new StoredMessage(sender, clean, System.currentTimeMillis()));
        while (MESSAGES.size() > MAX) MESSAGES.removeFirst();
    }

    public static StoredMessage latest() {
        return MESSAGES.peekLast();
    }

    public static StoredMessage previousBefore(String sender) {
        List<StoredMessage> list = new ArrayList<>(MESSAGES);
        for (int i = list.size() - 1; i >= 0; i--) {
            if (!list.get(i).sender().equals(sender)) return list.get(i);
        }
        return latest();
    }

    public static int size() {
        return MESSAGES.size();
    }

    public static void clear() {
        MESSAGES.clear();
    }
}
