package sbs.immovablerod.metaDungeon.elements.events;


import sbs.immovablerod.metaDungeon.classes.MetaDungeonEffect;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonEvent;
import sbs.immovablerod.metaDungeon.elements.EventInterface;
import sbs.immovablerod.metaDungeon.enums.Colors;
import sbs.immovablerod.metaDungeon.enums.Effects;
import sbs.immovablerod.metaDungeon.game.GConfig;
import sbs.immovablerod.metaDungeon.util.Random;

public class MonsterRush extends EventInterface {
    private final MetaDungeonEvent root;
    private final int duration;
    private MetaDungeonEffect effect ;
    private final int eventStrength;
    public MonsterRush(MetaDungeonEvent root) {

        this.root = root;
        this.eventStrength = this.root.getTemplate().path("strength").asInt();
        this.duration = Random.getRandInt(this.root.getTemplate().path("durationMin").asInt(),
                                           this.root.getTemplate().path("durationMax").asInt()
                );
        this.effect = new MetaDungeonEffect(Effects.SPEED, this.duration, this.eventStrength);
    }

    @Override
    public void onInitiated() {
        super.onInitiated();
        GConfig.messageManager.messageAll(this.root.getTemplate().at("/onInitiated/message").asText(), Colors.YELLOW);
        GConfig.entityManager.getAll().forEach(ele -> {
            ele.addEffect(this.effect);
        });
    }

}
