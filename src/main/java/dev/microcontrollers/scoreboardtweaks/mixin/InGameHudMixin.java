package dev.microcontrollers.scoreboardtweaks.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.microcontrollers.scoreboardtweaks.config.ScoreboardTweaksConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.scoreboard.*;
import net.minecraft.scoreboard.number.NumberFormat;
import net.minecraft.scoreboard.number.StyledNumberFormat;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// TODO: remember to do multiversion fixes
@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Unique
    MinecraftClient client = MinecraftClient.getInstance();

    @Inject(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", at = @At("HEAD"), cancellable = true)
    private void cancelScoreboardRendering(DrawContext context, ScoreboardObjective objective, CallbackInfo ci) {
        if (ScoreboardTweaksConfig.CONFIG.instance().removeScoreboard) ci.cancel();
        //#if MC >= 1.20.4
        if (ScoreboardTweaksConfig.CONFIG.instance().removeScoreboardInDebugHud && client.getDebugHud().shouldShowDebugHud()) {
        //#else
        //$$ if (ScoreboardTweaksConfig.CONFIG.instance().removeScoreboardInDebugHud && MinecraftClient.getInstance().options.debugEnabled) {
        //#endif
            ci.cancel();
        }
    }

    @ModifyExpressionValue(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/scoreboard/ScoreboardObjective;getNumberFormatOr(Lnet/minecraft/scoreboard/number/NumberFormat;)Lnet/minecraft/scoreboard/number/NumberFormat;"))
    private NumberFormat changeNumberColor(NumberFormat original) {
        return new StyledNumberFormat(Style.EMPTY.withColor(ScoreboardTweaksConfig.CONFIG.instance().scoreColor.getRGB()));
    }

    // TODO: sequential score check
    @WrapWithCondition(method = "method_55440", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIZ)I", ordinal = 2))
    private boolean removeScore(DrawContext instance, TextRenderer textRenderer, Text text, int x, int y, int color, boolean shadow) {
        return !ScoreboardTweaksConfig.CONFIG.instance().removeScore;
    }

    @ModifyArg(method = "method_55440", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V", ordinal = 0), index = 4)
    private int changeHeaderColor(int color) {
        return ScoreboardTweaksConfig.CONFIG.instance().headerColor.getRGB();
    }

    @ModifyArg(method = "method_55440", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V", ordinal = 1), index = 4)
    private int changeBodyColor(int color) {
        return ScoreboardTweaksConfig.CONFIG.instance().bodyColor.getRGB();
    }

    @ModifyArg(method = "method_55440", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIZ)I", ordinal = 0), index = 5)
    private boolean headerShadow(boolean shadow) {
        return ScoreboardTweaksConfig.CONFIG.instance().headerShadow;
    }

    @ModifyArg(method = "method_55440", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIZ)I", ordinal = 1), index = 5)
    private boolean bodyShadow(boolean shadow) {
        return ScoreboardTweaksConfig.CONFIG.instance().bodyShadow;
    }

    @ModifyArg(method = "method_55440", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIZ)I", ordinal = 2), index = 5)
    private boolean numberShadow(boolean shadow) {
        return ScoreboardTweaksConfig.CONFIG.instance().numberShadow;
    }

}
