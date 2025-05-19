package Classes;

import Utilities.*;

import java.util.List;

public class Playable extends Character {

    private int exp;
    private List<Skill> skills;
    private int relic;



    public Playable() {}

    public Playable(Playable character) {
        super(character);
        this.exp = character.exp;
        this.skills = character.skills;
        this.relic = character.relic;
    }


    public Relic getRelic()
    {
        User user = User.getInstance();
        return user.getRelic(relic);
    }

}
