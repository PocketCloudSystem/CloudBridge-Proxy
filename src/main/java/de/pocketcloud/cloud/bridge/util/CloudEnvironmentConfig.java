package de.pocketcloud.cloud.bridge.util;

import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.utils.config.YamlConfig;

import java.util.HashMap;
import java.util.Map;

final public class CloudEnvironmentConfig {

    private static final Map<String, Object> data = new HashMap<>();

    static {
        data.put("cloud-address", null);
        data.put("cloud-port", null);
        data.put("network-encryption", null);
        data.put("server-name", null);
        data.put("server-uuid", null);
        data.put("template", null);
        data.put("cloud-path", null);
        data.put("cloud-language", null);
        data.put("server-timeout", null);
        data.put("auth-key", null);
    }

    private static YamlConfig serverProperties() {
        return new YamlConfig(ProxyServer.getInstance().getDataPath().toString() + "/config.yml");
    }

    public static void sync() {
        YamlConfig serverProperties = serverProperties();
        for (String key : data.keySet()) {
            syncVariable(key, serverProperties);
        }
    }

    public static Object syncVariable(String variable) {
        return syncVariable(variable, serverProperties());
    }

    public static Object syncVariable(String variable, YamlConfig config) {
        if (!config.exists(variable)) throw new RuntimeException("Variable '" + variable + "' should not be null, therefore CloudEnvironmentConfig didn't sync yet");
        data.put(variable, config.get(variable));
        return data.get(variable);
    }

    public static <T> T fetchVariable(String variable, Class<T> type) {
        return fetchVariable(variable, true, type);
    }

    @SuppressWarnings("unchecked")
    public static <T> T fetchVariable(String variable, boolean canReturnNull, Class<T> type) {
        if (!data.containsKey(variable) && !canReturnNull) throw new IllegalStateException("Variable '" + variable + "' does not exist");
        Object result = data.get(variable);
        if (result == null && !canReturnNull) throw new IllegalStateException("Variable '" + variable + "' should not be null, therefore CloudEnvironmentConfig didn't sync yet");
        if (!type.isInstance(result)) throw new IllegalStateException("Variable '" + variable + "' should be of type " + type.getCanonicalName() + ", is of type " + (result == null ? "null" : result.getClass().getCanonicalName()));
        return (T) result;
    }

    public static String getNetworkAddress() {
        return fetchVariable("cloud-address", false, String.class);
    }

    public static int getNetworkPort() {
        return fetchVariable("cloud-port", false, Integer.class);
    }

    public static String getServerName() {
        return fetchVariable("server-name", false, String.class);
    }

    public static String getServerUuid() {
        return fetchVariable("server-uuid", false, String.class);
    }

    public static String getTemplateName() {
        return fetchVariable("template", false, String.class);
    }

    public static String getCloudPath() {
        return fetchVariable("cloud-path", false, String.class);
    }

    public static String getLanguage() {
        return fetchVariable("cloud-language", false, String.class);
    }

    public static boolean isNetworkEncryptionEnabled() {
        return fetchVariable("network-encryption", false, Boolean.class);
    }

    public static int getServerTimeout() {
        return fetchVariable("server-timeout", false, Integer.class);
    }

    public static String getNetworkAuthKey() {
        return fetchVariable("auth-key", false, String.class);
    }
}