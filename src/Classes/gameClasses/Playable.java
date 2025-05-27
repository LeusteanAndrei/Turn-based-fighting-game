package Classes.gameClasses;

import utilities.Service;

import java.util.ArrayList;
import java.util.List;

public class Playable extends Character {

    private int exp;
    private List<Skill> skills;
    private int relic = -1;



    public Playable() {}

    public Playable(Character character)
    {
        super(character);
    }

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
        User user = Service.getInstance().getUser();
        return user.getRelic(relic);
    }



    @Override
    public void turnPassed() {
        super.turnPassed();
        for (Skill skill : skills) {
            if (skill.getCurrentCooldown() > 0) {
                skill.setCurrentCooldown(skill.getCurrentCooldown() - 1);
            }
        }
    }

    public String quickPrint()
    {
        String s = super.quickPrint();
        s += "Exp: " + exp + "\n";
        s += "Skills: \n";
        for (Skill skill : skills) {
            s += skill.getName()+"( " + skill.getDescription() + " )\n";
        }
        if (this.relic == -1)
            s += "Relic: None\n";
        else
            s += "Relic: " + Service.getInstance().getRelic(relic).toString()+ "\n";
        return s;
    }

    @Override
    public String toString() {
        String s = super.toString();
        s += "Exp: " + exp + "\n";
        s += "Skills: \n";
        for (Skill skill : skills) {
            s += skill.getName()+"( " + skill.getDescription() + " )\n";
        }
        if (this.relic == -1)
            s += "Relic: None\n";
        else
            s += "Relic: " + Service.getInstance().getRelic(relic).toString()+ "\n";


        return s;
    }
}
