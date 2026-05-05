package net.matteo.playerbounty;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.ModList;

import net.matteo.playerbounty.capabilities.PlayerDataBountyCapabilities;
import net.matteo.playerbounty.network.PBNetwork;
import net.matteo.playerbounty.network.payload.SyncServerConfigS2C;
import net.matteo.playerbounty.utils.Timer;
import net.matteo.playerbounty.configs.Config;
import net.matteo.playerbounty.configs.MagicCoinsConfig;

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

        modContainer.registerConfig(ModConfig.Type.SERVER, Config.builder.build());

        if (ModList.get().isLoaded("sg_economy_api")) {
            modContainer.registerConfig(ModConfig.Type.SERVER, MagicCoinsConfig.builder.build(),
                    String.format("%1$s-%2$s.toml", MOD_ID, "sg_economy"));
        }
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