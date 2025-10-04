package sbs.immovablerod.metaDungeon;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonMonster;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonItem;
import sbs.immovablerod.metaDungeon.commands.*;
import sbs.immovablerod.metaDungeon.game.DungeonMap;
import sbs.immovablerod.metaDungeon.game.Game;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonPlayer;
import sbs.immovablerod.metaDungeon.util.LoadSqlTable;
import sbs.immovablerod.metaDungeon.util.RenderItems;

import java.util.*;

public final class MetaDungeon extends JavaPlugin implements Listener {
    public HashMap<UUID, MetaDungeonPlayer>     players = new HashMap<>();
    public HashMap<UUID, MetaDungeonMonster> entities = new HashMap<>();
    public List<BukkitTask> tasks = new ArrayList<>();
    public World world;
    public Game game = null;
    public DungeonMap map = null;

    public final HashMap<String, HashMap<String, String>> entitiesDB = LoadSqlTable.loadTable("entities", "name");
    public final HashMap<String, HashMap<String, String>> itemsDB = LoadSqlTable.loadTable("items", "name");
    public final HashMap<String, HashMap<String, String>> worldEventsDB = LoadSqlTable.loadTable("world_events", "name");

    public HashMap<String, ReadWriteNBT> baseItemNbts  = RenderItems.renderBaseItemNbt(itemsDB);
    public HashMap<UUID, MetaDungeonItem> items = new HashMap<>();//RenderItems.loadAdvancedItems(itemsDB, renderedItems);

    private static MetaDungeon instance;

    public static MetaDungeon getInstance() {
        return MetaDungeon.instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getDataFolder().mkdirs();
        MetaDungeon.instance = this;
        world = Bukkit.getWorld("world");
        getServer().getPluginManager().registerEvents(new ServerListener(), this);

        this.getCommand("test").setExecutor(new Test());
        this.getCommand("init_map").setExecutor(new InitMap());
        this.getCommand("add_item_to_db").setExecutor(new AddItemToDB());
        this.getCommand("load_item_from_db").setExecutor(new LoadItemFromDB());
        this.getCommand("add_entity_to_db").setExecutor(new AddEntityToDB());
        this.getCommand("load_entity_to_db").setExecutor(new LoadEntityFromDB());
        this.getCommand("add_world_event").setExecutor(new AddWorldEvent());
        this.getCommand("debug_sql_write").setExecutor(new DebugSQLWrite());
        this.getCommand("debug_sql_query").setExecutor(new DebugSQLQuery());

        this.getCommand("start_game").setExecutor(new StartGame());
        this.getCommand("stop_game").setExecutor(new StopGame());

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

}
