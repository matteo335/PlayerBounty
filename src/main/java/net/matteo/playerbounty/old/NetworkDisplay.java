package net.matteo.playerbounty.old;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

import net.neoforged.neoforge.network.handling.IPayloadContext;

import javax.annotation.Nullable;

public record NetworkDisplay(
        String bountyDisplay1, int bounty, String bountyDisplay2, Player player,
        @Nullable String coinsDisplay1, @Nullable Integer balance, @Nullable String coinsDisplay2) implements CustomPacketPayload {

    public void payload(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            DisplayEvents.bountyTags(player, bountyDisplay1, bounty, bountyDisplay2);

            var info = minecraft.player.connection.getPlayerInfo(player.getName().getString());
            info.setTabListDisplayName(Component.literal(bountyDisplay1 + bounty + bountyDisplay2));
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return DisplayEvents.TYPE;
    }
}
