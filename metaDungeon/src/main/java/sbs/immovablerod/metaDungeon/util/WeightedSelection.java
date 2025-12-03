package sbs.immovablerod.metaDungeon.util;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WeightedSelection {
    private final ArrayList<RandomCollection<String>> collections;

    public WeightedSelection(JsonNode... selections) {
        //creates a set of weighed random collection that can be rolled at the same time

        this.collections = new ArrayList<>();
        for (JsonNode selection : selections) {
            Iterator<JsonNode> values  = selection.elements();
            Iterator<String> keys = selection.fieldNames();
            collections.add(new RandomCollection<>());
            while (values.hasNext()) {
                JsonNode value = values.next();
                String key = keys.next();
                collections.get(collections.size()-1).add(value.asDouble(), key);
            }
        }
    }

    public ArrayList<String> resolveRoll() {
        ArrayList<String> output = new ArrayList<>();
        for (RandomCollection<String> collection : collections) {
            output.add(collection.next());
        }

        return output;
    }
}
