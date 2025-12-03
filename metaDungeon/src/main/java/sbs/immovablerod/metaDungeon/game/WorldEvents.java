package sbs.immovablerod.metaDungeon.game;

import sbs.immovablerod.metaDungeon.enums.Colors;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonMonster;
import sbs.immovablerod.metaDungeon.util.Random;

import java.util.Objects;

public class WorldEvents {
    private static final MetaDungeon plugin = MetaDungeon.getInstance();
    public static void triggerEvent(Game root, String event) {

        if (Objects.equals(event, "entity_hp_buff")) {buffEntityHp(root);}
        else if (Objects.equals(event, "monster_speed_buff"))  {buffSpeed(root);}
        else if (Objects.equals(event, "monster_speed_rush"))  {monsterSpeedRush(root);}
        else if (Objects.equals(event, "monster_damage_buff")) {monsterSpeedRush(root);}

    }
    private static void buffEntityHp(Game root) {
        Message.messageAll("[EVENT] " + Colors.RED.code() + "Some monsters have mutated beware!" , Colors.DARK_GREEN);
        for (MetaDungeonMonster entity : plugin.entities.values()) {
            if (Random.getRandInt(1,2) ==  2) {
            }
        }
    }

    private static void monsterSpeedRush(Game root) {
        Message.messageAll("[EVENT] " + Colors.RED.code() + "All monsters have gain a sudden burst of energy" , Colors.DARK_GREEN);
        for (MetaDungeonMonster entity : plugin.entities.values()) {
        }
    }

    private static void buffSpeed(Game root) {
        Message.messageAll("[EVENT] " + Colors.RED.code() + "Some monsters have mutated beware! (speed)" , Colors.DARK_GREEN);
        for (MetaDungeonMonster entity : plugin.entities.values()) {
            if (Random.getRandInt(1,2) ==  2) {
            }

        }
    }
}
