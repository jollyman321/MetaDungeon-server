package sbs.immovablerod.metaDungeon;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonMonster;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonItem;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonProjectile;
import sbs.immovablerod.metaDungeon.commands.*;
import sbs.immovablerod.metaDungeon.game.DungeonMap;
import sbs.immovablerod.metaDungeon.game.Game;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonPlayer;
import sbs.immovablerod.metaDungeon.util.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public final class MetaDungeon extends JavaPlugin implements Listener {
    public HashMap<UUID, MetaDungeonPlayer>     players = new HashMap<>();
    public HashMap<UUID, MetaDungeonMonster> entities = new HashMap<>();
    public HashMap<UUID, MetaDungeonProjectile> projectiles = new HashMap<>();

    public List<BukkitTask> tasks = new ArrayList<>();
    public World world;
    public Game game = null;
    public DungeonMap map = null;

    public final HashMap<String, HashMap<String, String>> mapsDB = LoadSqlTable.loadTable("maps", "name");
    public final HashMap<String, HashMap<String, String>> worldEventsDB = LoadSqlTable.loadTable("world_events", "name");

    public HashMap<UUID, MetaDungeonItem> items = new HashMap<>();
    public final ObjectMapper objectMapper = new ObjectMapper();
    public final JsonNode itemsV2 = objectMapper.readTree(new File("plugins" + File.separator + "metaDungeon" + File.separator + "items.json"));
    public final JsonNode entityTemplates = objectMapper.readTree(new File("plugins" + File.separator + "metaDungeon" + File.separator + "monsters.json"));
    public final JsonNode gameplay = objectMapper.readTree(new File("plugins" + File.separator + "metaDungeon" + File.separator + "gameplay.json"));

    public HashMap<String, ReadWriteNBT> baseItemNbts  = RenderItemsV2.renderBaseItemNbt(itemsV2);

    private static MetaDungeon instance;

    public MetaDungeon() throws IOException {
    }

    public static MetaDungeon getInstance() {
        return MetaDungeon.instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getLogger().setLevel(Level.ALL);
        getDataFolder().mkdirs();
        MetaDungeon.instance = this;
        world = Bukkit.getWorld("world");

        getServer().getPluginManager().registerEvents(new ServerListener(), this);

        this.getCommand("init_map").setExecutor(new InitMap());
        this.getCommand("start_game").setExecutor(new StartGame());
        this.getCommand("stop_game").setExecutor(new StopGame());
        this.getCommand("md_apply_effect").setExecutor(new ApplyEffect());

        try {
            for (Player p : world.getPlayers()) {
                System.out.println(items.keySet());
                if (!players.containsKey(p.getUniqueId())) {
                    players.put(p.getUniqueId(), new MetaDungeonPlayer(p.getPlayer()));
                    //SpawnEntity.spawn("zombie_level_1", p.getLocation());
                }
            }
        } catch (NullPointerException ignored) {}

    }

    @Override
    public void onDisable() {
        if (this.game != null) this.game.stopGame();

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        System.out.println("player " + event.getPlayer().getName() + " has joined");
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new EmptyChunkGenerator();
    }

}
