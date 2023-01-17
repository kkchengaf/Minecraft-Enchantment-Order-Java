package kkchengaf.EnchantmentOrder;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *  Class that store information of a tree node,
 *  cost: the cost of enchantment weight,
 *  prior: the cost of prior work penalty,
 *  enchant: the mapping of id:lv of the enchantments of this node
 */
public class NodeValue {
    private Integer cost;
    private Integer height;
    private Integer prior;
    private HashMap<Integer, Integer> enchant;

    /**
     * default constructor
     */
    public NodeValue() {

    }

    /**
     * construct the NodeValue
     * @param c, the equivalent enchantment weight
     * @param h, the height of the node, 0 for leaves
     * @param p, the prior work penalty
     * @param e, the HashMap Integer,Integer of the enchantments
     */
    public NodeValue(Integer c, Integer h, Integer p, HashMap<Integer,Integer> e) {
        this.cost = c;
        this.height = h;
        this.prior = p;
        this.enchant = e;
    }

    /**
     * Check if two node values are equal
     * @param obj, object to be compared
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof NodeValue)) {
            return false;
        }
        return this.cost.equals(((NodeValue) obj).cost) &&
                this.height.equals(((NodeValue) obj).height) &&
                this.prior.equals(((NodeValue) obj).prior) &&
                this.enchant.equals(((NodeValue) obj).enchant);
    }

    /**
     * set the equivalent cost of the enchantments
     * @param cost, Integer
     */
    public void setCost(Integer cost) {
        this.cost = cost;
    }
    /**
     * set the enchantments
     * @param enchant, Hashmap
     */
    public void setEnchant(HashMap<Integer, Integer> enchant) {
        this.enchant = enchant;
    }

    /**
     * set the height
     * @param h, Integer
     */
    public void setHeight(Integer h) {
        this.height = h;
    }

    /**
     * set the prior work penalty
     * @param prior, Integer
     */
    public void setPrior(Integer prior) {
        this.prior = prior;
    }

    /**
     * get the equivalent cost of the enchantments
     * @return Integer
     */
    public Integer getCost() {
        return cost;
    }

    /**
     * get the height, 0 for leaves
     * @return Integer
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * get the prior work penalty, non 0 for non leaf nodes
     * @return Integer
     */
    public Integer getPrior() {
        return prior;
    }

    /**
     * get the enchantments
     * @return HashMap
     */
    public HashMap<Integer, Integer> getEnchant() {
        return enchant;
    }

    /**
     * check whether this item is non book
     * @return boolean
     */
    public boolean isItem() {
        return this.enchant.containsKey(-100);
    }

    /**
     * to String value, return "" if enchantment is null,
     * return "item" for the item to be combined,
     * return the enchantments for the books
     * @return String
     */
    @Override
    public String toString() {
        if(this.enchant==null){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if(this.isItem()){
            sb.append("item");
            return sb.toString();
        }
        Iterator it = this.enchant.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<Integer, Integer> pair = (Map.Entry) it.next();
            if(pair.getKey()==-100){
                continue;
            }
            sb.append(EnchantmentMap.fromNumericID(pair.getKey().toString()))
                    .append("-")
                    .append(EnchantmentMap.toEnchantLevel(pair.getValue()));
            if(it.hasNext())
                sb.append(",");
        }
        return sb.toString();
    }
}