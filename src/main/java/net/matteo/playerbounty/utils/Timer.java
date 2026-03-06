package net.matteo.playerbounty.utils;

import net.minecraft.util.Tuple;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Timer {

    @EventBusSubscriber
    private static class Delay {
        int ticks;
        Runnable methods;

        Delay(int ticks, Runnable methods) {
            this.ticks = ticks;
            this.methods = methods;
        }
    }

    private static final List<Delay> tasks = new ArrayList<>();
    private static final List<Delay> wait = new ArrayList<>();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onServerTick(ServerTickEvent.Post event) {
        if (!wait.isEmpty()) {
            tasks.addAll(wait);
            wait.clear();
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
        wait.add(new Delay(ticks, methods));
    }

    public static final Collection<Tuple<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

    public static void queueServerWork(int tick, Runnable action) {
        workQueue.add(new Tuple<>(action, tick));
    }
}
