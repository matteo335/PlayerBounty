package net.matteo.playerbounty.configs;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CreateNumismaticsConfig {

    public static ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

    public static ModConfigSpec.BooleanValue System;
    public static ModConfigSpec.BooleanValue InventoryMath;
    public static ModConfigSpec.BooleanValue InventoryDisplay;
    public static ModConfigSpec.BooleanValue InventoryCoinsMove;

    public static ModConfigSpec.BooleanValue EnableDisplay;
    public static ModConfigSpec.ConfigValue<String> Display1;
    public static ModConfigSpec.ConfigValue<String> Display2;
    public static ModConfigSpec.ConfigValue<Integer> Color;

    public static ModConfigSpec.BooleanValue Bold;
    public static ModConfigSpec.BooleanValue Italic;
    public static ModConfigSpec.BooleanValue Underlined;
    public static ModConfigSpec.BooleanValue Strikethrough;

    public static ModConfigSpec.BooleanValue CompleteLoss;
    public static ModConfigSpec.ConfigValue<Double> Gain;
    public static ModConfigSpec.ConfigValue<Double> Loss;

    public static ModConfigSpec.ConfigValue<Double> ClaimMultiplier;
    public static ModConfigSpec.ConfigValue<Double> KillerMultiplier;
    public static ModConfigSpec.ConfigValue<Double> TargetMultiplier;

    public static ModConfigSpec.ConfigValue<Double> RandomGainMin;
    public static ModConfigSpec.ConfigValue<Double> RandomGainMax;
    public static ModConfigSpec.ConfigValue<Double> RandomLossMin;
    public static ModConfigSpec.ConfigValue<Double> RandomLossMax;

    public static ModConfigSpec.ConfigValue<Double> RandomGainMultiplierMin;
    public static ModConfigSpec.ConfigValue<Double> RandomGainMultiplierMax;
    public static ModConfigSpec.ConfigValue<Double> RandomLossMultiplierMin;
    public static ModConfigSpec.ConfigValue<Double> RandomLossMultiplierMax;

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
