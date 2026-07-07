package de.pocketcloud.cloud.bridge.network.packet.impl;

import de.pocketcloud.cloud.bridge.api.object.group.ServerGroup;
import de.pocketcloud.cloud.bridge.api.object.player.CloudPlayer;
import de.pocketcloud.cloud.bridge.api.object.server.CloudServer;
import de.pocketcloud.cloud.bridge.api.object.template.Template;
import de.pocketcloud.cloud.bridge.api.provider.CloudPlayerProvider;
import de.pocketcloud.cloud.bridge.api.provider.CloudServerProvider;
import de.pocketcloud.cloud.bridge.api.provider.ServerGroupProvider;
import de.pocketcloud.cloud.bridge.api.provider.TemplateProvider;
import de.pocketcloud.network.packet.ClientboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.network.packet.data.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@NoArgsConstructor
final public class BulkSyncPacket extends CloudPacket implements ClientboundPacket {

    private List<CloudServer> servers;
    private List<Template> templates;
    private List<CloudPlayer> players;
    private List<ServerGroup> groups;

    public BulkSyncPacket(List<CloudServer> servers, List<Template> templates, List<CloudPlayer> players, List<ServerGroup> groups) {
        this.servers = servers;
        this.templates = templates;
        this.players = players;
        this.groups = groups;
    }

    @Override
    public void handle() {
        CloudServerProvider.provider().addAll(servers);
        TemplateProvider.provider().addAll(templates);
        CloudPlayerProvider.provider().addAll(players);
        ServerGroupProvider.provider().addAll(groups);
    }

    @Override
    public void encodePayload(PacketData packetData) {}

    @Override
    public void decodePayload(PacketData packetData) {
        List<CloudServer> servers = new ArrayList<>();
        List<Template> templates = new ArrayList<>();
        List<CloudPlayer> players = new ArrayList<>();
        List<ServerGroup> groups = new ArrayList<>();

        for (Object serverData : Objects.requireNonNull(packetData.readArray())) {
            CloudServer server;
            if ((server = CloudServer.read((Map<String, Object>) serverData)) != null) {
                servers.add(server);
            }
        }

        for (Object templateData : Objects.requireNonNull(packetData.readArray())) {
            Template template;
            if ((template = Template.read((Map<String, Object>) templateData)) != null) {
                templates.add(template);
            }
        }

        for (Object playerData : Objects.requireNonNull(packetData.readArray())) {
            CloudPlayer player;
            if ((player = CloudPlayer.read((Map<String, Object>) playerData)) != null) {
                players.add(player);
            }
        }

        for (Object groupData : Objects.requireNonNull(packetData.readArray())) {
            ServerGroup group;
            if ((group = ServerGroup.read((Map<String, Object>) groupData)) != null) {
                groups.add(group);
            }
        }

        this.servers = servers;
        this.templates = templates;
        this.players = players;
        this.groups = groups;
    }
}