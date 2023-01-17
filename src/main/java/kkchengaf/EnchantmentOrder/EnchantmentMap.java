package kkchengaf.EnchantmentOrder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Utility class to:
 * convert enchantment id from/to String and Integer,
 * get the enchantment weight of a X level enchantment
 */
public class EnchantmentMap {
    private static String PATH_TO_ID_FILE = "EnchantmentOrder/enchants.json";
    private static String PATH_TO_COST_FILE = "EnchantmentOrder/costs.json";
    private static JSONObject dictID = null;
    private static JSONObject dictBookCost = null;

    private static JSONObject getDictID() {
        if(dictID==null) {
            dictID = JSONLoader.from(EnchantmentMap.class, EnchantmentMap.PATH_TO_ID_FILE);
        }
        return dictID;
    }

    private static JSONObject getDictBookCost() {
        if(dictBookCost==null) {
            dictBookCost = JSONLoader.from(EnchantmentMap.class, EnchantmentMap.PATH_TO_COST_FILE);
        }
        return dictBookCost;
    }

    /**
     * Convert Minecraft Enchantment from String ID to Numeric ID for the search
     * @param id, Minecraft String enchantment ID
     * @return Numeric ID for the search
     */
    public static Integer fromStringID(String id) {
        return EnchantmentMap.getDictID().getInt(id);
    }

    /**
     * Convert Minecraft Enchantment from Numeric ID to readable String ID
     * @param id, Numeric enchantment ID in String Type
     * @return  readable String ID
     */
    public static String fromNumericID(String id) {
        return EnchantmentMap.getDictID().getString(id);
    }

    /**
     * Get the Enchantment Weight of a enchantment
     * @param id, Numeric enchantment ID
     * @return Integer, the weight per level
     */
    public static Integer getBookWeight(Integer id) {
        return EnchantmentMap.getDictBookCost().getInt(id.toString());
    }

    /**
     * Get the Enchantment Weight of a X level enchantment
     * @param id, Numeric enchantment ID
     * @param level, Integer level
     * @return Integer, the weight at X level
     */
    public static Integer getCost(Integer id, Integer level) {
        return EnchantmentMap.getBookWeight(id) * level;
    }

    /**
     * String ID Version of EnchantmentMap.getCost,
     * Get the Enchantment Weight of a X level enchantment
     * @param id, String enchantment ID
     * @param level, Integer level
     * @return Integer, the weight at X level
     */
    public static Integer getCostFromString(String id, Integer level) {
        return EnchantmentMap.getCost(EnchantmentMap.fromStringID(id), level);
    }

    /**
     * Convert numeric number to roman number, from 1 to 5
     * @param i, numeric number
     * @return string of the representing roman number
     */
    public static String toEnchantLevel(Integer i){
        if(i>5 || i<=0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if(i%5!=0) {
            sb.append("I".repeat(i%5!=4?i:1));
        }
        if(i%5==4 || i%5==0) {
            sb.append("V");
        }
        return sb.toString();
    }

    /**
     * Convert to HashMap Integer, Integer
     * @param hmap HashMap String, Integer
     * @return HashMap Integer, Integer
     */
    public static HashMap<Integer, Integer> numericHashMap(HashMap<String, Integer> hmap) {
        HashMap<Integer, Integer> newHMap = new HashMap<>();
        Iterator it = hmap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> pair = (Map.Entry) it.next();
            newHMap.put(EnchantmentMap.fromStringID(pair.getKey()), pair.getValue());
        }
        return newHMap;
    }


    /**
     * Process a dict of id:lv to the search algorithm
     * @param hmap, ACCEPT INTEGER ID
     * @return ArrayList ready for search
     */
    public static ArrayList<Integer> inputFromHashMap(HashMap<Integer, Integer> hmap) {
        ArrayList<Integer> result = new ArrayList<>();
        result.add(0); //the item itself has zero weight, not needed in hmap
        Iterator it = hmap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> pair = (Map.Entry<Integer, Integer>) it.next();
            result.add(EnchantmentMap.getCost(pair.getKey(), pair.getValue()));
        }
        return result;
    }

    /**
     * This is STRING ID VERSION of EnchantmentMap.inputFromHashMap,
     * Process a dict of "id":lv to the search algorithm
     * @param hmap, ACCEPT STRING ID
     * @return ArrayList ready for search
     */
    public static ArrayList<Integer> inputFromHashMapString(HashMap<String, Integer> hmap) {
        return inputFromHashMap(EnchantmentMap.numericHashMap(hmap));
    }
}
