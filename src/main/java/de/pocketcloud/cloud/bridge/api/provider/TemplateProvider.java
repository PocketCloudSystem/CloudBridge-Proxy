package de.pocketcloud.cloud.bridge.api.provider;

import de.pocketcloud.cloud.bridge.api.CloudAPI;
import de.pocketcloud.cloud.bridge.api.object.template.Template;
import de.pocketcloud.cloud.bridge.util.CloudEnvironmentConfig;

import java.util.HashMap;
import java.util.Map;

public final class TemplateProvider implements CloudAPIProvider {

    private final Map<String, Template> templates = new HashMap<>();

    public void add(Template template) {
        if (isset(template)) {
            templates.get(template.getName()).sync(template.write());
        } else {
            templates.put(template.getName(), template);
        }
    }

    public void remove(Template template) {
        if (isset(template)) {
            templates.remove(template.getName());
        }
    }

    public boolean isset(Template template) {
        return isset(template.getName());
    }
    
    public boolean isset(String name) {
        return templates.containsKey(name);
    }

    public Template get(String name) {
        return templates.get(name);
    }
    
    public Template current() {
        Template template = get(CloudEnvironmentConfig.getTemplateName());
        if (template == null) throw new RuntimeException("The return value of current() should not be null, wait for CloudAPI to index");
        return template;
    }

    public Map<String, Template> getAll() {
        return new HashMap<>(templates);
    }

    public static TemplateProvider provider() {
        return CloudAPI.get().getProvider(TemplateProvider.class);
    }
}