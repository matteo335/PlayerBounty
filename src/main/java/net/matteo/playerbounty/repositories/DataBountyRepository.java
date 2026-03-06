package net.matteo.playerbounty.repositories;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.matteo.playerbounty.capabilities.PlayerDataBountyCapabilities;
import net.matteo.playerbounty.capabilities.PlayerDataBountyCapabilities.PlayerDataBounty;

public class DataBountyRepository {

    private static PlayerDataBounty dataBounty(Entity entity) {
        return entity.getData(PlayerDataBountyCapabilities.PLAYER_DATA_BOUNTY);
    }
    public static boolean hasDataBounty(Entity entity) {
        return dataBounty(entity) != null;
    }
    
    public static long getPointsBounty(Entity entity) {
        return dataBounty(entity).bountyPoints;
    }

    public static void setPointsBounty(Entity entity, long points) {
        dataBounty(entity).bountyPoints = Math.max(0, points);

        if (entity instanceof ServerPlayer serverPlayer) {
            serverPlayer.getData(PlayerDataBountyCapabilities.PLAYER_DATA_BOUNTY).syncPlayerDataBounty(serverPlayer);
        }
    }

    public static void addPointsBounty(Entity entity, long points) {
        dataBounty(entity).bountyPoints = Math.max(0, dataBounty(entity).bountyPoints + points);

        if (entity instanceof ServerPlayer serverPlayer) {
            serverPlayer.getData(PlayerDataBountyCapabilities.PLAYER_DATA_BOUNTY).syncPlayerDataBounty(serverPlayer);
        }
    }

    public static void removePointsBounty(Entity entity, long points) {
        dataBounty(entity).bountyPoints = Math.max(0, dataBounty(entity).bountyPoints - points);

        if (entity instanceof ServerPlayer serverPlayer) {
            serverPlayer.getData(PlayerDataBountyCapabilities.PLAYER_DATA_BOUNTY).syncPlayerDataBounty(serverPlayer);
        }
    }

}
