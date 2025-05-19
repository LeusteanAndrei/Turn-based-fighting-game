package Classes;

import Utilities.*;

import java.util.ArrayList;
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


    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public List<Skill> getSkills() {
        List<Skill> skills = new ArrayList<>();
        for (Skill skill : this.skills) {
            skills.add(new Skill(skill));
        }
        return skills;
    }
    public void setSkills(List<Skill> skills) {
        this.skills = new ArrayList<>();
        for (Skill skill : skills) {
            this.skills.add(new Skill(skill));
        }
    }
    public void setRelic(int relic) {
        this.relic = relic;
    }

    public Relic getRelic()
    {
        User user = User.getInstance();
        return user.getRelic(relic);
    }

    @Override
    public String toString() {
        String s = super.toString();
        s += "Exp: " + exp + "\n";
        s += "Skills: \n";
        for (Skill skill : skills) {
            s += skill.getName()+"( " + skill.getDescription() + " )\n";
        }
        s += "Relic: " + this.relic + "\n";

        return s;
    }
}
