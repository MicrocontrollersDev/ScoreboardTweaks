package dev.microcontrollers.scoreboardtweaks;

import dev.microcontrollers.scoreboardtweaks.config.ScoreboardTweaksConfig;
import net.fabricmc.api.ModInitializer;

public class ScoreboardTweaks implements ModInitializer {
	@Override
	public void onInitialize() {
		ScoreboardTweaksConfig.CONFIG.load();
	}

}