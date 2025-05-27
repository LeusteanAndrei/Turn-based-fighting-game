package utilities;

import Classes.gameClasses.Effect;

import preparation.PrepareGame;

import java.util.Map;
import java.util.Objects;

public class Triplet{

    private int duration;
    private float scale;
    private String functionName;

    public Triplet(int duration, float scale, String functionName) {
        this.duration = duration;
        this.scale = scale;
        this.functionName = functionName;
    }

    public Triplet(Triplet triplet) {
        this.duration = triplet.duration;
        this.scale = triplet.scale;
        this.functionName = triplet.functionName;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public Effect getEffect()
    {
        Map<String, Effect> effectMap = PrepareGame.getInstance().getEffectsMap();
        return effectMap.get(functionName);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Triplet triplet = (Triplet) o;
        return duration == triplet.duration && Float.compare(scale, triplet.scale) == 0 && Objects.equals(functionName, triplet.functionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(duration, scale, functionName);
    }

    @Override
    public String toString() {
        return String.format("(%s %dT, scale :", functionName, duration) + scale + ")";
    }
}
