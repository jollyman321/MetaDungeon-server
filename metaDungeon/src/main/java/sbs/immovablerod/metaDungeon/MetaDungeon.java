package sbs.immovablerod.metaDungeon;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonProjectile;
import sbs.immovablerod.metaDungeon.commands.*;
import sbs.immovablerod.metaDungeon.game.Runtime;
import sbs.immovablerod.metaDungeon.util.*;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public final class MetaDungeon extends JavaPlugin implements Listener {
    private static final Logger log = LogManager.getLogger(MetaDungeon.class);
    public JsonLoader jsonLoader;

    public HashMap<UUID, MetaDungeonProjectile> projectiles = new HashMap<>();

    public World world = Bukkit.getWorld("world");
    public Runtime game = null;

    private static MetaDungeon instance;

    public static MetaDungeon getInstance() {
        return MetaDungeon.instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getLogger().setLevel(Level.ALL);
        MetaDungeon.instance = this;
        try {
            jsonLoader = new JsonLoader(this.getDataFolder());
        } catch (IOException e) {
            log.error("e: ", e);
        }


        getServer().getPluginManager().registerEvents(new ServerListener(), this);

        this.getCommand("start_game").setExecutor(new StartGame());
        this.getCommand("stop_game").setExecutor(new StopGame());
        this.getCommand("md_apply_effect").setExecutor(new ApplyEffect());
        this.getCommand("md_give").setExecutor(new GiveItem());
        this.getCommand("md_event").setExecutor(new TriggerEvent());

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands ->
                commands.registrar().register(CreateMap.createCommand("create_map"),
                        "Generates a new map and saves it to maps.json")
        );
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands ->
                commands.registrar().register(Reload.createCommand("md_reload"),
                        "Reloads json files (experimental)")
        );
    }

    @Override
    public void onDisable() {
        if (this.game != null) this.game.stopGame();

    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new EmptyChunkGenerator();
    }

}
