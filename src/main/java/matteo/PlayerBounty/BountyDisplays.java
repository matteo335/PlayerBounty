package matteo.PlayerBounty;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent.Context;

public class BountyDisplays {

    public String bountyDisplay1;
    public int bounty;
    public String bountyDisplay2;

    public int entityId;
    public int deleteDisplay;

    public BountyDisplays(FriendlyByteBuf buf) {
        this.bountyDisplay1 = buf.readUtf();
        this.bounty = Integer.parseInt(buf.readUtf());
        this.bountyDisplay2 = buf.readUtf();
        this.entityId = buf.readInt();
        this.deleteDisplay = buf.readInt();
    }

    public BountyDisplays(String bountydisplay1, int bounty, String bountydisplay2, int entityID, int deletedisplay)
    {
        this.bountyDisplay1 = bountydisplay1;
        this.bounty = bounty;
        this.bountyDisplay2 = bountydisplay2;
        this.entityId = entityID;
        this.deleteDisplay = deletedisplay;
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeUtf(bountyDisplay1);
        buf.writeUtf(String.valueOf(bounty));
        buf.writeUtf(bountyDisplay2);
        buf.writeInt(entityId);
        buf.writeInt(deleteDisplay);
    }

    public void handle(Supplier<Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();

            Player toSync = (Player) minecraft.level.getEntity(entityId);

            if (toSync != null)
            {
                ctx.get().setPacketHandled(true);

                PlayerBounty.bountyTags(toSync, bountyDisplay1, bounty, bountyDisplay2, deleteDisplay);

                if(deleteDisplay == 0)
                    minecraft.player.connection.getPlayerInfo(toSync.getGameProfile().getId()).setTabListDisplayName(Component.literal(bountyDisplay1 + bounty + bountyDisplay2));
                else
                    minecraft.player.connection.getPlayerInfo(toSync.getGameProfile().getId()).setTabListDisplayName(Component.literal(toSync.getGameProfile().getName()));
            }

        });
    }

}
