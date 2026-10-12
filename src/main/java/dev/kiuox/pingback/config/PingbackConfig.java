package dev.kiuox.pingback.config;

public class PingbackConfig {
    public static final int CURRENT_CONFIG_VERSION = 2;

    public int configVersion = CURRENT_CONFIG_VERSION;

    public boolean enabled = true;
    public int quoteLength = 26;
    public int maxMessageLength = 250;
    public EmptyQuoteMode emptyQuoteMode = EmptyQuoteMode.OMIT;

    public boolean quoteBox = true;
    public QuoteStyle quoteStyle = QuoteStyle.BRACKET;
    public String arrow = "--->";
    public boolean includeSenderInQuote = true;

    public MentionMode mentionMode = MentionMode.LEADING_ONLY;
    public boolean onlyRealPlayers = true;
    public boolean stripNamePrefixes = true;

    public IndicatorMode indicatorMode = IndicatorMode.ACTIONBAR;
    public boolean debug = false;

    public enum EmptyQuoteMode { OMIT, EMPTY }
    public enum MentionMode { OFF, LEADING_ONLY, ALL_MENTIONS, PRESERVE }
    public enum IndicatorMode { OFF, ACTIONBAR, HUD }

    public enum QuoteStyle {
        BRACKET("[", "]"),
        GUILLEMET("«", "»"),
        PAREN("(", ")"),
        NONE("", "");

        private final String open;
        private final String close;

        QuoteStyle(String open, String close) { this.open = open; this.close = close; }

        public String open() { return open; }
        public String close() { return close; }
    }
}
