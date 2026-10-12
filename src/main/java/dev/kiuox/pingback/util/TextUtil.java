package dev.kiuox.pingback.util;

public final class TextUtil {
    public static final int MIN_QUOTE = 8;
    public static final int MAX_QUOTE = 40;

    private TextUtil() {}

    public static String truncate(String input, int chars) {
        if (input == null) return "";
        String text = input.strip();
        if (chars <= 0) return "";
        if (text.codePointCount(0, text.length()) <= chars) return text;

        int end = text.offsetByCodePoints(0, chars);
        String cut = text.substring(0, end).replaceAll("[\\s\\p{Punct}]+$", "");
        if (cut.isEmpty()) cut = text.substring(0, end);
        return cut + "...";
    }
}
