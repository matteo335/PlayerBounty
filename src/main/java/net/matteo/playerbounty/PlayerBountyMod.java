package net.matteo.playerbounty;

import net.matteo.playerbounty.old.DisplayEvents;
import net.matteo.playerbounty.old.NetworkDisplay;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import net.matteo.playerbounty.capabilities.PlayerDataBountyCapabilities;
import net.matteo.playerbounty.network.Network;
import net.matteo.playerbounty.utils.Timer;
import net.matteo.playerbounty.configs.Config;
import net.matteo.playerbounty.configs.SGEconomyConfig;
import net.matteo.playerbounty.utils.InvalidConfigException;

import net.minecraft.network.chat.Component;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;

@Mod("playerbounty")
@EventBusSubscriber
public class PlayerBountyMod {
    public static final String MOD_ID = "playerbounty";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public PlayerBountyMod(IEventBus modEventBus, ModContainer modContainer) throws InvalidConfigException {
        modEventBus.addListener(Network::registerNetworking);
        PlayerDataBountyCapabilities.ATTACHMENT_TYPES.register(modEventBus);

        Network.addNetworkMessage(
                DisplayEvents.TYPE,
                NetworkDisplay.STREAM_CODEC,
                NetworkDisplay::payload,
                Network.Direction.CLIENTBOUND
        );

        modContainer.registerConfig(ModConfig.Type.SERVER, Config.builder.build());

        if (ModList.get().isLoaded("sg_economy")) {
            modContainer.registerConfig(ModConfig.Type.SERVER, SGEconomyConfig.builder.build(),
                    String.format("%1$s-%2$s.toml", MOD_ID, "sg_economy"));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onServerStart(ServerStartingEvent start) throws InvalidConfigException {
        InvalidConfigException.CheckInvalidConfigs();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onServerLoad(ServerStartedEvent start) {
        if (Config.StartupWarning.get()) {
            Timer.runLater(6000, () -> {
                if (start.getServer().getPlayerCount() != 0) {
                    start.getServer().getPlayerList().broadcastSystemMessage(Component.literal("<PlayerBounty>: This warning is for people who forgot to read the mod description\nIn order for the mod to work as you wish, you need to change the server config and restart the world"), false);
                } else onServerLoad(start);
            });
        }
    }
}