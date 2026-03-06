package net.matteo.playerbounty.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class ClientSyncedConfig {
    private static double baseRateBountyHunter = 1.0;
    private static int percentageRewardBountyHunter = 5;

    private ClientSyncedConfig() {
    }

    public static double getBaseRateBountyHunter() {
        return baseRateBountyHunter;
    }

    public static int getPercentageRewardBountyHunter() {
        return percentageRewardBountyHunter;
    }

    public static void apply(double baseRateBountyHunter, int percentageRewardBountyHunter) {
        ClientSyncedConfig.baseRateBountyHunter = baseRateBountyHunter;
        ClientSyncedConfig.percentageRewardBountyHunter = percentageRewardBountyHunter;
    }
}