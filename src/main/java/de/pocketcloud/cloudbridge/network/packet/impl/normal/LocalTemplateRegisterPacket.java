package de.pocketcloud.cloudbridge.network.packet.impl.normal;

import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.content.PacketContent;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
public class LocalTemplateRegisterPacket extends CloudPacket {

    private Map<?,?> template;

    public LocalTemplateRegisterPacket(Map<?,?> template) {
        this.template = template;
    }

    @Override
    protected void encodePayload(PacketContent content) {
        super.encodePayload(content);
        content.put(template);
    }

    @Override
    protected void decodePayload(PacketContent content) {
        super.decodePayload(content);
        template = content.readMap();
    }

    public Map<?,?> getTemplate() {
        return template;
    }
}