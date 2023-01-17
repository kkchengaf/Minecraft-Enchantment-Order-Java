package kkchengaf.EnchantmentOrder;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.*;

/**
 * Utility to load json file
 *
 */
public class JSONLoader {
    /**
     * load the json file from the package
     * @param classObj, the class that holds the resource
     * @param relativePath, the path of file
     * @return JSONObject, contains the content of the file
     */
    public static JSONObject from(Class classObj, String relativePath) {
        JSONObject data = null;
        try {
            InputStream is = classObj.getClassLoader().getResourceAsStream(relativePath);
            data = new JSONObject(IOUtils.toString(is, "UTF-8"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
}
