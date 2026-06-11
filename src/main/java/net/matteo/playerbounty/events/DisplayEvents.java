package net.matteo.playerbounty.events;

import net.matteo.playerbounty.configs.Config;
import net.matteo.playerbounty.network.Packets;
import net.matteo.playerbounty.utils.GetValues;
import net.matteo.playerbounty.utils.Cooldowns;
import net.matteo.playerbounty.utils.mods.CreateNumismaticsUtils;
import net.matteo.playerbounty.PlayerBountyMod;

import tallestred.numismaticoverhaul.cap.CurrencyHolder;
import static tallestred.numismaticoverhaul.cap.CurrencyHolderAttacher.EXAMPLE_CAPABILITY;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.event.TickEvent.ServerTickEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.util.RandomSource;

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
    public static void tick(ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        int playerCount = event.getServer().getPlayerCount();
        if (playerCount == 0) return;

        Player player = event.getServer().getPlayerList().getPlayers().get(RandomSource.create().nextInt(0, playerCount));
        if (player == null) return;
        if (Cooldowns.isPlayerInCooldown(player.getUUID())) return;

        if (Config.System.get()) GetValues.playerbounty.put(player.getUUID(), (int) player.getPersistentData().getDouble("bounty"));

        if (GetValues.numismatic_overhaul_system) {
            GetValues.numismaticoverhaul.put(player.getUUID(), player.getCapability(EXAMPLE_CAPABILITY).map(CurrencyHolder::getValue).orElse(0L));
        }

        if (GetValues.create_numismatics_system) GetValues.create_numismatics.put(player.getUUID(), CreateNumismaticsUtils.spurValue(player, false) / 64);

        PlayerBountyMod.CHANNEL.send(PacketDistributor.ALL.noArg(),
                new Packets(
                player.getId(),
                Config.EnableDisplay.get() ? GetValues.playerbounty.get(player.getUUID()) : null,
                GetValues.numismatic_overhaul_display ? GetValues.numismaticoverhaul.get(player.getUUID()) : null,
                GetValues.create_numismatics_display ? GetValues.create_numismatics.get(player.getUUID()) : null
        ));

        player.refreshDisplayName();
    }
}