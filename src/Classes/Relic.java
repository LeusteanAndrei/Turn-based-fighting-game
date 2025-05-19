package Classes;

import Utilities.Triplet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Relic {


    private String name;
    private String decription;
    private int level;

    Map<String, Number> buffs;
    List<Triplet> effects;



    public Relic(){}
    public Relic(Relic relic)
    {
        this.name = relic.name;
        this.decription = relic.decription;
        this.level = relic.level;

        this.buffs = new HashMap<>();
        for (Map.Entry<String, Number> entry : relic.buffs.entrySet()) {
            this.buffs.put(entry.getKey(), entry.getValue());
        }

        this.effects = new ArrayList<>();
        for (Triplet e : relic.effects) {
            this.effects.add(new Triplet(e));
        }
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
    }

}
