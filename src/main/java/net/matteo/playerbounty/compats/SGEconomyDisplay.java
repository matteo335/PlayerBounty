package net.matteo.playerbounty.compats;

import net.matteo.playerbounty.PlayerBountyMod;
import net.matteo.playerbounty.configs.Config;
import net.matteo.playerbounty.configs.SGEconomyConfig;
import net.matteo.playerbounty.network.Packets;
import net.matteo.playerbounty.events.DisplayEvents;

import net.sirgrantd.sg_economy.api.SGEconomyApi;
import net.sirgrantd.sg_economy.capabilities.CoinsBagCapabilities;

import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Objects;

@EventBusSubscriber
public class SGEconomyDisplay {

    private static final Map<UUID, Long> cooldown = new HashMap<>();

    public static void onJoin(Player player) {
        CompoundTag tag = player.getPersistentData();

        if (Config.EnableDisplay.get() && !SGEconomyConfig.EnableDisplay.get()) {
            if (!tag.contains("bounty")) return;

            DisplayEvents.updateDisplay(player.getId(), player.level(), (int) tag.getDouble("bounty"), null);

            for (Player playerlist : player.getServer().getPlayerList().getPlayers()) {
                PacketDistributor.sendToPlayer((ServerPlayer) player,
                        new Packets((int) playerlist.getPersistentData().getDouble("bounty"), playerlist.getId(), null));

                //player.refreshDisplayName();
                //playerlist.refreshDisplayName();
                //updateLoop(new PlayerEvent.NameFormat(player, player.getName()));
            }
        } else if (!Config.EnableDisplay.get() && SGEconomyConfig.EnableDisplay.get()) {

            DisplayEvents.updateDisplay(player.getId(), player.level(), (int) SGEconomyApi.get().getBalance(player), null);

            PacketDistributor.sendToPlayer((ServerPlayer) player, new Packets((int) SGEconomyApi.get().getBalance(player), player.getId(), null));

            //for (Player playerlist : player.getServer().getPlayerList().getPlayers()) {
                //PacketDistributor.sendToPlayer((ServerPlayer) playerlist, new Packets((int) SGEconomyApi.get().getBalance(player), playerlist.getId(), null));

                //player.getServer().getPlayerList().getPlayer(playerlist.getUUID()).refreshTabListName();
                //playerlist.getServer().getPlayerList().getPlayer(playerlist.getUUID()).refreshTabListName();
                //playerlist.refreshDisplayName();
                //updateLoop(playerlist, new PlayerEvent.NameFormat(playerlist, playerlist.getName()));
            //}

        } else if (Config.EnableDisplay.get() && SGEconomyConfig.EnableDisplay.get()) {
            DisplayEvents.updateDisplay(player.getId(), player.level(), SGEconomyApi.get().getBalanceAsInt(player), (int) tag.getDouble("bounty"));

            PacketDistributor.sendToPlayer((ServerPlayer) player, new Packets(
                    (int) tag.getDouble("bounty"),
                    SGEconomyApi.get().getBalanceAsInt(player),
                    player.getId()));

            for (Player playerlist : player.getServer().getPlayerList().getPlayers()) {
                //PlayerBountyMod.LOGGER.info("onJoin playerlist\nPlayer: {}\nPlayerlist: {}", player.getName().getString(), playerlist.getName().getString());

                PacketDistributor.sendToPlayer((ServerPlayer) player, new Packets(
                        (int) tag.getDouble("bounty"),
                        SGEconomyApi.get().getBalanceAsInt(player),
                        playerlist.getId()));

                //player.getServer().getPlayerList().getPlayer(playerlist.getUUID()).refreshTabListName();
                //playerlist.getServer().getPlayerList().getPlayer(playerlist.getUUID()).refreshTabListName();
                //playerlist.refreshDisplayName();
                //renderName(new PlayerEvent.NameFormat(playerlist, playerlist.getName()));
            }
            //updateLoop(player, new PlayerEvent.NameFormat(player, player.getName()));
        }
    }

