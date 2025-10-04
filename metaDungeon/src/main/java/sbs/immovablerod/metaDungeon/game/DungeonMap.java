package sbs.immovablerod.metaDungeon.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bukkit.Location;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.util.SQL;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DungeonMap {
    private final MetaDungeon plugin = MetaDungeon.getInstance();
    // handles json data conversion
    private final ObjectMapper chestLocationsObjectMapper = new ObjectMapper();

    private final List<Location> chestLocations = new ArrayList<>();
    private final String mapName;
    private List<Location> entityLocations = null;

    public DungeonMap(String mapName) {
        this.mapName = mapName;

        this.renderLocations();
    }

    private void renderLocations() {
        SQL database = null;

        try {
            database = new SQL("plugins" + File.separator + "skillfulhacks" + File.separator + "database.sqlite");
            try (ResultSet query = database.execute_query("SELECT * FROM maps WHERE '" + this.mapName + "' IS name")) {
                try {
                    List<List<Integer>> chestLocationsXYZ = chestLocationsObjectMapper.readValue(query.getString("chest_locations"), List.class);

                    for (List<Integer> xyz : chestLocationsXYZ) {
                        chestLocations.add(new Location(plugin.world, xyz.get(0), xyz.get(1), xyz.get(2)));
                    }
                    entityLocations = chestLocations;
                } catch (JsonProcessingException e) {
                    e.printStackTrace(System.err);
                }
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }
        } finally {
            database.Close();
        }

    }

    public List<Location> getChestLocations()  {return this.chestLocations; }
    public List<Location> getEntityLocations() {return this.entityLocations;}
}
