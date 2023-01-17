package EnchantmentOrder;

import kkchengaf.EnchantmentOrder.EnchantStep;
import kkchengaf.EnchantmentOrder.Main;
import kkchengaf.EnchantmentOrder.Node;
import kkchengaf.EnchantmentOrder.SearchResult;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testGodBoots() {
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
        SearchResult boots = Main.searchAndParse(enchants);
        assertNotEquals(null, boots);
        assertNotEquals("", boots.toString());
        assertEquals(103 ,boots.parseTreeString(enchants));
    }


    @Test
    void testBestBoots() {
        HashMap<String, Integer> enchants = new HashMap<>();
        enchants.put("protection", 4);
        enchants.put("feather_falling", 4);
        enchants.put("thorns", 3);
        enchants.put("depth_strider", 3);
        enchants.put("unbreaking", 3);
        enchants.put("soul_speed", 3);
        enchants.put("mending", 1);
        SearchResult boots = Main.searchAndParse(enchants);
        assertEquals(66 ,boots.parseTreeString(enchants));
    }

    @Test
    void testGodHelmet() {
        HashMap<String, Integer> enchants = new HashMap<>();
        enchants.put("protection", 4);
        enchants.put("fire_protection", 4);
        enchants.put("blast_protection", 4);
        enchants.put("projectile_protection", 4);
        enchants.put("mending", 1);
        enchants.put("aqua_affinity", 1);
        enchants.put("respiration", 3);
        enchants.put("thorns", 3);
        enchants.put("unbreaking", 3);
        SearchResult helmet = Main.searchAndParse(enchants);
        assertEquals(80 ,helmet.parseTreeString(enchants));
    }

    @Test
    void testBestHelmet() {
        HashMap<String, Integer> enchants = new HashMap<>();
        enchants.put("protection", 4);
        enchants.put("mending", 1);
        enchants.put("aqua_affinity", 1);
        enchants.put("respiration", 3);
        enchants.put("thorns", 3);
        enchants.put("unbreaking", 3);
        SearchResult helmet = Main.searchAndParse(enchants);
        assertEquals(45 ,helmet.parseTreeString(enchants));
    }

    @Test
    void testBestSword() {
        HashMap<String, Integer> enchants = new HashMap<>();
        enchants.put("sharpness", 5);
        enchants.put("mending", 1);
        enchants.put("fire_aspect", 2);
        enchants.put("knockback", 2);
        enchants.put("looting", 3);
        enchants.put("sweeping_edge", 3);
        enchants.put("unbreaking", 3);
        SearchResult sword = Main.searchAndParse(enchants);
        assertEquals(49 ,sword.parseTreeString(enchants));
    }


    @Test
    void testBestBow() {
        HashMap<String, Integer> enchants = new HashMap<>();
        enchants.put("mending", 1);
        enchants.put("flame", 1);
        enchants.put("power", 5);
        enchants.put("punch", 2);
        enchants.put("unbreaking", 3);
        SearchResult bow = Main.searchAndParse(enchants);
        assertEquals(26 ,bow.parseTreeString(enchants));
    }

    @Test
    void testNormalAxe() {
        HashMap<String, Integer> enchants = new HashMap<>();
        enchants.put("sharpness", 3);
        enchants.put("fortune", 3);
        enchants.put("mending", 1);
        enchants.put("efficiency", 3);
        enchants.put("unbreaking", 3);
        SearchResult axe = Main.searchAndParse(enchants);
        assertEquals(28 ,axe.parseTreeString(enchants));
    }

    @Test
    void testTreeTraversal1() {
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
        SearchResult boots = Main.searchAndParse(enchants);

        Integer[] totalCost = new Integer[] {0, 0};
        boots.traversal(new EnchantStep() {
            @Override
            public void parse(Node node) {
                assertNotEquals(null, node);
                assertNotEquals("",   node.toString());
                assertNotEquals(null, node.getLeft());
                assertNotEquals(null, node.getRight());
                assertNotEquals(null, node.getValue());
                assertNotEquals("",   node.getValue().toString());
                assertNotEquals(-1,   node.getValue().getHeight());
                assertNotEquals(0,    node.getValue().getHeight());
                assertNotEquals(null, node.getValue().getEnchant());
                assertNotEquals("",   node.getValue().getEnchant().toString());
                totalCost[0] += node.getValue().getCost();
                totalCost[1] += node.getValue().getPrior();
            }
        });
        assertEquals(103, totalCost[0]+totalCost[1]);
    }
}