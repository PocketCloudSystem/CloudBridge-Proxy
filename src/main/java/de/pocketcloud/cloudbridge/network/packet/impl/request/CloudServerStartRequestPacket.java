package de.pocketcloud.cloudbridge.network.packet.impl.request;

import de.pocketcloud.cloudbridge.network.packet.RequestPacket;
import de.pocketcloud.cloudbridge.network.packet.content.PacketContent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CloudServerStartRequestPacket extends RequestPacket {

    private String template;
    private int count;

    public CloudServerStartRequestPacket(String template, int count) {
        super();
        this.template = template;
        this.count = count;
    }

    @Override
    protected void encodePayload(PacketContent content) {
        super.encodePayload(content);
        content.put(template);
        content.put(content);
    }

    @Override
    protected void decodePayload(PacketContent content) {
        super.decodePayload(content);
        template = content.readString();
        count = content.readInt();
    }

    public String getTemplate() {
        return template;
    }

    public int getCount() {
        return count;
    }
}
