package matteo.PlayerBounty;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@EventBusSubscriber
public class Utils {

    private static class Delay {
        int ticks;
        Runnable methods;

        Delay(int ticks, Runnable methods) {
            this.ticks = ticks;
            this.methods = methods;
        }
    }

    private static final List<Delay> tasks = new ArrayList<>();
    private static final List<Delay> riichi = new ArrayList<>();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onServerTick(ServerTickEvent.Post event) {
        if (!riichi.isEmpty()) {
            tasks.addAll(riichi);
            riichi.clear();
        }

        Iterator<Delay> it = tasks.iterator();
        while (it.hasNext()) {
            Delay task = it.next();
            task.ticks--;
            if (task.ticks <= 0) {
                try {
                    task.methods.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                it.remove();
            }
        }
    }

    public static void runLater(int ticks, Runnable methods) {
        if (methods == null) {
            return;
        }
        riichi.add(new Delay(ticks, methods));
    }
}
