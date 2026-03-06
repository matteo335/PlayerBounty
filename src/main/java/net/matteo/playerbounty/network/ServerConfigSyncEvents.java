package net.matteo.playerbounty.network;

import net.matteo.playerbounty.PlayerBountyMod;
import net.matteo.playerbounty.configs.ServerConfig;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.matteo.playerbounty.network.payload.SyncServerConfigS2C;

@EventBusSubscriber(modid = PlayerBountyMod.MOD_ID)
public final class ServerConfigSyncEvents {

    private ServerConfigSyncEvents() {}

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        PacketDistributor.sendToPlayer(
                player,
                new SyncServerConfigS2C(
                    ServerConfig.baseRateBountyHunter,
                    ServerConfig.percentageRewardBountyHunter
                )
        );
    }
}