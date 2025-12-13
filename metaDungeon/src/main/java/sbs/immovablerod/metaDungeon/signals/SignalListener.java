package sbs.immovablerod.metaDungeon.signals;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

public class SignalListener {
    HashMap<UUID, Consumer<BaseSignal>> tasks;
    public SignalListener() {
        this.tasks = new HashMap<>();
    }
    public UUID registerTask(Consumer<BaseSignal> func) {
        UUID id = UUID.randomUUID();
        this.tasks.put(id, func);
        return id;
    }

    public void unregisterTask(UUID id) {
        this.tasks.remove(id);
    }

    public void trigger(BaseSignal event) {
        System.out.println(this.tasks);
        this.tasks.values().forEach(func -> func.accept(event));
    }

    public void clearTasks() {
        this.tasks.clear();
    }

}
