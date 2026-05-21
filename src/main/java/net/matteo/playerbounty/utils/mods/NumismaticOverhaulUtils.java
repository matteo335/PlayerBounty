package net.matteo.playerbounty.utils.mods;

import static net.matteo.playerbounty.configs.NumismaticOverhaulConfig.*;
import static net.matteo.playerbounty.utils.GetValues.numismaticoverhaul;

import tallestred.numismaticoverhaul.currency.CurrencyResolver;

import net.minecraft.network.chat.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.RandomSource;

public class NumismaticOverhaulUtils {

    public static Double randomGain() {
        if (RandomGainMin.get().equals(RandomGainMax.get())) {
            return RandomGainMin.get();
        } else {
            return RandomSource.create().nextDouble() * (RandomGainMax.get() - RandomGainMin.get()) + RandomGainMin.get();
        }
    }

    public static Double randomLoss() {
        if (RandomLossMin.get().equals(RandomLossMax.get())) {
            return RandomLossMin.get();
        } else {
            return RandomSource.create().nextDouble() * (RandomLossMax.get() - RandomLossMin.get()) + RandomLossMin.get();
        }
    }

    public static Double randomGainMultiplier() {
        if (RandomGainMultiplierMin.get().equals(RandomGainMultiplierMax.get())) {
            return RandomGainMultiplierMin.get();
        } else {
            return RandomSource.create().nextDouble() * (RandomGainMultiplierMax.get() - RandomGainMultiplierMin.get()) + RandomGainMultiplierMin.get();
        }
    }

    public static Double randomLossMultiplier() {
        if (RandomLossMultiplierMin.get().equals(RandomGainMultiplierMax.get())) {
            return RandomLossMultiplierMin.get();
        } else {
            return RandomSource.create().nextDouble() * (RandomLossMultiplierMax.get() - RandomLossMultiplierMin.get()) + RandomLossMultiplierMin.get();
        }
    }

    public static MutableComponent NumismaticOverhaulDisplay(Player player) {
        MutableComponent component = Component.empty();

        long[] value = CurrencyResolver.splitValues(numismaticoverhaul.get(player.getUUID()));
        Style bold = Style.EMPTY.withBold(Bold.get());
        Style italic = Style.EMPTY.withItalic(Italic.get());
        Style underlined = Style.EMPTY.withUnderlined(Underlined.get());
        Style strikethrough = Style.EMPTY.withStrikethrough(Strikethrough.get());

        if (EnableGoldDisplay.get()) {
            MutableComponent gold = Component.literal(String.valueOf(value[2])).withStyle(bold).withStyle(italic).withStyle(underlined).withStyle(strikethrough);
            Style hoverEvent = Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("NumismaticOverhaul Gold Coin")));

            component.append(DisplayGold1.get());
            component.append(gold.withStyle(hoverEvent).withColor(GoldColor.get()));
            component.append(DisplayBronze2.get());
        }

        if (EnableSilverDisplay.get()) {
            MutableComponent silver = Component.literal(String.valueOf(value[1])).withStyle(bold).withStyle(italic).withStyle(underlined).withStyle(strikethrough);
            Style hoverEvent = Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("NumismaticOverhaul Silver Coin")));

            component.append(DisplaySilver1.get());
            component.append(silver.withStyle(hoverEvent).withColor(SilverColor.get()));
            component.append(DisplaySilver2.get());
        }

        if (EnableBronzeDisplay.get()) {
            MutableComponent bronze = Component.literal(String.valueOf(value[0]));
            Style hoverEvent = Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("NumismaticOverhaul Bronze Coin")));

            component.append(DisplayBronze1.get());
            component.append(bronze.withStyle(hoverEvent).withColor(BronzeColor.get()));
            component.append(DisplayBronze2.get());
        }

        return component;
    }
}
