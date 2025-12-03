package sbs.immovablerod.metaDungeon.util;

import java.util.ArrayList;
import java.util.HashMap;

class NestedMap<K, V> {

    private final HashMap<K, NestedMap> child;
    private ArrayList<String> value;

    public NestedMap() {
        child = new HashMap<>();
        value = new ArrayList<String>();
    }

    public boolean hasChild(K k) {
        return this.child.containsKey(k);
    }

    public NestedMap<K, V> getChild(K k) {
        return this.child.get(k);
    }

    public void makeChild(K k) {
        this.child.put(k, new NestedMap());
    }

    public ArrayList<String> getValues() {
        return value;
    }

    public void add(String v) {
        value.add(v);
    }
}
