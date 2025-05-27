package enumerations;

public enum EffectType {
    ONCE,
    EVERY_TURN,
    ACTIVATED;

    public static EffectType getEffectType(String type) {
        for (EffectType effectType : EffectType.values()) {
            if (effectType.name().equalsIgnoreCase(type)) {
                return effectType;
            }
        }
        return null;
    }
}
