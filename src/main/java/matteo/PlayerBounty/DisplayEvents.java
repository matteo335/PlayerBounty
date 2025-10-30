package matteo.PlayerBounty;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import static matteo.PlayerBounty.BountyConfig.BountyDisplay1;
import static matteo.PlayerBounty.BountyConfig.BountyDisplay2;

@EventBusSubscriber
public class DisplayEvents {

    @SubscribeEvent
    public static void onJoinWorld(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (!player.getCommandSenderWorld().isClientSide) {
            if (player.getPersistentData().contains("bounty")) {
                PlayerBounty.packets((ServerPlayer) player, player.getPersistentData().getString("bountydisplay1").orElse("test5" + BountyDisplay1.get()), player.getPersistentData().getInt("bounty").orElse(0), player.getPersistentData().getString("bountydisplay2").orElse(BountyDisplay2.get()), false);

                for (Player playerlist : player.getServer().getPlayerList().getPlayers()) {
                    PacketDistributor.sendToPlayer((ServerPlayer) player, new BountyDisplays(playerlist.getPersistentData().getString("bountydisplay1").orElse("test4" + BountyDisplay1.get()), playerlist.getPersistentData().getInt("bounty").orElse(0), playerlist.getPersistentData().getString("bountydisplay2").orElse(BountyDisplay2.get()), playerlist.getId(), false));
                }
            }
        }
    }

    @SubscribeEvent
    public static void renderName(PlayerEvent.NameFormat event) {
        CompoundTag tag = event.getEntity().getPersistentData();
        if (tag.contains("bounty")) { event.setDisplayname(Component.translatable(tag.getString("bountydisplay1").orElse("test3" + BountyDisplay1.get()) + tag.getInt("bounty").orElse(0) + tag.getString("bountydisplay2").orElse(BountyDisplay2.get()))); }
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();

        String bountydisplay1 = oldPlayer.getPersistentData().getString("bountydisplay1").orElse(event.getOriginal().getName().getString() + BountyDisplay1.get());
        int bounty = oldPlayer.getPersistentData().getInt("bounty").orElse(0);
        String bountydisplay2 = oldPlayer.getPersistentData().getString("bountydisplay2").orElse(BountyDisplay2.get());

        newPlayer.getPersistentData().putString("bountydisplay1", event.getEntity().getName().getString() + BountyDisplay1.get());
        newPlayer.getPersistentData().putInt("bounty", bounty);
        newPlayer.getPersistentData().putString("bountydisplay2", bountydisplay2);
    }

    @SubscribeEvent
    public static void onTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof Player player) {
            if (player.getPersistentData().contains("bounty")) {
                ServerPlayer display = (ServerPlayer) event.getEntity();
                PacketDistributor.sendToPlayer(display, new BountyDisplays(player.getPersistentData().getString("bountydisplay1").orElse("test1" + BountyDisplay1.get()), player.getPersistentData().getInt("bounty").orElse(0), player.getPersistentData().getString("bountydisplay2").orElse(BountyDisplay2.get()), player.getId(), false));
            }
        }
    }
}
