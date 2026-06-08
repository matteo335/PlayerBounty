package net.matteo.playerbounty;

import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.ModList;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import net.matteo.playerbounty.configs.*;
import net.matteo.playerbounty.utils.InvalidConfigException;
import net.matteo.playerbounty.network.Packets;
import net.matteo.playerbounty.utils.Cooldowns;
import net.matteo.playerbounty.utils.GetValues;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("playerbounty")
@EventBusSubscriber
public class PlayerBountyMod {
    /// 1300 lines of code!
    /// powershell "Get-ChildItem -Recurse -Include  *.java,build.gradle,settings.gradle,gradle.properties,*.mcmeta,neoforge.mods.toml | Get-Content | Measure-Object -Line"
    public static final Logger LOGGER = LogManager.getLogger("playerbounty");
    public static boolean sent = false;

    public PlayerBountyMod(ModContainer mod) {
        mod.registerConfig(ModConfig.Type.SERVER, Config.builder.build());

        if (ModList.get().isLoaded("sg_economy")) {
            mod.registerConfig(ModConfig.Type.SERVER, SGEconomyConfig.builder.build(),
                    String.format("%1$s-%2$s.toml", "playerbounty", "sg_economy"));
            GetValues.sg_economy_config = true;
        }

        if (ModList.get().isLoaded("numismaticoverhaul")) {
            mod.registerConfig(ModConfig.Type.SERVER, NumismaticOverhaulConfig.builder.build(),
                    String.format("%1$s-%2$s.toml", "playerbounty", "NumismaticOverhaul"));
            GetValues.numismatic_overhaul_config = true;
        }

        if (ModList.get().isLoaded("numismatics")) {
            mod.registerConfig(ModConfig.Type.SERVER, CreateNumismaticsConfig.builder.build(),
                    String.format("%1$s-%2$s.toml", "playerbounty", "CreateNumismatic"));
            GetValues.create_numismatics_config = true;
        }

        mod.getEventBus().addListener(Packets::registerPackets);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void configCheck(ServerStartingEvent event) throws InvalidConfigException {
        InvalidConfigException.CheckInvalidConfigs();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void serverLoadLoop(ServerStartedEvent event) {
        if (GetValues.numismatic_overhaul_config && NumismaticOverhaulConfig.System.get()) {
            GetValues.numismatic_overhaul_system = true;
            if (NumismaticOverhaulConfig.EnableDisplay.get()) GetValues.numismatic_overhaul_display = true;
        }

        if (GetValues.sg_economy_config && SGEconomyConfig.System.get()) {
            GetValues.sg_economy_system = true;
            if (SGEconomyConfig.EnableDisplay.get()) GetValues.sg_economy_display = true;
        }

        if (GetValues.create_numismatics_config && CreateNumismaticsConfig.System.get()) {
            GetValues.create_numismatics_system = true;
            if (CreateNumismaticsConfig.EnableDisplay.get()) GetValues.create_numismatics_display = true;
        }

        if (Config.StartupWarning.get()) Cooldowns.runLater(6000, () -> {

            if (event.getServer().getPlayerCount() != 0)
                for (Player player : event.getServer().getPlayerList().getPlayers()) {

                    if (player.hasPermissions(4)) {
                        player.displayClientMessage(Component.literal("§ePlayerBounty§r: This warning is for people who forgot to read the mod description\nIn order for the mod to work as you wish, you need to change the server config and restart the world"), false);
                        sent = true;
                    }
                }

            if (!sent) serverLoadLoop(event);
        });
    }

    @SubscribeEvent
    public static void clientJoin(RegisterClientCommandsEvent event) {
        if (GetValues.numismatic_overhaul_config && NumismaticOverhaulConfig.System.get()) {
            GetValues.numismatic_overhaul_system = true;
            if (NumismaticOverhaulConfig.EnableDisplay.get()) GetValues.numismatic_overhaul_display = true;
        }

        if (GetValues.sg_economy_config && SGEconomyConfig.System.get()) {
            GetValues.sg_economy_system = true;
            if (SGEconomyConfig.EnableDisplay.get()) GetValues.sg_economy_display = true;
        }

        if (GetValues.create_numismatics_config && CreateNumismaticsConfig.System.get()) {
            GetValues.create_numismatics_system = true;
            if (CreateNumismaticsConfig.EnableDisplay.get()) GetValues.create_numismatics_display = true;
        }
    }
}