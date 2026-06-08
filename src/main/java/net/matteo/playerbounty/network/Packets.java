package net.matteo.playerbounty.network;

import net.matteo.playerbounty.utils.GetValues;
import net.matteo.playerbounty.utils.Cooldowns;
import net.matteo.playerbounty.configs.Config;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import javax.annotation.Nullable;

public record Packets(int playerID, @Nullable Integer playerbounty, @Nullable Integer sg_economy, @Nullable Long numismatic_overhaul,
                      @Nullable Integer create_numismatics) implements CustomPacketPayload {

    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1.0.0");

        registrar.playToClient(TYPE, STREAM_CODEC, Packets::handle);
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, Packets> STREAM_CODEC = StreamCodec.of(
            (RegistryFriendlyByteBuf buf, Packets packet) -> {
                buf.writeInt(packet.playerID);

                buf.writeBoolean(packet.playerbounty != null);
                if (packet.playerbounty != null) buf.writeInt(packet.playerbounty);

                buf.writeBoolean(packet.sg_economy != null);
                if (packet.sg_economy != null) buf.writeInt(packet.sg_economy);

                buf.writeBoolean(packet.numismatic_overhaul != null);
                if (packet.numismatic_overhaul != null) buf.writeLong(packet.numismatic_overhaul);

                buf.writeBoolean(packet.create_numismatics != null);
                if (packet.create_numismatics != null) buf.writeInt(packet.create_numismatics);
            },
            (RegistryFriendlyByteBuf buf) -> {
                int playerID = buf.readInt();

                Integer playerbounty = buf.readBoolean() ? buf.readInt() : null;
                Integer sg_economy = buf.readBoolean() ? buf.readInt() : null;
                Long numismatic_overhaul = buf.readBoolean() ? buf.readLong() : null;
                Integer create_numismatics = buf.readBoolean() ? buf.readInt() : null;

                return new Packets(playerID, playerbounty, sg_economy, numismatic_overhaul, create_numismatics);
            }
    );

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = (Player) Minecraft.getInstance().level.getEntity(playerID);
            if (player == null) return;
            if (Cooldowns.isPlayerInCooldown(player.getUUID())) return;

            if (Config.EnableDisplay.get()) GetValues.playerbounty.put(player.getUUID(), playerbounty);
            if (GetValues.sg_economy_display) GetValues.sg_economy.put(player.getUUID(), sg_economy);
            if (GetValues.numismatic_overhaul_display) GetValues.numismaticoverhaul.put(player.getUUID(), numismatic_overhaul);
            if (GetValues.create_numismatics_display) GetValues.create_numismatics.put(player.getUUID(), create_numismatics);

            Minecraft.getInstance().player.connection.getPlayerInfo(player.getUUID()).setTabListDisplayName(GetValues.name(player));
            player.refreshDisplayName();
        });
    }

    public static final Type<Packets> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("playerbounty", "display"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
