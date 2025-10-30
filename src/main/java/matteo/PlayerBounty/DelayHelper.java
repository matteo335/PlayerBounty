package matteo.PlayerBounty;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@EventBusSubscriber
public class DelayHelper {

    private static class Delay {
        int ticks;
        Runnable methods;

        Delay(int ticks, Runnable methods) {
            this.ticks = ticks;
            this.methods = methods;
        }
    }

    private static final List<Delay> tasks = new ArrayList<>();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onServerTick(ServerTickEvent.Post event) {
        Iterator<Delay> it = tasks.iterator();
        while (it.hasNext()) {
            Delay task = it.next();
            task.ticks--;
            if (task.ticks <= 0) {
                task.methods.run();
                it.remove();
            }
        }
    }

    public static void runLater(int ticks, Runnable methods) {
        tasks.add(new Delay(ticks, methods));
    }
}