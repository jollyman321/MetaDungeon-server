package sbs.immovablerod.metaDungeon.util;

import com.fasterxml.jackson.databind.JsonNode;

public class EntityUtil {
    public static int getScaledInt(JsonNode template, String baseKey, String scalingKey, int level) {

        float baseValue = template.path(baseKey).asInt(0);
        float scaleValue = (float) (template.path(scalingKey).asDouble(0) / 100.0);

        return (int) (baseValue * (1 + scaleValue * (level - 1)));

    }
}