    public static void renderName(PlayerEvent.NameFormat event) {
        if (!Config.EnableDisplay.get() && !SGEconomyConfig.EnableDisplay.get()) return;
        CompoundTag tag = event.getEntity().getPersistentData();
        Player player = event.getEntity();
        String playerName = player.getName().getString();

        if (Config.EnableDisplay.get() && !SGEconomyConfig.EnableDisplay.get()) {
            if (!tag.contains("bounty")) return;

            event.setDisplayname(Component.translatable(
                    Config.BountyDisplay1.get()
                            + (int) tag.getDouble("bounty")
                            + Config.BountyDisplay2.get()));

        } else if (!Config.EnableDisplay.get() && SGEconomyConfig.EnableDisplay.get()) {
            event.setDisplayname(Component.translatable(playerName
                    + SGEconomyConfig.CoinsDisplay1.get()
                    + player.getData(CoinsBagCapabilities.COINS_IN_BAG).valueTotalInCoins
                    + SGEconomyConfig.CoinsDisplay2.get()));

            //Enable a constant loop to update the displays Utils.runLater(BountyConfig.CoinsDisplayTimer.get(), updateLoop(event.getEntity(), new PlayerEvent.NameFormat(event.getEntity(), event.getEntity().getName())));
        } else if (Config.EnableDisplay.get() && SGEconomyConfig.EnableDisplay.get()) {
            //PlayerBountyMod.LOGGER.info("renderName {}", playerName);

            if (cooldown.containsKey(event.getEntity().getUUID()) && System.currentTimeMillis() < cooldown.get(event.getEntity().getUUID())) return;
            cooldown.put(event.getEntity().getUUID(), System.currentTimeMillis() + (SGEconomyConfig.CoinsDisplayTimer.get() * 50L));
            PlayerBountyMod.LOGGER.info("ran");

            String string =
                    event.getEntity().getName().getString()
                            + SGEconomyConfig.CoinsDisplay1.get()
                            + SGEconomyApi.get().getBalanceAsInt(event.getEntity())
                            + SGEconomyConfig.CoinsDisplay2.get()
                            + Config.BountyDisplay1.get()
                            + (int) event.getEntity().getPersistentData().getDouble("bounty")
                            + Config.BountyDisplay2.get();

            PlayerBountyMod.LOGGER.info(string);

            event.setDisplayname(Component.literal(string));
            //event.getEntity().setCustomName(Component.literal(string));
            //renderName(event);
        }
    }

    public static void onTracking(PlayerEvent.StartTracking event) {
        Player target = (Player) event.getTarget();
        Player player = event.getEntity();
        CompoundTag tag = target.getPersistentData();

        if (Config.EnableDisplay.get() && !SGEconomyConfig.EnableDisplay.get()) {
            if (!target.getPersistentData().contains("bounty")) return;
            PacketDistributor.sendToPlayer((ServerPlayer) player, new Packets((int) tag.getDouble("bounty"), target.getId(), null));

        } else if (!Config.EnableDisplay.get() && SGEconomyConfig.EnableDisplay.get()) {
            PacketDistributor.sendToPlayer((ServerPlayer) player, new Packets(SGEconomyApi.get().getBalanceAsInt(target), target.getId(), null));

        } else if (Config.EnableDisplay.get() && SGEconomyConfig.EnableDisplay.get()) {
            PlayerBountyMod.LOGGER.info("tracking: " + player.getName().getString());

            PacketDistributor.sendToPlayer((ServerPlayer) player,
                    new Packets(SGEconomyApi.get().getBalanceAsInt(target), target.getId(), (int) tag.getDouble("bounty")));
        }
    }

    //Constant loop to update the displays
    public static void updateLoop(PlayerEvent.NameFormat event) {
        if (cooldown.containsKey(event.getEntity().getUUID()) && System.currentTimeMillis() < cooldown.get(event.getEntity().getUUID())) return;
        PlayerBountyMod.LOGGER.info("ran");

        cooldown.put(event.getEntity().getUUID(), System.currentTimeMillis() + (SGEconomyConfig.CoinsDisplayTimer.get() * 50L));

        String playerName =
                event.getEntity().getName().getString()
                        + SGEconomyConfig.CoinsDisplay1.get()
                        + SGEconomyApi.get().getBalanceAsInt(event.getEntity())
                        + SGEconomyConfig.CoinsDisplay2.get()
                        + Config.BountyDisplay1.get()
                        + (int) event.getEntity().getPersistentData().getDouble("bounty")
                        + Config.BountyDisplay2.get();

        event.setDisplayname(Component.literal(playerName));
        PlayerBountyMod.LOGGER.info(event.getDisplayname());
    }

    @SubscribeEvent
    public static void tick(ServerTickEvent.Post event) {
        Player player = event.getServer().overworld().getRandomPlayer();

        if (player == null) return;
        //renderName(new PlayerEvent.NameFormat(player, playr.getName()));
        //player.refreshDisplayName();
    }
}