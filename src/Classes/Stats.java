package Classes;

public class Stats{
    private int attack = 0,  health = 0;
    private float damageRes = 0, critRate = 0,  critDamage = 0,  gain = 0;

    public Stats(Stats stats) {
        this.attack = stats.attack;
        this.health = stats.health;
        this.damageRes = stats.damageRes;
        this.critRate = stats.critRate;
        this.critDamage = stats.critDamage;
        this.gain = stats.gain;
    }

    public  Stats(){}

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public float getDamageRes() {
        return damageRes;
    }

    public void setDamageRes(float damageRes) {
        this.damageRes = damageRes;
    }

    public float getCritRate() {
        return critRate;
    }

    public void setCritRate(float critRate) {
        this.critRate = critRate;
    }

    public float getCritDamage() {
        return critDamage;
    }

    public void setCritDamage(float critDamage) {
        this.critDamage = critDamage;
    }

    public float getGain() {
        return gain;
    }

    public void setGain(float gain) {
        this.gain = gain;
    }

    @Override
    public String toString() {
        return "Stats{" +
                "attack=" + attack +
                ", health=" + health +
                ", damageRes=" + damageRes +
                ", critRate=" + critRate +
                ", critDamage=" + critDamage +
                ", gain=" + gain +
                '}';
    }
}
