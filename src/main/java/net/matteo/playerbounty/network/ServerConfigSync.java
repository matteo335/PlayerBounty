package net.matteo.playerbounty.network;

import net.matteo.playerbounty.configs.ServerConfig;
import net.matteo.playerbounty.network.payload.SyncServerConfigS2C;

import net.matteo.playerbounty.utils.Timer;

import net.neoforged.neoforge.network.PacketDistributor;

public final class ServerConfigSync {
    private ServerConfigSync() {
    }

    public static void syncToAllPlayersNextTick() {

        Timer.queueServerWork(1, () -> {
            PacketDistributor.sendToAllPlayers(new SyncServerConfigS2C(
                    ServerConfig.baseRateBountyHunter,
                    ServerConfig.percentageRewardBountyHunter));
        });
    }
}