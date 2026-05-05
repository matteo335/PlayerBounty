package net.matteo.playerbounty.old;

import net.matteo.playerbounty.configs.Config;
import net.matteo.playerbounty.utils.Timer;
import net.matteo.playerbounty.PlayerBountyMod;
import net.matteo.playerbounty.configs.MagicCoinsConfig;

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

import net.sirgrantd.sg_economy.api.SGEconomyApi;
import net.sirgrantd.sg_economy.capabilities.CoinsBagCapabilities;

import java.util.concurrent.atomic.AtomicInteger;

@EventBusSubscriber
public class DisplayEvents implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<DisplayEvents> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PlayerBountyMod.MOD_ID, "display"));

    public static void packetDisplay(Player player, String bountydisplay1, int bounty, String bountydisplay2) {
        bountyTags(player, bountydisplay1, bounty, bountydisplay2);
        PacketDistributor. sendToAllPlayers(new NetworkDisplay(bountydisplay1, bounty, bountydisplay2, player.getId()));
    }

    public static void bountyTags(Player player, String bountydisplay1, double Bounty, String bountydisplay2) {
        CompoundTag tag = player.getPersistentData();

        if (!Config.DisableDisplay.get()) {
            tag.putString("bountydisplay1", bountydisplay1);
            tag.putDouble("bounty", Bounty);
            tag.putString("bountydisplay2", bountydisplay2);
            player.refreshDisplayName();
        }
    }

    @SubscribeEvent
    public static void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        String playerName = event.getEntity().getName().getString();
        CompoundTag tag = event.getEntity().getPersistentData();

        if (!player.getCommandSenderWorld().isClientSide) {
            if (Config.IsPlayerBountyDisplayEnabled.get() && !MagicCoinsConfig.IsMagicCoinsDisplayEnabled.get()) {
                if (tag.contains("bounty")) {
                    packetDisplay(player, tag.getString("bountydisplay1"), tag.getInt("bounty"), tag.getString("bountydisplay2"));

                    for (Player playerlist : player.getServer().getPlayerList().getPlayers()) {
                        PacketDistributor.sendToPlayer(player, new NetworkDisplay(
                                playerlist.getPersistentData().getString("bountydisplay1"),
                                playerlist.getPersistentData().getInt("bounty"),
                                playerlist.getPersistentData().getString("bountydisplay2"),
                                playerlist.getId()));
                    }
                }
            }
        } else if (!Config.IsPlayerBountyDisplayEnabled.get() && MagicCoinsConfig.IsMagicCoinsDisplayEnabled.get()) {
            packetDisplay(
                    player,
                    playerName + MagicCoinsConfig.CoinsDisplay1.get(),
                    (int) SGEconomyApi.get().getBalance(player),
                    MagicCoinsConfig.CoinsDisplay2.get()
            );

            for (Player playerlist : player.getServer().getPlayerList().getPlayers()) {
                PacketDistributor.sendToPlayer(player, new NetworkDisplay(
                        playerName
                                + MagicCoinsConfig.CoinsDisplay1.get(),
                        (int) SGEconomyApi.get().getBalance(player),
                        MagicCoinsConfig.CoinsDisplay2.get(),
                        playerlist.getId())
                );

                player.refreshDisplayName();
                playerlist.refreshDisplayName();
                //Enable a constant loop to update the displays updateLoop(player, new PlayerEvent.NameFormat(player, player.getDisplayName()));
            }
        } else if (Config.IsPlayerBountyDisplayEnabled.get() && MagicCoinsConfig.IsMagicCoinsDisplayEnabled.get()) {
            packetDisplay(
                    player,
                    playerName
                            + MagicCoinsConfig.CoinsDisplay1.get()
                            + (int) SGEconomyApi.get().getBalance(player)
                            + MagicCoinsConfig.CoinsDisplay2.get()
                            + Config.BountyDisplay1.get(),
                    tag.getInt("bounty"),
                    Config.BountyDisplay2.get()
            );

            for (Player playerlist : player.getServer().getPlayerList().getPlayers()) {
            PacketDistributor.sendToPlayer(player, new NetworkDisplay(
                    playerName
                            + MagicCoinsConfig.CoinsDisplay1.get()
                            + (int) SGEconomyApi.get().getBalance(player)
                            + MagicCoinsConfig.CoinsDisplay2.get()
                            + Config.BountyDisplay1.get(), playerlist.getPersistentData().getInt("bounty"),
                    MagicCoinsConfig.CoinsDisplay2.get(),
                    playerlist.getId())
            );

                player.refreshDisplayName();
                playerlist.refreshDisplayName();
                //Enable a constant loop to update the displays updateLoop(player, new PlayerEvent.NameFormat(player, player.getName()));
            }
        }
    }


    @SubscribeEvent
    public static void renderName(PlayerEvent.NameFormat event) {
        CompoundTag tag = event.getEntity().getPersistentData();
        Player player = event.getEntity();
        String playerName = event.getEntity().getName().getString();
        int ticks = 1;

        if (Config.IsPlayerBountyDisplayEnabled.get() && !MagicCoinsConfig.IsMagicCoinsDisplayEnabled.get()) {
            if (tag.contains("bounty")) {
                event.setDisplayname(Component.translatable(tag.getString("bountydisplay1") + tag.getInt("bounty") + tag.getString("bountydisplay2")));
            }
        }

        else if (!Config.IsPlayerBountyDisplayEnabled.get() && MagicCoinsConfig.IsMagicCoinsDisplayEnabled.get()) {
            event.setDisplayname(Component.translatable(playerName
                    + MagicCoinsConfig.CoinsDisplay1.get()
                    + player.getData(CoinsBagCapabilities.COINS_IN_BAG).valueTotalInCoins
                    + MagicCoinsConfig.CoinsDisplay2.get()));

            //Enable a constant loop to update the displays Utils.runLater(BountyConfig.CoinsDisplayTimer.get(), updateLoop(event.getEntity(), new PlayerEvent.NameFormat(event.getEntity(), event.getEntity().getName())));
        } else if (Config.IsPlayerBountyDisplayEnabled.get() && MagicCoinsConfig.IsMagicCoinsDisplayEnabled.get()) {
            event.setDisplayname(Component.literal(playerName
                    + MagicCoinsConfig.CoinsDisplay1.get()
                    + (int) SGEconomyApi.get().getBalance(event.getEntity())
                    + MagicCoinsConfig.CoinsDisplay2.get()
                    + Config.BountyDisplay1.get()
                    + tag.getInt("bounty")
                    + Config.BountyDisplay2.get()));

            //Enable a constant loop to update the displays updateLoop(event.getEntity(), new PlayerEvent.NameFormat(event.getEntity(), event.getEntity().getDisplayName()));
        }
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();

        if (Config.IsPlayerBountyDisplayEnabled.get()) {
            if (oldPlayer.getPersistentData().contains("bounty")) {
                String bountydisplay1 = oldPlayer.getPersistentData().getString("bountydisplay1");
                int bounty = oldPlayer.getPersistentData().getInt("bounty");
                String bountydisplay2 = oldPlayer.getPersistentData().getString("bountydisplay2");

                newPlayer.getPersistentData().putString("bountydisplay1", bountydisplay1);
                newPlayer.getPersistentData().putInt("bounty", bounty);
                newPlayer.getPersistentData().putString("bountydisplay2", bountydisplay2);
            }
        }

        if (MagicCoinsConfig.MagicCoinsSystem.get()) {
            SGEconomyApi.get().getBalance(newPlayer);
        }
    }

    @SubscribeEvent
    public static void onTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof ServerPlayer player) {
                ServerPlayer display = (ServerPlayer) event.getEntity();
                CompoundTag tag = player.getPersistentData();
                String playerName = event.getEntity().getName().getString();

            if (Config.IsPlayerBountyDisplayEnabled.get() && !MagicCoinsConfig.IsMagicCoinsDisplayEnabled.get()) {
                if (player.getPersistentData().contains("bounty")) {
                    PacketDistributor.sendToPlayer(display, new NetworkDisplay(
                            tag.getString("bountydisplay1"),
                            tag.getInt("bounty"),
                            tag.getString("bountydisplay2"),
                            player.getId()));
                }

            } else if (!Config.IsPlayerBountyDisplayEnabled.get() && MagicCoinsConfig.IsMagicCoinsDisplayEnabled.get()) {

                PacketDistributor.sendToPlayer(display, new NetworkDisplay(
                        MagicCoinsConfig.CoinsDisplay1.get(),
                        (int) SGEconomyApi.get().getBalance(player),
                        MagicCoinsConfig.CoinsDisplay2.get(),
                        player.getId())
                );

            } else if (Config.IsPlayerBountyDisplayEnabled.get() && MagicCoinsConfig.IsMagicCoinsDisplayEnabled.get()) {
                if (event.getTarget() instanceof Player player1) {
                    if (player1.getPersistentData().contains("bounty")) {
                        ServerPlayer display1 = (ServerPlayer) event.getEntity();

                        PacketDistributor.sendToPlayer(display1, new NetworkDisplay(

                                MagicCoinsConfig.CoinsDisplay1.get()
                                        + (int) SGEconomyApi.get().getBalance(player)
                                        + MagicCoinsConfig.CoinsDisplay2.get()
                                        + Config.BountyDisplay1.get(),
                                tag.getInt("bounty"),
                                tag.getString("bountydisplay2"),
                                player.getId())
                        );

                    }
                }
            }
        }
    }

    //Constant loop to update the displays
    public static void updateLoop(Player player, PlayerEvent.NameFormat name) {
        AtomicInteger tick = new AtomicInteger();

        if (tick.get() == 0) {
            Timer.runLater(MagicCoinsConfig.CoinsDisplayTimer.get(), () -> {

                name.setDisplayname(Component.translatable(player.getName().getString()
                        + MagicCoinsConfig.CoinsDisplay1.get()
                        + (int) SGEconomyApi.get().getBalance(player)
                        + MagicCoinsConfig.CoinsDisplay2.get())
                );

                renderName(name);
                onTracking(new PlayerEvent.StartTracking(player, player));
                tick.addAndGet(1);
            });
        } else if (tick.get() != 0) {
            Timer.runLater(MagicCoinsConfig.CoinsDisplayTimer.get(), () -> tick.addAndGet(-1));
        } //new PlayerEvent.NameFormat is a bit broken, let's fix tomorrow
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}