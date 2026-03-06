package net.matteo.playerbounty.configs;

import net.matteo.playerbounty.PlayerBountyMod;
import net.matteo.playerbounty.network.payload.SyncServerConfigS2C;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = PlayerBountyMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ServerConfig {

    public static double baseRateBountyHunter;
    public static int percentageRewardBountyHunter;

    public static class Config {
        public static final ModConfigSpec.Builder CONFIG_BUILDER = new ModConfigSpec.Builder();

        public static final ModConfigSpec.ConfigValue<Double> BASE_RATE_BOUNTY_HUNTER;
        public static final ModConfigSpec.ConfigValue<Integer> PERCENTAGE_REWARD_BOUNTY_HUNTER;

        static {
            CONFIG_BUILDER.push("Bounty Hunter Settings");

            BASE_RATE_BOUNTY_HUNTER = CONFIG_BUILDER
                    .comment("Base rate for Bounty Hunter mode")
                    .defineInRange("baseRateBountyHunter", 1.0, 0.0, Double.MAX_VALUE);

            PERCENTAGE_REWARD_BOUNTY_HUNTER = CONFIG_BUILDER
                    .comment("Percentage reward for Bounty Hunter mode")
                    .defineInRange("percentageRewardBountyHunter", 5, 0, 100);

            CONFIG_BUILDER.pop();

            SPEC = CONFIG_BUILDER.build();
        }
        public static final ModConfigSpec SPEC;
    }

    public static void bakeConfig() {
        baseRateBountyHunter = Config.BASE_RATE_BOUNTY_HUNTER.get();
        percentageRewardBountyHunter = Config.PERCENTAGE_REWARD_BOUNTY_HUNTER.get();

        PacketDistributor.sendToAllPlayers(
                new SyncServerConfigS2C(
                        baseRateBountyHunter,
                    percentageRewardBountyHunter));

    }

    private static void onConfigUnload() {
        baseRateBountyHunter = 1.0;
        percentageRewardBountyHunter = 5;
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        try {
            if (event.getConfig().getType() == ModConfig.Type.SERVER && event.getConfig().getSpec() == Config.SPEC) {
                bakeConfig();
            }
        } catch (Exception e) {
            PlayerBountyMod.LOGGER.error("Error loading server config", e);
            onConfigUnload();
        }
    }

}
