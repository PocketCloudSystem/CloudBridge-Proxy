package de.pocketcloud.cloud.bridge.api.object.server.data;

public record CloudServerData(String serverName, int port, int maxPlayers, Integer processId) { }