package enumerations;

public enum AttackType {
    NORMAL,
    STRONG,
    ENHANCED;

    public static AttackType getAttackType(String type) {
        for (AttackType attackType : AttackType.values()) {
            if (attackType.name().equalsIgnoreCase(type)) {
                return attackType;
            }
        }
        throw new IllegalArgumentException("Unknown attack type: " + type);
    }

}
