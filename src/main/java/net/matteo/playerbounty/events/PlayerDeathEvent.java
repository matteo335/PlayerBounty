package net.matteo.playerbounty.events;

import net.matteo.playerbounty.PlayerBountyMod;
import net.matteo.playerbounty.internal.RulesGame;
import net.matteo.playerbounty.internal.provider.BountyHunterProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

@EventBusSubscriber
public class PlayerDeathEvent {

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {

        if (event.getEntity().level().isClientSide()) {
            return;
        }

        Entity target = event.getEntity();
        Entity source = event.getSource().getEntity();

        if (!(target instanceof ServerPlayer deadPlayer)) {
            return;
        }
        if (!(source instanceof ServerPlayer killerPlayer)) {
            return;
        }
        if (deadPlayer == killerPlayer) {
            return;
        }
        if (!deadPlayer.gameMode.isSurvival() || !killerPlayer.gameMode.isSurvival()) {
            return;
        }

        handlePvPDeath(killerPlayer, deadPlayer);
    }

    private static void handlePvPDeath(ServerPlayer killer, ServerPlayer victim) {
        BountyHunterProvider events = RulesGame.get();
        boolean success = events.updateBountyHunter(killer, victim);
        if (!success) {
            PlayerBountyMod.LOGGER.error("Failed to update bounty hunter for killer: {} and victim: {}",
                    killer.getName().getString(), victim.getName().getString());
        }
    }
}