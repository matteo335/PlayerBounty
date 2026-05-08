package net.matteo.playerbounty.events;


import net.matteo.playerbounty.utils.GetValues;
import net.matteo.playerbounty.configs.SGEconomyConfig;
import net.matteo.playerbounty.configs.Config;
import net.matteo.playerbounty.compats.SG_Economy;
import net.matteo.playerbounty.PlayerBountyMod;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.api.distmarker.Dist;

@EventBusSubscriber(Dist.DEDICATED_SERVER)
public class PlayerDeathEvent {

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        Entity source = event.getSource().getEntity();

        if (!(source instanceof ServerPlayer killer)) return;
        if (!(entity instanceof ServerPlayer target)) return;

        if (killer == target) return;
        if (!target.gameMode.isSurvival() || !killer.gameMode.isSurvival()) return;

        if (Config.DefaultSystem.get()) {
            handleKillerBounty(killer, target);
            handleTargetBounty(target);
        }

        if (PlayerBountyMod.sg_economy_config && SGEconomyConfig.CoinsSystem.get()) {
            SG_Economy.handleKillerEconomy(killer, target);
            SG_Economy.handleTargetEconomy(target);
        }
    }

    public static void handleKillerBounty(ServerPlayer killer, ServerPlayer target) {
        if (Config.DefaultSystem.get()) {
            double killerBounty = killer.getPersistentData().getDouble("bounty");

            killerBounty += (Config.GainOnKilling.get() + GetValues.randomGain())
                            + (killerBounty * (GetValues.randomGainMultiplier() + Config.KillerMultiplier.get())
                            + (target.getPersistentData().getDouble("bounty") * Config.ClaimMultiplier.get()));


            killer.getPersistentData().putDouble("bounty", killerBounty);
        }
    }

    public static void handleTargetBounty(ServerPlayer target) {
        if (Config.DefaultSystem.get()) {
            double targetBounty = target.getPersistentData().getDouble("bounty");

            if (Config.LoseCompleteBountyOnDeath.get()) {
                targetBounty = (-Config.LossOnDeath.get() - GetValues.randomLoss())
                        - (targetBounty * (Config.TargetMultiplier.get() + GetValues.randomLossMultiplier()));
            } else {
                targetBounty -= (Config.LossOnDeath.get() + GetValues.randomLoss())
                        + (targetBounty * (Config.TargetMultiplier.get() + GetValues.randomLossMultiplier()));
            }

            target.getPersistentData().putDouble("bounty", targetBounty);
        }
    }
}