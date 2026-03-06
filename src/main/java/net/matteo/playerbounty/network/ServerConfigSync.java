package net.matteo.playerbounty.network;

import net.matteo.playerbounty.PlayerBountyMod;
import net.matteo.playerbounty.configs.ServerConfig;
import net.neoforged.neoforge.network.PacketDistributor;
import net.matteo.playerbounty.network.payload.SyncServerConfigS2C;

public final class ServerConfigSync {
    private ServerConfigSync() {
    }

    public static void syncToAllPlayersNextTick() {

        PlayerBountyMod.queueServerWork(1, () -> {
            PacketDistributor.sendToAllPlayers(new SyncServerConfigS2C(
                    ServerConfig.baseRateBountyHunter,
                    ServerConfig.percentageRewardBountyHunter));
        });
    }
}