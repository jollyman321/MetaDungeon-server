package sbs.immovablerod.metaDungeon.managers;

import org.bukkit.Location;
import org.bukkit.Sound;
import sbs.immovablerod.metaDungeon.MetaDungeon;

public class SoundManager {
    private final MetaDungeon plugin = MetaDungeon.getInstance();

    public SoundManager() {

    }
    public void play(Location location, Sound sound) {
        plugin.world.playSound(location, sound, 10, 1);
    }


}
