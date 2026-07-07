package de.pocketcloud.cloud.bridge.network.packet.impl;

import com.google.gson.Gson;
import de.pocketcloud.cloud.bridge.CloudBridge;
import de.pocketcloud.cloud.bridge.language.Language;
import de.pocketcloud.network.packet.ClientboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.network.packet.data.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

@Getter
@NoArgsConstructor
public final class LanguageSyncPacket extends CloudPacket implements ClientboundPacket {
    
    private String language;
    private Map<String, String> messages;
    
    public LanguageSyncPacket(String language, Map<String, String> messages) {
        this.language = language;
        this.messages = messages;
    }
    
    @Override
    public void handle() {
        Language.sync(language, messages);
    }

    @Override
    public void encodePayload(PacketData packetData) {}

    @Override
    @SuppressWarnings("unchecked")
    public void decodePayload(PacketData packetData) {
        language = packetData.readString();
        
        String encoded = packetData.readString();
        try {
            byte[] decoded = Base64.getDecoder().decode(encoded);
            byte[] uncompressed = gunzip(decoded);
            String json = new String(uncompressed, StandardCharsets.UTF_8);
            messages = new Gson().fromJson(json, Map.class);
        } catch (Exception e) {
            CloudBridge.getInstance().getLogger().error("Failed to decode language messages", e);
            messages = new HashMap<>();
        }
    }
    
    private byte[] gunzip(byte[] compressed) throws IOException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(compressed);
             GZIPInputStream gis = new GZIPInputStream(bis);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gis.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        }
    }

    public static LanguageSyncPacket create(String language, Map<String, String> messages) {
        return new LanguageSyncPacket(language, messages);
    }
}