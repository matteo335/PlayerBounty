package net.matteo.playerbounty.network;

import net.matteo.playerbounty.utils.GetValues;
import net.matteo.playerbounty.events.DisplayEvents;
import net.matteo.playerbounty.utils.Cooldowns;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import javax.annotation.Nullable;

public record Packets(int bounty, int playerID, @Nullable Integer balance) implements CustomPacketPayload {

    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1.0.0");

        registrar.playToClient(TYPE, STREAM_CODEC, Packets::handle);
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, Packets> STREAM_CODEC = StreamCodec.of(
            (RegistryFriendlyByteBuf buf, Packets packet) -> {
                buf.writeInt(packet.bounty);
                buf.writeInt(packet.playerID);

                buf.writeBoolean(packet.balance != null);
                if (packet.balance != null) {
                    buf.writeInt(packet.balance);
                }
            },
            (RegistryFriendlyByteBuf buf) -> {
                int bounty = buf.readInt();
                int playerID = buf.readInt();

                Integer balance = null;
                if (buf.readBoolean()) {
                    balance = buf.readInt();
                }

                return new Packets(bounty, playerID, balance);
            }
    );

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = (Player) Minecraft.getInstance().level.getEntity(playerID);
            if (player == null) return;
            if (Cooldowns.isPlayerInCooldown(player.getUUID())) return;
            DisplayEvents.coins.put(player.getUUID(), balance);
            DisplayEvents.bounty.put(player.getUUID(), bounty);
            player.refreshDisplayName();

            Minecraft.getInstance().player.connection.getPlayerInfo(player.getUUID()).setTabListDisplayName(Component.literal(GetValues.name(player)));
        });
    }


    public static final Type<Packets> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("playerbounty", "display"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
