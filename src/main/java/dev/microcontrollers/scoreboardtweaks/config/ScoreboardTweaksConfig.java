package dev.microcontrollers.scoreboardtweaks.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
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
    @SerialEntry public float scale = 1F;
    @SerialEntry public int maxLines = 15;
    @SerialEntry public boolean removeScore = false;
    @SerialEntry public boolean removeOnlySequentialScore = true;
    @SerialEntry public Color scoreColor = new Color(16733525);
    @SerialEntry public Color headerColor = new Color(0, 0, 0, 102);
    @SerialEntry public Color bodyColor = new Color(0, 0, 0, 76);
    @SerialEntry public boolean headerShadow = false;
    @SerialEntry public boolean bodyShadow = false;
    @SerialEntry public boolean numberShadow = false;
    @SerialEntry public int moveHorizontally = 0;
    @SerialEntry public int moveVertically = 0;

    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(CONFIG, ((defaults, config, builder) -> builder
                .title(Text.translatable("scoreboard-tweaks.scoreboard-tweaks"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("scoreboard-tweaks.scoreboard-tweaks"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("scoreboard-tweaks.remove-scoreboard"))
                                .description(OptionDescription.of(Text.translatable("scoreboard-tweaks.remove-scoreboard.description")))
                                .binding(defaults.removeScoreboard, () -> config.removeScoreboard, newVal -> config.removeScoreboard = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("scoreboard-tweaks.remove-scoreboard-in-debug-hud"))
                                .description(OptionDescription.of(Text.translatable("scoreboard-tweaks.remove-scoreboard-in-debug-hud.description")))
                                .binding(defaults.removeScoreboardInDebugHud, () -> config.removeScoreboardInDebugHud, newVal -> config.removeScoreboardInDebugHud = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Float>createBuilder()
                                .name(Text.translatable("scoreboard-tweaks.scoreboard-scale"))
                                .description(OptionDescription.of(Text.translatable("scoreboard-tweaks.scoreboard-scale.description")))
                                .binding(1F, () -> config.scale, newVal -> config.scale = newVal)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .range(0.1F, 3F)
                                        .step(0.1F))
                                .build())
                        .option(Option.<Integer>createBuilder()
                                .name(Text.translatable("scoreboard-tweaks.max-lines"))
                                .description(OptionDescription.of(Text.translatable("scoreboard-tweaks.max-lines.description")))
                                .binding(15, () -> config.maxLines, newVal -> config.maxLines = newVal)
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                        .range(1, 30)
                                        .step(1))
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("scoreboard-tweaks.remove-score"))
                                .description(OptionDescription.of(Text.translatable("scoreboard-tweaks.remove-score.description")))
                                .binding(defaults.removeScore, () -> config.removeScore, newVal -> config.removeScore = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("scoreboard-tweaks.remove-only-sequential-score"))
                                .description(OptionDescription.of(Text.translatable("scoreboard-tweaks.remove-only-sequential-score.description")))
                                .binding(defaults.removeOnlySequentialScore, () -> config.removeOnlySequentialScore, newVal -> config.removeOnlySequentialScore = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Color>createBuilder()
                                .name(Text.translatable("scoreboard-tweaks.number-color"))
                                .description(OptionDescription.of(Text.translatable("scoreboard-tweaks.number-color.description")))
                                .binding(defaults.scoreColor, () -> config.scoreColor, value -> config.scoreColor = value)
                                .customController(opt -> new ColorController(opt, false))
                                .build())
                        .option(Option.<Color>createBuilder()
                                .name(Text.translatable("scoreboard-tweaks.header-color"))
                                .description(OptionDescription.of(Text.translatable("scoreboard-tweaks.header-color.description")))
                                .binding(defaults.headerColor, () -> config.headerColor, value -> config.headerColor = value)
                                .customController(opt -> new ColorController(opt, true))
                                .build())
                        .option(Option.<Color>createBuilder()
                                .name(Text.translatable("scoreboard-tweaks.body-color"))
                                .description(OptionDescription.of(Text.translatable("scoreboard-tweaks.body-color.description")))
                                .binding(defaults.bodyColor, () -> config.bodyColor, value -> config.bodyColor = value)
                                .customController(opt -> new ColorController(opt, true))
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("scoreboard-tweaks.add-header-text-shadow"))
                                .description(OptionDescription.of(Text.translatable("scoreboard-tweaks.add-header-text-shadow.description")))
                                .binding(defaults.headerShadow, () -> config.headerShadow, newVal -> config.headerShadow = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("scoreboard-tweaks.add-body-text-shadow"))
                                .description(OptionDescription.of(Text.translatable("scoreboard-tweaks.add-body-text-shadow.description")))
                                .binding(defaults.bodyShadow, () -> config.bodyShadow, newVal -> config.bodyShadow = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("scoreboard-tweaks.add-number-text-shadow"))
                                .description(OptionDescription.of(Text.translatable("scoreboard-tweaks.add-number-text-shadow.description")))
                                .binding(defaults.numberShadow, () -> config.numberShadow, newVal -> config.numberShadow = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Integer>createBuilder()
                                .name(Text.translatable("scoreboard-tweaks.move-horizontally"))
                                .description(OptionDescription.of(Text.translatable("scoreboard-tweaks.move-horizontally.description")))
                                .binding(0, () -> config.moveVertically, newVal -> config.moveVertically = newVal)
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                        .range(0, 1200)
                                        .step(1))
                                .build())
                        .option(Option.<Integer>createBuilder()
                                .name(Text.translatable("scoreboard-tweaks.move-vertically"))
                                .description(OptionDescription.of(Text.translatable("scoreboard-tweaks.move-vertically.description")))
                                .binding(0, () -> config.moveHorizontally, newVal -> config.moveHorizontally = newVal)
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                        .range(-400, 400)
                                        .step(1))
                                .build())
                        .build())
        )).generateScreen(parent);
    }

}