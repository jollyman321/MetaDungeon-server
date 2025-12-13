package sbs.immovablerod.metaDungeon.elements.effects;

import org.bukkit.Bukkit;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonEffect;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonEntity;
import sbs.immovablerod.metaDungeon.elements.EffectInterface;

import static sbs.immovablerod.metaDungeon.game.GConfig.taskManager;

public class Regen extends EffectInterface {
    private final MetaDungeonEffect root;
    private final MetaDungeon plugin = MetaDungeon.getInstance();

    public Regen(MetaDungeonEffect root) {
        this.root = root;
    }

    @Override
    public void onInitiated(MetaDungeonEntity entity) {
        super.onInitiated(entity);
        this.update(entity);
    }

    public void update(MetaDungeonEntity entity) {
        if (this.root.isActive()) {
            taskManager.runTaskLater(() -> {
                entity.changeHealth(this.root.getLevel());
                this.update(entity);
            }, 20L);
        }
    }
}