package dev.microcontrollers.scoreboardtweaks.mixin;

import dev.microcontrollers.scoreboardtweaks.config.ScoreboardTweaksConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "renderScoreboardSidebar", at = @At("HEAD"), cancellable = true)
    private void cancelScoreboardRendering(DrawContext context, ScoreboardObjective objective, CallbackInfo ci) {
        if (ScoreboardTweaksConfig.CONFIG.instance().removeScoreboard) ci.cancel();
    }
}
