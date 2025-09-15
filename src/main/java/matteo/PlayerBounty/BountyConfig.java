package matteo.PlayerBounty;

import net.minecraftforge.common.ForgeConfigSpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.lang3.tuple.Pair;

public class BountyConfig {

    public static final BountyConfig CONFIG;
    public static final ForgeConfigSpec CONFIG_SPEC;
    private static final Logger LOGGER = LogManager.getLogger("PlayerBounty-Config");


    public static ForgeConfigSpec.ConfigValue<String> BountyDisplay1;
    public static ForgeConfigSpec.ConfigValue<String> BountyDisplay2;

    public static ForgeConfigSpec.BooleanValue LoseCompleteBountyOnDeath;

    public static ForgeConfigSpec.ConfigValue<Integer> BountyMinimumValue;
    public static ForgeConfigSpec.ConfigValue<Integer> BountyMaximumValue;

    public static ForgeConfigSpec.ConfigValue<Integer> GainOnKilling;
    public static ForgeConfigSpec.ConfigValue<Integer> LossOnDeath;

    public static ForgeConfigSpec.DoubleValue MultiplierOfGainOverKillerBounty;
    public static ForgeConfigSpec.DoubleValue MultiplierOfGainOverClaimedBounty;
    public static ForgeConfigSpec.DoubleValue MultiplierOfLossOverTargetBounty;


    public static ForgeConfigSpec.DoubleValue RandomGainMin;
    public static ForgeConfigSpec.DoubleValue RandomGainMax;
    public static ForgeConfigSpec.DoubleValue RandomLossMin;
    public static ForgeConfigSpec.DoubleValue RandomLossMax;

    public static ForgeConfigSpec.DoubleValue RandomGainMultiplierMin;
    public static ForgeConfigSpec.DoubleValue RandomGainMultiplierMax;
    public static ForgeConfigSpec.DoubleValue RandomLossMultiplierMin;
    public static ForgeConfigSpec.DoubleValue RandomLossMultiplierMax;

    private BountyConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("""
                Note that you can use + and - but you CANNOT use %
                Make sure you do the calculations correctly, some values might do the opposite in some scenarios
               
                Target is calculated like this: Bounty = Bounty * (MultiplierOfLossOverTargetBounty + RandomLossMultiplier) - (LossOnDeath + RandomLoss)
               
                If LossCompleteBountyOnDeath is true: Bounty = (-RandomLoss - LossOnDeath) + (Bounty * (MultiplierOfLossOverTargetBounty + RandomLossMultiplier)) - Bounty
               
                Killer is calculated like this: Bounty = (BountyTarget * MultiplierOfGainOverClaimedBounty) + (BountyKiller * (MultiplierOfGainOverKillerBounty + RandomGainMultiplier)) + (GainOnKilling + RandomLoss)
               """);


        BountyDisplay1 = builder.define("Formatting Codes Before Bounty", " [$§6§l");
        BountyDisplay2 = builder.define("Formatting Codes After Bounty", "§r]");



        LoseCompleteBountyOnDeath = builder.define("Lose Complete Bounty On Death", false);
        BountyMinimumValue = builder.define("Bounty Minimum Value", Integer.MIN_VALUE);
        BountyMaximumValue = builder.define("Bounty Maximum Value", Integer.MAX_VALUE);


        GainOnKilling = builder.define("Bounty Gain On Killing (cannot have a decimal)", 10);
        LossOnDeath = builder.define("Bounty Loss On Death (cannot have a decimal)", 10);

        MultiplierOfGainOverKillerBounty = builder.defineInRange("Multiplier Of Gain Over The Killer Bounty (1 = No Change).Must have a decimal", 1.0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        MultiplierOfGainOverClaimedBounty = builder.defineInRange("Multiplier Of Gain Over the Claimed Bounty (1 = Claim 100% of the bounty).Must have an decimal", 0.5, Integer.MIN_VALUE, Integer.MAX_VALUE);
        MultiplierOfLossOverTargetBounty = builder.defineInRange("Multiplier Of Loss On Death (1 = No Change).Must have a decimal", 1.0, Integer.MIN_VALUE, Integer.MAX_VALUE);

        RandomGainMin = builder.defineInRange("Random Gain Min (0 + decimal = No Change) - Cannot be equal or superior than the Max.Must have a decimal", 0.00, Integer.MIN_VALUE, Integer.MAX_VALUE);
        RandomGainMax = builder.defineInRange("Random Gain Max (0 + decimal 001 = No Change) - Cannot be equal or inferior than the Min.Must have a decimal", 0.001, Integer.MIN_VALUE, Integer.MAX_VALUE);
        RandomLossMin = builder.defineInRange("Random Loss Min (0 + decimal = No Change) - Cannot be equal or superior than the Max.Must have a decimal", 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        RandomLossMax = builder.defineInRange("Random Loss Max (0 + decimal 001 = No Change) - Cannot be equal or inferior than the Min.Must have a decimal", 0.001, Integer.MIN_VALUE, Integer.MAX_VALUE);

        RandomGainMultiplierMin = builder.defineInRange("Random Gain Multiplier Min (0 = No Change) - Cannot be equal or superior than the Max.Must have a decimal", 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        RandomGainMultiplierMax = builder.defineInRange("Random Gain Multiplier Max (0 + decimal 001 = No Change) - Cannot be equal or inferior than the Min.Must have a decimal", 0.001, Integer.MIN_VALUE, Integer.MAX_VALUE);
        RandomLossMultiplierMin = builder.defineInRange("Random Loss Multiplier Min (0 = No Change) - Cannot be equal or superior than the Max.Must have a decimal", 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        RandomLossMultiplierMax = builder.defineInRange("Random Loss Multiplier Max (0 + decimal 001 = No Change) - Cannot be equal or interior than the Min.Must have a decimal", 0.001, Integer.MIN_VALUE, Integer.MAX_VALUE);

        LOGGER.atInfo().log("Config initialized");
    }

    static {
        Pair<BountyConfig, ForgeConfigSpec> pair =
                new ForgeConfigSpec.Builder().configure(BountyConfig::new);

        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }
}