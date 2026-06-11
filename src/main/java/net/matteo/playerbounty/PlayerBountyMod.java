package net.matteo.playerbounty;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.network.NetworkRegistry;

import net.matteo.playerbounty.configs.*;
import net.matteo.playerbounty.utils.InvalidConfigException;
import net.matteo.playerbounty.utils.Cooldowns;
import net.matteo.playerbounty.utils.GetValues;
import net.matteo.playerbounty.network.Packets;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings({"removal", "unused"})
@Mod("playerbounty")
@Mod.EventBusSubscriber
public class PlayerBountyMod {
    /// 1300 lines of code!
    /// powershell "Get-ChildItem -Recurse -Include  *.java,build.gradle,settings.gradle,gradle.properties,*.mcmeta,mods.toml | Get-Content | Measure-Object -Line"
    public static final Logger LOGGER = LogManager.getLogger("playerbounty");
    public static boolean sent = false;
    public static String version = ModList.get().getModFileById("playerbounty").versionString();

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("playerbounty", version),
            () -> version,
            version::equals,
            version::equals
    );

    public PlayerBountyMod() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.builder.build());

        if (ModList.get().isLoaded("numismaticoverhaul")) {
            ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, NumismaticOverhaulConfig.builder.build(),
                    String.format("%1$s-%2$s.toml", "playerbounty", "NumismaticOverhaul"));
            GetValues.numismatic_overhaul_config = true;
        }

        if (ModList.get().isLoaded("numismatics")) {
            ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, CreateNumismaticsConfig.builder.build(),
                    String.format("%1$s-%2$s.toml", "playerbounty", "CreateNumismatic"));
            GetValues.create_numismatics_config = true;
        }

        CHANNEL.registerMessage(
                0,
                Packets.class,
                Packets::encode,
                Packets::decode,
                Packets::handle
        );
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

        if (GetValues.create_numismatics_config && CreateNumismaticsConfig.System.get()) {
            GetValues.create_numismatics_system = true;
            if (CreateNumismaticsConfig.EnableDisplay.get()) GetValues.create_numismatics_display = true;
        }
    }
}