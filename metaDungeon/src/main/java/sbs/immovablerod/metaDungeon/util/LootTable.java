package sbs.immovablerod.metaDungeon.util;


import com.fasterxml.jackson.databind.JsonNode;
import sbs.immovablerod.metaDungeon.MetaDungeon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import static sbs.immovablerod.metaDungeon.util.Random.getRandInt;

public class LootTable {
    private final MetaDungeon plugin = MetaDungeon.getInstance();
    private final JsonNode data;
    private final HashMap<String, List<String>> selections;
    private final NestedMap mappings;
    private final HashMap<Integer, List<String>> hashMappings;
    public LootTable(JsonNode data) {
        this.data = data;
        this.selections = new HashMap<>();
        this.mappings = new NestedMap<String, String>();
        this.hashMappings = new HashMap<>();
    }

    public int computeHash(List<String> values) {
        // converts a list of strings into a unique repeatable hash
        final int prime = 31;
        int result = 1;
        for( String s : values ) {
            result = result * prime + s.hashCode();
        }
        return result;
    }

    public void generateMappings(String... keys) {
        // pregenerates the hash for the selected keys
        Iterator<JsonNode> values = this.data.elements();

        while (values.hasNext()) {
            JsonNode value = values.next();

            List<String> unhashed = new ArrayList<>();
            for (String key : keys) {
                unhashed.add(value.path(key).asText());
            }

            int hash = computeHash(unhashed);

            if (!this.hashMappings.containsKey(hash)) {
                this.hashMappings.put(hash, new ArrayList<>());

            }
            this.hashMappings.get(hash).add(value.path("internalName").asText());
        }

        plugin.getLogger().info("generated hashmap= " +  this.hashMappings.toString());
    }
    public String fetch(List<String> parameters) {
        // NOTE the order of the parameters affects the hash
        int targetHash = computeHash(parameters);

        if (!this.hashMappings.containsKey(targetHash)) {
            plugin.getLogger().warning("[LootTable] Could not find target params='" +  parameters +"' hash='" + targetHash + "'");
            return null;
        }
        return this.hashMappings.get(targetHash).get(getRandInt(0, this.hashMappings.get(targetHash).size()-1));
    }
}
