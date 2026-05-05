package net.matteo.playerbounty.compats;

import net.minecraft.server.level.ServerPlayer;
import net.sirgrantd.sg_economy.api.SGEconomyApi;

public class SG_Economy {

    public static void handleKillerEconomy(ServerPlayer killer, ServerPlayer target) {
        double killerCoins = SGEconomyApi.get().getBalance(killer);

        SGEconomyApi.get().setBalance(killer, (int) killerCoins);
    }
}
