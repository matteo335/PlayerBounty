package net.matteo.playerbounty.utils;

import static net.matteo.playerbounty.utils.mods.PlayerBountyUtils.DefaultDisplay;
import static net.matteo.playerbounty.utils.mods.SGEconomyUtils.SGEconomyDisplay;
import static net.matteo.playerbounty.utils.mods.NumismaticOverhaulUtils.NumismaticOverhaulDisplay;
import static net.matteo.playerbounty.utils.mods.CreateNumismaticsUtils.CreateNumismaticsDisplay;
import net.matteo.playerbounty.configs.*;

import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GetValues {
    public static Map<UUID, Integer> sg_economy = new HashMap<>();
    public static Map<UUID, Integer> playerbounty = new HashMap<>();
    public static Map<UUID, Long> numismaticoverhaul = new HashMap<>();
    public static Map<UUID, Integer> create_numismatics = new HashMap<>();

    public static boolean sg_economy_config = false;
    public static boolean sg_economy_system = false;
    public static boolean sg_economy_display = false;

    public static boolean numismatic_overhaul_config = false;
    public static boolean numismatic_overhaul_system = false;
    public static boolean numismatic_overhaul_display = false;

    public static boolean create_numismatics_config = false;
    public static boolean create_numismatics_system = false;
    public static boolean create_numismatics_display = false;

    public static MutableComponent name(Player player) {
        MutableComponent component = Component.literal(player.getName().getString());

        if (Config.EnableDisplay.get() && playerbounty.containsKey(player.getUUID())) component.append(DefaultDisplay(player));
        if (sg_economy_display && sg_economy.containsKey(player.getUUID())) component.append(SGEconomyDisplay(player));
        if (numismatic_overhaul_display && numismaticoverhaul.containsKey(player.getUUID())) component.append(NumismaticOverhaulDisplay(player));
        if (create_numismatics_display && create_numismatics.containsKey(player.getUUID())) component.append(CreateNumismaticsDisplay(player));

        return component;
    }
}