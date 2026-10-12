package dev.kiuox.pingback;

import dev.kiuox.pingback.config.ConfigManager;
import dev.kiuox.pingback.util.Preview;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public final class PingbackText {
    private static MutableText example = Text.literal("");

    private PingbackText() {}

    public static void rebuild() {
        example = exampleFor(ConfigManager.get().quoteLength);
    }

    public static MutableText exampleFor(int quoteLength) {
        return Text.literal(Preview.reply(ConfigManager.get(), quoteLength));
    }

    public static MutableText quoteTooltip(int quoteLength) {
        return Text.translatable("pingback.tooltip.quoteLength")
                .append(Text.literal("\n\n"))
                .append(Text.translatable("pingback.tooltip.original").formatted(Formatting.GRAY))
                .append(Text.literal("\n" + Preview.original() + "\n\n"))
                .append(Text.translatable("pingback.tooltip.reply").formatted(Formatting.GRAY))
                .append(Text.literal("\n"))
                .append(exampleFor(quoteLength));
    }

    public static MutableText example() {
        return example.copy();
    }

    public static String get(String key, Object... args) {
        return Text.translatable(key, args).getString();
    }
}
