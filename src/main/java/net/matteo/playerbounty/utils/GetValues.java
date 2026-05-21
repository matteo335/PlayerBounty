package net.matteo.playerbounty.utils;

import static net.matteo.playerbounty.utils.mods.PlayerBountyUtils.DefaultDisplay;
import static net.matteo.playerbounty.utils.mods.SGEconomyUtils.SGEconomyDisplay;
import static net.matteo.playerbounty.utils.mods.NumismaticOverhaulUtils.NumismaticOverhaulDisplay;

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

    public static MutableComponent name(Player player) {
        MutableComponent component = Component.literal(player.getName().getString());

        component.append(DefaultDisplay(player));
        component.append(SGEconomyDisplay(player));
        component.append(NumismaticOverhaulDisplay(player));

        return component;
    }
}