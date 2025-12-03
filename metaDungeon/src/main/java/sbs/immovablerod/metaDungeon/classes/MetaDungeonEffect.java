package sbs.immovablerod.metaDungeon.classes;

import org.bukkit.Bukkit;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.elements.EffectInterface;
import sbs.immovablerod.metaDungeon.enums.Effects;

public class MetaDungeonEffect {
    private final MetaDungeon plugin = MetaDungeon.getInstance();
    private final EffectInterface controller;
    private final int level;
    private final int duration;

    public MetaDungeonEffect(Effects name, int level, int duration) {
        this.level = level;
        this.duration = duration;

        System.out.println(name.toString());
        // implement logic functions
        this.controller = Effects.get(name.toString(), this);
    }

    public void apply(MetaDungeonEntity... targets) {
        for (MetaDungeonEntity entity : targets) {
            entity.addEffect(this);
        }
    }

    public void clear(MetaDungeonEntity target) {

    }

    public int getLevel() {
        return this.level;
    }

    public int getDuration() {
        return this.duration;
    }

    public EffectInterface getController() {
        return  this.controller;
    };
}
