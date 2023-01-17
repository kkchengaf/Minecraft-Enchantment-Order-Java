package kkchengaf.EnchantmentOrder;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Main Entry Point
 */
public class Main {


    /**
     * a wrapper to process input, search, and parse tree structure
     * @param enchants, a HashMap from enchantment_id_string to level
     * @return SearchResult, contains all information
     */
    public static SearchResult searchAndParse(HashMap<String, Integer> enchants) {
        //compute a list of weights from inputs
        ArrayList<Integer> input = EnchantmentMap.inputFromHashMapString(enchants);
        //the search sort the weights with reference to the optimal tree structure
        SearchResult result = BinaryTreeOptimization.search(input);
        if(result==null) {
            return null;
        }
        //apply the sorted result to the tree, update the tree values, return the total combining cost
        Integer totalCost = result.parseTreeString(enchants);
        System.out.println("Total Cost: " + totalCost.toString());
        return result;
    }

    /**
     * Main Entry Point
     * @param args, arguments
     */
    public static void main(String[] args) {

        //Create a HashMap, store the inputs
        HashMap<String, Integer> enchants = new HashMap<>();
        enchants.put("protection", 4);
        enchants.put("fire_protection", 4);
        enchants.put("blast_protection", 4);
        enchants.put("projectile_protection", 4);
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