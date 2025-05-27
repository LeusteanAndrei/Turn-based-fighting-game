package enumerations;

public enum CharacterType {

    EARTH;

    public static CharacterType getCharacterType(String type) {
        for (CharacterType characterType : CharacterType.values()) {
            if (characterType.name().equalsIgnoreCase(type)) {
                return characterType;
            }
        }
       return null;
    }
}
