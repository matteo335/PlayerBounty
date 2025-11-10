package matteo.PlayerBounty;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BountyConfig {

    public static final BountyConfig SERVER;
    public static final ModConfigSpec CONFIG_SPEC;
    private static final Logger LOGGER = LogManager.getLogger("PlayerBounty");


    public static ModConfigSpec.BooleanValue StartupWarning;

    public static ModConfigSpec.BooleanValue DefaultSystem;
    public static ModConfigSpec.BooleanValue MagicCoinsSystem;

    public static ModConfigSpec.BooleanValue IsPlayerBountyDisplayEnabled;
    public static ModConfigSpec.BooleanValue DeleteDisplay;
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

    //Magic Coins
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

    private BountyConfig(ModConfigSpec.Builder builder) {
        builder.comment("""
                Note that you can use + and - but you CANNOT use %
                Make sure you do the calculations correctly, some values might do the opposite in some scenarios.
               
                Target is calculated like this: Bounty = Bounty * (Target-Multiplier + RandomLossMultiplier) - (LossOnDeath + RandomLoss)
               
                If LossCompleteBountyOnDeath is true: Bounty = (-RandomLoss - LossOnDeath) + (Bounty * (Target-Multiplier + RandomLossMultiplier)) - Bounty
               
                Killer is calculated like this: Bounty = (BountyTarget * Claim-Multiplier) + (BountyKiller * (Killer-Multiplier + RandomGainMultiplier)) + (GainOnKilling + RandomLoss)
               """);
        builder.push("PlayerBounty");
        builder.comment("Base mod mechanic, without compats");

        StartupWarning = builder.define("Enable the warning in the server start", true);

        DefaultSystem = builder.define("Enable the default mechanics, disable it if you prefer the compats", true);

        IsPlayerBountyDisplayEnabled = builder.define("Display for the default system", true);
        DeleteDisplay = builder.define("Completely delete the display", false);
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

        RandomGainMin = builder.defineInRange("Random Gain Min (0 + decimal = No Change) - Cannot be equal or superior than the Max.Must have a decimal", 0.00, Integer.MIN_VALUE, Integer.MAX_VALUE);
        RandomGainMax = builder.defineInRange("Random Gain Max (0 + decimal 001 = No Change) - Cannot be equal or inferior than the Min.Must have a decimal", 0.001, Integer.MIN_VALUE, Integer.MAX_VALUE);
        RandomLossMin = builder.defineInRange("Random Loss Min (0 + decimal = No Change) - Cannot be equal or superior than the Max.Must have a decimal", 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        RandomLossMax = builder.defineInRange("Random Loss Max (0 + decimal 001 = No Change) - Cannot be equal or inferior than the Min.Must have a decimal", 0.001, Integer.MIN_VALUE, Integer.MAX_VALUE);

        RandomGainMultiplierMin = builder.defineInRange("Random Gain Multiplier Min (0 = No Change) - Cannot be equal or superior than the Max.Must have a decimal", 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        RandomGainMultiplierMax = builder.defineInRange("Random Gain Multiplier Max (0 + decimal 001 = No Change) - Cannot be equal or inferior than the Min.Must have a decimal", 0.001, Integer.MIN_VALUE, Integer.MAX_VALUE);
        RandomLossMultiplierMin = builder.defineInRange("Random Loss Multiplier Min (0 = No Change) - Cannot be equal or superior than the Max.Must have a decimal", 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        RandomLossMultiplierMax = builder.defineInRange("Random Loss Multiplier Max (0 + decimal 001 = No Change) - Cannot be equal or interior than the Min.Must have a decimal", 0.001, Integer.MIN_VALUE, Integer.MAX_VALUE);
        builder.pop(1);

        builder.push("MagicCoins");
        builder.comment("Please remember MagicCoins base configs still apply");

        MagicCoinsSystem = builder.define("Enable the MagicCoins compat", false);

        CoinsDisplay1 = builder.define("Formatting Codes before the coins display inside the username", " [$§d§l");
        CoinsDisplay2 = builder.define("Formatting Codes after the coins display inside the username", "§r]");

        IsMagicCoinsDisplayEnabled = builder.define("Display for the Magic Coins Compat", false);
        CoinsDisplayTimer = builder.define("How much ticks before the displays get updated, 20 ticks is 1 seconds", 40);

        GainCoins = builder.define("How much coins you gain after killing", 0);
        LossCoins = builder.define("How much coins you loss after being killed", 0);

        MultiplierOfCoinsStealing = builder.defineInRange("Multiplier of coins you steal from the target.Must be decimal", 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        MultiplierSelfCoins = builder.defineInRange("How much do you multiply from yourself after killing another player.Must be decimal", 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        MultiplierOfCoinsLoss = builder.defineInRange("Multiplier of loss after being killed.Must be decimal", 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE);

        RandomGainCoinsMin = builder.defineInRange("Minimum of random gain after killing another player.Must be decimal and lower than Max", 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        RandomGainCoinsMax = builder.defineInRange("Maximum of random gain after killing another player.Must be decimal and higher than Min", 0.001, Integer.MIN_VALUE, Integer.MAX_VALUE);
        RandomLossCoinsMin = builder.defineInRange("Minimum of random loss after being killed by another player.Must be decimal and lower than Max", 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        RandomLossCoinsMax = builder.defineInRange("Maximum of random loss after being killed by another player.Must be decimal and higher than Min", 0.001, Integer.MIN_VALUE, Integer.MAX_VALUE);

        RandomGainCoinsMultiplierMin = builder.defineInRange("Minimum of random gain multiplier after killing another player.Must be decimal and lower than Max", 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        RandomGainCoinsMultiplierMax = builder.defineInRange("Maximum of random gain multiplier after killing another player.Must be decimal and higher than Min", 0.001, Integer.MIN_VALUE, Integer.MAX_VALUE);
        RandomLossCoinsMultiplierMin = builder.defineInRange("Minimum of random loss multiplier after being killed by another player.Must be decimal and lower than Max", 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        RandomLossCoinsMultiplierMax = builder.defineInRange("Maximum of random loss multiplier after being killed by another player.Must be decimal and higher than Min", 0.001, Integer.MIN_VALUE, Integer.MAX_VALUE);
        builder.pop();

        LOGGER.atInfo().log("Config initialized");
    }

    static {
        Pair<BountyConfig, ModConfigSpec> pair =
                new ModConfigSpec.Builder().configure(BountyConfig::new);

        SERVER = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }
}