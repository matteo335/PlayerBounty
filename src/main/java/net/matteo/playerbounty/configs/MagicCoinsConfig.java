package net.matteo.playerbounty.configs;

import net.neoforged.neoforge.common.ModConfigSpec;

public class MagicCoinsConfig {

    public static ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

    public static ModConfigSpec.BooleanValue MagicCoinsSystem;

    public static ModConfigSpec.BooleanValue IsMagicCoinsDisplayEnabled;
    public static ModConfigSpec.ConfigValue<Integer> CoinsDisplayTimer;

    public static ModConfigSpec.ConfigValue<String> CoinsDisplay1;
    public static ModConfigSpec.ConfigValue<String> CoinsDisplay2;

    public static ModConfigSpec.ConfigValue<Integer> GainCoins;
    public static ModConfigSpec.ConfigValue<Integer> LossCoins;

    public static ModConfigSpec.DoubleValue MultiplierOfCoinsStealing;
    public static ModConfigSpec.DoubleValue MultiplierOfCoinsLoss;
    public static ModConfigSpec.DoubleValue MultiplierSelfCoins;

    public static ModConfigSpec.DoubleValue RandomGainCoinsMin;
    public static ModConfigSpec.DoubleValue RandomGainCoinsMax;
    public static ModConfigSpec.DoubleValue RandomLossCoinsMin;
    public static ModConfigSpec.DoubleValue RandomLossCoinsMax;

    public static ModConfigSpec.DoubleValue RandomGainCoinsMultiplierMin;
    public static ModConfigSpec.DoubleValue RandomGainCoinsMultiplierMax;
    public static ModConfigSpec.DoubleValue RandomLossCoinsMultiplierMin;
    public static ModConfigSpec.DoubleValue RandomLossCoinsMultiplierMax;

    public static double baseRateBountyHunter;
    public static int percentageRewardBountyHunter;

    public static ModConfigSpec.DoubleValue Base_rate_bounty_hunter;
    public static ModConfigSpec.DoubleValue Percentage_reward_bounty_hunter;

    static {
        builder.comment("""
                 Note that you can use + and - but you CANNOT use %
                 Make sure you do the calculations correctly, some values might do the complete opposite in some scenarios.
                
                 Target is calculated like this: your Bounty * (Target-Multiplier + RandomLossMultiplier) - (LossOnDeath + RandomLoss)
                
                 If LossCompleteBountyOnDeath is true: your Bounty = (-RandomLoss - LossOnDeath) + (Bounty * (Target-Multiplier + RandomLossMultiplier)) - Bounty
                
                 Killer is calculated like this: your Bounty = (BountyTarget * Claim-Multiplier) + (BountyKiller * (Killer-Multiplier + RandomGainMultiplier)) + (GainOnKilling + RandomLoss)
                """);

        MagicCoinsSystem = builder.define("Enable the MagicCoins compat", false);
        IsMagicCoinsDisplayEnabled = builder.define("Display the Magic Coins in the player name", true);

        CoinsDisplay1 = builder.define("Formatting Codes before the coin display", "[$§d§l");
        CoinsDisplay2 = builder.define("Formatting Codes after the coin display", "§r]");

        GainCoins = builder.define("How much coins you gain after killing another player", 100);
        LossCoins = builder.define("How much coins you loss after being killed", 100);

        MultiplierOfCoinsStealing = builder.defineInRange("Multiplier of coins you steal from the target.Must be decimal", 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        MultiplierSelfCoins = builder.defineInRange("How much do you multiply from yourself after killing.Must be decimal", 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        MultiplierOfCoinsLoss = builder.defineInRange("Multiplier of how much you loss after being killed.Must be decimal", 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE);

        RandomGainCoinsMin = builder.defineInRange("Minimum of random gain after killing another player.Must be decimal", 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        RandomGainCoinsMax = builder.defineInRange("Maximum of random gain after killing another player.Must be decimal", 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        RandomLossCoinsMin = builder.defineInRange("Minimum of random loss after being killed by another player.Must be decimal", 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        RandomLossCoinsMax = builder.defineInRange("Maximum of random loss after being killed by another player.Must be decimal", 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE);

        RandomGainCoinsMultiplierMin = builder.defineInRange("Minimum of random gain multiplier after killing another player.Must be decimal and lower than Max", 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        RandomGainCoinsMultiplierMax = builder.defineInRange("Maximum of random gain multiplier after killing another player.Must be decimal and higher than Min", 0.001, Integer.MIN_VALUE, Integer.MAX_VALUE);
        RandomLossCoinsMultiplierMin = builder.defineInRange("Minimum of random loss multiplier after being killed by another player.Must be decimal and lower than Max", 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        RandomLossCoinsMultiplierMax = builder.defineInRange("Maximum of random loss multiplier after being killed by another player.Must be decimal and higher than Min", 0.001, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
}
