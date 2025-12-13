package sbs.immovablerod.metaDungeon.managers;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import sbs.immovablerod.metaDungeon.MetaDungeon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TaskManager {
    private final MetaDungeon plugin = MetaDungeon.getInstance();

    private final HashMap<UUID, BukkitTask> tasks;
    public TaskManager() {
        this.tasks = new HashMap<>();
    }

    public UUID runTaskLater(Runnable runnable, long when) {
        UUID id = UUID.randomUUID();
        this.tasks.put(id, Bukkit.getScheduler().runTaskLater(this.plugin, runnable, when));
        return id;

    }

    public UUID runTaskTimer(Runnable runnable, long l1, long l2) {
        UUID id = UUID.randomUUID();
        this.tasks.put(id, Bukkit.getScheduler().runTaskTimer(this.plugin, runnable, l1, l2));

        return id;
    }

    public void cancelTask(UUID id) {
        if (this.tasks.containsKey(id)) {
            this.tasks.get(id).cancel();
            this.tasks.remove(id);
        } else {
            plugin.getLogger().warning("Could not cancel task (unknown id)");
        }

    }
    public void clear() {
        this.tasks.values().forEach(BukkitTask::cancel);
        this.tasks.clear();
    }
}
