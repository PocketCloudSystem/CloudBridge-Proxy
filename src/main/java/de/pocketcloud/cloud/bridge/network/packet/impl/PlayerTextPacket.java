package de.pocketcloud.cloud.bridge.network.packet.impl;

import de.pocketcloud.cloud.bridge.network.packet.ClientboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.data.TextType;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.bedrock.packet.SetTitlePacket;

import java.util.Arrays;

@Getter
@NoArgsConstructor
final public class PlayerTextPacket extends CloudPacket implements ClientboundPacket, CloudboundPacket {

    private String player;
    private String text;
    private TextType type;

    public PlayerTextPacket(String player, String text, TextType type) {
        this.player = player;
        this.text = text;
        this.type = type;
    }

    @Override
    public void handle() {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(this.player);
        if (player != null) {
            switch (this.type) {
                case MESSAGE -> player.sendMessage(this.text);
                case POPUP -> player.sendPopup(this.text, "");
                case TIP -> player.sendTip(this.text);
                case TITLE -> {
                    String[] parts = this.text.split("\n", -1);

                    String title = parts.length > 0 ? parts[0].trim() : "";
                    String subTitle = "";

                    if (parts.length > 1) {
                        subTitle = String.join("\n", Arrays.copyOfRange(parts, 1, parts.length)).trim();
                    }

                    player.sendTitle(title, subTitle);
                }
                case ACTION_BAR -> {
                    SetTitlePacket packet = new SetTitlePacket();
                    packet.setType(org.cloudburstmc.protocol.bedrock.packet.SetTitlePacket.Type.ACTIONBAR);
                    packet.setText((CharSequence) text);
                    packet.setXuid(player.getXuid());
                    packet.setPlatformOnlineId("");
                    player.sendPacket(packet);
                }
                case TOAST_NOTIFICATION -> {
                    String[] parts = this.text.split("\n", -1);

                    String title = parts.length > 0 ? parts[0].trim() : "";
                    String content = "";

                    if (parts.length > 1) {
                        content = String.join("\n", Arrays.copyOfRange(parts, 1, parts.length)).trim();
                    }

                    player.sendToastMessage(title, content);
                }
            }
        }
    }

    @Override
    public void encodePayload(PacketData packetData) {
        packetData.writeAll(this.player, this.text, this.type);
    }

    @Override
    public void decodePayload(PacketData packetData) {
        player = packetData.readString();
        text = packetData.readString();
        type = packetData.readTextType();
    }

    public static PlayerTextPacket create(String player, String text, TextType type) {
        return new PlayerTextPacket(player, text, type);
    }
}
