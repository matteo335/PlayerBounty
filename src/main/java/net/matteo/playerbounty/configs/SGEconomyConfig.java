package net.matteo.playerbounty.configs;

import net.neoforged.neoforge.common.ModConfigSpec;

public class SGEconomyConfig {

    public static ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

    public static ModConfigSpec.BooleanValue System;

    public static ModConfigSpec.BooleanValue EnableDisplay;
    public static ModConfigSpec.ConfigValue<Integer> Color;

    public static ModConfigSpec.BooleanValue Bold;
    public static ModConfigSpec.BooleanValue Italic;
    public static ModConfigSpec.BooleanValue Underlined;
    public static ModConfigSpec.BooleanValue Strikethrough;

    public static ModConfigSpec.ConfigValue<String> Display1;
    public static ModConfigSpec.ConfigValue<String> Display2;

    public static ModConfigSpec.ConfigValue<Double> Gain;
    public static ModConfigSpec.ConfigValue<Double> Loss;

    public static ModConfigSpec.DoubleValue ClaimMultiplier;
    public static ModConfigSpec.DoubleValue TargetMultiplier;
    public static ModConfigSpec.DoubleValue KillerMultiplier;

    public static ModConfigSpec.DoubleValue RandomGainMin;
    public static ModConfigSpec.DoubleValue RandomGainMax;
    public static ModConfigSpec.DoubleValue RandomLossMin;
    public static ModConfigSpec.DoubleValue RandomLossMax;

    public static ModConfigSpec.DoubleValue RandomGainMultiplierMin;
    public static ModConfigSpec.DoubleValue RandomGainMultiplierMax;
    public static ModConfigSpec.DoubleValue RandomLossMultiplierMin;
    public static ModConfigSpec.DoubleValue RandomLossMultiplierMax;

    static {
        builder.comment("""
                Make sure you do the calculations correctly, you can use the link below to do the math yourself.
                
                Target is calculated like this: your Bounty * (Target-Multiplier + RandomLossMultiplier) - (LossOnDeath + RandomLoss)
                
                Killer is calculated like this: your Bounty = (BountyTarget * Claim-Multiplier) + (BountyKiller * (Killer-Multiplier + RandomGainMultiplier)) + (GainOnKilling + RandomLoss)
                
                There is no LossCompleteBounty in this config, because it is already present in the SGEconomy-API config.
                
                For colors, pick a RGB color from the first page and copy the R, G, and B, numbers. Then put these numbers inside the second page to get a Decimal RGB Color
                https://www.rapidtables.com/web/color/RGB_Color.html
                https://www.checkyourmath.com/convert/color/rgb_decimal.php
                
                 https://onlinegdb.com/BCyy-0Pi-Q
                """);

        System = builder.define("Enable the SG Economy compat", false);
        EnableDisplay = builder.define("Display the balance in the player name", true);
        Color = builder.define("RGB Decimal color of the balance in chat", 16733695);

        Bold = builder.define("Make the color bold", true);
        Italic = builder.define("Make the color italic", false);
        Underlined = builder.define("Underline the color", false);
        Strikethrough = builder.define("Strikethrough the color", false);

        Display1 = builder.define("Formatting Codes before the coin display", " [$");
        Display2 = builder.define("Formatting Codes after the coin display", "§r]");

        Gain = builder.define("How much coins you gain after killing another player", 10.0);
        Loss = builder.define("How much coins you loss after being killed", 10.0);

        ClaimMultiplier = builder.defineInRange("Claim-Multiplier, how much you take from your victim's balance 1 = 100%", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        KillerMultiplier = builder.defineInRange("Killer-Multiplier multiply from your own balance everytime you kill someone 1 = 100%", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        TargetMultiplier = builder.defineInRange("Target-Multiplier, how much is divided from your balance when you get killed 1 = 100", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);

        RandomGainMin = builder.defineInRange("Minimum Random Coin Gain", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        RandomGainMax = builder.defineInRange("Maximum Random Coin Gain", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        RandomLossMin = builder.defineInRange("Minimum Random Coin Loss", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        RandomLossMax = builder.defineInRange("Maximum Random Coin Loss", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);

        RandomGainMultiplierMin = builder.defineInRange("Minimum Random Coin Gain Multiplier 1 = 100%", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        RandomGainMultiplierMax = builder.defineInRange("Maximum Random Coin Gain Multiplier 1 = 100%", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        RandomLossMultiplierMin = builder.defineInRange("Minimum Random Coin Loss Multiplier 1 = 100%", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        RandomLossMultiplierMax = builder.defineInRange("Maximum Random Coin loss Multiplier 1 = 100%", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
    }
}
