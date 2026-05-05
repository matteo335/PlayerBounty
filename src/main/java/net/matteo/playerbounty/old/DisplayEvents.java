package net.matteo.playerbounty.old;

import net.matteo.playerbounty.configs.Config;
import net.matteo.playerbounty.utils.Timer;
import net.matteo.playerbounty.PlayerBountyMod;
import net.matteo.playerbounty.configs.SGEconomyConfig;

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
import javax.annotation.Nullable;

@EventBusSubscriber
public class DisplayEvents implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<DisplayEvents> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PlayerBountyMod.MOD_ID, "display"));

    public static void packetDisplay(Player player, String bountydisplay1, int bounty, String bountydisplay2,
                                     @Nullable String bountydisplay3, @Nullable Integer bounty2, @Nullable String bountydisplay4) {

        bountyTags(player, bountydisplay1, bounty, bountydisplay2);
        PacketDistributor.sendToAllPlayers(new NetworkDisplay(bountydisplay1, bounty, bountydisplay2, player, null, null, null));
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
        CompoundTag tag = event.getEntity().getPersistentData();

        if (!player.getCommandSenderWorld().isClientSide) {
            if (Config.IsPlayerBountyDisplayEnabled.get() && !SGEconomyConfig.IsCoinsDisplayEnabled.get()) {
                if (tag.contains("bounty")) {
                    packetDisplay(
                            player, tag.getString("bountydisplay1"), (int) tag.getDouble("bounty"),
                            tag.getString("bountydisplay2"), null, null, null);

                    for (Player playerlist : player.getServer().getPlayerList().getPlayers()) {
                        PacketDistributor.sendToPlayer(player, new NetworkDisplay(
                                Config.BountyDisplay1.get(),
                                (int) playerlist.getPersistentData().getDouble("bounty"),
                                Config.BountyDisplay2.get(),
                                playerlist, null, null, null));
                    }
                }
            }
        } else if (!Config.IsPlayerBountyDisplayEnabled.get() && SGEconomyConfig.IsCoinsDisplayEnabled.get()) {
            packetDisplay(
                    player,
                    SGEconomyConfig.CoinsDisplay1.get(),
                    (int) SGEconomyApi.get().getBalance(player),
                    SGEconomyConfig.CoinsDisplay2.get(),
                    null, null, null);

            for (Player playerlist : player.getServer().getPlayerList().getPlayers()) {
                PacketDistributor.sendToPlayer(player, new NetworkDisplay(
                        SGEconomyConfig.CoinsDisplay1.get(),
                        (int) SGEconomyApi.get().getBalance(player),
                        SGEconomyConfig.CoinsDisplay2.get(),
                        playerlist, null, null, null)
                );

                player.refreshDisplayName();
                playerlist.refreshDisplayName();
                //Enable a constant loop to update the displays updateLoop(player, new PlayerEvent.NameFormat(player, player.getDisplayName()));
            }
        } else if (Config.IsPlayerBountyDisplayEnabled.get() && SGEconomyConfig.IsCoinsDisplayEnabled.get()) {
            packetDisplay(player,
                    SGEconomyConfig.CoinsDisplay1.get(),
                    SGEconomyApi.get().getBalanceAsInt(player),
                    SGEconomyConfig.CoinsDisplay2.get(),
                    Config.BountyDisplay1.get(),
                    (int) tag.getDouble("bounty"),
                    Config.BountyDisplay2.get()
            );

            player.refreshDisplayName();
            //playerlist.refreshDisplayName();
            //Enable a constant loop to update the displays updateLoop(player, new PlayerEvent.NameFormat(player, player.getName()));
        }
    }


    @SubscribeEvent
    public static void renderName(PlayerEvent.NameFormat event) {
        CompoundTag tag = event.getEntity().getPersistentData();
        Player player = event.getEntity();
        String playerName = event.getEntity().getName().getString();
        int ticks = 1;

        if (Config.IsPlayerBountyDisplayEnabled.get() && !SGEconomyConfig.IsCoinsDisplayEnabled.get()) {
            if (tag.contains("bounty")) {
                event.setDisplayname(Component.translatable(tag.getString("bountydisplay1") + tag.getDouble("bounty") + tag.getString("bountydisplay2")));
            }
        } else if (!Config.IsPlayerBountyDisplayEnabled.get() && SGEconomyConfig.IsCoinsDisplayEnabled.get()) {
            event.setDisplayname(Component.translatable(playerName
                    + SGEconomyConfig.CoinsDisplay1.get()
                    + player.getData(CoinsBagCapabilities.COINS_IN_BAG).valueTotalInCoins
                    + SGEconomyConfig.CoinsDisplay2.get()));

            //Enable a constant loop to update the displays Utils.runLater(BountyConfig.CoinsDisplayTimer.get(), updateLoop(event.getEntity(), new PlayerEvent.NameFormat(event.getEntity(), event.getEntity().getName())));
        } else if (Config.IsPlayerBountyDisplayEnabled.get() && SGEconomyConfig.IsCoinsDisplayEnabled.get()) {
            event.setDisplayname(Component.literal(playerName
                    + SGEconomyConfig.CoinsDisplay1.get()
                    + (int) SGEconomyApi.get().getBalance(event.getEntity())
                    + SGEconomyConfig.CoinsDisplay2.get()
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

        if (SGEconomyConfig.CoinsSystem.get()) {
            SGEconomyApi.get().getBalance(newPlayer);
        }
    }

    @SubscribeEvent
    public static void onTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof ServerPlayer player) {
            ServerPlayer display = (ServerPlayer) event.getEntity();
            CompoundTag tag = player.getPersistentData();
            String playerName = event.getEntity().getName().getString();

            if (Config.IsPlayerBountyDisplayEnabled.get() && !SGEconomyConfig.IsCoinsDisplayEnabled.get()) {
                if (player.getPersistentData().contains("bounty")) {
                    PacketDistributor.sendToPlayer(display, new NetworkDisplay(
                            Config.BountyDisplay1.get(),
                            tag.getInt("bounty"),
                            Config.BountyDisplay2.get(),
                            player, null, null, null));
                }

            } else if (!Config.IsPlayerBountyDisplayEnabled.get() && SGEconomyConfig.IsCoinsDisplayEnabled.get()) {

                PacketDistributor.sendToPlayer(display, new NetworkDisplay(
                        SGEconomyConfig.CoinsDisplay1.get(),
                        SGEconomyApi.get().getBalanceAsInt(player),
                        SGEconomyConfig.CoinsDisplay2.get(),
                        player, null, null, null)
                );

            } else if (Config.IsPlayerBountyDisplayEnabled.get() && SGEconomyConfig.IsCoinsDisplayEnabled.get()) {
                PacketDistributor.sendToPlayer(display, new NetworkDisplay(

                        SGEconomyConfig.CoinsDisplay1.get(),
                        SGEconomyApi.get().getBalanceAsInt(player),
                        SGEconomyConfig.CoinsDisplay2.get(),
                        player,
                        Config.BountyDisplay1.get(),
                        (int) tag.getDouble("bounty"),
                        Config.BountyDisplay2.get())
                );
            }
        }
    }

    //Constant loop to update the displays
    public static void updateLoop(Player player, PlayerEvent.NameFormat name) {
        AtomicInteger tick = new AtomicInteger();

        if (tick.get() == 0) {
            Timer.runLater(SGEconomyConfig.CoinsDisplayTimer.get(), () -> {

                name.setDisplayname(Component.translatable(player.getName().getString()
                        + SGEconomyConfig.CoinsDisplay1.get()
                        + (int) SGEconomyApi.get().getBalance(player)
                        + SGEconomyConfig.CoinsDisplay2.get())
                );

                renderName(name);
                onTracking(new PlayerEvent.StartTracking(player, player));
                tick.addAndGet(1);
            });
        } else if (tick.get() != 0) {
            Timer.runLater(SGEconomyConfig.CoinsDisplayTimer.get(), () -> tick.addAndGet(-1));
        } //new PlayerEvent.NameFormat is a bit broken, let's fix tomorrow
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}