package dev.kiuox.pingback;

import dev.kiuox.pingback.config.ConfigManager;
import dev.kiuox.pingback.util.Feedback;

public final class ReplyActions {
    private ReplyActions() {}

    public static void replyToLast() {
        setTarget(MessageStore.latest());
    }

    public static void cycleTarget() {
        StoredMessage current = ReplyState.target();
        StoredMessage next = current == null ? MessageStore.latest() : MessageStore.previousBefore(current.sender());
        setTarget(next);
    }

    public static void setTarget(StoredMessage m) {
        if (!ConfigManager.get().enabled) {
            Feedback.info(PingbackText.get("pingback.status.disabled"));
            return;
        }
        if (m == null) {
            Feedback.error(PingbackText.get("pingback.status.noMessages"));
            return;
        }
        ReplyState.setTarget(m);
        Feedback.preview(PingbackText.get("pingback.status.target", m.sender()));
    }
}
