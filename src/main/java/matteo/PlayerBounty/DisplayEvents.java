package matteo.PlayerBounty;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber
public class DisplayEvents {

    @SubscribeEvent
    public static void onJoinWorld(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (!player.getCommandSenderWorld().isClientSide) {
            if (player.getPersistentData().contains("bounty")) {
                PlayerBounty.packets((ServerPlayer) player, player.getPersistentData().getString("bountydisplay1"), player.getPersistentData().getInt("bounty"), player.getPersistentData().getString("bountydisplay2"), false);

                for (Player playerlist : player.getServer().getPlayerList().getPlayers()) {
                    PacketDistributor.sendToPlayer((ServerPlayer) player, new BountyDisplays(playerlist.getPersistentData().getString("bountydisplay1"), playerlist.getPersistentData().getInt("bounty"), playerlist.getPersistentData().getString("bountydisplay2"), playerlist.getId(), false));
                }
            }
        }
    }

    @SubscribeEvent
    public static void renderName(PlayerEvent.NameFormat event) {
        CompoundTag tag = event.getEntity().getPersistentData();
        if (tag.contains("bounty")) { event.setDisplayname(Component.translatable(tag.getString("bountydisplay1") + tag.getInt("bounty") + tag.getString("bountydisplay2"))); }
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();
        if (oldPlayer.getPersistentData().contains("bounty")) {
            String bountydisplay1 = oldPlayer.getPersistentData().getString("bountydisplay1");
            int bounty = oldPlayer.getPersistentData().getInt("bounty");
            String bountydisplay2 = oldPlayer.getPersistentData().getString("bountydisplay2");

            newPlayer.getPersistentData().putString("bountydisplay1", bountydisplay1);
            newPlayer.getPersistentData().putInt("bounty", bounty);
            newPlayer.getPersistentData().putString("bountydisplay2", bountydisplay2);
        }
    }

    @SubscribeEvent
    public static void onTracking (PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof Player player) {
            if (player.getPersistentData().contains("bounty")) {
                ServerPlayer display = (ServerPlayer) event.getEntity();
                PacketDistributor.sendToPlayer(display, new BountyDisplays(player.getPersistentData().getString("bountydisplay1"), player.getPersistentData().getInt("bounty"), player.getPersistentData().getString("bountydisplay2"), player.getId(), false));
            }
        }
    }
}
