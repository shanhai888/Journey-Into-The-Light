package net.jitl.common.helper;

import net.jitl.client.render.displayinfo.KnowledgeXPDisplay;
import net.jitl.client.render.overlay.JDisplayInfo;
import net.jitl.core.init.JItems;
import net.minecraft.network.FriendlyByteBuf;

import java.util.HashMap;

public enum EnumKnowledgeType {
    OVERWORLD("overworld", 32, 10, new KnowledgeXPDisplay("overworld", false)
            , new KnowledgeXPDisplay("overworld", true)),

    NETHER("nether", 64, 10, new KnowledgeXPDisplay("the_nether", false)
            , new KnowledgeXPDisplay("the_nether", true)),

    END("end", 96, 10, new KnowledgeXPDisplay("the_end", false)
            , new KnowledgeXPDisplay("the_end", true)),

    EUCA("euca", 192, 10, new KnowledgeXPDisplay("euca", false)
            , new KnowledgeXPDisplay("euca", true)),

    BOIL("boil", 128, 10, new KnowledgeXPDisplay("boiling_point", false)
            , new KnowledgeXPDisplay("boiling_point", true)),

    FROZEN("frozen", 160, 10, new KnowledgeXPDisplay("frozen", false)
            , new KnowledgeXPDisplay("frozen", true)),

    DEPTHS("depths", 224, 10, new KnowledgeXPDisplay("the_depths", false)
            , new KnowledgeXPDisplay("the_depths", true)),

    CORBA("corba", 0, 42, new KnowledgeXPDisplay("corba", false)
            , new KnowledgeXPDisplay("corba", true)),

    CLOUDIA("cloudia", 64, 42,new KnowledgeXPDisplay("cloudia", false)
            , new KnowledgeXPDisplay("cloudia", true)),

    TERRANIA("terrania", 32, 42, new KnowledgeXPDisplay("terrania", false)
            , new KnowledgeXPDisplay("terrania", true)),

    SENTERIAN("senterian", 96, 42, new KnowledgeXPDisplay("senterian", false)
            , new KnowledgeXPDisplay("senterian", true));

    private static final HashMap<String, EnumKnowledgeType> BY_NAME = new HashMap<>();

    static {
        for (EnumKnowledgeType value : values()) {
            BY_NAME.put(value.getName(), value);
        }
    }

    private final String name;
    private JDisplayInfo xp, level;
    private int spriteX, spriteY;

    EnumKnowledgeType(String name, int spriteX, int spriteY, JDisplayInfo xp, JDisplayInfo level) {
        this.name = name;
        this.xp = xp;
        this.level = level;
        this.spriteX = spriteX;
        this.spriteY = spriteY;
    }

    public int getSpriteX() {
        return spriteX;
    }

    public int getSpriteY() {
        return spriteY;
    }

    public String getName() {
        return name;
    }

    public JDisplayInfo getXPDisplay() {
        return xp;
    }

    public JDisplayInfo getLevelDisplay() {
        return level;
    }

    public static EnumKnowledgeType getKnowledgeFromName(String name) {
        return switch (name.toLowerCase()) {
            case "overworld" -> OVERWORLD;
            case "nether" -> NETHER;
            case "end" -> END;
            case "euca" -> EUCA;
            case "boil" -> BOIL;
            case "frozen" -> FROZEN;
            case "depths" -> DEPTHS;
            case "corba" -> CORBA;
            case "cloudia" -> CLOUDIA;
            case "terrania" -> TERRANIA;
            case "senterian" -> SENTERIAN;
            default -> null;
        };
    }

    public static void writeToBuffer(EnumKnowledgeType type, FriendlyByteBuf buf) {
        buf.writeUtf(type.getName());
    }

    public static EnumKnowledgeType readFromBuffer(FriendlyByteBuf buf) {
        String name = buf.readUtf(Short.MAX_VALUE);

        return BY_NAME.get(name);
    }
}