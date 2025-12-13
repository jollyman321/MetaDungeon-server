package sbs.immovablerod.metaDungeon.signals;

import java.util.UUID;

public class BaseSignal {
    private final UUID id;

    public BaseSignal() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }
}
