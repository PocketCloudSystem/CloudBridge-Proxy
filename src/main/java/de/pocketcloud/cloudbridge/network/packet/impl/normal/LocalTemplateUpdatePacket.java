package de.pocketcloud.cloudbridge.network.packet.impl.normal;

import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.content.PacketContent;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
public class LocalTemplateUpdatePacket extends CloudPacket {

    private String template;
    private Map<?,?> newData;

    public LocalTemplateUpdatePacket(String template, Map<?,?> newData) {
        this.template = template;
        this.newData = newData;
    }

    @Override
    protected void encodePayload(PacketContent content) {
        super.encodePayload(content);
        content.put(template);
        content.put(newData);
    }

    @Override
    protected void decodePayload(PacketContent content) {
        super.decodePayload(content);
        template = content.readString();
        newData = content.readMap();
    }

    public String getTemplate() {
        return template;
    }

    public Map<?,?> getNewData() {
        return newData;
    }
}
