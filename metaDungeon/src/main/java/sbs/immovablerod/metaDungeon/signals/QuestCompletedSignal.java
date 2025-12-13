package sbs.immovablerod.metaDungeon.signals;

import sbs.immovablerod.metaDungeon.classes.events.MetaDungeonEvent;

public class QuestCompletedSignal extends BaseSignal {

    private final MetaDungeonEvent quest;

    public QuestCompletedSignal(MetaDungeonEvent quest) {
        super();
        this.quest = quest;
    }

    public MetaDungeonEvent getQuest() {
        return quest;
    }
}
