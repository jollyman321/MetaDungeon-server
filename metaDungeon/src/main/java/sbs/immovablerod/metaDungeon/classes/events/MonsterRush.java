package sbs.immovablerod.metaDungeon.classes.events;


import com.fasterxml.jackson.databind.JsonNode;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonEffect;
import sbs.immovablerod.metaDungeon.enums.Effects;
import sbs.immovablerod.metaDungeon.game.GConfig;
import sbs.immovablerod.metaDungeon.util.Random;

import static sbs.immovablerod.metaDungeon.game.GConfig.taskManager;

public class MonsterRush extends MetaDungeonEvent {
    private final MetaDungeonEffect effect ;

    public MonsterRush(String name, JsonNode template, int level) {
        super(name, template, level);
        int eventStrength = this.getTemplate().path("strength").asInt();
        this.effect = new MetaDungeonEffect(Effects.SPEED, this.getDuration(), eventStrength);
    }
    @Override
    public void onInitiated() {
        super.onInitiated();
        GConfig.entityManager.getAll().forEach(this.effect::addTarget);
        this.effect.trigger();
    }
}
