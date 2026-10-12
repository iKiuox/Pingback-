package dev.kiuox.pingback.integration;

import dev.isxander.yacl3.api.ButtonOption;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.kiuox.pingback.PingbackClient;
import dev.kiuox.pingback.PingbackText;
import dev.kiuox.pingback.config.ConfigManager;
import dev.kiuox.pingback.config.PingbackConfig;
import dev.kiuox.pingback.util.TextUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public final class PingbackConfigScreen {
    private PingbackConfigScreen() {}

    public static Screen create(Screen parent) {
        return build(parent);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Screen build(Screen parent) {
        PingbackConfig c = ConfigManager.get();

        Option<Boolean> enabled = Option.<Boolean>createBuilder()
                .name(Text.literal("Enable Pingback"))
                .description(desc("Master switch. Also togglable in-game with the bound key (default K)."))
                .binding(true, () -> c.enabled, v -> { c.enabled = v; ConfigManager.save(); PingbackText.rebuild(); })
                .controller(BooleanControllerBuilder::create)
                .build();

        Option<Integer> quoteLength = Option.<Integer>createBuilder()
                .name(Text.literal("Quote length"))
                .description(v -> OptionDescription.of(PingbackText.quoteTooltip(v)))
                .binding(26, () -> c.quoteLength, v -> { c.quoteLength = v; ConfigManager.save(); PingbackText.rebuild(); })
                .controller(o -> IntegerSliderControllerBuilder.create((Option<Integer>) o).range(TextUtil.MIN_QUOTE, TextUtil.MAX_QUOTE).step(1))
                .build();

        Option<Boolean> quoteBox = Option.<Boolean>createBuilder()
                .name(Text.literal("Show quote brackets"))
                .description(desc("Turn off to send only '@Name: message' with no [...] around the quote."))
                .binding(true, () -> c.quoteBox, v -> { c.quoteBox = v; ConfigManager.save(); PingbackText.rebuild(); })
                .controller(BooleanControllerBuilder::create)
                .build();

        Option<Boolean> includeSender = Option.<Boolean>createBuilder()
                .name(Text.literal("Include sender name"))
                .description(desc("Prefix the reply with '@Sender:'."))
                .binding(true, () -> c.includeSenderInQuote, v -> { c.includeSenderInQuote = v; ConfigManager.save(); PingbackText.rebuild(); })
                .controller(BooleanControllerBuilder::create)
                .build();

        Option<PingbackConfig.MentionMode> mentions = Option.<PingbackConfig.MentionMode>createBuilder()
                .name(Text.literal("Mention stripping"))
                .description(desc("OFF: never touch mentions.\nLEADING_ONLY: remove @name(s) at the start (recommended).\nALL_MENTIONS: remove every @name.\nPRESERVE: keep the mention inside the quote."))
                .binding(PingbackConfig.MentionMode.LEADING_ONLY, () -> c.mentionMode, v -> { c.mentionMode = v; ConfigManager.save(); PingbackText.rebuild(); })
                .controller(o -> EnumControllerBuilder.create((Option<PingbackConfig.MentionMode>) o).enumClass(PingbackConfig.MentionMode.class))
                .build();

        Option<Boolean> realPlayers = Option.<Boolean>createBuilder()
                .name(Text.literal("Only strip real players"))
                .description(desc("@everyone / @channel are left untouched."))
                .binding(true, () -> c.onlyRealPlayers, v -> { c.onlyRealPlayers = v; ConfigManager.save(); PingbackText.rebuild(); })
                .controller(BooleanControllerBuilder::create)
                .build();

        Option<Boolean> stripPrefix = Option.<Boolean>createBuilder()
                .name(Text.literal("Strip rank/plugin prefixes"))
                .description(desc("Quotes only the real content of '[Admin] Name: hello'."))
                .binding(true, () -> c.stripNamePrefixes, v -> { c.stripNamePrefixes = v; ConfigManager.save(); PingbackText.rebuild(); })
                .controller(BooleanControllerBuilder::create)
                .build();

        Option<PingbackConfig.QuoteStyle> style = Option.<PingbackConfig.QuoteStyle>createBuilder()
                .name(Text.literal("Quote style"))
                .description(desc("BRACKET [ ]  |  GUILLEMET « »  |  PAREN ( )  |  NONE."))
                .binding(PingbackConfig.QuoteStyle.BRACKET, () -> c.quoteStyle, v -> { c.quoteStyle = v; ConfigManager.save(); PingbackText.rebuild(); })
                .controller(o -> EnumControllerBuilder.create((Option<PingbackConfig.QuoteStyle>) o).enumClass(PingbackConfig.QuoteStyle.class))
                .build();

        Option<String> arrow = Option.<String>createBuilder()
                .name(Text.translatable("text.pingback.arrow"))
                .description(OptionDescription.of(Text.translatable("pingback.tooltip.arrow")))
                .binding("--->", () -> c.arrow, v -> { c.arrow = v; ConfigManager.save(); PingbackText.rebuild(); })
                .controller(StringControllerBuilder::create)
                .build();

        Option<Integer> maxLen = Option.<Integer>createBuilder()
                .name(Text.literal("Max message length"))
                .description(desc("Vanilla servers cap chat at 256. Pingback shortens the quote instead of letting the server reject the reply."))
                .binding(250, () -> c.maxMessageLength, v -> { c.maxMessageLength = v; ConfigManager.save(); PingbackText.rebuild(); })
                .controller(o -> IntegerSliderControllerBuilder.create((Option<Integer>) o).range(40, 250).step(1))
                .build();

        Option<PingbackConfig.IndicatorMode> indicator = Option.<PingbackConfig.IndicatorMode>createBuilder()
                .name(Text.literal("Status indicator"))
                .description(desc("OFF / ACTIONBAR (above hotbar) / HUD (corner)."))
                .binding(PingbackConfig.IndicatorMode.ACTIONBAR, () -> c.indicatorMode, v -> { c.indicatorMode = v; ConfigManager.save(); PingbackText.rebuild(); })
                .controller(o -> EnumControllerBuilder.create((Option<PingbackConfig.IndicatorMode>) o).enumClass(PingbackConfig.IndicatorMode.class))
                .build();

        Option<Boolean> debug = Option.<Boolean>createBuilder()
                .name(Text.literal("Debug logging"))
                .description(desc("Writes logs/pingback-debug.log."))
                .binding(false, () -> c.debug, v -> { c.debug = v; ConfigManager.save(); PingbackText.rebuild(); })
                .controller(BooleanControllerBuilder::create)
                .build();

        ButtonOption reset = ButtonOption.createBuilder()
                .name(Text.literal("Reset to defaults"))
                .description(desc("Restore every setting to its default."))
                .action((yaclScreen, option) -> {
                    resetToDefaults(c);
                    ConfigManager.save();
                    PingbackText.rebuild();
                })
                .build();

        return YetAnotherConfigLib.createBuilder()
                .title(Text.literal("Pingback " + PingbackClient.version()))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("General"))
                        .tooltip(Text.literal("Keys: R reply, J cycle, B cancel, K toggle."))
                        .option(enabled)
                        .option(indicator)
                        .option(reset)
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Quote"))
                        .option(quoteLength)
                        .option(quoteBox)
                        .option(style)
                        .option(includeSender)
                        .option(arrow)
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Mentions"))
                        .option(mentions)
                        .option(realPlayers)
                        .option(stripPrefix)
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Safety & Debug"))
                        .option(maxLen)
                        .option(debug)
                        .build())
                .build()
                .generateScreen(parent);
    }

    private static void resetToDefaults(PingbackConfig c) {
        PingbackConfig d = new PingbackConfig();
        c.enabled = d.enabled;
        c.quoteLength = d.quoteLength;
        c.maxMessageLength = d.maxMessageLength;
        c.emptyQuoteMode = d.emptyQuoteMode;
        c.quoteBox = d.quoteBox;
        c.quoteStyle = d.quoteStyle;
        c.arrow = d.arrow;
        c.includeSenderInQuote = d.includeSenderInQuote;
        c.mentionMode = d.mentionMode;
        c.onlyRealPlayers = d.onlyRealPlayers;
        c.stripNamePrefixes = d.stripNamePrefixes;
        c.indicatorMode = d.indicatorMode;
        c.debug = d.debug;
    }

    private static OptionDescription desc(String text) {
        return OptionDescription.of(Text.literal(text));
    }
}
