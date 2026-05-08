package net.matteo.playerbounty.events;

import net.matteo.playerbounty.configs.Config;
import net.matteo.playerbounty.network.Packets;
import net.matteo.playerbounty.PlayerBountyMod;
import net.matteo.playerbounty.configs.SGEconomyConfig;

import net.matteo.playerbounty.utils.Cooldowns;
import net.matteo.playerbounty.utils.GetValues;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.sirgrantd.sg_economy.api.SGEconomyApi;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber
public class DisplayEvents {
    public static final Map<UUID, Integer> coins = new HashMap<>();
    public static final Map<UUID, Integer> bounty = new HashMap<>();

    @SubscribeEvent
    public static void renderName(PlayerEvent.NameFormat event) {
        String string = GetValues.name(event.getEntity());

        event.setDisplayname(Component.translatable(string));
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();

        if (!Config.DefaultSystem.get()) return;
        if (!oldPlayer.getPersistentData().contains("bounty")) return;

        double bounty = oldPlayer.getPersistentData().getDouble("bounty");
        newPlayer.getPersistentData().putDouble("bounty", bounty);
    }

    ///Tick -> refreshDisplayName -> renderName -> setDisplayName
    @SubscribeEvent
    public static void tick(ServerTickEvent.Post event) {
        Player player = event.getServer().overworld().getRandomPlayer();
        if (player == null) return;
        if (Cooldowns.isPlayerInCooldown(player.getUUID())) return;

        CompoundTag tag = player.getPersistentData();

        if (PlayerBountyMod.sg_economy_config) {
            coins.put(player.getUUID(), SGEconomyApi.get().getBalanceAsInt(player));
        }

        bounty.put(player.getUUID(), (int) tag.getDouble("bounty"));
        if (Config.EnableDisplay.get() || SGEconomyConfig.EnableDisplay.get()) {
            player.refreshDisplayName();
        }

        PacketDistributor.sendToAllPlayers(new Packets((int) tag.getDouble("bounty"), player.getId(), PlayerBountyMod.sg_economy_config ? coins.get(player.getUUID()) : null));
    }
}