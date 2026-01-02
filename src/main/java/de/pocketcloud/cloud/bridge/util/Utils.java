package de.pocketcloud.cloud.bridge.util;

import java.util.Map;

public class Utils {

    public static boolean containKeys(Map<?, ?> map, String... key) {
        for (String s : key) {
            if (!map.containsKey(s))
                return false;
        }

        return true;
    }
}