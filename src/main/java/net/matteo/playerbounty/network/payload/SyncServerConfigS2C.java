package net.matteo.playerbounty.network.payload;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import net.matteo.playerbounty.PlayerBountyMod;
import net.matteo.playerbounty.client.ClientSyncedConfig;

public record SyncServerConfigS2C(
        double baseRateBountyHunter,
    int percentageRewardBountyHunter) implements CustomPacketPayload {

    public static final Type<SyncServerConfigS2C> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(PlayerBountyMod.MOD_ID, "sync_server_config"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncServerConfigS2C> STREAM_CODEC = StreamCodec.of(
            (buf, msg) -> {
                buf.writeDouble(msg.baseRateBountyHunter);
                buf.writeInt(msg.percentageRewardBountyHunter);
            },
            (buf) -> new SyncServerConfigS2C(
                    buf.readDouble(),
                    buf.readInt()));

    @Override
    public Type<SyncServerConfigS2C> type() {
        return TYPE;
    }

    public static void handle(final SyncServerConfigS2C msg, final IPayloadContext ctx) {
        if (ctx.flow() != PacketFlow.CLIENTBOUND) {
            return;
        }

        ctx.enqueueWork(() -> applyClient(msg));
    }

    @OnlyIn(Dist.CLIENT)
    private static void applyClient(SyncServerConfigS2C msg) {
        ClientSyncedConfig.apply(msg.baseRateBountyHunter(), msg.percentageRewardBountyHunter());
    }
}