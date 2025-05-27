package Classes.gameClasses;

import Classes.gameFunctionality.Function;
import Classes.gameFunctionality.GameState;
import enumerations.*;
import preparation.PrepareGame;

import java.util.Objects;

public class Effect {

    int id;
    private String name, functionName, removeFunctionName;
    private GamePeriod period;
    private EffectType type;
    public int on_hit_count, when_hit_count;

    public Effect(String name,  String functionName, String removeFunctionName, GamePeriod period, EffectType type) {
        this.name = name;
        this.period = period;
        this.type = type;
        this.functionName = functionName;
        this.removeFunctionName = removeFunctionName;
    }

    public Effect(String name,  String functionName, String removeFunctionName, GamePeriod period, EffectType type, int on_hit_count, int when_hit_count) {
        this.name = name;
        this.period = period;
        this.type = type;
        this.functionName = functionName;
        this.removeFunctionName = removeFunctionName;
        this.on_hit_count = on_hit_count;
        this.when_hit_count = when_hit_count;
    }

    public Effect(){};

    public Effect ( Effect effect )
    {
        this.id = effect.id;
        this.name = effect.name;
        this.period = effect.period;
        this.type = effect.type;
        this.functionName = effect.functionName;
        this.removeFunctionName = effect.removeFunctionName;
        this.on_hit_count = effect.on_hit_count;
        this.when_hit_count = effect.when_hit_count;
    }

    public void applyEffect(GameState state, float scale)
    {
        PrepareGame prepareGame = PrepareGame.getInstance();
        Function function = prepareGame.getFunction(this.functionName);

        if (function != null )
            function.apply(state, scale);

    }

    public void removeEffect(GameState state, float scale)
    {
        PrepareGame prepareGame = PrepareGame.getInstance();
        Function function = prepareGame.getFunction(this.removeFunctionName);
        if (function != null )
            function.apply(state, scale);
    }

    public int getWhen_hit_count() {
        return when_hit_count;
    }

    public void setWhen_hit_count(int when_hit_count) {
        this.when_hit_count = when_hit_count;
    }

    public int getOn_hit_count() {
        return on_hit_count;
    }

    public void setOn_hit_count(int on_hit_count) {
        this.on_hit_count = on_hit_count;
    }

    public EffectType getType() {
        return type;
    }

    public void setType(EffectType type) {
        this.type = type;
    }

    public GamePeriod getPeriod() {
        return period;
    }

    public void setPeriod(GamePeriod period) {
        this.period = period;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getRemoveFunctionName() {
        return removeFunctionName;
    }

    public void setRemoveFunctionName(String removeFunctionName) {
        this.removeFunctionName = removeFunctionName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Effect effect = (Effect) o;
        return Objects.equals(name, effect.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "Effect{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", functionName='" + functionName + '\'' +
                ", removeFunctionName='" + removeFunctionName + '\'' +
                ", period=" + period +
                ", type=" + type +
                ", on_hit_count=" + on_hit_count +
                ", when_hit_count=" + when_hit_count +
                '}';
    }
}
