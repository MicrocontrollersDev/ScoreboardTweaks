package dev.microcontrollers.scoreboardtweaks.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.gui.controllers.ColorController;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.Color;

public class ScoreboardTweaksConfig {
    public static final ConfigClassHandler<ScoreboardTweaksConfig> CONFIG = ConfigClassHandler.createBuilder(ScoreboardTweaksConfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("scoreboardtweaks.json"))
                    .build())
            .build();

    @SerialEntry public boolean removeScoreboard = false;
    @SerialEntry public boolean removeScoreboardInDebugHud = false;
//    @SerialEntry public float scale = 1F;
    @SerialEntry public boolean removeScore = false;
//    @SerialEntry public boolean removeOnlySequentialScore = true;
    @SerialEntry public Color scoreColor = new Color(16733525);
    @SerialEntry public Color headerColor = new Color(0, 0, 0, 102);
    @SerialEntry public Color bodyColor = new Color(0, 0, 0, 76);
    @SerialEntry public boolean headerShadow = false;
    @SerialEntry public boolean bodyShadow = false;
    @SerialEntry public boolean numberShadow = false;

    @SuppressWarnings("deprecation")
    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(CONFIG, ((defaults, config, builder) -> builder
                .title(Text.literal("Scoreboard Tweaks"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Scoreboard Tweaks"))
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("General"))
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Remove Scoreboard"))
                                        .description(OptionDescription.of(Text.of("Remove the scoreboard entirely.")))
                                        .binding(defaults.removeScoreboard, () -> config.removeScoreboard, newVal -> config.removeScoreboard = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Remove Scoreboard in Debug HUD"))
                                        .description(OptionDescription.of(Text.of("Remove the scoreboard when the debug / F3 menu is open.")))
                                        .binding(defaults.removeScoreboardInDebugHud, () -> config.removeScoreboardInDebugHud, newVal -> config.removeScoreboardInDebugHud = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
//                                .option(Option.createBuilder(float.class)
//                                        .name(Text.literal("Scoreboard Scale"))
//                                        .description(OptionDescription.of(Text.of("Scales the scoreboard by the specified amount.")))
//                                        .binding(1F, () -> config.scale, newVal -> config.scale = newVal)
//                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
//                                                .range(0.1F, 3F)
//                                                .step(0.1F))
//                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Remove Score"))
                                        .description(OptionDescription.of(Text.of("Remove the score entirely.")))
                                        .binding(defaults.removeScore, () -> config.removeScore, newVal -> config.removeScore = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
//                                .option(Option.createBuilder(boolean.class)
//                                        .name(Text.literal("Remove Only Sequential Score"))
//                                        .description(OptionDescription.of(Text.of("Remove the score only when the numbers are sequential. This means the numbers won't show when on, for example, Hypxiel lobbies where they only exist for ordering the components.")))
//                                        .binding(defaults.removeOnlySequentialScore, () -> config.removeOnlySequentialScore, newVal -> config.removeOnlySequentialScore = newVal)
//                                        .controller(TickBoxControllerBuilder::create)
//                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.literal("Number Color"))
                                        .description(OptionDescription.of(Text.of("Number color.")))
                                        .binding(defaults.scoreColor, () -> config.scoreColor, value -> config.scoreColor = value)
                                        .customController(opt -> new ColorController(opt, true))
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.literal("Header Color"))
                                        .description(OptionDescription.of(Text.of("Header color.")))
                                        .binding(defaults.headerColor, () -> config.headerColor, value -> config.headerColor = value)
                                        .customController(opt -> new ColorController(opt, true))
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.literal("Body Color"))
                                        .description(OptionDescription.of(Text.of("Body color.")))
                                        .binding(defaults.bodyColor, () -> config.bodyColor, value -> config.bodyColor = value)
                                        .customController(opt -> new ColorController(opt, true))
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Add Header Text Shadow"))
                                        .description(OptionDescription.of(Text.of("Add shadow to the header text.")))
                                        .binding(defaults.headerShadow, () -> config.headerShadow, newVal -> config.headerShadow = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Add Body Text Shadow"))
                                        .description(OptionDescription.of(Text.of("Add shadow to the body text.")))
                                        .binding(defaults.bodyShadow, () -> config.bodyShadow, newVal -> config.bodyShadow = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Add Number Text Shadow"))
                                        .description(OptionDescription.of(Text.of("Add shadow to the number text.")))
                                        .binding(defaults.numberShadow, () -> config.numberShadow, newVal -> config.numberShadow = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .build())
        )).generateScreen(parent);
    }

}