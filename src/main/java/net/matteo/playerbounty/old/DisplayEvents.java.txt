package matteo.PlayerBounty;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import net.sirgrantd.magic_coins.api.MagicCoinsApi;
import net.sirgrantd.magic_coins.capabilities.CoinsBagCapabilities;

import java.util.concurrent.atomic.AtomicInteger;

@EventBusSubscriber
public class DisplayEvents {

    @SubscribeEvent
    public static void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        if (!player.getCommandSenderWorld().isClientSide) {
            if (BountyConfig.IsPlayerBountyDisplayEnabled.get() && !BountyConfig.IsMagicCoinsDisplayEnabled.get()) {
                if (player.getPersistentData().contains("bounty")) {
                    PlayerBounty.packets((ServerPlayer) player, player.getPersistentData().getString("bountydisplay1"), player.getPersistentData().getInt("bounty"), player.getPersistentData().getString("bountydisplay2"), BountyConfig.DeleteDisplay.get());

                    for (Player playerlist : player.getServer().getPlayerList().getPlayers()) {
                        PacketDistributor.sendToPlayer((ServerPlayer) player, new BountyDisplays(playerlist.getPersistentData().getString("bountydisplay1"), playerlist.getPersistentData().getInt("bounty"), playerlist.getPersistentData().getString("bountydisplay2"), playerlist.getId(), BountyConfig.DeleteDisplay.get()));
                    }
                }
            }
        } else if (!BountyConfig.IsPlayerBountyDisplayEnabled.get() && BountyConfig.IsMagicCoinsDisplayEnabled.get()) {
            PlayerBounty.packets((ServerPlayer) player, player.getName().getString() + BountyConfig.CoinsDisplay1.get(), MagicCoinsApi.getTotalCoins(player), BountyConfig.CoinsDisplay2.get(), BountyConfig.DeleteDisplay.get());

            for (Player playerlist : player.getServer().getPlayerList().getPlayers()) {
                PacketDistributor.sendToPlayer((ServerPlayer) player, new BountyDisplays(player.getName().getString() + BountyConfig.CoinsDisplay1.get(), MagicCoinsApi.getTotalCoins(player), BountyConfig.CoinsDisplay2.get(), playerlist.getId(), BountyConfig.DeleteDisplay.get()));
                player.refreshDisplayName();
                playerlist.refreshDisplayName();
                //Enable a constant loop to update the displays updateLoop(player, new PlayerEvent.NameFormat(player, player.getDisplayName()));
            }
        } else if (BountyConfig.IsPlayerBountyDisplayEnabled.get() && BountyConfig.IsMagicCoinsDisplayEnabled.get()) {
            PlayerBounty.packets((ServerPlayer) player, player.getName().getString() + BountyConfig.CoinsDisplay1.get() + MagicCoinsApi.getTotalCoins(player) + BountyConfig.CoinsDisplay2.get() + BountyConfig.BountyDisplay1.get(), player.getPersistentData().getInt("bounty"), BountyConfig.BountyDisplay2.get(), BountyConfig.DeleteDisplay.get());

            for (Player playerlist : player.getServer().getPlayerList().getPlayers()) {
                PacketDistributor.sendToPlayer((ServerPlayer) player, new BountyDisplays(player.getName().getString() + BountyConfig.CoinsDisplay1.get() + MagicCoinsApi.getTotalCoins(player) + BountyConfig.CoinsDisplay2.get() + BountyConfig.BountyDisplay1.get(), playerlist.getPersistentData().getInt("bounty"), BountyConfig.CoinsDisplay2.get(), playerlist.getId(), BountyConfig.DeleteDisplay.get()));
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

        if (BountyConfig.IsPlayerBountyDisplayEnabled.get() && !BountyConfig.IsMagicCoinsDisplayEnabled.get()) {
            if (tag.contains("bounty")) {
                event.setDisplayname(Component.translatable(tag.getString("bountydisplay1") + tag.getInt("bounty") + tag.getString("bountydisplay2")));
            }
        }

        else if (!BountyConfig.IsPlayerBountyDisplayEnabled.get() && BountyConfig.IsMagicCoinsDisplayEnabled.get()) {
            event.setDisplayname(Component.translatable(event.getEntity().getName().getString() + BountyConfig.CoinsDisplay1.get() + event.getEntity().getData(CoinsBagCapabilities.COINS_IN_BAG).valueTotalInCoins + BountyConfig.CoinsDisplay2.get()));

            //Enable a constant loop to update the displays Utils.runLater(BountyConfig.CoinsDisplayTimer.get(), updateLoop(event.getEntity(), new PlayerEvent.NameFormat(event.getEntity(), event.getEntity().getName())));
        } else if (BountyConfig.IsPlayerBountyDisplayEnabled.get() && BountyConfig.IsMagicCoinsDisplayEnabled.get()) {
            event.setDisplayname(Component.literal(event.getEntity().getName().getString() + BountyConfig.CoinsDisplay1.get() + MagicCoinsApi.getTotalCoins(event.getEntity()) + BountyConfig.CoinsDisplay2.get() + BountyConfig.BountyDisplay1.get() + tag.getInt("bounty") + BountyConfig.BountyDisplay2.get()));

            //Enable a constant loop to update the displays updateLoop(event.getEntity(), new PlayerEvent.NameFormat(event.getEntity(), event.getEntity().getDisplayName()));
        }
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();

        if (BountyConfig.IsPlayerBountyDisplayEnabled.get()) {
            if (oldPlayer.getPersistentData().contains("bounty")) {
                String bountydisplay1 = oldPlayer.getPersistentData().getString("bountydisplay1");
                int bounty = oldPlayer.getPersistentData().getInt("bounty");
                String bountydisplay2 = oldPlayer.getPersistentData().getString("bountydisplay2");

                newPlayer.getPersistentData().putString("bountydisplay1", bountydisplay1);
                newPlayer.getPersistentData().putInt("bounty", bounty);
                newPlayer.getPersistentData().putString("bountydisplay2", bountydisplay2);
            }
        }

        if (BountyConfig.MagicCoinsSystem.get()) {
            int coins = MagicCoinsApi.getTotalCoins(oldPlayer);

            MagicCoinsApi.setTotalCoins(newPlayer, coins);
        }
    }

    @SubscribeEvent
    public static void onTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof Player player) {
                ServerPlayer display = (ServerPlayer) event.getEntity();

            if (BountyConfig.IsPlayerBountyDisplayEnabled.get() && !BountyConfig.IsMagicCoinsDisplayEnabled.get()) {
                if (player.getPersistentData().contains("bounty")) {
                    PacketDistributor.sendToPlayer(display, new BountyDisplays(player.getPersistentData().getString("bountydisplay1"), player.getPersistentData().getInt("bounty"), player.getPersistentData().getString("bountydisplay2"), player.getId(), BountyConfig.DeleteDisplay.get()));
                }
            } else if (!BountyConfig.IsPlayerBountyDisplayEnabled.get() && BountyConfig.IsMagicCoinsDisplayEnabled.get()) {
                PacketDistributor.sendToPlayer(display, new BountyDisplays(player.getName().getString() + BountyConfig.CoinsDisplay1.get(), MagicCoinsApi.getTotalCoins(player), BountyConfig.CoinsDisplay2.get(), player.getId(), BountyConfig.DeleteDisplay.get()));
            } else if (BountyConfig.IsPlayerBountyDisplayEnabled.get() && BountyConfig.IsMagicCoinsDisplayEnabled.get()) {
                PacketDistributor.sendToPlayer(display, new BountyDisplays(player.getName().getString() + BountyConfig.CoinsDisplay1.get() + MagicCoinsApi.getTotalCoins(player) + BountyConfig.CoinsDisplay2.get() + BountyConfig.BountyDisplay1.get() + player.getPersistentData().getString("bountydisplay1"), player.getPersistentData().getInt("bounty"), player.getPersistentData().getString("bountydisplay2"), player.getId(), BountyConfig.DeleteDisplay.get()));
            }
        }
    }

    //Constant loop to update the displays
    public static Runnable updateLoop(Player player, PlayerEvent.NameFormat name) {
        AtomicInteger tick = new AtomicInteger();

        if (tick.get() == 0) {
            Utils.runLater(BountyConfig.CoinsDisplayTimer.get(), () -> {
                name.setDisplayname(Component.translatable(player.getName().getString() + BountyConfig.CoinsDisplay1.get() + MagicCoinsApi.getTotalCoins(player) + BountyConfig.CoinsDisplay2.get()));
                renderName(name);
                onTracking(new PlayerEvent.StartTracking(player, player));
                tick.addAndGet(1);
            });
        } else if (tick.get() != 0) {
            Utils.runLater(BountyConfig.CoinsDisplayTimer.get(), () -> tick.addAndGet(-1));
        } //new PlayerEvent.NameFormat is a bit broken, let's fix tomorrow
        return null;
    }
}