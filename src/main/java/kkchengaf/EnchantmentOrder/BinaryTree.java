package kkchengaf.EnchantmentOrder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * The underlying tree structure of a search result,
 * it stores a reference to the result data after constructor,
 * buildTree(): Node, build the tree structure for later traversal,
 * parseTree(inputHashMap): Integer, assign the data to all the tree nodes,
 * traversal(node, callback): Boolean, post-order traversal and callback non leaf node
 */
public class BinaryTree {
    private ArrayList<Integer> flat;
    private JSONArray strc;
    private ArrayList<Integer> sort;
    private Integer cost;
    private Node root;

    /**
     * Constructor of the tree
     * @param packed, JSONObject from data in json file
     */
    public BinaryTree(JSONObject packed) {
        if(packed==null) {
            return;
        }
        this.flat = new ArrayList<>();
        this.sort = new ArrayList<>();
        JSONArray flat = packed.getJSONArray("flat");
        JSONArray sort = packed.getJSONArray("sort");
        for(Integer i = 0; i<flat.length(); i++) {
            this.flat.add(flat.getInt(i));
            this.sort.add(sort.getInt(i));
        }
        this.strc = packed.getJSONArray("strc");
        this.cost = 0;
        this.root = null;
    }

    /**
     * Get flatten array of the tree structure
     * @return Integer ArrayList
     */
    public ArrayList<Integer> getFlatten() {
        return flat;
    }

    /**
     * Get sorted flatten array of the tree structure
     * @return Integer ArrayList
     */
    public ArrayList<Integer> getSort() {
        return sort;
    }

    /**
     * Get the JSONArray of the structure
     * @return JSONArray of Integer
     */
    public JSONArray getStructure() {
        return strc;
    }

    /**
     * set the total cost of enchantment weights
     * @param cost, Integer
     */
    public void setCost(Integer cost) {
        this.cost = cost;
    }

    /**
     * get the total cost of enchantment weights
     * @return Integer
     */
    public Integer getCost() {
        return this.cost;
    }

    /**
     * get the tree root of the structure
     * @return Node
     */
    public Node getRoot() {
        return root;
    }

    @Override
    public String toString() {
        return this.getStructure().toString();
    }

    /**
     * Build the tree based on the structure strc,
     * value of the node are not built,
     * Return the tree root for traversal
     * @return Node, the tree root of the structure
     */
    public Node buildTree() {
        if(this.root==null) {
            this.root = this.recursion(this.strc);
        }
        return this.root;
    }

    /**
     * Build the Node based on JSONObject/JSONArray/Integer obj
     * @param obj
     * @return
     */
    private Node recursion(Object obj) {
        if(obj instanceof JSONArray) {
            Integer length = ((JSONArray) obj).length();
            if(length==2)
                return new Node(
                        this.recursion(((JSONArray) obj).get(0)),
                        this.recursion(((JSONArray) obj).get(1)),
                        null);
            else
                return new Node(null, null, null);
        } else if(obj instanceof Integer) {
            return new Node(null, null, null);
        }
        return null;
    }


    private static Integer wrtIndex;
    private static Integer priorPenalty;
    private static Integer tmpSum;
    private ArrayList<NodeValue> wrt;

