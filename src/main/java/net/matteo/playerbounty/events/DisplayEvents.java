package net.matteo.playerbounty.events;

import net.matteo.playerbounty.PlayerBountyMod;
import net.matteo.playerbounty.configs.Config;
import net.matteo.playerbounty.network.Packets;
import net.matteo.playerbounty.compats.SGEconomyDisplay;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.fml.ModList;

import javax.annotation.Nullable;

@EventBusSubscriber
public class DisplayEvents {

    public static void updateDisplay(int player, Level level, int bounty, @Nullable Integer balance) {
        PlayerBountyMod.LOGGER.info("updateDisplay");
        bountyTags((ServerPlayer) level.getEntity(player), bounty);
        PacketDistributor.sendToAllPlayers(new Packets(bounty, player, balance));
    }

    public static void bountyTags(Player player, double bounty) {
        CompoundTag tag = player.getPersistentData();
        PlayerBountyMod.LOGGER.info("bountyTags for " + player.getName().getString());

        if (Config.EnableDisplay.get()) {
            tag.putDouble("bounty", bounty);
            //player.refreshDisplayName();
            player.getServer().getPlayerList().getPlayer(player.getUUID()).refreshTabListName();
        }
    }

    @SubscribeEvent
    public static void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        if (!player.getCommandSenderWorld().isClientSide) {

            if (!ModList.get().isLoaded("sg_economy")) {
                CompoundTag tag = event.getEntity().getPersistentData();

                if (!Config.EnableDisplay.get()) return;
                if (!tag.contains("bounty")) return;

                updateDisplay(player.getId(), player.level(), (int) tag.getDouble("bounty"), null);

                for (Player playerlist : player.getServer().getPlayerList().getPlayers()) {
                    PacketDistributor.sendToPlayer(player,
                            new Packets((int) playerlist.getPersistentData().getDouble("bounty"), playerlist.getId(), null));
                }
            } else {
                SGEconomyDisplay.onJoin(player);
            }
        }
    }

    @SubscribeEvent
    public static void renderName(PlayerEvent.NameFormat event) {

        if (!ModList.get().isLoaded("sg_economy")) {
            if (!Config.EnableDisplay.get()) return;

            CompoundTag tag = event.getEntity().getPersistentData();
            if (!tag.contains("bounty")) return;

            event.setDisplayname(Component.translatable(Config.BountyDisplay1.get() + tag.getDouble("bounty") + Config.BountyDisplay2.get()));

        } else {
            SGEconomyDisplay.renderName(event);
        }
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();

        if (!Config.EnableDisplay.get()) return;
        if (!oldPlayer.getPersistentData().contains("bounty")) return;

        int bounty = oldPlayer.getPersistentData().getInt("bounty");
        newPlayer.getPersistentData().putDouble("bounty", bounty);

        /*if (SGEconomyConfig.CoinsSystem.get()) {
            SGEconomyApi.get().getBalance(newPlayer);
        }*/
    }

    @SubscribeEvent
    public static void onTracking(PlayerEvent.StartTracking event) {
        if (!(event.getTarget() instanceof ServerPlayer target)) return;
        if (!ModList.get().isLoaded("sg_economy")) {
            ServerPlayer player = (ServerPlayer) event.getEntity();
            CompoundTag tag = target.getPersistentData();

            if (!Config.EnableDisplay.get()) return;
            if (!target.getPersistentData().contains("bounty")) return;

            PacketDistributor.sendToPlayer(player, new Packets((int) tag.getDouble("bounty"), target.getId(), null));
        } else {
            SGEconomyDisplay.onTracking(event);
        }
    }
}