package sbs.immovablerod.metaDungeon.util;

import java.util.HashMap;
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

    public static String resolveSelection(String category, String tier, String rarity, HashMap<String, HashMap<String, HashMap<String, List<String>>>> selection) {
        HashMap<String, HashMap<String, List<String>>> itemsByCategory = selection.get(category);
        if (itemsByCategory.isEmpty()) {
            System.out.println("Could not find any items for category '" + category + "' aborting!");
            return null;
        }

        for (int i = Integer.parseInt(tier); i > 0; i--) {
            HashMap<String, List<String>> itemsByTier = itemsByCategory.get(tier);
            if (itemsByTier == null) {
                if (i == 1) {
                    System.out.println("Could not find any items for tier '" + tier +"'(category)='" + category +"' aborting!");
                    return null;
                }
                continue;
            }
            for (int j = Integer.parseInt(rarity); j > 0; j--) {
                List<String> itemsByRarity = itemsByTier.get(rarity);
                if (itemsByRarity == null) {
                    if (i == 1) {
                        System.out.println("Could not find any items for rarity '" + rarity + "'(tier)='" + tier + "' (category)='" + category +"' aborting!");
                        return null;
                    }
                    continue;
                }
                return itemsByRarity.get(Random.getRandInt(0, itemsByRarity.size() - 1));
            }
        }

        return null;
    }
}
