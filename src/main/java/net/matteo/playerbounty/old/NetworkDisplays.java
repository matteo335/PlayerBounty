package net.matteo.playerbounty.old;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

import net.matteo.playerbounty.PlayerBountyMod;

import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BountyDisplays(String bountyDisplay1, int bounty, String bountyDisplay2, int entityID, boolean deleteDisplay) implements CustomPacketPayload {

    public void payload(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();

            Player toSync = (Player) minecraft.level.getEntity(entityID);

            if (toSync != null) {

                PlayerBountyMod.bountyTags(toSync, bountyDisplay1, bounty, bountyDisplay2, deleteDisplay);
                minecraft.player.connection.getPlayerInfo(toSync.getGameProfile().getId()).setTabListDisplayName(Component.translatable(bountyDisplay1 + bounty + bountyDisplay2));
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PlayerBounty.TYPE;
    }
}
