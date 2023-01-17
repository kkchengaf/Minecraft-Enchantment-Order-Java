package kkchengaf.EnchantmentOrder;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class that store the Search Result,
 * Extends this class as you like
 */
public class SearchResult{
    private ArrayList<Integer> weightOrder;
    private BinaryTree btree;
    private Integer anvilCost;
    private Integer enchantmentCost;

    /**
     * Constructor of the search result
     * @param weightOrder, ArrayList of sorted weight
     * @param btree, BinaryTree of the optimal order
     * @param anvilCost, the total anvil cost of the result
     * @param enchantmentCost, the total enchantment cost of the result
     */
    public SearchResult(ArrayList<Integer> weightOrder, BinaryTree btree,
                 Integer anvilCost, Integer enchantmentCost)
    {
        this.anvilCost = anvilCost;
        this.enchantmentCost = enchantmentCost;
        this.btree = btree;
        this.weightOrder = weightOrder;
    }

    /**
     * get the ArrayList of sorted weight
     * @return ArrayList Integer
     */
    public ArrayList<Integer> getWeightOrder() {
        return weightOrder;
    }
    /**
     * get the Binary Tree
     * @return BinaryTree
     */
    public BinaryTree getBTree() {
        return btree;
    }

    /**
     * get the total cost of prior work penalty
     * @return Integer
     */
    public Integer getAnvilCost() {
        return anvilCost;
    }

    /**
     * get the total cost of the enchantments weights
     * @return Integer
     */
    public Integer getEnchantmentCost() {
        return enchantmentCost;
    }

    /**
     * to String, return the costs, the sorted weights and the tree structure string
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Total Cost: %s + %s\n".formatted(enchantmentCost.toString(), anvilCost.toString()))
                .append(weightOrder.toString())
                .append("\n")
                .append(btree.toString());
        return sb.toString();
    }

    /**
     * Wrapper to tree.parseTree(arrayList, hashmap)
     * @param input, hashmap input
     * @return the total cost to combine all items
     */
    public Integer parseTree(HashMap<Integer, Integer> input) {
        return this.getBTree().parseTree(this.getWeightOrder(), input) + this.getAnvilCost();
    }

    /**
     * String version of searchResult.parseTree,
     * Wrapper to tree.parseTree(arrayList, hashmap)
     * @param input, hashmap input
     * @return the total cost to combine all items
     */
    public Integer parseTreeString(HashMap<String, Integer> input) {
        return this.parseTree(EnchantmentMap.numericHashMap(input));
    }

    /**
     * Wrapper to searchResult.getBTree().traversal(root, f)
     * @param f, the function to handle the node information
     */
    public void traversal(EnchantStep f) {
        this.getBTree().traversal(this.getBTree().getRoot(), f);
    }



    /**
     * Override this function as you like, or just call this.traversal(f)
     */
    public void printResult() {
        this.traversal(new EnchantStep() {
            /**
             * This function contains the information of every combine in anvil
             * invoked every combine step in anvil during tree traversal
             * Implements EnchantStep as you like
             * @param node, the node that contains the information
             */
            @Override
            public void parse(Node node) {
                StringBuilder sb = new StringBuilder();
                Integer cost = node.getValue().getCost(), prior = node.getValue().getPrior();
                sb
                        .append("Cost: %2s ( %2s + %2s ) ".formatted(cost+prior, cost, prior))
                        .append(node.getLeft().toString())
                        .append(" + ")
                        .append(node.getRight().toString())
                        .append(" ===> ")
                        .append(node.toString())
                ;
                System.out.println(sb.toString());
            }
        });
    }
}
