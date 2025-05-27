package Classes.gameClasses;

import preparation.PrepareGame;
import utilities.Triplet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Skill {

    private  int id;
    private String name;
    private String description;
    private int cooldown, currentCooldown = 0;
    private List<Triplet> effects;
//    private Triplet damageFunction;


    public Skill(){}

    public Skill(Skill skill)
    {
        this.id = skill.id;
        this.name = skill.name;
        this.description = skill.description;
        this.cooldown = skill.cooldown;
        this.currentCooldown = skill.currentCooldown;
        this.effects = new ArrayList<>();
        for (Triplet effect : skill.effects) {
            this.effects.add(new Triplet(effect));
        }
//        if (skill.damageFunction != null)
//            this.damageFunction = new Triplet(skill.damageFunction);
//        else
//            this.damageFunction = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<Effect> getEffectList()
    {
        List<Effect> effectList = new ArrayList<>();
        for (Triplet effect : this.effects)
        {
            Effect e = PrepareGame.getInstance().getEffect(effect.getFunctionName());
            effectList.add(e);
        }
        return effectList;
    }

    public void setEffects(List<Triplet> effects) {
        this.effects = new ArrayList<>();
        for (Triplet effect : effects) {
            this.effects.add(new Triplet(effect));
        }
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        text.append(String.format("%-15s: %s %d/%d\n", name, description, currentCooldown, cooldown));
        return text.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        for (Triplet triplet: skill.effects) {
            if (!this.effects.contains(triplet)) {
                return false;
            }
        }
        for (Triplet triplet: this.effects) {
            if (!skill.effects.contains(triplet)) {
                return false;
            }
        }
        return  cooldown == skill.cooldown ;
    }

    @Override
    public int hashCode() {
        return Objects.hash( cooldown, effects);
    }
}
