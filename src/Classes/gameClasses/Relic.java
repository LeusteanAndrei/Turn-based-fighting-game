package Classes.gameClasses;

import utilities.Triplet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Relic {

    private int id=-1;

    private String name;
    private String description;
    private int level=0;
    private boolean equipped = false;

    Map<String, Number> buffs = new HashMap<>();
    List<Triplet> effects = new ArrayList<>();



    public Relic(){
        this.name = "";
        this.description = "";
        this.level = 0;
        this.buffs = new HashMap<>();
        this.effects = new ArrayList<>();
    }
    public Relic(Relic relic)
    {
        this.id = relic.id;
        this.name = relic.name;
        this.description = relic.description;
        this.level = relic.level;
        this.equipped = relic.equipped;

        this.buffs = new HashMap<>();
        for (Map.Entry<String, Number> entry : relic.buffs.entrySet()) {
            this.buffs.put(entry.getKey(), entry.getValue());
        }

        this.effects = new ArrayList<>();
        for (Triplet e : relic.effects) {
            this.effects.add(new Triplet(e));
        }
    }

    public void setBuffs(Map<String, Number> buffs) {
        this.buffs = new HashMap<>();
        for (Map.Entry<String, Number> entry : buffs.entrySet()) {
            this.buffs.put(entry.getKey(), entry.getValue());
        }
    }

    public Map<String, Number> getBuffs() {
        Map<String, Number> buffs = new HashMap<>();
        for (Map.Entry<String, Number> entry : this.buffs.entrySet()) {
            buffs.put(entry.getKey(), entry.getValue());
        }
        return buffs;
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

    public void setDescription(String decription) {
        this.description = decription;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<Triplet> getEffects()
    {
        List<Triplet> effects = new ArrayList<>();
        for (Triplet e : this.effects) {
            effects.add(new Triplet(e));
        }
        return effects;
    }

    public void setEffects(List<Triplet> effects)
    {
        this.effects = new ArrayList<>();
        for (Triplet e : effects) {
            this.effects.add(new Triplet(e));
        }
    }

    public void applyBuffs(Character character)
    {
        Stats stats = character.getInitial_stats();

        for (Map.Entry<String, Number> entry : this.buffs.entrySet()) {
            String stat  = entry.getKey();
            Number value = entry.getValue();

            switch (stat)
            {
                case "attack" -> {
                    stats.setAttack(stats.getAttack() + (int)value);
                }
                case "health" -> {
                    stats.setHealth(stats.getHealth() + (int)value);
                }
                case "damageRes" -> {
                    stats.setDamageRes(stats.getDamageRes() + (float)value);
                }
                case "critRate" -> {
                    stats.setCritRate(stats.getCritRate() + (float)value);
                }
                case "critDamage" -> {
                    stats.setCritDamage(stats.getCritDamage() + (float)value);
                }
                case "gain" -> {
                    stats.setGain(stats.getGain() + (float)value);
                }
            }
        }
        character.setInitial_stats(stats);
        character.setCurrent_stats(stats);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEquipped() {
        return equipped;
    }
    public void setEquipped(boolean equipped) {
        this.equipped = equipped;
    }

    @Override
    public String toString() {
        String s =  name + ", level: " + level +
                "\nDescription: " + description + "\n";
        s += "Buffs: \n";
        for (Map.Entry<String, Number> entry : buffs.entrySet()) {
            s +="   " +entry.getKey() + ": " + entry.getValue() + "\n";
        }
        s += "Effects: \n";
        for (Triplet e : effects) {
            s += "  "+e.getFunctionName() + ": " + e.getScale() + ", " + e.getDuration() + " Turns \n";
        }
        s += "Equipped: " + equipped + "\n";
        return s;
    }
}
