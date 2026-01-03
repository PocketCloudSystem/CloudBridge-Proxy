package de.pocketcloud.cloud.bridge.api.cache;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InGameModuleCache {

    public final static String SIGN_MODULE = "sign_module";
    public final static String NPC_MODULE = "npc_module";
    public final static String HUB_COMMAND_MODULE = "hub_command_module";

    @Getter
    private static final Map<String, Boolean> moduleStates = new HashMap<>();

    public static void sync(Map<String, Boolean> data) {
        moduleStates.clear();
        moduleStates.putAll(data);
    }

    public static void setModuleState(String module, boolean state) {
        moduleStates.put(module, state);
    }

    public static boolean getModuleState(String module) {
        return moduleStates.get(module);
    }

    public static List<String> getAll() {
        return List.of(SIGN_MODULE, NPC_MODULE, HUB_COMMAND_MODULE);
    }
}