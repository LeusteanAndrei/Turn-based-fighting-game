package Classes.gameClasses;

import java.util.Objects;

public class Stats{
    private int id=-1, attack = 0,  health = 0;
    private float damageRes = 0, critRate = 0,  critDamage = 0,  gain = 0;

    public Stats(Stats stats) {
        this.id = id;
        this.attack = stats.attack;
        this.health = stats.health;
        this.damageRes = stats.damageRes;
        this.critRate = stats.critRate;
        this.critDamage = stats.critDamage;
        this.gain = stats.gain;
    }
    public Stats(int attack, int health, float damageRes, float critRate, float critDamage, float gain) {
        this.attack = attack;
        this.health = health;
        this.damageRes = damageRes;
        this.critRate = critRate;
        this.critDamage = critDamage;
        this.gain = gain;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Stats stats = (Stats) o;
        return id == stats.id && attack == stats.attack && health == stats.health && Float.compare(damageRes, stats.damageRes) == 0 && Float.compare(critRate, stats.critRate) == 0 && Float.compare(critDamage, stats.critDamage) == 0 && Float.compare(gain, stats.gain) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, attack, health, damageRes, critRate, critDamage, gain);
    }
}
