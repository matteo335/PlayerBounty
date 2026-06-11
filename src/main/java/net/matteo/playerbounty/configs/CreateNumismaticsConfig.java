package net.matteo.playerbounty.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public class CreateNumismaticsConfig {

    public static ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec.BooleanValue System;
    public static ForgeConfigSpec.BooleanValue InventoryMath;
    public static ForgeConfigSpec.BooleanValue InventoryDisplay;
    public static ForgeConfigSpec.BooleanValue InventoryCoinsMove;

    public static ForgeConfigSpec.BooleanValue EnableDisplay;
    public static ForgeConfigSpec.ConfigValue<String> Display1;
    public static ForgeConfigSpec.ConfigValue<String> Display2;
    public static ForgeConfigSpec.ConfigValue<Integer> Color;

    public static ForgeConfigSpec.BooleanValue Bold;
    public static ForgeConfigSpec.BooleanValue Italic;
    public static ForgeConfigSpec.BooleanValue Underlined;
    public static ForgeConfigSpec.BooleanValue Strikethrough;

    public static ForgeConfigSpec.BooleanValue CompleteLoss;
    public static ForgeConfigSpec.ConfigValue<Double> Gain;
    public static ForgeConfigSpec.ConfigValue<Double> Loss;

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
                Total value means inventory + ender chest + bank
                
                Killer is calculated like this: Total value + (Gain On Killing + Random Gain) + (Total value * (Killer-Multiplier + Random Gain Multiplier)) + (target's total value * Claim-Multiplier)
                
                Target is calculated like this: Bank - (Loss On Death + Random Loss) + (Bank * (Target-Multiplier + Random Loss Multiplier)
                
                If LossCompleteBountyOnDeath is true: Bank = (-LossOnDeath - randomLoss) + (Bank * (Target-Multiplier + RandomLossMultiplier))
                
                For colors, pick an RGB color from the first page and copy the R, G, and B, numbers. Then put these numbers inside the second page to get a Decimal RGB Color
                https://www.rapidtables.com/web/color/RGB_Color.html
                https://www.checkyourmath.com/convert/color/rgb_decimal.php
                
                Use this link to calculate maths: https://onlinegdb.com/UUpB2waOS
                """);

        System = builder.define("Enable the Create Numismatics compat", false);
        InventoryMath = builder.define("Includes the coins in the inventory and ender chest during the math", true);
        InventoryDisplay = builder.define("Includes the coins in the inventory and ender chest inside the display value", true);
        InventoryCoinsMove = builder.define("Instead of dropping the coins when being killed, move them in the bank", false);

        EnableDisplay = builder.define("Enable the display", true);
        Color = builder.define("Decimal RGB Color of the value", 16777215);

        Bold = builder.define("Write the values in bold", true);
        Italic = builder.define("Write the values in italic", false);
        Underlined = builder.define("Underline the values", false);
        Strikethrough = builder.define("Strike through the values", false);

        Display1 = builder.define("Text before the value", " [");
        Display2 = builder.define("Text after the value", " Cogs]");

        CompleteLoss = builder.define("Lose every of your spurs on death", false);

        Gain = builder.define("Spurs gained in bank or inventory on killing", 640.0);
        Loss = builder.define("Spurs lose in bank or inventory on death", 640.0);

        KillerMultiplier = builder.define("Killer-Multiplier, multiply from your total balance everytime you kill someone 1 = 100%", 0.0);
        ClaimMultiplier = builder.define("Claim-Multiplier, steal from your target 1 = 100%", 0.0);
        TargetMultiplier = builder.define("Target-Multiplier, how much is divided from your total balance when you get killed 1 = 100%", 0.0);

        RandomGainMin = builder.define("Spur Minimum Random Gain", 0.0);
        RandomGainMax = builder.define("Spur Maximum Random Gain", 0.0);
        RandomLossMin = builder.define("Spur Minimum Random Loss", 0.0);
        RandomLossMax = builder.define("Spur Maximum Random Loss", 0.0);

        RandomGainMultiplierMin = builder.define("Random Killer Multiplier Min 1 = 100%", 0.0);
        RandomGainMultiplierMax = builder.define("Random Killer Multiplier Max 1 = 100%", 0.0);
        RandomLossMultiplierMin = builder.define("Random Target Multiplier Min 1 = 100%", 0.0);
        RandomLossMultiplierMax = builder.define("Random Target Multiplier Max 1 = 100%", 0.0);
    }
}
