package utilities;

import Classes.gameClasses.Playable;

public class Pair {

    private Playable character;
    private int relic;

    public Pair() {}

    public Pair(Pair pair){
        this.setCharacter(pair.getCharacter());
        this.setRelic(pair.getRelic());
    }

    public Pair(Playable character, int relic) {
        this.character = character;
        this.relic = relic;
    }
    public Playable getCharacter() {
        return new Playable(character);
    }
    public int getRelic() {
        return relic;
    }
    public void setCharacter(Playable character) {
        this.character = new Playable(character);
    }
    public void setRelic(int relic) {
        this.relic = relic;
        this.character.setRelic(relic);
    }


    @Override
    public String toString() {
        return "Pair{" +
                "character=" + character +
                ", relic=" + relic +
                '}';
    }
}
