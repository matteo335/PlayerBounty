package net.matteo.playerbounty.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public class NumismaticOverhaulConfig {

    public static ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec.BooleanValue System;
    public static ForgeConfigSpec.BooleanValue EnableDisplay;

    public static ForgeConfigSpec.BooleanValue EnableBronzeDisplay;
    public static ForgeConfigSpec.BooleanValue EnableSilverDisplay;
    public static ForgeConfigSpec.BooleanValue EnableGoldDisplay;

    public static ForgeConfigSpec.BooleanValue Bold;
    public static ForgeConfigSpec.BooleanValue Italic;
    public static ForgeConfigSpec.BooleanValue Underlined;
    public static ForgeConfigSpec.BooleanValue Strikethrough;

    public static ForgeConfigSpec.ConfigValue<Integer> BronzeColor;
    public static ForgeConfigSpec.ConfigValue<String> DisplayBronze1;
    public static ForgeConfigSpec.ConfigValue<String> DisplayBronze2;

    public static ForgeConfigSpec.ConfigValue<Integer> SilverColor;
    public static ForgeConfigSpec.ConfigValue<String> DisplaySilver1;
    public static ForgeConfigSpec.ConfigValue<String> DisplaySilver2;

    public static ForgeConfigSpec.ConfigValue<Integer> GoldColor;
    public static ForgeConfigSpec.ConfigValue<String> DisplayGold1;
    public static ForgeConfigSpec.ConfigValue<String> DisplayGold2;

    public static ForgeConfigSpec.ConfigValue<Double> Gain;
    public static ForgeConfigSpec.ConfigValue<Double> Loss;

    public static ForgeConfigSpec.BooleanValue CompleteLoss;
    public static ForgeConfigSpec.ConfigValue<Double> ClaimMultiplier;
    public static ForgeConfigSpec.ConfigValue<Double> KillerMultiplier;
    public static ForgeConfigSpec.ConfigValue<Double> TargetMultiplier;

    public static ForgeConfigSpec.ConfigValue<Double> RandomGainMin;
    public static ForgeConfigSpec.ConfigValue<Double> RandomGainMax;
    public static ForgeConfigSpec.ConfigValue<Double> RandomLossMin;
    public static ForgeConfigSpec.ConfigValue<Double> RandomLossMax;

    public static ForgeConfigSpec.ConfigValue<Double> RandomGainMultiplierMin;
    public static ForgeConfigSpec.ConfigValue<Double> RandomGainMultiplierMax;
    public static ForgeConfigSpec.ConfigValue<Double> RandomLossMultiplierMin;
    public static ForgeConfigSpec.ConfigValue<Double> RandomLossMultiplierMax;

    static {
        builder.comment("""
                Make sure you do the calculations correctly, you can use the link below to do the math yourself.
                
                Killer is calculated like this: Your Bounty + (Gain On Killing + Random Gain) + (Your Bounty * (Killer-Multiplier + Random Gain Multiplier)) + (Target Bounty * Claim-Multiplier)
                
                Target is calculated like this: Your Bounty - (Loss On Death + Random Loss) + (Your Bounty * (Target-Multiplier + Random Loss Multiplier)
                
                If LossCompleteBountyOnDeath is true: Your Bounty = (-LossOnDeath - randomLoss) + (Bounty * (Target-Multiplier + RandomLossMultiplier))
                
                For colors, pick an RGB color from the first page and copy the R, G, and B, numbers. Then put these numbers inside the second page to get a Decimal RGB Color
                https://www.rapidtables.com/web/color/RGB_Color.html
                https://www.checkyourmath.com/convert/color/rgb_decimal.php
                
                Use this link to calculate maths: https://onlinegdb.com/BCyy-0Pi-Q
                """);

        System = builder.define("Enable the NumismaticOverhaul compat", false);

        EnableDisplay = builder.define("Enable the display", true);
        EnableBronzeDisplay = builder.define("Enable Bronze Display", true);
        EnableSilverDisplay = builder.define("Enable Silver Display", true);
        EnableGoldDisplay = builder.define("Enable Gold Display", true);

        Bold = builder.define("Write the values in bold", true);
        Italic = builder.define("Write the values in italic", false);
        Underlined = builder.define("Underline the values", false);
        Strikethrough = builder.define("Strike through the values", false);

        BronzeColor = builder.define("Bronze Decimal RGB Color", 11426620);
        DisplayBronze1 = builder.define("Bronze Display1", " [");
        DisplayBronze2 = builder.define("Bronze Display2", "]");

        SilverColor = builder.define("Silver Decimal RGB Color", 6386036);
        DisplaySilver1 = builder.define("Silver Display1", " [§l");
        DisplaySilver2 = builder.define("Silver Display2", "]§r");

        GoldColor = builder.define("Gold Decimal RGB Color", 12425272);
        DisplayGold1 = builder.define("Gold Display1", " [§l");
        DisplayGold2 = builder.define("Gold Display2", "]§r");

        CompleteLoss = builder.define("Loss the equivalent of your entire bounty on death", false);
        Gain = builder.define("GainOnKilling 10 = 10 bronze, 101 = one silver and one bronze, 10101 = one gold, one silver, one bronze", 100.0);
        Loss = builder.define("LossOnKilling", 10.0);

        ClaimMultiplier = builder.define("Claim-Multiplier, how much you take from your victim's balance 1 = 100%", 0.0);
        KillerMultiplier = builder.define("Killer-Multiplier, multiply from your own balance everytime you kill someone 1 = 100%", 0.0);
        TargetMultiplier = builder.define("Target-Multiplier, how much is divided from your balance when you get killed = 1 = 100%", 0.0);

        RandomGainMin = builder.define("Random Gain Minimum", 0.0);
        RandomGainMax = builder.define("Random Gain Maximum", 0.0);
        RandomLossMin = builder.define("Random Loss Minimum", 0.0);
        RandomLossMax = builder.define("Random Loss Maximum", 0.0);

        RandomGainMultiplierMin = builder.define("Random Gain Multiplier Minimum", 0.0);
        RandomGainMultiplierMax = builder.define("Random Gain Multiplier Maximum", 0.0);
        RandomLossMultiplierMin = builder.define("Random Loss Multiplier Minimum", 0.0);
        RandomLossMultiplierMax = builder.define("Random Loss Multiplier Maximum", 0.0);
    }
}
