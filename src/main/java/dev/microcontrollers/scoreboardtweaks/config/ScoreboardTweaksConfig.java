package dev.microcontrollers.scoreboardtweaks.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ScoreboardTweaksConfig {
    public static final ConfigClassHandler<ScoreboardTweaksConfig> CONFIG = ConfigClassHandler.createBuilder(ScoreboardTweaksConfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("scoreboardtweaks.json"))
                    .build())
            .build();

    @SerialEntry public boolean removeScoreboard = false;

    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(CONFIG, ((defaults, config, builder) -> builder
                .title(Text.literal("Scoreboard Tweaks"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Scoreboard Tweaks"))

                        // Visual

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("General"))
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Remove Scoreboard"))
                                        .description(OptionDescription.of(Text.of("Remove the scoreboard entirely.")))
                                        .binding(defaults.removeScoreboard, () -> config.removeScoreboard, newVal -> config.removeScoreboard = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .build())
        )).generateScreen(parent);
    }

}