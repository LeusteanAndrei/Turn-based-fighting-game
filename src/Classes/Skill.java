package Classes;

import Preparation.PrepareGame;
import Utilities.Triplet;

import java.util.ArrayList;
import java.util.List;

public class Skill {

    private String name;
    private String description;
    private int cooldown, currentCooldown = 0;
    private List<Triplet> effects;
    private Triplet damageFunction;


    public Skill(){}

    public Skill(Skill skill)
    {
        this.name = skill.name;
        this.description = skill.description;
        this.cooldown = skill.cooldown;
        this.currentCooldown = skill.currentCooldown;
        this.effects = new ArrayList<>();
        for (Triplet effect : skill.effects) {
            this.effects.add(new Triplet(effect));
        }
        if (skill.damageFunction != null)
            this.damageFunction = new Triplet(skill.damageFunction);
        else
            this.damageFunction = null;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public int getCurrentCooldown() {
        return currentCooldown;
    }

    public void setCurrentCooldown(int currentCooldown) {
        this.currentCooldown = currentCooldown;
    }

    public List<Triplet> getEffects() {
        List<Triplet> effects = new ArrayList<>();
        for (Triplet effect : this.effects) {
            effects.add(new Triplet(effect));
        }
        return effects;
    }


    public void setEffects(List<Triplet> effects) {
        this.effects = new ArrayList<>();
        for (Triplet effect : effects) {
            this.effects.add(new Triplet(effect));
        }
    }
}
