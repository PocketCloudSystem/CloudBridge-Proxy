package de.pocketcloud.cloudbridge.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Utils {

    public static byte[] compress(String string) throws IOException {
        return Base64.getEncoder().encode(string.getBytes(StandardCharsets.UTF_8));
    }

    public static String decompress(final byte[] compressed) throws IOException {
        return new String(Base64.getDecoder().decode(compressed));
    }

    public static ArrayList<Object> convertJsonArray(JsonArray jsonArray) {
        ArrayList<Object> list = new ArrayList<>();
        for (int i = 0, l = jsonArray.size(); i < l; i++) list.add(jsonArray.get(i));
        return list;
    }

    public static HashMap<Object, Object> convertJsonObjectToHashMap(JsonObject jsonObject) {
        HashMap<Object, Object> map = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    public static boolean containKeys(Map<?,?> map, String... keys) {
        boolean result = true;
        for (String key : keys)
            if (!map.containsKey(key)) {
                result = false;
                break;
            }
        return !result;
    }

    public static long time() {
        return System.currentTimeMillis() / 1000;
    }
}
