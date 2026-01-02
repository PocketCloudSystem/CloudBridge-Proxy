package de.pocketcloud.cloud.bridge.api.object.group;

import de.pocketcloud.cloud.bridge.api.object.template.Template;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import de.pocketcloud.cloud.bridge.util.Utils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ServerGroup implements PacketData.Writable {

    @Getter
    private final String name;
    private List<String> templates;
    
    public ServerGroup(String name, List<String> templates) {
        this.name = name;
        this.templates = new ArrayList<>(templates);
    }

    @SuppressWarnings("unchecked")
    public void sync(Map<String, Object> data) {
        if (data.containsKey("templates")) {
            Object templatesObj = data.get("templates");
            if (templatesObj instanceof List) {
                this.templates = new ArrayList<>((List<String>) templatesObj);
            }
        }
    }

    public boolean is(Template template) {
        return is(template.getName());
    }
    
    public boolean is(String templateName) {
        return templates.contains(templateName);
    }

    public List<String> getTemplates() {
        return new ArrayList<>(templates);
    }
    
    @Override
    public Map<String, Object> write() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("templates", new ArrayList<>(templates));
        return data;
    }
    
    @SuppressWarnings("unchecked")
    public static ServerGroup read(Map<String, Object> data) {
        if (data == null) return null;
        
        if (!Utils.containKeys(data, "name", "templates")) return null;
        
        try {
            Object templatesObj = data.get("templates");
            List<String> templates;
            
            if (templatesObj instanceof List) {
                templates = new ArrayList<>((List<String>) templatesObj);
            } else {
                return null;
            }
            
            return new ServerGroup(
                (String) data.get("name"),
                templates
            );
        } catch (ClassCastException e) {
            return null;
        }
    }
}
