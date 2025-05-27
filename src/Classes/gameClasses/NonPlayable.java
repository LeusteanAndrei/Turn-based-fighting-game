package Classes.gameClasses;


import utilities.Triplet;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class NonPlayable extends Character{


    Stack<Integer> health_bars = new Stack<>();
    List<Triplet> effects = new ArrayList<>();
    int maxCharge = 0;

    public NonPlayable(){}

    public NonPlayable(Character character)
    {
        super(character);
    }

    public NonPlayable(NonPlayable np)
    {
        super(np);
        this.setHealth_bars(np.health_bars);
        this.setEffects(np.effects);
        this.maxCharge = np.maxCharge;
    }

    public void setHealth_bars(Stack<Integer> health_bars) {
        this.health_bars = new Stack<>();
        for (Integer health : health_bars) {
            this.health_bars.push(health);
        }
    }

    public Stack<Integer> getHealth_bars() {
        Stack<Integer> health_bars = new Stack<>();
        for (Integer health : this.health_bars) {
            health_bars.push(health);
        }
        return health_bars;
    }


    public void setEffects(List<Triplet> effects) {
        this.effects = new ArrayList<>();
        for (Triplet effect : effects) {
            this.effects.add(new Triplet(effect));
        }
    }

    public List<Triplet> getEffects()
    {
        List<Triplet> effects = new ArrayList<>();
        for (Triplet effect : this.effects) {
            effects.add(new Triplet(effect));
        }
        return effects;
    }

    public int getMaxCharge() {
        return maxCharge;
    }

    public void setMaxCharge(int maxCharge) {
        this.maxCharge = maxCharge;
    }

    public String quickPrint()
    {
        return super.quickPrint();
    }

    @Override
    public String toString() {
        String s = super.toString();
        s += "    max charge: " + maxCharge + "\n";
        s += "    health bars: " + health_bars.toString() + "\n";
        s += "    effects: ";
        for (Triplet effect : effects) {
            s += " ( "+ effect.toString() + " ) ";
        }
        return s;
    }
}



