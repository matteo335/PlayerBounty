package net.matteo.playerbounty.events;

import net.matteo.playerbounty.configs.Config;
import net.matteo.playerbounty.network.Packets;
import net.matteo.playerbounty.utils.GetValues;
import net.matteo.playerbounty.utils.Cooldowns;
import net.matteo.playerbounty.utils.mods.CreateNumismaticsUtils;

import net.sirgrantd.sg_economy.api.SGEconomyApi;
import tallestred.numismaticoverhaul.cap.CurrencyHolder;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import net.minecraft.world.entity.player.Player;

@EventBusSubscriber
public class DisplayEvents {

    @SubscribeEvent
    public static void renderName(PlayerEvent.NameFormat event) {
        event.setDisplayname(GetValues.name(event.getEntity()));
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();

        if (!Config.System.get()) return;
        if (!oldPlayer.getPersistentData().contains("bounty")) return;

        double bounty = oldPlayer.getPersistentData().getDouble("bounty");
        newPlayer.getPersistentData().putDouble("bounty", bounty);
    }

    //Tick -> networking -> refreshDisplayName -> renderName
    @SubscribeEvent
    public static void tick(ServerTickEvent.Post event) {
        Player player = event.getServer().overworld().getRandomPlayer();
        if (player == null) return;
        if (Cooldowns.isPlayerInCooldown(player.getUUID())) return;

        if (Config.System.get()) GetValues.playerbounty.put(player.getUUID(), (int) player.getPersistentData().getDouble("bounty"));

        if (GetValues.sg_economy_system) GetValues.sg_economy.put(player.getUUID(), SGEconomyApi.get().getBalanceAsInt(player));

        if (GetValues.numismatic_overhaul_system) GetValues.numismaticoverhaul.put(player.getUUID(), CurrencyHolder.getValue(player));

        if (GetValues.create_numismatics_system) GetValues.create_numismatics.put(player.getUUID(), CreateNumismaticsUtils.spurValue(player, false) / 64);

        PacketDistributor.sendToAllPlayers(new Packets(
                player.getId(),
                Config.EnableDisplay.get() ? GetValues.playerbounty.get(player.getUUID()) : null,
                GetValues.sg_economy_display ? GetValues.sg_economy.get(player.getUUID()) : null,
                GetValues.numismatic_overhaul_display ? GetValues.numismaticoverhaul.get(player.getUUID()) : null,
                GetValues.create_numismatics_display ? GetValues.create_numismatics.get(player.getUUID()) : null
        ));

        player.refreshDisplayName();
    }
}