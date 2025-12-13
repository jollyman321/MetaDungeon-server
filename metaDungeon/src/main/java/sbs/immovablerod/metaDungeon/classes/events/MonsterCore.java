package sbs.immovablerod.metaDungeon.classes.events;

import com.fasterxml.jackson.databind.JsonNode;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonEffect;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonMonster;
import sbs.immovablerod.metaDungeon.enums.Effects;
import sbs.immovablerod.metaDungeon.enums.Signal;
import sbs.immovablerod.metaDungeon.game.GConfig;
import sbs.immovablerod.metaDungeon.signals.BaseSignal;
import sbs.immovablerod.metaDungeon.signals.MonsterSpawnResolvedSignal;
import sbs.immovablerod.metaDungeon.signals.QuestCompletedSignal;
import sbs.immovablerod.metaDungeon.util.Random;

import java.util.ArrayList;

public class MonsterCore extends QuestEvent {
    private Effects effectType;
    private ArrayList<MetaDungeonEffect> effects ;
    private MetaDungeonMonster entity;
    private final Integer eventStrength;
    public MonsterCore(String name, JsonNode template, int level) {
        super(name, template, level);
        this.eventStrength = this.getLevel() * 2;
        this.effects = new ArrayList<>();

        int roll = Random.getRandInt(1, 2);
        if (roll == 1) {
            this.effectType = Effects.GUARDED;
        } else if (roll == 2) {
            this.effectType = Effects.STRENGTH;
        }
        // quest location must be set before onInitiated
        this.setQuestLocation(GConfig.mapManager.getEntityLocation(true));
    }

    @Override
    public void onInitiated() {
        super.onInitiated();
        MetaDungeonEffect effect = new MetaDungeonEffect(this.effectType, -1, this.eventStrength);
        this.effects.add(effect);
        for (MetaDungeonMonster metaDungeonMonster : GConfig.entityManager.getAll()) {
            effect.addTarget(metaDungeonMonster);
        } effect.trigger();

        this.entity = GConfig.entityManager.add("mobTotem", this.getQuestLocation(), this.getLevel());
        GConfig.signalListeners.get(Signal.MONSTER_SPAWN_RESOLVED).registerTask(this::onMonstersSpawn);

        this.startUpdateLoop(20L * 2);
    }

    @Override
    protected void onUpdate() {
        if (this.entity.getEntity().isDead()) {
            this.onCompleted();
        }
    }

    public void onMonstersSpawn(BaseSignal event) {
        MetaDungeonEffect effect = new MetaDungeonEffect(this.effectType, -1, this.eventStrength);
        this.effects.add(effect);
        for (MetaDungeonMonster metaDungeonMonster : ((MonsterSpawnResolvedSignal) event).getMonsterSpawn().getMonsters()) {
            effect.addTarget(metaDungeonMonster);
        }

    }
    @Override
    public void onCompleted() {
        super.onCompleted();
        GConfig.signalListeners.get(Signal.QUEST_COMPLETED).trigger(new QuestCompletedSignal(this));
        this.effects.forEach(MetaDungeonEffect::deactivate);
    }
}
