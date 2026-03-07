package net.matteo.playerbounty.events;

import net.matteo.playerbounty.PlayerBountyMod;
import net.matteo.playerbounty.internal.RulesGame;
import net.matteo.playerbounty.internal.provider.BountyHunterProvider;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.api.distmarker.Dist;


@EventBusSubscriber(Dist.DEDICATED_SERVER)
public class PlayerDeathEvent {

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {

        Entity target = event.getEntity();
        Entity source = event.getSource().getEntity();

        if (!(target instanceof ServerPlayer killer)) {
            return;
        }
        if (!(source instanceof ServerPlayer victim)) {
            return;
        }
        if (killer == victim) {
            return;
        }
        if (!victim.isCreative() || !killer.gameMode.isSurvival()) {
            return;
        }

        handlePvPDeath(killer, victim);
    }

    private static void handlePvPDeath(ServerPlayer killer, ServerPlayer victim) {
        BountyHunterProvider events = RulesGame.get();
        boolean success = events.updateBountyHunter(killer, victim);
        if (!success) {
            String string = String.format("Failed to update bounty for killer %1$s and victim %2$s",
                    killer.getName().getString(), victim.getName().getString());

            PlayerBountyMod.LOGGER.error(string);
            for (ServerPlayer player : killer.getServer().getPlayerList().getPlayers()) {
                if (player.hasPermissions(4)) {
                    player.displayClientMessage(Component.literal(string), false);
                }
            }
        }
    }
}