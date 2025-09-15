package matteo.PlayerBounty;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mod.EventBusSubscriber
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

    public static void runLater(int ticks, Runnable methods) {
        tasks.add(new Delay(ticks, methods));
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
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
    }
}
