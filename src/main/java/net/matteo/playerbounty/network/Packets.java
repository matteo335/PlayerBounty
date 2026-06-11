package net.matteo.playerbounty.network;

import net.matteo.playerbounty.utils.GetValues;
import net.matteo.playerbounty.utils.Cooldowns;
import net.matteo.playerbounty.configs.Config;

import net.minecraftforge.network.NetworkEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public record Packets(int playerID, @Nullable Integer playerbounty, @Nullable Long numismatic_overhaul, @Nullable Integer create_numismatics) {

    public static void encode(Packets packet, FriendlyByteBuf   buffer) {
        buffer.writeInt(packet.playerID);

        buffer.writeBoolean(packet.playerbounty != null);
        if (packet.playerbounty != null) buffer.writeInt(packet.playerbounty);

        buffer.writeBoolean(packet.numismatic_overhaul != null);
        if (packet.numismatic_overhaul != null) buffer.writeLong(packet.numismatic_overhaul);

        buffer.writeBoolean(packet.create_numismatics != null);
        if (packet.create_numismatics != null) buffer.writeInt(packet.create_numismatics);
    }

    public static Packets decode(FriendlyByteBuf buffer) {
        int playerID = buffer.readInt();

        Integer playerbounty = buffer.readBoolean() ? buffer.readInt() : null;
        Long numismatic_overhaul = buffer.readBoolean() ? buffer.readLong() : null;
        Integer create_numismatics = buffer.readBoolean() ? buffer.readInt() : null;

        return new Packets(playerID, playerbounty, numismatic_overhaul, create_numismatics);
    }

    public static void handle(Packets packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = (Player) Minecraft.getInstance().level.getEntity(packet.playerID);
            if (player == null) return;
            if (Cooldowns.isPlayerInCooldown(player.getUUID())) return;

            if (Config.EnableDisplay.get()) GetValues.playerbounty.put(player.getUUID(), packet.playerbounty);
            if (GetValues.numismatic_overhaul_display) GetValues.numismaticoverhaul.put(player.getUUID(), packet.numismatic_overhaul);
            if (GetValues.create_numismatics_display) GetValues.create_numismatics.put(player.getUUID(), packet.create_numismatics);

            Minecraft.getInstance().player.connection.getPlayerInfo(player.getUUID()).setTabListDisplayName(GetValues.name(player));
            player.refreshDisplayName();
        });

        ctx.get().setPacketHandled(true);
    }
}
