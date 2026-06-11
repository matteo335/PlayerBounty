package net.matteo.playerbounty.utils.mods;

import static net.matteo.playerbounty.configs.CreateNumismaticsConfig.*;
import static net.matteo.playerbounty.utils.GetValues.create_numismatics;

import dev.ithundxr.createnumismatics.content.backend.BankSavedData;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.util.RandomSource;

public class CreateNumismaticsUtils {

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
            return RandomGainMultiplierMax.get();
        } else {
            return RandomSource.create().nextDouble() * (RandomGainMultiplierMax.get() - RandomGainMultiplierMin.get()) + RandomGainMultiplierMin.get();
        }
    }

    public static Double randomLossMultiplier() {
        if (RandomLossMultiplierMin.get().equals(RandomLossMultiplierMax.get())) {
            return RandomLossMultiplierMax.get();
        } else {
            return RandomSource.create().nextDouble() * (RandomLossMultiplierMax.get() - RandomLossMultiplierMin.get()) + RandomLossMultiplierMin.get();
        }
    }

    @SuppressWarnings("deprecation")
    public static Integer spurValue(Player player, boolean mathElseDisplay) {
        int spurs = BankSavedData.load(player.getServer()).getAccounts().get(player.getUUID()).getBalance();

        if ((InventoryMath.get() && mathElseDisplay) || (InventoryDisplay.get() && !mathElseDisplay)) {
            for (ItemStack stack : player.getInventory().items) {
                ResourceLocation location = BuiltInRegistries.ITEM.getKey(stack.getItem());

                if ("numismatics".equals(location.getNamespace())) {
                    switch (location.getPath()) {
                        case "sun" -> spurs += stack.getCount() * 4096;
                        case "crown" -> spurs += stack.getCount() * 512;
                        case "cog" -> spurs += stack.getCount() * 64;
                        case "sprocket" -> spurs += stack.getCount() * 16;
                        case "bevel" -> spurs += stack.getCount() * 8;
                        case "spur" -> spurs += stack.getCount();
                    }
                }
            }

            for (int slot = 0; slot < player.getEnderChestInventory().getContainerSize(); slot++) {
                ItemStack stack = player.getEnderChestInventory().getItem(slot);
                ResourceLocation location = BuiltInRegistries.ITEM.getKey(stack.getItem());

                if ("numismatics".equals(location.getNamespace())) {
                    switch (location.getPath()) {
                        case "sun" -> spurs += stack.getCount() * 4096;
                        case "crown" -> spurs += stack.getCount() * 512;
                        case "cog" -> spurs += stack.getCount() * 64;
                        case "sprocket" -> spurs += stack.getCount() * 16;
                        case "bevel" -> spurs += stack.getCount() * 8;
                        case "spur" -> spurs += stack.getCount();
                    }
                }
            }
        }

        return spurs;
    }


    public static MutableComponent CreateNumismaticsDisplay(Player player) {
        MutableComponent component = Component.empty();
        String hoverString = InventoryDisplay.get() ? "Create Numismatics Cogs in inventory, ender chest, or bank" : "Create Numismatics Cogs in bank only";
        Style bold = Style.EMPTY.withBold(Bold.get());
        Style italic = Style.EMPTY.withItalic(Italic.get());
        Style underlined = Style.EMPTY.withUnderlined(Underlined.get());
        Style strikethrough = Style.EMPTY.withStrikethrough(Strikethrough.get());
        Style color = Style.EMPTY.withColor(Color.get());
        Style hoverEvent = Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(hoverString)));
        int coins = create_numismatics.get(player.getUUID());
        MutableComponent balance = Component.literal(String.valueOf(coins));

        component.append(Display1.get());
        component.append(balance.withStyle(bold).withStyle(italic).withStyle(underlined).withStyle(strikethrough).withStyle(color).withStyle(hoverEvent));
        component.append(Display2.get());
        return component;
    }
}
