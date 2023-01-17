import kkchengaf.EnchantmentOrder.EnchantStep;
import kkchengaf.EnchantmentOrder.Node;
import kkchengaf.EnchantmentOrder.SearchResult;

import java.util.HashMap;

import static kkchengaf.EnchantmentOrder.Main.searchAndParse;

public class Main {
    public static void main(String[] args) {
        //Create a HashMap, store the inputs
        HashMap<String, Integer> enchants = new HashMap<>();
        enchants.put("protection", 4);/*
        enchants.put("fire_protection", 4);
        enchants.put("blast_protection", 4);
        enchants.put("projectile_protection", 4);*/
        enchants.put("feather_falling", 4);
        enchants.put("thorns", 3);
        enchants.put("depth_strider", 3);
        enchants.put("unbreaking", 3);
        enchants.put("soul_speed", 3);
        enchants.put("mending", 1);

        //wrapper to process input, search, and parse tree structure
        SearchResult result = searchAndParse(enchants);
        //custom function to traversal the tree, output the result to external
        result.printResult();

        //custom function to traversal the tree
        Integer[] totalCost = new Integer[] {0, 0};
        result.traversal(new EnchantStep() {
            @Override
            public void parse(Node node) {
                totalCost[0] += node.getValue().getCost();
                totalCost[1] += node.getValue().getPrior();
            }
        });
        System.out.println("Total Enchant Cost: %2s".formatted(totalCost[0]));
        System.out.println("Total  Anvil  Cost: %2s".formatted(totalCost[1]));
    }
}