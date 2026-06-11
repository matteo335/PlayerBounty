package net.matteo.playerbounty.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {

    public static ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec.BooleanValue StartupWarning;

    public static ForgeConfigSpec.BooleanValue System;

    public static ForgeConfigSpec.BooleanValue EnableDisplay;
    public static ForgeConfigSpec.ConfigValue<Integer> DisplayCooldown;
    public static ForgeConfigSpec.ConfigValue<Integer> Color;

    public static ForgeConfigSpec.BooleanValue Bold;
    public static ForgeConfigSpec.BooleanValue Italic;
    public static ForgeConfigSpec.BooleanValue Underlined;
    public static ForgeConfigSpec.BooleanValue Strikethrough;

    public static ForgeConfigSpec.ConfigValue<String> Display1;
    public static ForgeConfigSpec.ConfigValue<String> Display2;

    public static ForgeConfigSpec.BooleanValue LoseCompleteBountyOnDeath;

    public static ForgeConfigSpec.ConfigValue<Double> MinimumValue;
    public static ForgeConfigSpec.ConfigValue<Double> MaximumValue;

    public static ForgeConfigSpec.ConfigValue<Double> Gain;
    public static ForgeConfigSpec.ConfigValue<Double> Loss;

    public static ForgeConfigSpec.ConfigValue<Double> KillerMultiplier;
    public static ForgeConfigSpec.ConfigValue<Double> ClaimMultiplier;
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
                
                For calculations, check there! https://onlinegdb.com/BCyy-0Pi-Q
                """);

        StartupWarning = builder.define("Enable the warning in the server start", true);

        System = builder.define("Enable the default maths, disable the display to prevent the name change", true);

        EnableDisplay = builder.define("Enable the display", true);
        DisplayCooldown = builder.define("How much ticks before displays get updated", 100);
        Color = builder.define("RGB Decimal color of the bounty in chat", 16755200);

        Bold = builder.define("Make the color bold", true);
        Italic = builder.define("Make the color italic", false);
        Underlined = builder.define("Underline the color", false);
        Strikethrough = builder.define("Strikethrough the color", false);

        Display1 = builder.define("Text before the value", " [$");
        Display2 = builder.define("Text after the value", "]");

        LoseCompleteBountyOnDeath = builder.define("Loss the equivalent of your entire bounty on death", false);
        MinimumValue = builder.define("Bounty Minimum Value", -Double.MAX_VALUE);
        MaximumValue = builder.define("Bounty Maximum Value", Double.MAX_VALUE);

        Gain = builder.define("Bounty Gain On Killing", 10.0);
        Loss = builder.define("Bounty Loss On Death", 10.0);

        KillerMultiplier = builder.define("Killer-Multiplier multiply from your own balance everytime you kill someone 1 = 100%", 0.0);
        ClaimMultiplier = builder.define("Claim-Multiplier, how much you take from your victim's balance 1 = 100%", 0.0);
        TargetMultiplier = builder.define("Target-Multiplier, how much is divided from your balance when you get killed 1 = 100%", 0.0);

        RandomGainMin = builder.define("Minimum Random Gain", 0.0);
        RandomGainMax = builder.define("Maximum Random Gain", 0.0);
        RandomLossMin = builder.define("Minimum Random Loss", 0.0);
        RandomLossMax = builder.define("Maximum Random Loss", 0.0);

        RandomGainMultiplierMin = builder.define("Random Killer Multiplier Min 1 = 100%", 0.0);
        RandomGainMultiplierMax = builder.define("Random Killer Multiplier Max 1 = 100%", 0.0);
        RandomLossMultiplierMin = builder.define("Random Target Multiplier Min 1 = 100%", 0.0);
        RandomLossMultiplierMax = builder.define("Random Target Multiplier Max 1 = 100%", 0.0);
    }
}
