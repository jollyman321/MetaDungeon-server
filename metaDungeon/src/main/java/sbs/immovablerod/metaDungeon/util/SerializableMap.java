package sbs.immovablerod.metaDungeon.util;

import java.util.List;

public record SerializableMap(String name, List<Integer[]> chestLocations,
                              List<Integer[]> entityLocations, List<Integer[]> oversizedEntityLocations) {
}
