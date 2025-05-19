package Utilities;

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

    @Override
    public String toString() {
        return "Triplet{" +
                "duration=" + duration +
                ", scale=" + scale +
                ", functionName='" + functionName + '\'' +
                '}';
    }
}
