package net.matteo.playerbounty.old;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import net.matteo.playerbounty.configs.ServerConfig;
import net.matteo.playerbounty.utils.Timer;
import net.matteo.playerbounty.PlayerBountyMod;

import net.sirgrantd.sg_economy.api.SGEconomyApi;
import net.sirgrantd.sg_economy.capabilities.CoinsBagCapabilities;

import java.util.concurrent.atomic.AtomicInteger;

@EventBusSubscriber
public class DisplayEvents implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<DisplayEvents> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PlayerBountyMod.MOD_ID, "display"));

    public static void packetDisplay(Player player, String bountydisplay1, double bounty, String bountydisplay2) {
        bountyTags(player, bountydisplay1, bounty, bountydisplay2);
        PacketDistributor.sendToAllPlayers(new NetworkDisplay(bountydisplay1, bounty, bountydisplay2, player.getId()));
    }

    public static void bountyTags(Player player, String bountydisplay1, double Bounty, String bountydisplay2) {
        CompoundTag tag = player.getPersistentData();

        if (!ServerConfig.DisableDisplay.get()) {
            tag.putString("bountydisplay1", bountydisplay1);
            tag.putDouble("bounty", Bounty);
            tag.putString("bountydisplay2", bountydisplay2);
            player.refreshDisplayName();
        }
    }

    @SubscribeEvent
    public static void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();

        if (!player.getCommandSenderWorld().isClientSide) {
            if (ServerConfig.IsPlayerBountyDisplayEnabled.get() && !ServerConfig.IsMagicCoinsDisplayEnabled.get()) {
                if (player.getPersistentData().contains("bounty")) {
                    packetDisplay(player, player.getPersistentData().getString("bountydisplay1"), player.getPersistentData().getInt("bounty"), player.getPersistentData().getString("bountydisplay2"));

                    for (Player playerlist : player.getServer().getPlayerList().getPlayers()) {
                        PacketDistributor.sendToPlayer(player, new NetworkDisplay(playerlist.getPersistentData().getString("bountydisplay1"), playerlist.getPersistentData().getInt("bounty"), playerlist.getPersistentData().getString("bountydisplay2"), playerlist.getId()));
                    }
                }
            }
        } else if (!ServerConfig.IsPlayerBountyDisplayEnabled.get() && ServerConfig.IsMagicCoinsDisplayEnabled.get()) {
            packetDisplay(player, player.getName().getString() + ServerConfig.CoinsDisplay1.get(), SGEconomyApi.get().getBalance(player), ServerConfig.CoinsDisplay2.get());

            for (Player playerlist : player.getServer().getPlayerList().getPlayers()) {
                PacketDistributor.sendToPlayer(player, new NetworkDisplay(player.getName().getString() + ServerConfig.CoinsDisplay1.get(), SGEconomyApi.get().getBalance(player), ServerConfig.CoinsDisplay2.get(), playerlist.getId()));
                player.refreshDisplayName();
                playerlist.refreshDisplayName();
                //Enable a constant loop to update the displays updateLoop(player, new PlayerEvent.NameFormat(player, player.getDisplayName()));
            }
        } else if (ServerConfig.IsPlayerBountyDisplayEnabled.get() && ServerConfig.IsMagicCoinsDisplayEnabled.get()) {
            packetDisplay(player, player.getName().getString() + ServerConfig.CoinsDisplay1.get() + SGEconomyApi.get().getBalance(player) + ServerConfig.CoinsDisplay2.get() + ServerConfig.BountyDisplay1.get(), player.getPersistentData().getInt("bounty"), ServerConfig.BountyDisplay2.get());

            for (Player playerlist : player.getServer().getPlayerList().getPlayers()) {
            PacketDistributor.sendToPlayer(player, new NetworkDisplay(player.getName().getString() + ServerConfig.CoinsDisplay1.get() + SGEconomyApi.get().getBalance(player) + ServerConfig.CoinsDisplay2.get() + ServerConfig.BountyDisplay1.get(), playerlist.getPersistentData().getInt("bounty"), ServerConfig.CoinsDisplay2.get(), playerlist.getId()));
                player.refreshDisplayName();
                playerlist.refreshDisplayName();
                //Enable a constant loop to update the displays updateLoop(player, new PlayerEvent.NameFormat(player, player.getName()));
            }
        }
    }


    @SubscribeEvent

    public static void renderName(PlayerEvent.NameFormat event) {
        CompoundTag tag = event.getEntity().getPersistentData();
        int ticks = 1;

        if (ServerConfig.IsPlayerBountyDisplayEnabled.get() && !ServerConfig.IsMagicCoinsDisplayEnabled.get()) {
            if (tag.contains("bounty")) {
                event.setDisplayname(Component.translatable(tag.getString("bountydisplay1") + tag.getInt("bounty") + tag.getString("bountydisplay2")));
            }
        }

        else if (!ServerConfig.IsPlayerBountyDisplayEnabled.get() && ServerConfig.IsMagicCoinsDisplayEnabled.get()) {
            event.setDisplayname(Component.translatable(event.getEntity().getName().getString() + ServerConfig.CoinsDisplay1.get() + event.getEntity().getData(CoinsBagCapabilities.COINS_IN_BAG).valueTotalInCoins + ServerConfig.CoinsDisplay2.get()));

            //Enable a constant loop to update the displays Utils.runLater(BountyConfig.CoinsDisplayTimer.get(), updateLoop(event.getEntity(), new PlayerEvent.NameFormat(event.getEntity(), event.getEntity().getName())));
        } else if (ServerConfig.IsPlayerBountyDisplayEnabled.get() && ServerConfig.IsMagicCoinsDisplayEnabled.get()) {
            event.setDisplayname(Component.literal(event.getEntity().getName().getString() + ServerConfig.CoinsDisplay1.get() + SGEconomyApi.get().getBalance(event.getEntity()) + ServerConfig.CoinsDisplay2.get() + ServerConfig.BountyDisplay1.get() + tag.getInt("bounty") + ServerConfig.BountyDisplay2.get()));

            //Enable a constant loop to update the displays updateLoop(event.getEntity(), new PlayerEvent.NameFormat(event.getEntity(), event.getEntity().getDisplayName()));
        }
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();

        if (ServerConfig.IsPlayerBountyDisplayEnabled.get()) {
            if (oldPlayer.getPersistentData().contains("bounty")) {
                String bountydisplay1 = oldPlayer.getPersistentData().getString("bountydisplay1");
                int bounty = oldPlayer.getPersistentData().getInt("bounty");
                String bountydisplay2 = oldPlayer.getPersistentData().getString("bountydisplay2");

                newPlayer.getPersistentData().putString("bountydisplay1", bountydisplay1);
                newPlayer.getPersistentData().putInt("bounty", bounty);
                newPlayer.getPersistentData().putString("bountydisplay2", bountydisplay2);
            }
        }

        if (ServerConfig.MagicCoinsSystem.get()) {
            SGEconomyApi.get().getBalance(newPlayer);
        }
    }

    @SubscribeEvent
    public static void onTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof Player player) {
                ServerPlayer display = (ServerPlayer) event.getEntity();

            if (ServerConfig.IsPlayerBountyDisplayEnabled.get() && !ServerConfig.IsMagicCoinsDisplayEnabled.get()) {
                if (player.getPersistentData().contains("bounty")) {
                    PacketDistributor.sendToPlayer(display, new NetworkDisplay(player.getPersistentData().getString("bountydisplay1"), player.getPersistentData().getInt("bounty"), player.getPersistentData().getString("bountydisplay2"), player.getId()));
                }
            } else if (!ServerConfig.IsPlayerBountyDisplayEnabled.get() && ServerConfig.IsMagicCoinsDisplayEnabled.get()) {
                PacketDistributor.sendToPlayer(display, new NetworkDisplay(player.getName().getString() + ServerConfig.CoinsDisplay1.get(), SGEconomyApi.get().getBalance(player), ServerConfig.CoinsDisplay2.get(), player.getId()));
            } else if (ServerConfig.IsPlayerBountyDisplayEnabled.get() && ServerConfig.IsMagicCoinsDisplayEnabled.get()) {
                PacketDistributor.sendToPlayer(display, new NetworkDisplay(player.getName().getString() + ServerConfig.CoinsDisplay1.get() + SGEconomyApi.get().getBalance(player) + ServerConfig.CoinsDisplay2.get() + ServerConfig.BountyDisplay1.get() + player.getPersistentData().getString("bountydisplay1"), player.getPersistentData().getInt("bounty"), player.getPersistentData().getString("bountydisplay2"), player.getId()));
            }
        }
    }

    //Constant loop to update the displays
    public static void updateLoop(Player player, PlayerEvent.NameFormat name) {
        AtomicInteger tick = new AtomicInteger();

        if (tick.get() == 0) {
            Timer.runLater(ServerConfig.CoinsDisplayTimer.get(), () -> {
                name.setDisplayname(Component.translatable(player.getName().getString() + ServerConfig.CoinsDisplay1.get() + SGEconomyApi.get().getBalance(player) + ServerConfig.CoinsDisplay2.get()));
                renderName(name);
                onTracking(new PlayerEvent.StartTracking(player, player));
                tick.addAndGet(1);
            });
        } else if (tick.get() != 0) {
            Timer.runLater(ServerConfig.CoinsDisplayTimer.get(), () -> tick.addAndGet(-1));
        } //new PlayerEvent.NameFormat is a bit broken, let's fix tomorrow
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}