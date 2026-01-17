package de.pocketcloud.cloud.bridge.util;

import java.util.Map;

final public class Utils {

    public static double time() {
        return System.currentTimeMillis() / 1000.0;
    }

    public static boolean containKeys(Map<?, ?> map, String... key) {
        for (String s : key) {
            if (!map.containsKey(s))
                return false;
        }

        return true;
    }
}