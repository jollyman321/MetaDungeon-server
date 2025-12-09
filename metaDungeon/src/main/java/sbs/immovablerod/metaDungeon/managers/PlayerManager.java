package sbs.immovablerod.metaDungeon.managers;

import org.bukkit.entity.Player;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonPlayer;

import java.util.*;

public class PlayerManager {
    private final MetaDungeon plugin = MetaDungeon.getInstance();

    private final HashMap<UUID, MetaDungeonPlayer> players;

    public PlayerManager() {
        this.players = new HashMap<>();
    }

    public void add(Player player) {

        if (!this.players.containsKey(player.getUniqueId())) {
            plugin.getLogger().info("player '" + player.getDisplayName() + "' added to manager id=" + player.getUniqueId());
            this.players.put(player.getUniqueId(), new MetaDungeonPlayer(player));
        } else {
            plugin.getLogger().info("could not add player '" + player.getDisplayName() + "' to manager (reason=already added) id=" + player.getUniqueId());
        }

        // add player to the active game
        if (this.getFromID(player.getUniqueId()).isInGame()) {
            plugin.getLogger().info("readded player " + player.getDisplayName());
            this.players.get(player.getUniqueId()).setPlayer(player);
        }
    }

    public void addPlayersFromWorld() {
        try {
            for (Player p : plugin.world.getPlayers()) {
                this.add(p);
            }
        } catch (NullPointerException ignored) {}
    }

    public void reload() {
        for (MetaDungeonPlayer p : this.players.values()) {
            this.players.put(p.getPlayer().getUniqueId(), new MetaDungeonPlayer(p.getPlayer()));
        }
    }


    public MetaDungeonPlayer getFromID(UUID id) {
        return this.players.get(id);
    }

    public Collection<MetaDungeonPlayer> getAll() {
        return this.players.values();
    }

    public ArrayList<MetaDungeonPlayer> getTeam(String team) {
        ArrayList<MetaDungeonPlayer> output = new ArrayList<>();
        for (MetaDungeonPlayer player : this.players.values()) {
            if (Objects.equals(player.getTeam(), "team")) {
                output.add(player);
            }
        }
        return output;
    }

    public Set<UUID> getAllIDs() {
        return this.players.keySet();
    }

    public void clear() {
        this.players.clear();
    }
}
