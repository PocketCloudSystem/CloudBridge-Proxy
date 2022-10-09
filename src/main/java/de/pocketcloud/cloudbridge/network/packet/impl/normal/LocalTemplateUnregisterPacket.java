package de.pocketcloud.cloudbridge.network.packet.impl.normal;

import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.content.PacketContent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LocalTemplateUnregisterPacket extends CloudPacket {

    private String template;

    public LocalTemplateUnregisterPacket(String template) {
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
        template = content.readString();
    }

    public String getTemplate() {
        return template;
    }
}
