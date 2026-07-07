package de.pocketcloud.cloud.bridge.api.object.group;

import de.pocketcloud.cloud.bridge.api.object.template.Template;
import de.pocketcloud.cloud.bridge.util.Writable;
import de.pocketcloud.cloud.bridge.util.mapper.MapperUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ServerGroup implements Writable<Map<String, Object>> {

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
        return MapperUtils.toMap(this);
    }
    
    public static ServerGroup read(Map<String, Object> data) {
        return MapperUtils.fromMap(data, ServerGroup.class);
    }
}