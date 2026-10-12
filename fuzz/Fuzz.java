import dev.kiuox.pingback.config.PingbackConfig;
import dev.kiuox.pingback.util.Preview;
import dev.kiuox.pingback.util.QuoteCleaner;
import dev.kiuox.pingback.util.TextUtil;

import java.util.function.Predicate;

public final class Fuzz {
    private static int failures = 0;

    public static void main(String[] args) {
        PingbackConfig c = new PingbackConfig();
        Predicate<String> all = n -> true;
        Predicate<String> none = n -> false;

        check("plain", QuoteCleaner.clean("@Alex Hey, are you free tonight?", "Steve", c, all).content(), "Hey, are you free tonight?");
        check("rankPrefix", QuoteCleaner.clean("[Admin] Steve: hello there", "Steve", c, all).content(), "hello there");
        check("angleHeader", QuoteCleaner.clean("<Steve> hello there", "Steve", c, all).content(), "hello there");
        check("rankInAngle", QuoteCleaner.clean("<[VIP] Steve> hello there", "Steve", c, all).content(), "hello there");
        check("twoMentions", QuoteCleaner.clean("@A @B lets go", "X", c, all).content(), "lets go");
        check("mentionOnly", QuoteCleaner.clean("@A", "X", c, all).content(), "");
        check("punctuation", QuoteCleaner.clean("@Steve, come here", "X", c, all).content(), "come here");
        check("notRealPlayer", QuoteCleaner.clean("@everyone hi", "X", c, none).content(), "@everyone hi");

        PingbackConfig p = new PingbackConfig();
        p.mentionMode = PingbackConfig.MentionMode.PRESERVE;
        QuoteCleaner.Clean pr = QuoteCleaner.clean("@Steve hi", "X", p, all);
        check("preserve", pr.mentionPrefix() + "|" + pr.content(), "@Steve|hi");

        PingbackConfig a = new PingbackConfig();
        a.mentionMode = PingbackConfig.MentionMode.ALL_MENTIONS;
        check("allMentions", QuoteCleaner.clean("hi @A and @B bye", "X", a, all).content(), "hi and bye");

        check("noCut", TextUtil.truncate("hi", 10), "hi");
        check("nonAscii", TextUtil.truncate("میای تلپورت کنیم؟", 5), "میای...");
        check("emoji", TextUtil.truncate("abc😀def", 4), "abc😀...");
        PingbackConfig d = new PingbackConfig();
        check("previewOriginal", Preview.original(), "Kiuox: Hey, are you free tonight? Come join me at the new base, I'm building a big farm.");
        check("previewDefault8", Preview.reply(d, 8), "@Kiuox: [Hey, are...] ---> Sure, I'm on my way!");
        check("previewDefault13", Preview.reply(d, 13), "@Kiuox: [Hey, are you...] ---> Sure, I'm on my way!");
        check("previewDefault26", Preview.reply(d, 26), "@Kiuox: [Hey, are you free tonight...] ---> Sure, I'm on my way!");
        check("previewDefault40", Preview.reply(d, 40), "@Kiuox: [Hey, are you free tonight? Come join me...] ---> Sure, I'm on my way!");
        PingbackConfig nb = new PingbackConfig();
        nb.quoteBox = false;
        check("previewNoBrackets", Preview.reply(nb, 13), "@Kiuox: Hey, are you... ---> Sure, I'm on my way!");
        PingbackConfig ns = new PingbackConfig();
        ns.includeSenderInQuote = false;
        ns.quoteStyle = PingbackConfig.QuoteStyle.PAREN;
        ns.arrow = "=>";
        check("previewCustom", Preview.reply(ns, 13), "(Hey, are you...) => Sure, I'm on my way!");
        check("noSpace", TextUtil.truncate("aaaaaaaaaa", 4), "aaaa...");

        if (failures == 0) {
            System.out.println("All fuzz checks passed.");
        } else {
            System.out.println(failures + " check(s) failed.");
            System.exit(1);
        }
    }

    private static void check(String name, boolean actual, boolean expected) {
        check(name, String.valueOf(actual), String.valueOf(expected));
    }

    private static void check(String name, String actual, String expected) {
        boolean ok = actual.equals(expected);
        if (!ok) failures++;
        System.out.println((ok ? "PASS " : "FAIL ") + name + " -> '" + actual + "'"
                + (ok ? "" : " (expected '" + expected + "')"));
    }
}
