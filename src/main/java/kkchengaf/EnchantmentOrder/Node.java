package kkchengaf.EnchantmentOrder;


/**
 * The Node Class that maintains the optimal combining structure,
 * left, right: the items in anvil to be combined,
 * value: object of NodeValue that stores the information of a tree node
 */
public class Node {
    private Node left;
    private Node right;
    private NodeValue value;

    /**
     * Tree Node Constructor
     * @param l, Node, left child
     * @param r, Node, right child
     * @param v, NodeValue, the data
     */
    public Node(Node l, Node r, NodeValue v) {
        this.left = l;
        this.right = r;
        this.value = v;
    }

    /**
     * get the left child
     * @return Node
     */
    public Node getLeft() {
        return left;
    }

    /**
     * get the right child
     * @return Node
     */
    public Node getRight() {
        return right;
    }

    /**
     * get the data of this node
     * @return NodeValue
     */
    public NodeValue getValue() {
        return value;
    }

    /**
     * set the data of this node
     * @param value, NodeValue
     */
    public void setValue(NodeValue value) {
        this.value = value;
    }

    /**
     * check whether this node's data is an item
     * @return boolean
     */
    public boolean isItem() {
        return this.value.isItem();
    }

    /**
     * to String, return "" if data is unset
     * @return String
     */
    public String toString() {
        if(this.value==null)
            return "";
        return this.value.toString();
    }
}