    /**
     * format the input before computing the tree nodes
     * @param sortedTarget, the sorted list of weights
     * @param inputsSrc, the input map from enchant id (numeric) to level
     * @return
     */
    private ArrayList<NodeValue> preprocess(ArrayList<Integer> sortedTarget, HashMap<Integer, Integer> inputsSrc) {
        HashMap<Integer, Integer> inputs = (HashMap) inputsSrc.clone();
        ArrayList<NodeValue> formattedInput = new ArrayList<>();
        formattedInput.add(new NodeValue(0, 0, 0, new HashMap<Integer, Integer>(){{
            put(-100, 0);
        }}));
        for(Integer i=1;i<sortedTarget.size();i++) {
            Integer currentID = -1;

            Iterator it = inputs.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry<Integer, Integer> pair = (Map.Entry) it.next();
                if(EnchantmentMap.getCost(pair.getKey(), pair.getValue()) == sortedTarget.get(i)) {
                    currentID = pair.getKey();
                    break;
                }
            }
            HashMap<Integer, Integer> tmpEnchant = new HashMap<Integer, Integer>();
            tmpEnchant.put(currentID, inputs.get(currentID));
            formattedInput.add(new NodeValue(0, 0, 0, tmpEnchant));
            inputs.put(currentID, 0);
        }
        //the item to be combined
        inputsSrc.put(-100, 0);
        return formattedInput;
    }

    /**
     * Assign and updates NodeValues to the tree nodes
     * @param sortedTarget, ArrayList NodeValue, the mapping from sortedlist of weight to NodeValue
     * @param inputsSrc, HashMap Integer, Integer, the dict of id:lv of the books
     * @return the total Enchantment cost to combine all the things (exclude anvil cost)
     */
    public Integer parseTree(ArrayList<Integer> sortedTarget, HashMap<Integer, Integer> inputsSrc) {
        ArrayList<NodeValue> newList = this.preprocess(sortedTarget, inputsSrc);
        if(newList.equals(this.wrt)) {
            return BinaryTree.tmpSum;
        }
        this.wrt = newList;
        BinaryTree.wrtIndex = 0;
        BinaryTree.priorPenalty = 0;
        BinaryTree.tmpSum = 0;
        this.traversalParse(this.buildTree());
        inputsSrc.remove(-100);
        return BinaryTree.tmpSum;
    }

    /**
     * Recursion helper to update the underlying tree nodes
     * @param current, the node to be processed
     * @return NodeValue of this node after processed
     */
    private NodeValue traversalParse(Node current) {
        if(current==null) {
            return new NodeValue(-1, -1, -1, null);
        }
        NodeValue left = this.traversalParse(current.getLeft());
        NodeValue right = this.traversalParse(current.getRight());
        current.setValue(this.packNode(left, right));
        return current.getValue();
    }


    private int [] anvilCosts = new int[] {0, 1, 3, 7, 15, 31, 63, 127, 255};

    /**
     * Calculate the height and prior work penalty of non leaf nodes,
     * pass the calculation of cost and enchants to pseudoEnchant
     * @param left
     * @param right
     * @return
     */
    private NodeValue packNode(NodeValue left, NodeValue right) {
        NodeValue result = new NodeValue();
        NodeValue tmpCurrent = null;
        result.setHeight(Math.max(left.getHeight(), right.getHeight()) + 1);
        //Leaves
        if(left.getCost()==-1 || right.getCost()==-1) {
            tmpCurrent = this.wrt.get(BinaryTree.wrtIndex);
            BinaryTree.wrtIndex += 1;
        } else {
            tmpCurrent = this.pseudoEnchant(left.getEnchant(), right.getEnchant());
            result.setPrior(anvilCosts[left.getHeight()] + anvilCosts[right.getHeight()]);
            BinaryTree.priorPenalty += result.getPrior();
        }
        if(tmpCurrent!=null) {
            result.setCost(tmpCurrent.getCost());
            result.setEnchant(tmpCurrent.getEnchant());
        }
        return result;
    }

    /**
     * Calculate the cost and output enchantment as it would be when it is combined
     * Assumption of perfect case:
     * 1. all books are with single enchantment
     * 2. all books are distinct (no two books has same enchants, regardless level)
     * 3. there is always an item with no enchantment
     * 4. all enchantments are compatible
     * 5. all item/books as input have no prior work penalty
     * 6. enchant in Java edition
     * @param left, enchantments on left item
     * @param right, enchantments on right item
     * @return
     */
    private NodeValue pseudoEnchant(HashMap<Integer, Integer> left, HashMap<Integer,Integer> right) {
        Integer enchantCost = 0;
        HashMap<Integer, Integer> result = new HashMap<>();

        //keep left enchantments
        Iterator it = left.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<Integer, Integer> pair = (Map.Entry) it.next();
            result.put(pair.getKey(), pair.getValue());
        }
        //combine right enchantments, increase cost
        it = right.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> pair = (Map.Entry) it.next();
            enchantCost += EnchantmentMap.getCost(pair.getKey(), pair.getValue());
            result.put(pair.getKey(), pair.getValue());
        }
        BinaryTree.tmpSum += enchantCost;

        return new NodeValue(enchantCost, 0, 0, result);
    }

    /**
     * MUST invoke tree.parseTree before this function,
     * Traverse the tree, execute the function every non leaf node
     * @param node, the node that is processing
     * @param f, implements the EnchantStep().parse function
     * @return boolean, indicate whether it is non leaf node/root
     */
    public boolean traversal(Node node, EnchantStep f) {
        if(node==null || node.getValue()==null) {
            return false;
        }
        Boolean left = this.traversal(node.getLeft(), f);
        Boolean right = this.traversal(node.getRight(), f);
        if(left && right) {
            f.parse(node);
        }
        return true;
    }


}

