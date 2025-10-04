package sbs.immovablerod.metaDungeon.elements.effects;

import org.bukkit.Bukkit;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonEntity;
import sbs.immovablerod.metaDungeon.elements.BaseEffect;

public class Speed extends BaseEffect {
    public Speed(MetaDungeonEntity owner, int value, int duration) {
        super(owner, value, duration);
        owner.setMovementSpeed(owner.getMovementSpeed() + value);
        if (duration > -1) {
            plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, this::onEffectEnd, duration));
        }
    }
    private void onEffectEnd() {
        this.owner.setMovementSpeed(this.owner.getMovementSpeed() - value);
    }

}
