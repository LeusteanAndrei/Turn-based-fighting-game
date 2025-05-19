package Utilities;

public class Pair {

    private float scale;
    private String Function;


    public Pair(float scale, String Function) {
        this.scale = scale;
        this.Function = Function;
    }

    public Pair(Pair pair) {
        this.scale = pair.scale;
        this.Function = pair.Function;
    }

    public String getFunction() {
        return Function;
    }

    public void setFunction(String function) {
        Function = function;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }




}
