package sbs.immovablerod.metaDungeon.elements.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonEffect;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonEvent;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonMonster;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonPlayer;
import sbs.immovablerod.metaDungeon.elements.EventInterface;
import sbs.immovablerod.metaDungeon.enums.Colors;
import sbs.immovablerod.metaDungeon.enums.Effects;
import sbs.immovablerod.metaDungeon.game.GConfig;
import sbs.immovablerod.metaDungeon.util.Random;

public class AcidRain extends EventInterface {
    private final MetaDungeon plugin = MetaDungeon.getInstance();
    private final MetaDungeonEvent root;
    private final int duration;
    private MetaDungeonEffect effect ;
    private MetaDungeonMonster entity;
    private final Integer eventStrength;
    public AcidRain(MetaDungeonEvent root) {
        this.root = root;
        this.eventStrength = root.getLevel();
        this.duration = Random.getRandInt(60, 180);
    }

    @Override
    public void onInitiated() {
        super.onInitiated();
        GConfig.messageManager.messageAll(this.root.getTemplate().at("/onInitiated/message").asText(), Colors.RED);
        plugin.world.setStorm(true);
        plugin.world.setWeatherDuration(this.duration * 20);
        Bukkit.getScheduler().runTaskLater(plugin, this.root::end, 20L * (this.duration - 2));
        this.update();
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
        if (!this.root.isCompleted()) {
            plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, this::update, 20L));
        }
    }

    @Override
    public void onCompleted() {
        super.onCompleted();
        plugin.world.setStorm(false);
        plugin.world.setClearWeatherDuration(100000);
        GConfig.messageManager.messageAll(this.root.getTemplate().at("/onCompleted/message").asText(), Colors.GREEN);
    }
}
