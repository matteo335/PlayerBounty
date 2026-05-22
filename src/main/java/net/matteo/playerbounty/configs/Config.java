package net.matteo.playerbounty.configs;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {

    public static ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

    public static ModConfigSpec.BooleanValue StartupWarning;

    public static ModConfigSpec.BooleanValue System;

    public static ModConfigSpec.BooleanValue EnableDisplay;
    public static ModConfigSpec.ConfigValue<Integer> DisplayCooldown;
    public static ModConfigSpec.ConfigValue<Integer> Color;

    public static ModConfigSpec.BooleanValue Bold;
    public static ModConfigSpec.BooleanValue Italic;
    public static ModConfigSpec.BooleanValue Underlined;
    public static ModConfigSpec.BooleanValue Strikethrough;

    public static ModConfigSpec.ConfigValue<String> Display1;
    public static ModConfigSpec.ConfigValue<String> Display2;

    public static ModConfigSpec.BooleanValue LoseCompleteBountyOnDeath;

    public static ModConfigSpec.DoubleValue MinimumValue;
    public static ModConfigSpec.DoubleValue MaximumValue;

    public static ModConfigSpec.ConfigValue<Double> Gain;
    public static ModConfigSpec.ConfigValue<Double> Loss;

    public static ModConfigSpec.DoubleValue KillerMultiplier;
    public static ModConfigSpec.DoubleValue ClaimMultiplier;
    public static ModConfigSpec.DoubleValue TargetMultiplier;


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

        Display1 = builder.define("Formatting Codes Before Bounty", " [$");
        Display2 = builder.define("Formatting Codes After Bounty", "§r]");

        LoseCompleteBountyOnDeath = builder.define("Loss the equivalent of your entire bounty on death", false);
        MinimumValue = builder.defineInRange("Bounty Minimum Value", -Double.MAX_VALUE, -Double.MAX_VALUE, Double.MAX_VALUE);
        MaximumValue = builder.defineInRange("Bounty Maximum Value", Double.MAX_VALUE, -Double.MAX_VALUE, Double.MAX_VALUE);

        Gain = builder.define("Bounty Gain On Killing", 10.0);
        Loss = builder.define("Bounty Loss On Death", 10.0);

        KillerMultiplier = builder.defineInRange("Killer-Multiplier multiply from your own balance everytime you kill someone 1 = 100%", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        ClaimMultiplier = builder.defineInRange("Claim-Multiplier, how much you take from your victim's balance 1 = 100%", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        TargetMultiplier = builder.defineInRange("Target-Multiplier, how much is divided from your balance when you get killed 1 = 100%", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);

        RandomGainMin = builder.defineInRange("Minimum Random Gain", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        RandomGainMax = builder.defineInRange("Maximum Random Gain", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        RandomLossMin = builder.defineInRange("Minimum Random Loss", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        RandomLossMax = builder.defineInRange("Maximum Random Loss", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);

        RandomGainMultiplierMin = builder.defineInRange("Random Killer Multiplier Min 1 = 100%", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        RandomGainMultiplierMax = builder.defineInRange("Random Killer Multiplier Max 1 = 100%", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        RandomLossMultiplierMin = builder.defineInRange("Random Target Multiplier Min 1 = 100%", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        RandomLossMultiplierMax = builder.defineInRange("Random Target Multiplier Max 1 = 100%", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
    }
}
