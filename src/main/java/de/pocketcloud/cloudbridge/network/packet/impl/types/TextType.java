package de.pocketcloud.cloudbridge.network.packet.impl.types;

public enum TextType {

    MESSAGE("MESSAGE"),
    POPUP("POPUP"),
    TIP("TIP"),
    TITLE("TITLE"),
    ACTION_BAR("ACTION_BAR");

    public static TextType getTypeByName(String name) {
        return valueOf(name);
    }

    private final String name;

    TextType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
