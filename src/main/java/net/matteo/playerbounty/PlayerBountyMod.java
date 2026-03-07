package net.matteo.playerbounty;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.fml.common.EventBusSubscriber;

import net.matteo.playerbounty.capabilities.PlayerDataBountyCapabilities;
import net.matteo.playerbounty.configs.ServerConfig;
import net.matteo.playerbounty.network.PBNetwork;
import net.matteo.playerbounty.network.payload.SyncServerConfigS2C;
import net.matteo.playerbounty.utils.Timer;

import net.minecraft.network.chat.Component;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;

@Mod("playerbounty")
@EventBusSubscriber
public class PlayerBountyMod {
    public static final String MOD_ID = "playerbounty";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public PlayerBountyMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(PBNetwork::registerNetworking);

        PBNetwork.addNetworkMessage(
                SyncServerConfigS2C.TYPE,
                SyncServerConfigS2C.STREAM_CODEC,
                SyncServerConfigS2C::handle
        );

        PlayerDataBountyCapabilities.ATTACHMENT_TYPES.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.builder.build(),
                String.format("%1$s-%2$s.toml", MOD_ID, "SG-Economy"));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onServerLoad(ServerStartedEvent start) {
        if (ServerConfig.StartupWarning.get()) {
            Timer.runLater(6000, () -> {
                if (start.getServer().getPlayerCount() != 0) {
                    start.getServer().getPlayerList().broadcastSystemMessage(Component.literal("<PlayerBounty>: THIS WARNING IS FOR PEOPLE WHO FORGOT TO READ THE MOD DESCRIPTION\nIN ORDER FOR THE MOD TO WORK AS YOU WISH, YOU NEED TO CHANGE THE CONFIG AND RESTART THE WORLD"), false);
                } else onServerLoad(start);
            });
        }
    }
}