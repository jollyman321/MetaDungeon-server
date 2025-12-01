package sbs.immovablerod.metaDungeon.util;

import sbs.immovablerod.metaDungeon.game.Debug;

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

    public static String resolveSelection(String category,
                                          String tier,
                                          String rarity,
                                          HashMap<String, HashMap<String, HashMap<String, List<String>>>> selection,
                                          int maxRetries) {
        int tierInt = Integer.parseInt(tier);
        int rarityInt = Integer.parseInt(rarity);

        try {
            List<String> selectionList = selection.get(tier).get(rarity).get(category);
            return selectionList.get(Random.getRandInt(0, selectionList.size() - 1));
        } catch (NullPointerException ignored) {
            if (maxRetries > 1) {
                return resolveSelection(category, tier, rarity, selection, maxRetries - 1);
            } else {
                System.out.println("[WARN] could not find selection (max retries)" + " tier=" + tier + " rarity=" + rarity + " category=" + category);
                return null;
            }
        }


//        // selected tier has no entry => try again at a lower tier
//        if (selectionByTier == null) {
//            if (tierInt > 1) {
//                System.out.println("Could not find any items for tier '" + tier + "' trying again @ tier=" + (tierInt - 1));
//                return resolveSelection(categories, String.valueOf(tierInt - 1), rarity, selection);
//            }
//            // critical fail
//            System.out.println("[WARN] Could not find any items for tier '" + tier + "' (no other tiers, aborting)");
//            return null;
//        }
//
//        HashMap<String, List<String>> selectionByRarity = selectionByTier.get(rarity);
//
//        if (selectionByRarity == null) {
//            if (rarityInt > 1) {
//                System.out.println("Could not find any items for rarity '" + rarity + "' tier='" + tier + "' trying again @ rarity=" + (rarityInt - 1));
//                return resolveSelection(categories,  tier, String.valueOf(rarityInt - 1), selection);
//            }
//            // critical fail
//            System.out.println("Could not find any items for rarity '" + rarity + "' tier='" + tier + "' (no other rarities, aborting)");
//            return null;
//        }
//
//        String category = categories.next();
//        List<String>  selectionByCategory = selectionByRarity.get(category);
//        if (selectionByCategory == null) {
//            System.out.println("Could not find any items for category '" + category + "' rarity='" + rarity + "' tier='" + tier + "' trying again");
//            return resolveSelection(categories,  tier,rarity, selection);
//        }
//        return null;


//        if (selectionByTier.isEmpty()) {
//            System.out.println("Could not find any items for tier '" + tier + "' aborting!");
//            return null;
//        }
//
//        for (int i = Integer.parseInt(rarity); i > 0; i--) {
//            HashMap<String, List<String>> selectionByRarity = selectionByTier.get(rarity);
//            if (selectionByRarity == null) {
//                if (i == 1) {
//                    System.out.println("Could not find any items for rarity '" + rarity +"'(tier)='" + tier +"' aborting!");
//                    return null;
//                }
//                continue;
//            }
//            for (int j = Integer.parseInt(rarity); j > 0; j--) {
//                List<String> selectionByCategory = selectionByRarity.get(category);
//                if (selectionByCategory == null) {
//                    if (Integer.parseInt(rarity) > 1) {
//                        return resolveSelection(category, tier, rarity - 1, selection);
//                    } else if (Integer.parseInt(tier) > 1) {
//                        return resolveSelection(category, tier, rarity, selection);
//                    } else {
//                        System.out.println("Could not find any items for rarity '" + rarity + "'(tier)='" + tier + "' (category)='" + category +"' aborting!");
//                        return null;
//                    }
//                    continue;
//                }
//                return selectionByCategory.get(Random.getRandInt(0, selectionByCategory.size() - 1));
//            }
//        }
//
//        return null;
    }
}
