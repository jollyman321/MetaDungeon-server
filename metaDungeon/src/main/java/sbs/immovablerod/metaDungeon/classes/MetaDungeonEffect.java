package sbs.immovablerod.metaDungeon.classes;


import org.bukkit.Bukkit;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.elements.EffectInterface;
import sbs.immovablerod.metaDungeon.enums.Colors;
import sbs.immovablerod.metaDungeon.enums.Effects;
import sbs.immovablerod.metaDungeon.game.GConfig;

import java.util.ArrayList;

import static sbs.immovablerod.metaDungeon.game.GConfig.taskManager;

public class MetaDungeonEffect {
    private final MetaDungeon plugin = MetaDungeon.getInstance();

    private final EffectInterface controller;
    private final int level;
    private final int duration;
    private final Effects name;
    private boolean expired;
    private boolean active;
    private final ArrayList<MetaDungeonEntity> affectedEntities;

    public MetaDungeonEffect(Effects name, int duration, int level) {
        this.name = name;
        this.level = level;
        this.duration = duration;
        this.active = false;
        this.expired = false;
        this.affectedEntities = new ArrayList<>();

        System.out.println(name.toString());
        // implement logic functions
        this.controller = Effects.get(name.toString(), this);
    }
    public void trigger() {
        if (!this.active && !this.expired) {
            this.active = true;
            // Use -1 for permanent effects
            if (this.duration > 0) {
                taskManager.runTaskLater(this::deactivate, 20L * this.duration);
            }
            this.affectedEntities.forEach(target -> {
                this.controller.onInitiated(target);
                if (target instanceof MetaDungeonPlayer) GConfig.messageManager.message(
                        ((MetaDungeonPlayer) target).getPlayer(),
                        "Received Effect: " + this.name.toString() + " " + this.level,
                        Colors.AQUA
                );
            });


        } else {
            plugin.getLogger().warning("Could not add inactive effect " + this.name);
        }

    }

    public void deactivate() {
        this.active = false;
        this.expired = false;
        this.affectedEntities.forEach( ele -> {
            ele.activeEffects.remove(this);
            this.controller.onClear(ele);
        });
    }

    public void addTarget(MetaDungeonEntity target) {
        if (!this.active && !this.expired) {
            this.affectedEntities.add(target);
        } else {
            plugin.getLogger().warning("Could not add effect " + this.name + " to " + target.toString() + "reason=active/expired");
        }


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


    public boolean isActive() {
        return active;
    }

    public EffectInterface getController() {
        return  this.controller;
    }
}
