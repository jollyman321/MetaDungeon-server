package sbs.immovablerod.metaDungeon.elements.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.weather.WeatherEvent;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonEffect;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonEvent;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonMonster;
import sbs.immovablerod.metaDungeon.elements.EventInterface;
import sbs.immovablerod.metaDungeon.enums.Colors;
import sbs.immovablerod.metaDungeon.enums.Effects;
import sbs.immovablerod.metaDungeon.game.GConfig;
import sbs.immovablerod.metaDungeon.util.Random;

public class MonsterCore extends EventInterface {
    private final MetaDungeonEvent root;
    private MetaDungeonEffect effect ;
    private MetaDungeonMonster entity;
    private final Integer eventStrength;
    public MonsterCore(MetaDungeonEvent root) {
        this.root = root;
        this.eventStrength = root.getLevel() * 2;
        this.entity = null;
        this.effect = null;
        int roll = Random.getRandInt(1, 2);
        if (roll == 1) {
            this.effect = new MetaDungeonEffect(Effects.GUARDED, -1, this.eventStrength);
        } else if (roll == 2) {
            this.effect = new MetaDungeonEffect(Effects.STRENGTH, -1, this.eventStrength);
        }
    }

    @Override
    public void onInitiated() {
        super.onInitiated();

        Location location = GConfig.entityManager.getRandomLocation();
        this.entity = GConfig.entityManager.add("mobTotem", location, this.root.getLevel());

        GConfig.messageManager.messageAll(
                this.root.getTemplate().at("/onInitiated/message").asText().replace
                        ("@POS",  location.getX() + ", " + location.getY() + ", " + location.getZ()).replace
                        ("@POWER", this.eventStrength.toString()).replace
                        ("@TYPE", effect.getName().toString()),
                Colors.RED
        );
        this.update();
        System.out.println("Monster Core Init " + location.toString());
    }

    private void update() {
        if (!this.entity.getEntity().isDead()) {
            for (MetaDungeonMonster monster : GConfig.entityManager.getAll()) {
                monster.addEffect(this.effect);
            }
            Bukkit.getScheduler().runTaskLater(plugin, this::update, 20L * 4);
        } else {
            this.root.end();
        }
    }

    @Override
    public void onCompleted() {
        super.onCompleted();
        this.effect.deactivate();
        GConfig.messageManager.messageAll(this.root.getTemplate().at("/onCompleted/message").asText(), Colors.GREEN);
    }
}
