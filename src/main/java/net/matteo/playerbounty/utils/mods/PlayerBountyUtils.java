package net.matteo.playerbounty.utils.mods;

import static net.matteo.playerbounty.configs.Config.*;
import net.matteo.playerbounty.utils.GetValues;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.*;

public class PlayerBountyUtils {

    public static Double randomGain() {
        if (RandomGainMin.get().equals(RandomGainMax.get())) {
            return RandomGainMin.get();
        } else {
            return RandomSource.create().nextDouble() * (RandomGainMax.get() - RandomGainMin.get()) + RandomGainMin.get();
        }
    }

    public static Double randomLoss() {
        if (RandomLossMin.get().equals(RandomLossMin.get())) {
            return RandomLossMin.get();
        } else {
            return RandomSource.create().nextDouble() * (RandomLossMax.get() - RandomLossMin.get()) + RandomLossMin.get();
        }
    }

    public static Double randomGainMultiplier() {
        if (RandomGainMultiplierMax.get().equals(RandomGainMultiplierMin.get())) {
            return RandomGainMultiplierMin.get();
        } else {
            return RandomSource.create().nextDouble() * (RandomGainMultiplierMax.get() - RandomGainMultiplierMin.get()) + RandomGainMultiplierMin.get();
        }
    }

    public static Double randomLossMultiplier() {
        if (RandomLossMultiplierMin.get().equals(RandomLossMultiplierMax.get())) {
            return RandomLossMultiplierMin.get();
        } else {
            return RandomSource.create().nextDouble() * (RandomLossMultiplierMax.get() - RandomLossMultiplierMin.get()) + RandomLossMin.get();
        }
    }

    public static MutableComponent DefaultDisplay(Player player) {
        MutableComponent component = Component.empty();
        Style bold = Style.EMPTY.withBold(Bold.get());
        Style italic = Style.EMPTY.withItalic(Italic.get());
        Style underlined = Style.EMPTY.withUnderlined(Underlined.get());
        Style strikethrough = Style.EMPTY.withStrikethrough(Strikethrough.get());
        Style hoverEvent = Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("PlayerBounty")));

        String bountyString = String.valueOf(GetValues.playerbounty.get(player.getUUID()));
        MutableComponent bounty = Component.literal(bountyString).withStyle(bold).withStyle(italic).withStyle(underlined).withStyle(strikethrough);

        component.append(Display1.get());
        component.append(bounty.withColor(Color.get()).withStyle(hoverEvent));
        component.append(Display2.get());

        return component;
    }
}
