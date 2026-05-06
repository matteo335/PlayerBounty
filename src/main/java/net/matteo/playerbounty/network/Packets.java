package net.matteo.playerbounty.network;

import net.matteo.playerbounty.PlayerBountyMod;
import net.matteo.playerbounty.utils.GetValues;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import javax.annotation.Nullable;

public record Packets(int bounty, int playerID, @Nullable Integer balance) implements CustomPacketPayload {

    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(PlayerBountyMod.version);

        registrar.playBidirectional(TYPE, STREAM_CODEC, Packets::handle);
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

                PlayerBountyMod.LOGGER.info("Stream Codec");

                return new Packets(bounty, playerID, balance);
            }
    );

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            PlayerBountyMod.LOGGER.info("payload");

            if (ctx.flow().isClientbound()) {
                net.minecraft.client.Minecraft minecraft = net.minecraft.client.Minecraft.getInstance();
                String string = GetValues.name(minecraft.player);

                minecraft.player.connection.getPlayerInfo(minecraft.player.getUUID()).setTabListDisplayName(Component.literal(string));
                //DisplayEvents.bountyTags(ctx.player(), bounty);
                //DisplayEvents.updateDisplay(bounty, ctx.player().level(), playerID, balance);
            }
        });
    }


    public static final Type<Packets> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("playerbounty", "display"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
