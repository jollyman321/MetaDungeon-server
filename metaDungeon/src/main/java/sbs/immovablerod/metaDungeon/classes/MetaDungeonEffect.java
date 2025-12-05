package sbs.immovablerod.metaDungeon.classes;


import sbs.immovablerod.metaDungeon.elements.EffectInterface;
import sbs.immovablerod.metaDungeon.enums.Effects;

import java.util.ArrayList;

public class MetaDungeonEffect {
    private final EffectInterface controller;
    private final int level;
    private final int duration;
    private final Effects name;
    private boolean active;
    private final ArrayList<MetaDungeonEntity> affectedEntities;

    public MetaDungeonEffect(Effects name, int duration, int level) {
        this.name = name;
        this.level = level;
        this.duration = duration;
        this.active = true;
        this.affectedEntities = new ArrayList<>();

        System.out.println(name.toString());
        // implement logic functions
        this.controller = Effects.get(name.toString(), this);
    }
    public void apply(MetaDungeonEntity target) {
        this.affectedEntities.add(target);

    }
    public Effects getName() {
        return name;
    }
    public int getLevel() {
        return this.level;
    }
    public int getDuration() {
        return this.duration;
    }

    public void deactivate() {
        this.active = false;
        this.affectedEntities.forEach( ele -> {
            ele.updateStats();
            this.controller.onClear(ele);});

    }

    public boolean isActive() {
        return active;
    }

    public EffectInterface getController() {
        return  this.controller;
    }
}
