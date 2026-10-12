package dev.kiuox.pingback.util;

import dev.kiuox.pingback.config.PingbackConfig;

public final class Preview {
    public static final String SENDER = "Kiuox";
    public static final String MESSAGE = "Hey, are you free tonight? Come join me at the new base, I'm building a big farm.";
    public static final String REPLY = "Sure, I'm on my way!";

    private Preview() {}

    public static String original() {
        return SENDER + ": " + MESSAGE;
    }

    public static String reply(PingbackConfig c, int quoteLength) {
        String quote = TextUtil.truncate(MESSAGE, quoteLength);
        StringBuilder sb = new StringBuilder();
        if (c.includeSenderInQuote) sb.append('@').append(SENDER).append(": ");
        if (c.quoteBox) sb.append(c.quoteStyle.open()).append(quote).append(c.quoteStyle.close());
        else sb.append(quote);
        return sb.append(' ').append(c.arrow).append(' ').append(REPLY).toString();
    }
}
