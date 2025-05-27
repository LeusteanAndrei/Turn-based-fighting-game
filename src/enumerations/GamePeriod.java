package enumerations;

public enum GamePeriod {
    START_OF_BATTLE,
    END_OF_BATTLE,
    START_OF_TURN,
    END_OF_TURN,
    BEFORE_ATTACK,
    AFTER_ATTACK,
    ON_DEATH,
    ON_HIT,
    WHEN_HIT;


    public static GamePeriod getGamePeriod(String period) {
        for (GamePeriod gamePeriod : GamePeriod.values()) {
            if (gamePeriod.name().equalsIgnoreCase(period)) {
                return gamePeriod;
            }
        }
        return null;
    }
}
