package matteo.PlayerBounty;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.Objects;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DisplayEvents {

    @SubscribeEvent
    public static void renderName(PlayerEvent.NameFormat event)
    {
        CompoundTag tag = event.getEntity().getPersistentData();
        if (tag.contains("bounty")) {
            event.setDisplayname(Component.literal(tag.getString("bountydisplay1") + tag.getInt("bounty") + tag.getString("bountydisplay2")));
        }
    }

    @SubscribeEvent
    public static void onJoinWorld(PlayerEvent.PlayerLoggedInEvent event)
    {
        Player player = event.getEntity();
        if (!player.getCommandSenderWorld().isClientSide) {
            if (player.getPersistentData().contains("bounty"))
                PlayerBounty.sendPacket(player, player.getPersistentData().getString("bountydisplay1"), player.getPersistentData().getInt("bounty"), player.getPersistentData().getString("bountydisplay2"), 0);

            for(Player playerDisplay : (player.getServer()).getPlayerList().getPlayers()) {
                if (playerDisplay.getPersistentData().contains("bounty")) {
                    PlayerBounty.network.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new BountyDisplays(playerDisplay.getPersistentData().getString("bountydisplay1"), playerDisplay.getPersistentData().getInt("bounty"), playerDisplay.getPersistentData().getString("bountydisplay2"), playerDisplay.getId(), 0));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event)
    {
        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();
        if (oldPlayer.getPersistentData().contains("bounty"))
        {
            String bountydisplay1 = oldPlayer.getPersistentData().getString("bountydisplay1");
            int bounty = oldPlayer.getPersistentData().getInt("bounty");
            String bountydisplay2 = oldPlayer.getPersistentData().getString("bountydisplay2");

            newPlayer.getPersistentData().putString("bountydisplay1", bountydisplay1);
            newPlayer.getPersistentData().putInt("bounty", bounty);
            newPlayer.getPersistentData().putString("bountydisplay2", bountydisplay2);
        }

    }

}
