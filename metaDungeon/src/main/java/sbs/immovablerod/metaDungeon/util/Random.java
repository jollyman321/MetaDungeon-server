package sbs.immovablerod.metaDungeon.util;

import com.fasterxml.jackson.databind.JsonNode;
import sbs.immovablerod.metaDungeon.game.Debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Random {
    public static RandomCollection<String> getRandomCollection(List<List<?>> selection) {
        RandomCollection<String> choices = new RandomCollection<>();
        for (List<?> ele : selection) {
            choices.add((double) Double.parseDouble(ele.get(1).toString()),  (String) ele.get(0));
        }
        return choices;
    }

    public static int getRandInt(int a, int b) {
        //System.out.println(a + " " + b + " " + ThreadLocalRandom.current().nextInt(a, b));
        if (a != b) {
            return ThreadLocalRandom.current().nextInt(a, b + 1);
        }
        return a;
    }

    public static RandomCollection<String> generateLootTier(int currentRound) {
        RandomCollection<String> choices = new RandomCollection<>();
        if (currentRound <= 5) {
            choices.add(Math.cos(((double) (currentRound) / 4.0)), "1");
        }
        if (currentRound<= 10) {
            choices.add(Math.sin(((double) (currentRound) / 12)) , "2");
        }
        if (5 <= currentRound && currentRound <= 10) {
            choices.add(Math.sin(((double) (currentRound) / 12.0) - Math.PI / 2.0) + 1, "3");
        }

        System.out.println("loot weights for " + currentRound);
        System.out.println(Math.cos(((double) (currentRound) / 4.0)));
        System.out.println(Math.sin(((double) (currentRound) / 12.0)));
        System.out.println(Math.sin(((double) (currentRound) / 12.0) - Math.PI / 2.0) + 1);

        return choices;
    }
}
