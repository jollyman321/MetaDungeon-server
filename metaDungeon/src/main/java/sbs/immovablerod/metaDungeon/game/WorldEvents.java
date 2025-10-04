package sbs.immovablerod.metaDungeon.game;

import sbs.immovablerod.metaDungeon.Enums.Colors;
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
                System.out.println((int) Math.round(entity.getHealth() * 0.1));
                entity.addEffect("health_boost_add", (int) Math.round(entity.getHealth() * 0.1), -1);
            }
        }
    }

    private static void monsterSpeedRush(Game root) {
        Message.messageAll("[EVENT] " + Colors.RED.code() + "All monsters have gain a sudden burst of energy" , Colors.DARK_GREEN);
        for (MetaDungeonMonster entity : plugin.entities.values()) {
            entity.addEffect("speed_boost", (int) Math.round(entity.getMovementSpeed() * 0.5), 300);
        }
    }

    private static void buffSpeed(Game root) {
        Message.messageAll("[EVENT] " + Colors.RED.code() + "Some monsters have mutated beware! (speed)" , Colors.DARK_GREEN);
        for (MetaDungeonMonster entity : plugin.entities.values()) {

            if (Random.getRandInt(1,2) ==  2) {
                entity.addEffect("speed_boost", (int) Math.round(entity.getMovementSpeed() * 0.05), -1);
            }

        }
    }
}
