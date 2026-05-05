package net.matteo.playerbounty.configs;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {

    public static ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

    public static ModConfigSpec.BooleanValue StartupWarning;

    public static ModConfigSpec.BooleanValue DefaultSystem;

    public static ModConfigSpec.BooleanValue IsPlayerBountyDisplayEnabled;
    public static ModConfigSpec.BooleanValue DisableDisplay;
    public static ModConfigSpec.ConfigValue<String> BountyDisplay1;
    public static ModConfigSpec.ConfigValue<String> BountyDisplay2;

    public static ModConfigSpec.BooleanValue LoseCompleteBountyOnDeath;

    public static ModConfigSpec.ConfigValue<Integer> BountyMinimumValue;
    public static ModConfigSpec.ConfigValue<Integer> BountyMaximumValue;

    public static ModConfigSpec.ConfigValue<Integer> GainOnKilling;
    public static ModConfigSpec.ConfigValue<Integer> LossOnDeath;

    public static ModConfigSpec.DoubleValue MultiplierOfGainOverKillerBounty;
    public static ModConfigSpec.DoubleValue MultiplierOfGainOverClaimedBounty;
    public static ModConfigSpec.DoubleValue MultiplierOfLossOverTargetBounty;


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
                 Note that you can use + and - but you CANNOT use %
                 Make sure you do the calculations correctly, some values might do the complete opposite in some scenarios.
                
                 Target is calculated like this: your Bounty * (Target-Multiplier + RandomLossMultiplier) - (LossOnDeath + RandomLoss)
                
                 If LossCompleteBountyOnDeath is true: your Bounty = (-RandomLoss - LossOnDeath) + (Bounty * (Target-Multiplier + RandomLossMultiplier)) - Bounty
                
                 Killer is calculated like this: your Bounty = (BountyTarget * Claim-Multiplier) + (BountyKiller * (Killer-Multiplier + RandomGainMultiplier)) + (GainOnKilling + RandomLoss)
                """);

        StartupWarning = builder.define("Enable the warning in the server start", true);

        DefaultSystem = builder.define("Enable the default mechanics, disable it if you prefer the compats", true);

        IsPlayerBountyDisplayEnabled = builder.define("Display for the default system", true);
        DisableDisplay = builder.define("Disable the display", false);
        BountyDisplay1 = builder.define("Formatting Codes Before Bounty", " [$§6§l");
        BountyDisplay2 = builder.define("Formatting Codes After Bounty", "§r]");

        LoseCompleteBountyOnDeath = builder.define("Lose Complete Bounty On Death", false);
        BountyMinimumValue = builder.define("Bounty Minimum Value", Integer.MIN_VALUE);
        BountyMaximumValue = builder.define("Bounty Maximum Value", Integer.MAX_VALUE);

        GainOnKilling = builder.define("Bounty Gain On Killing (cannot have a decimal)", 10);
        LossOnDeath = builder.define("Bounty Loss On Death (cannot have a decimal)", 10);

        MultiplierOfGainOverKillerBounty = builder.defineInRange("Killer-Multiplier of your own bounty (1 = No Change).Must have a decimal", 1.0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        MultiplierOfGainOverClaimedBounty = builder.defineInRange("Claim-Multiplier, how much you take from your victim (1 = Claim 100% of the bounty).Must have an decimal", 0.5, Integer.MIN_VALUE, Integer.MAX_VALUE);
        MultiplierOfLossOverTargetBounty = builder.defineInRange("Target-Multiplier, how much do the victim loss (1 = No Change).Must have a decimal", 1.0, Integer.MIN_VALUE, Integer.MAX_VALUE);

        RandomGainMin = builder.defineInRange("Minimum Random Gain.Must have a decimal", 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        RandomGainMax = builder.defineInRange("Maximum Random Gain.Must have a decimal", 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        RandomLossMin = builder.defineInRange("Minimum Random Loss.Must have a decimal", 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        RandomLossMax = builder.defineInRange("Maximum Random Loss.Must have a decimal", 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE);

        RandomGainMultiplierMin = builder.defineInRange("Random Gain Multiplier Min (0 = No Change) - Cannot be equal or superior than the Max.Must have a decimal", 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        RandomGainMultiplierMax = builder.defineInRange("Random Gain Multiplier Max (0 + decimal 001 = No Change) - Cannot be equal or inferior than the Min.Must have a decimal", 0.001, Integer.MIN_VALUE, Integer.MAX_VALUE);
        RandomLossMultiplierMin = builder.defineInRange("Random Loss Multiplier Min (0 = No Change) - Cannot be equal or superior than the Max.Must have a decimal", 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        RandomLossMultiplierMax = builder.defineInRange("Random Loss Multiplier Max (0 + decimal 001 = No Change) - Cannot be equal or interior than the Min.Must have a decimal", 0.001, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
}
