package sbs.immovablerod.metaDungeon.classes.events;

import com.fasterxml.jackson.databind.JsonNode;
import org.bukkit.Location;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonEffect;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonMonster;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonPlayer;
import sbs.immovablerod.metaDungeon.enums.Colors;
import sbs.immovablerod.metaDungeon.game.GConfig;
import sbs.immovablerod.metaDungeon.util.Random;

import static sbs.immovablerod.metaDungeon.game.GConfig.taskManager;

public class AcidRain extends MetaDungeonEvent {
    private final MetaDungeon plugin = MetaDungeon.getInstance();
    private MetaDungeonEffect effect;
    private MetaDungeonMonster entity;
    private final Integer eventStrength;
    public AcidRain(String name, JsonNode template, int level) {
        super(name, template, level);
        this.eventStrength = this.getLevel();
    }

    @Override
    public void onInitiated() {
        super.onInitiated();
        plugin.world.setStorm(true);
        plugin.world.setWeatherDuration((this.getDuration() - 3) * 20);
        this.startUpdateLoop(20L);
    }

    private void update() {
        for (MetaDungeonPlayer player : GConfig.playerManager.getAll()) {
            boolean inTheRain = true;
            for (int i = 0;i < 50; i++) {
                Location loc = player.getPlayer().getLocation().add(0, i, 0);
                if (!loc.getBlock().isPassable()) {
                    inTheRain = false;
                    break;
                }
            }
            if (inTheRain && !player.isDead()) {
                player.changeHealth(-this.eventStrength);
            }
        }
    }

    @Override
    public void onCompleted() {
        super.onCompleted();
        plugin.world.setStorm(false);
        plugin.world.setClearWeatherDuration(100000);
    }
}
