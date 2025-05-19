package Classes;

import Enumerations.AttackType;
import Utilities.Triplet;

import java.util.ArrayList;
import java.util.List;

public class Attack {

    AttackType type;
    List<Integer> hits; //multiplier = 400%
    List<Triplet> effects;

    public Attack(Attack attack){
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

}
