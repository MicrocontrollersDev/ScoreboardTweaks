package dev.microcontrollers.scoreboardtweaks.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.microcontrollers.scoreboardtweaks.config.ScoreboardTweaksConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.scoreboard.*;
import net.minecraft.scoreboard.number.BlankNumberFormat;
import net.minecraft.scoreboard.number.NumberFormat;
import net.minecraft.scoreboard.number.StyledNumberFormat;
import net.minecraft.text.Style;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow
    @Final
    private DebugHud debugHud;
    //#if MC == 1.20.4
    //$$ @Shadow
    //$$ private int scaledWidth;
    //$$ @Shadow
    //$$ private int scaledHeight;
    //#endif

    @Inject(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", at = @At("HEAD"), cancellable = true)
    private void cancelScoreboardRendering(DrawContext context, ScoreboardObjective objective, CallbackInfo ci) {
        if (ScoreboardTweaksConfig.CONFIG.instance().removeScoreboard) ci.cancel();
        //#if MC >= 1.20.4
        if (ScoreboardTweaksConfig.CONFIG.instance().removeScoreboardInDebugHud && this.debugHud.shouldShowDebugHud()) {
        //#else
        //$$ if (ScoreboardTweaksConfig.CONFIG.instance().removeScoreboardInDebugHud && MinecraftClient.getInstance().options.debugEnabled) {
        //#endif
            ci.cancel();
        }
    }

    @Inject(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", at = @At("HEAD"))
    private void scaleScoreboardPre(DrawContext context, ScoreboardObjective objective, CallbackInfo ci) {
        context.getMatrices().push();
        context.getMatrices().scale(ScoreboardTweaksConfig.CONFIG.instance().scale, ScoreboardTweaksConfig.CONFIG.instance().scale, ScoreboardTweaksConfig.CONFIG.instance().scale);
        //#if MC == 1.20.4
        //$$ this.scaledWidth = (int) (this.scaledWidth * 1 / ScoreboardTweaksConfig.CONFIG.instance().scale);
        //$$ this.scaledHeight = (int) (this.scaledHeight * 1 / ScoreboardTweaksConfig.CONFIG.instance().scale);
        //#endif
    }

    @Inject(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", at = @At("TAIL"))
    private void scaleScoreboardPost(DrawContext context, ScoreboardObjective objective, CallbackInfo ci) {
        context.getMatrices().pop();
        //#if MC == 1.20.4
        //$$ this.scaledWidth = (int) (this.scaledWidth * ScoreboardTweaksConfig.CONFIG.instance().scale);
        //$$ this.scaledHeight = (int) (this.scaledHeight * ScoreboardTweaksConfig.CONFIG.instance().scale);
        //#endif
    }

    //#if MC >= 1.20.6
    @WrapOperation(method = "method_55440", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowWidth()I"))
    private int fixWidth(DrawContext instance, Operation<Integer> original) {
        return (int) (instance.getScaledWindowWidth() / ScoreboardTweaksConfig.CONFIG.instance().scale);
    }

    @WrapOperation(method = "method_55440", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowHeight()I"))
    private int fixHeight(DrawContext instance, Operation<Integer> original) {
        return (int) (instance.getScaledWindowHeight() / ScoreboardTweaksConfig.CONFIG.instance().scale);
    }
    //#endif

    @ModifyExpressionValue(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", at = @At(value = "CONSTANT", args = "longValue=15"))
    private long changeMaxLineCount(long original) {
        return ScoreboardTweaksConfig.CONFIG.instance().maxLines;
    }

    @ModifyExpressionValue(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/scoreboard/ScoreboardObjective;getNumberFormatOr(Lnet/minecraft/scoreboard/number/NumberFormat;)Lnet/minecraft/scoreboard/number/NumberFormat;"))
    private NumberFormat changeNumberColor(NumberFormat original) {
        return new StyledNumberFormat(Style.EMPTY.withColor(ScoreboardTweaksConfig.CONFIG.instance().scoreColor.getRGB()));
    }

    @ModifyVariable(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", at = @At(value = "STORE"))
    private NumberFormat removeScore(NumberFormat numberFormat, DrawContext context, ScoreboardObjective objective) {
        if ((ScoreboardTweaksConfig.CONFIG.instance().removeOnlySequentialScore && !isNonConsecutive(objective)) || ScoreboardTweaksConfig.CONFIG.instance().removeScore)
            return BlankNumberFormat.INSTANCE;
        else
            return numberFormat;
    }

    @Unique
    private static boolean isNonConsecutive(ScoreboardObjective objective) {
        int[] scorePoints = objective.getScoreboard().getScoreboardEntries(objective).stream().mapToInt(ScoreboardEntry::value).limit(ScoreboardTweaksConfig.CONFIG.instance().maxLines).sorted().toArray();
        if (scorePoints.length > 1) {
            for (int line = 1; line < scorePoints.length; line++) {
                if (scorePoints[line] != scorePoints[line - 1] + 1) { // check if the score is just 1 higher than previous
                    return true;
                }
            }
        }
        return false;
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

    @ModifyVariable(method = "method_55440", at = @At("STORE"), name = "m")
    private int pos1(int value) {
        return value - ScoreboardTweaksConfig.CONFIG.instance().moveHorizontally;
    }

    @ModifyVariable(method = "method_55440", at = @At("STORE"), name = "o")
    private int pos2(int value) {
        return value - ScoreboardTweaksConfig.CONFIG.instance().moveVertically;
    }

    @ModifyVariable(method = "method_55440", at = @At("STORE"), name = "p")
    private int pos3(int value) {
        return value - ScoreboardTweaksConfig.CONFIG.instance().moveVertically;
    }
}
