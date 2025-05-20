package Classes;


import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class NonPlayable extends Character{


    Stack<Integer> health_bars = new Stack<>();
    List<String> effects = new ArrayList<String>();

    public NonPlayable(){}

    public NonPlayable(NonPlayable np)
    {
        super(np);
        this.setHealth_bars(np.health_bars);
        this.setEffects(np.effects);
    }

    public void setHealth_bars(Stack<Integer> health_bars) {
        this.health_bars = new Stack<>();
        for (Integer health : health_bars) {
            this.health_bars.push(health);
        }
    }

    public void setEffects(List<String> effects) {
        this.effects = new ArrayList<>();
        for (String effect : effects) {
            this.effects.add(effect);
        }
    }

    public void getHealth_bars(Stack<Integer> health_bars) {
        this.health_bars = new Stack<>();
        for (Integer health : health_bars) {
            this.health_bars.push(health);
        }
    }
    public void getEffects(List<String> effects) {
        this.effects = new ArrayList<>();
        for (String effect : effects) {
            this.effects.add(effect);
        }
    }
}



