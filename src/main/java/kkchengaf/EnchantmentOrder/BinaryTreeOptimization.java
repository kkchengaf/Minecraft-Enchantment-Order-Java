package kkchengaf.EnchantmentOrder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * static class of searching the optimal order
 */
public class BinaryTreeOptimization {
    private static String PATH_TO_BTREE = "EnchantmentOrder/trees.json";
    private static JSONObject dict = null;


    private static JSONObject getBTrees() {
        if(dict==null) {
            dict = JSONLoader.from(BinaryTreeOptimization.class, BinaryTreeOptimization.PATH_TO_BTREE);
        }
        return dict;
    }



    /**
     * Return the dot product of the input ArrayLists.
     * The first few elements in second List will be ignored if the size is not match.
     * @param a, ArrayList of enchantmentCost
     * @param b, ArrayList of contributions
     * @return dot product of 2 ArrayLists
     */
    private static Integer dotProduct(ArrayList<Integer> a, ArrayList<Integer> b) {
        Integer result = 0;
        Integer na = a.size(), nb = b.size();
        for (int i = 1; i<=na; i++) {
           result += a.get(na-i) * b.get(nb-i);
        }
        return result;
    }

    /**
     * Return a list of index of elements in the sorted list
     * @param arr input ArrayList (unchanged)
     * @param reverse sort in descending if true
     * @return a ArrayList of index
     */
    private static ArrayList<Integer> argsort(List<Integer> arr, boolean reverse) {
        ArrayList<Integer> res = new ArrayList<>();
        for(Integer i=0;i<arr.size();i++) {
            res.add(i);
        }
        if(reverse) {
            res.sort((a, b) -> arr.get(b)-arr.get(a));
        } else {
            res.sort(Comparator.comparingInt(arr::get));
        }
        return res;
    }

    /**
     * Return a copy of target ArrayList sorted based on:
     * smaller term in target goes to index of larger term in ref
     * Assume both list have same size
     * ignore the first element in both lists
     * @param target ArrayList to be sorted
     * @param ref ArrayList to determine the order
     * @return a copy of sorted ArrayList
     */
    private static ArrayList<Integer> sortWithReference(ArrayList<Integer> target, ArrayList<Integer> ref) {
        ArrayList<Integer> result = new ArrayList<>();
        for(Integer i=0;i<target.size();i++)
            result.add(0);
        ArrayList<Integer> srcIndexes = BinaryTreeOptimization.argsort(target.subList(1, target.size()), false);
        ArrayList<Integer> refIndexes = BinaryTreeOptimization.argsort(ref.subList(1, ref.size()), true);

        for(Integer i=0;i<srcIndexes.size();i++) {
            result.set(refIndexes.get(i) + 1, target.get(srcIndexes.get(i) + 1));
        }

        return result;
    }


    /**
     * Search
     * @param enchantmentWeights, a ArrayList of enchantment costs, order independent
     * @return a SearchResult Object
     */
    public static SearchResult search(ArrayList<Integer> enchantmentWeights) {
        if(enchantmentWeights.size()<=2) {
            System.out.println("Less than 3 items");
            return null;
        }
        JSONObject dict = BinaryTreeOptimization.getBTrees();
        if(dict==null) {
            System.out.println("dict is null");
            return null;
        }

        //ignore the first element (the zero representing the item's weight)
        ArrayList<Integer> sortedWeights = new ArrayList<Integer>(
                enchantmentWeights.subList(1, enchantmentWeights.size()));
        sortedWeights.sort((a, b) -> b - a);

        //storage to hold a SearchItem of [JSONObject, anvil cost, enchantment cost]
        ArrayList<SearchItem> minCostStructures = new ArrayList<SearchItem>();

        JSONObject entries = dict.getJSONObject(String.valueOf(enchantmentWeights.size()));
        Iterator<String> anvilCosts = entries.keys();

        //iterate over the set of anvil costs
        //store SearchItem into minCostStructures
        while(anvilCosts.hasNext()) {
            String anvilCost = anvilCosts.next();
            //For each anvil cost, there are multiple structures
            JSONArray listOfStructure = entries.getJSONArray(anvilCost);
            //track the lowest enchant cost for this anvil cost
            Integer minEnchantCost = 10000;
            //store all binary tree temporarily
            ArrayList<BinaryTree> allTrees = new ArrayList<>();

            //iterate over structures in same anvil cost,
            //store btrees and track min enchant cost
            for(Integer i=0;i<listOfStructure.length();i++) {
                //For each structure, it is represented in 3 forms: flat, strc, sort
                BinaryTree btree = new BinaryTree(listOfStructure.getJSONObject(i));
                Integer thisEnchantCost = BinaryTreeOptimization.dotProduct(sortedWeights, btree.getSort());
                btree.setCost(thisEnchantCost);
                allTrees.add(btree);
                if(thisEnchantCost<minEnchantCost) {
                    minEnchantCost = thisEnchantCost;
                }
            }

            //get any one btree with min cost, store it to SearchItem
            for(Integer i=0;i<allTrees.size();i++) {
                if(allTrees.get(i).getCost() == minEnchantCost) {
                    minCostStructures.add(
                      new SearchItem(allTrees.get(i), Integer.parseInt(anvilCost), minEnchantCost)
                    );
                    break;
                }
            }
        }

        //from minCostStructures, get the SearchItem with lowest anvil cost + enchant cost
        Integer minTotalCost = 10000;
        Integer minTotalIndex = -1;
        for (int i = 0; i < minCostStructures.size(); i++) {
            SearchItem si = minCostStructures.get(i);
            Integer thisTotalCost = si.getTotal();
            if(thisTotalCost<minTotalCost) {
                minTotalCost = thisTotalCost;
                minTotalIndex = i;
            }
        }
        if(minTotalIndex==-1) {
            return new SearchResult(new ArrayList<Integer>(), new BinaryTree(null), 10000, 10000);
        }
        SearchItem minResult = minCostStructures.get(minTotalIndex);

        ArrayList<Integer> sortedTarget = BinaryTreeOptimization.sortWithReference(
                enchantmentWeights, minResult.getBTree().getFlatten()
        );

        return new SearchResult(
                sortedTarget, minResult.getBTree(), minResult.getAnvil(), minResult.getEnchant()
        );
    }
}


/**
 * A class to store temporary search result
 */
class SearchItem {
    private BinaryTree btree;
    private Integer anvil;
    private Integer enchant;

    SearchItem(BinaryTree btree, Integer anvil, Integer enchant) {
        this.anvil = anvil;
        this.enchant = enchant;
        this.btree = btree;
    }

    public Integer getAnvil() {
        return anvil;
    }

    public Integer getEnchant() {
        return enchant;
    }

    public Integer getTotal() {
        return anvil + enchant;
    }

    public BinaryTree getBTree() {
        return btree;
    }
}

