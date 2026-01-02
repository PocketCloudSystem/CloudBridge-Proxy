package de.pocketcloud.cloud.bridge.network.packet.impl;

import de.pocketcloud.cloud.bridge.api.object.template.Template;
import de.pocketcloud.cloud.bridge.api.provider.TemplateProvider;
import de.pocketcloud.cloud.bridge.network.packet.ClientboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class TemplateSyncPacket extends CloudPacket implements ClientboundPacket {
    
    private Template template;
    private boolean removal;

    public TemplateSyncPacket(Template template, boolean removal) {
        this.template = template;
        this.removal = removal;
    }
    
    @Override
    public void handle() {
        if (removal) {
            TemplateProvider.provider().remove(template);
        } else {
            TemplateProvider.provider().add(template);
        }
    }
    
    @Override
    public void encodePayload(PacketData packetData) {}
    
    @Override
    public void decodePayload(PacketData packetData) {
        template = packetData.readTemplate();
        removal = Boolean.TRUE.equals(packetData.readBool());
    }

    public static TemplateSyncPacket create(Template template, boolean removal) {
        return new TemplateSyncPacket(template, removal);
    }
}