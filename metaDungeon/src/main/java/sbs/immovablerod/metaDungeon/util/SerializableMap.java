package sbs.immovablerod.metaDungeon.util;

import java.util.List;

public record SerializableMap(String name, Integer[] spawnPos, List<Integer[]> chestLocations,
                              List<Integer[]> entityLocations, List<Integer[]> oversizedEntityLocations) {
}
