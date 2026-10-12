package dev.kiuox.pingback;

import dev.kiuox.pingback.config.ConfigManager;
import dev.kiuox.pingback.config.PingbackConfig;
import dev.kiuox.pingback.util.PingbackServer;
import dev.kiuox.pingback.util.QuoteCleaner;
import dev.kiuox.pingback.util.TextUtil;

public final class ReplyComposer {
    private static final int MIN_QUOTE = 4;

    private ReplyComposer() {}

    public static String build(StoredMessage target, String typed) {
        PingbackConfig c = ConfigManager.get();
        String body = typed == null ? "" : typed;
        QuoteCleaner.Clean clean = QuoteCleaner.clean(target.content(), target.sender(), c, PingbackServer::isRealPlayer);

        if (clean.content().isEmpty() && c.emptyQuoteMode == PingbackConfig.EmptyQuoteMode.OMIT) {
            return "@" + target.sender() + ": " + body;
        }

        int quoteLen = c.quoteLength;
        String result = assemble(c, target.sender(), clean, quoteLen, body);

        while (result.length() > c.maxMessageLength && quoteLen > MIN_QUOTE) {
            quoteLen = Math.max(MIN_QUOTE, quoteLen - 4);
            result = assemble(c, target.sender(), clean, quoteLen, body);
        }
        if (result.length() > c.maxMessageLength) {
            result = "@" + target.sender() + ": " + body;
        }
        return result;
    }

    private static String assemble(PingbackConfig c, String sender, QuoteCleaner.Clean clean, int quoteLen, String body) {
        String inner = TextUtil.truncate(clean.content(), quoteLen);
        String quoted = (inner.isEmpty() && c.emptyQuoteMode == PingbackConfig.EmptyQuoteMode.OMIT)
                ? "" : wrap(c, clean.mentionPrefix(), inner);

        StringBuilder sb = new StringBuilder();
        if (c.includeSenderInQuote) sb.append('@').append(sender).append(": ");
        if (!quoted.isEmpty()) sb.append(quoted).append(' ');
        sb.append(c.arrow).append(' ').append(body);
        return sb.toString();
    }

    private static String wrap(PingbackConfig c, String mentionPrefix, String inner) {
        String text = mentionPrefix.isEmpty() ? inner : mentionPrefix + " " + inner;
        if (!c.quoteBox) return text;
        return c.quoteStyle.open() + text + c.quoteStyle.close();
    }
}
