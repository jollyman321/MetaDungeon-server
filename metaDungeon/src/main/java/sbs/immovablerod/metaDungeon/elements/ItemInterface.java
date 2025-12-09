package sbs.immovablerod.metaDungeon.elements;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonAttack;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonEntity;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonPlayer;

public class ItemInterface {
    public void onRightClick(MetaDungeonPlayer player) {};
    public void onCreated() {};
    public void onCommitAttack(MetaDungeonEntity attacker, MetaDungeonEntity victim) {}
    public void onReceiveAttack(MetaDungeonEntity attacker, MetaDungeonEntity victim) {}
    public void onTeamMemberReceiveAttack(MetaDungeonEntity itemUser, MetaDungeonAttack attack) {}
}
