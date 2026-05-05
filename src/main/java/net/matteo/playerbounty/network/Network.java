package net.matteo.playerbounty.network;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class Network {

    private Network() {}

    private static boolean networkingRegistered = false;

    private static final Map<CustomPacketPayload.Type<?>, NetworkMessage<?>> MESSAGES = new HashMap<>();

    public enum Direction {
        CLIENTBOUND,
        SERVERBOUND,
        BIDIRECTIONAL
    }

    private record NetworkMessage<T extends CustomPacketPayload>(
            StreamCodec<? extends FriendlyByteBuf, T> codec,
            IPayloadHandler<T> handler,
            Direction direction
    ) {}

    public static <T extends CustomPacketPayload> void addNetworkMessage(
            CustomPacketPayload.Type<T> id,
            StreamCodec<? extends FriendlyByteBuf, T> codec,
            IPayloadHandler<T> handler,
            Direction direction
    ) {
        if (networkingRegistered) {
            throw new IllegalStateException("Cannot register new network messages after networking has been registered");
        }

        MESSAGES.put(id, new NetworkMessage<>(codec, handler, direction));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void registerNetworking(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("playerbounty"); // or PlayerBountyMod.MOD_ID

        MESSAGES.forEach((id, message) -> {
            NetworkMessage msg = (NetworkMessage) message;

            switch (msg.direction()) {
                case CLIENTBOUND -> registrar.playToClient(
                        id,
                        msg.codec(),
                        msg.handler()
                );
                case SERVERBOUND -> registrar.playToServer(
                        id,
                        msg.codec(),
                        msg.handler()
                );
                case BIDIRECTIONAL -> registrar.playBidirectional(
                        id,
                        msg.codec(),
                        msg.handler()
                );
            }
        });

        networkingRegistered = true;
    }
}