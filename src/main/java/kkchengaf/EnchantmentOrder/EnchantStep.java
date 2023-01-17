package kkchengaf.EnchantmentOrder;

/**
 * The function that is passed to tree traversal,
 * invoke parse(Node) every non leaf node
 */
public interface EnchantStep {
    /**
     * This function will be called every combine (step) in anvil,
     * node.isItem(): boolean, whether this node represents an item,
     * node.getValue(): NodeValue, contains the information of this node,
     * node.getValue().getCost(): Integer, the enchantment cost of this node,
     * node.getValue().getPrior(): Integer, the prior work penalty of this node,
     * node.getValue().getEnchant(): HashMap Integer, Integer , the enchantments dict of this node
     * @param node, the node that is combined, with left child and right child
     */
    public void parse(Node node);
}
