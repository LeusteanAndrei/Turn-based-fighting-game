package Classes.gameClasses;

import enumerations.AttackType;
import utilities.Triplet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Attack {

    int id;
    AttackType type;
    List<Integer> hits = new ArrayList<>(); //multiplier = 400%
    List<Triplet> effects = new ArrayList<>();

    public Attack(Attack attack){
        this.id = attack.id;
        this.type = attack.type;
        this.hits = new ArrayList<>();
        for (Integer e : attack.hits) {
            this.hits.add(e);
        }
        this.effects = new ArrayList<>();
        for (Triplet e : attack.effects) {
            this.effects.add(new Triplet(e));
        }
    }


    public Attack(){}

    public List<Triplet> getEffects()
    {
        List<Triplet> effects = new ArrayList<>();
        for (Triplet e : this.effects) {
            effects.add(new Triplet(e));
        }
        return effects;
    }

    public List<Integer> getHits()
    {
        List<Integer> hits = new ArrayList<>();
        for (Integer e : this.hits) {
            hits.add(e);
        }
        return hits;
    }
    public void setHits(List<Integer> hits)
    {
        this.hits = new ArrayList<>();
        for (Integer e : hits) {
            this.hits.add(e);
        }
    }
    public AttackType getType()
    {
        return type;
    }
    public void setType(AttackType type)
    {
        this.type = type;
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Attack attack = (Attack) o;
        return id == attack.id && type == attack.type && Objects.equals(hits, attack.hits) && Objects.equals(effects, attack.effects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, hits, effects);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Attack ( type: " + type.toString() + ", hits : " + hits.size() + ", effects: ");
        for (Triplet e : effects) {
            s.append(e.toString()).append(", ");
        }
        s.append(" )");
        return s.toString();
    }
}
