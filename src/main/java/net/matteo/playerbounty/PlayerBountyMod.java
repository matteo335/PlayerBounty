package net.matteo.playerbounty;

import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;

import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.matteo.playerbounty.capabilities.PlayerDataBountyCapabilities;
import net.matteo.playerbounty.configs.ServerConfig;
import net.matteo.playerbounty.network.PBNetwork;
import net.matteo.playerbounty.network.payload.SyncServerConfigS2C;
import net.minecraft.util.Tuple;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;

@Mod("playerbounty")
public class PlayerBountyMod {
    public static final Logger LOGGER = LogManager.getLogger(PlayerBountyMod.class);
    public static final String MOD_ID = "playerbounty";

    public PlayerBountyMod(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(PlayerBountyMod.class);
		modEventBus.addListener(PBNetwork::registerNetworking);

        PBNetwork.addNetworkMessage(
                SyncServerConfigS2C.TYPE,
                SyncServerConfigS2C.STREAM_CODEC,
                SyncServerConfigS2C::handle
        );

        PlayerDataBountyCapabilities.ATTACHMENT_TYPES.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.Config.SPEC,
				String.format("%s-server.toml", MOD_ID));
    }

    private static final Collection<Tuple<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

	public static void queueServerWork(int tick, Runnable action) {
		workQueue.add(new Tuple<>(action, tick));
	}

	@SubscribeEvent
	public static void tick(ServerTickEvent.Post event) {
		List<Tuple<Runnable, Integer>> actions = new ArrayList<>();
		workQueue.forEach(work -> {
			work.setB(work.getB() - 1);
			if (work.getB() == 0)
				actions.add(work);
		});
		actions.forEach(e -> e.getA().run());
		workQueue.removeAll(actions);
	}
}