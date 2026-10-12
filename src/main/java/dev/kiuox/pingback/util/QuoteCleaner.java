package dev.kiuox.pingback.util;

import dev.kiuox.pingback.config.PingbackConfig;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public final class QuoteCleaner {
    public record Clean(String mentionPrefix, String content) {
    }

    private static final Pattern ANGLE_HEADER = Pattern.compile("^<[^>]{1,40}>\\s*");
    private static final Pattern LEADING_JUNK = Pattern.compile("^[\\s,:;.\\-]+");
    private static final Pattern ANY_MENTION = Pattern.compile("(?<![\\w@])@[A-Za-z0-9_]{1,16}(?!\\w)");

    private QuoteCleaner() {}

    public static Clean clean(String message, String sender, PingbackConfig c, Predicate<String> isPlayer) {
        String text = message == null ? "" : message.replaceAll("\\s+", " ").strip();

        if (c.stripNamePrefixes) {
            for (int i = 0; i < 2 && text.startsWith("["); i++) {
                int close = text.indexOf(']');
                if (close <= 0 || close >= 16) break;
                text = text.substring(close + 1).strip();
            }
            text = ANGLE_HEADER.matcher(text).replaceFirst("");
            if (sender != null && !sender.isEmpty()) {
                text = Pattern.compile("^" + Pattern.quote(sender) + "\\s*[:\u00bb]\\s*", Pattern.CASE_INSENSITIVE)
                        .matcher(text).replaceFirst("");
            }
            text = text.strip();
        }

        StringBuilder prefix = new StringBuilder();

        if (c.mentionMode != PingbackConfig.MentionMode.OFF) {
            int lead = 0;
            int stripped = 0;
            while (stripped < 3 && lead < text.length() && text.charAt(lead) == '@') {
                int end = lead + 1;
                while (end < text.length() && isNameChar(text.charAt(end))) end++;
                if (end == lead + 1) break;
                String name = text.substring(lead + 1, end);
                if (c.onlyRealPlayers && !isPlayer.test(name)) break;
                if (c.mentionMode == PingbackConfig.MentionMode.PRESERVE) {
                    if (prefix.length() > 0) prefix.append(' ');
                    prefix.append('@').append(name);
                }
                stripped++;
                lead = end;
                while (lead < text.length() && text.charAt(lead) == ' ') lead++;
            }
            text = text.substring(lead);
            if (stripped > 0) text = LEADING_JUNK.matcher(text).replaceFirst("");
            text = text.strip();
        }

        if (c.mentionMode == PingbackConfig.MentionMode.ALL_MENTIONS) {
            text = ANY_MENTION.matcher(text).replaceAll("").replaceAll("\\s+", " ").strip();
        }

        return new Clean(prefix.toString(), text);
    }

    private static boolean isNameChar(char c) {
        return c == '_' || Character.isLetterOrDigit(c);
    }
}
