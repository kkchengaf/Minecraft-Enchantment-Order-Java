# Minecraft-Enchantment-Order-Java
Java program to calculate the optimal combining order in anvil

[Web Version](https://kkchengaf.github.io/Minecraft-Enchantment-Order-Calculator/)

# Description
This is a Java implementation of the *fast search algorithm* in [my previous repo](https://github.com/kkchengaf/Minecraft-Enchantment-Order-Calculator). <br>
The search is limited to a tool + books with only one enchantment, such as when a villager's trading house is built in Minecraft. <br>
The algorithm works very well. It takes less than a second even if there are more than 8 books. <br>
If you are interested in writing Minecraft Mod for this, feel free to use the library. <br>

[Download Jar](https://github.com/kkchengaf/Minecraft-Enchantment-Order-Java/releases/download/v1.0.0/optimal-anvil-order-20230117.jar)

# Usage
Snippet of exmaple program [Main.java](https://github.com/kkchengaf/Minecraft-Enchantment-Order-Java/blob/master/demo/Main.java)
```Java    
    //Create a HashMap, store the inputs
    HashMap<String, Integer> enchants = new HashMap<>();
    enchants.put("protection", 4);
    enchants.put("feather_falling", 4);
    enchants.put("thorns", 3);
    enchants.put("depth_strider", 3);
    enchants.put("unbreaking", 3);
    enchants.put("soul_speed", 3);
    enchants.put("mending", 1);

    //searchAndParse: a wrapper to process input, search, and parse tree structure
    //SearchResult object: stores all information of one optimal combining order
    SearchResult result = searchAndParse(enchants);

    Integer[] totalCost = new Integer[] {0, 0};

    //result.traversal(f): traverse the binary tree, invoke function f every non leaf node (every combine in anvil)
    //write your own custom function to process the information
    result.traversal(new EnchantStep() {
        //all the information stored in the node
        @Override
        public void parse(Node node) {
            totalCost[0] += node.getValue().getCost();
            totalCost[1] += node.getValue().getPrior();
        }
    });
    System.out.println("Total Enchant Cost: %2s".formatted(totalCost[0]));
    System.out.println("Total  Anvil  Cost: %2s".formatted(totalCost[1]));
```

Compile:
```
javac Main.java -cp optimal-anvil-order-20230117.jar
```
Run:
```
java -cp .;optimal-anvil-order-20230117.jar Main
```
# Documentation
Read more in [Javadoc](https://kkchengaf.github.io/Minecraft-Enchantment-Order-Java/). There are several good starting points:
- [SearchResult Class](https://kkchengaf.github.io/Minecraft-Enchantment-Order-Java/EnchantmentOrder/SearchResult.html)
- [Node Class](https://kkchengaf.github.io/Minecraft-Enchantment-Order-Java/EnchantmentOrder/Node.html)
- [NodeValue Class](https://kkchengaf.github.io/Minecraft-Enchantment-Order-Java/EnchantmentOrder/NodeValue.html)
