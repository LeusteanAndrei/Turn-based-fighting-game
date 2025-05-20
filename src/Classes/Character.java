package Classes;

import Enumerations.CharacterType;
import Utilities.Triplet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Character {

    private String name;
    private int level = 0;
    private Stats initial_stats;
    private Stats current_stats;
    private int MaxHealth ;
    private int charge = 0;
    private CharacterType type;
    Set<String> attributes = new TreeSet<>();

    private List<Triplet> status = new ArrayList<>();
    private List<Attack> attacks = new ArrayList<>();



    public Character() {
        this.name = "";
        this.level = 0;
        this.initial_stats = new Stats();
        this.current_stats = new Stats();
        this.charge = 0;
        this.MaxHealth = initial_stats.getHealth();
    }

    public Character(String name, int level, Stats initial_stats, Stats current_stats, int charge,
                     List<Triplet> status, List<Attack> attacks,CharacterType type, Set<String> attributes) {
        this.name = name;
        this.level = level;
        this.initial_stats = new Stats(initial_stats);
        this.current_stats = new Stats(current_stats);
        this.charge = charge;
        this.type = type;
        this.MaxHealth = initial_stats.getHealth();
        this.setAttributes(attributes);
        this.setStatus(status);
        this.setAttacks(attacks);
    }

    public Character(Character character) {
        this.name = character.name;
        this.level = character.level;
        this.initial_stats = new Stats(character.initial_stats);
        this.current_stats = new Stats(character.current_stats);
        this.charge = character.charge;
        this.type = character.type;
        this.MaxHealth = this.current_stats.getHealth();
        this.setStatus(character.status);
        this.setAttacks(character.attacks);
        this.setAttributes(character.attributes);
    }


    public void setAttributes(Set<String> attributes)
    {
        this.attributes = new TreeSet<>();
        this.attributes.addAll(attributes);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public List<Triplet> getStatus() {
        List<Triplet> status = new ArrayList<>();
        for (Triplet e : this.status) {
            status.add(new Triplet(e));
        }
        return status;
    }

    public void setStatus(List<Triplet> status) {
        this.status = new ArrayList<>();
        for (Triplet e : status) {
            this.status.add(new Triplet(e));
        }
    }

    public void setAttacks(List<Attack> attacks) {
        this.attacks = new ArrayList<>();
        for (Attack p : attacks) {
            this.attacks.add(new Attack(p));
        }
    }

    public List<Attack> getAttacks()
    {
        List<Attack> attacks = new ArrayList<>();
        for (Attack p : this.attacks) {
            attacks.add(new Attack(p));
        }
        return attacks;
    }

    public Stats getInitial_stats(){
        return new Stats(initial_stats);
    }
    public void setInitial_stats(Stats initial_stats) {
        this.initial_stats = new Stats(initial_stats);
    }
    public Stats getCurrent_stats() {
        return new Stats(current_stats);
    }
    public void setCurrent_stats(Stats current_stats) {
        this.current_stats = new Stats(current_stats);
    }

    public int getMaxHealth() {
        return MaxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        MaxHealth = maxHealth;
    }

    public void setAttack(int attack)
    {
        this.current_stats.setAttack(attack);
    }
    public void setHealth(int health)
    {
        this.current_stats.setHealth(health);
    }
    public void setDamageRes(float damageRes)
    {
        this.current_stats.setDamageRes(damageRes);
    }
    public void setCritRate(float critRate)
    {
        this.current_stats.setCritRate(critRate);
    }
    public void setCritDamage(float critDamage)
    {
        this.current_stats.setCritDamage(critDamage);
    }
    public void setGain(float gain)
    {
        this.current_stats.setGain(gain);
    }


    @Override
    public String toString() {


        String s = "Character: " + name + ", level: " + level +
                "\n    initial_stats: " + initial_stats +
                "\n    current_stats: " + current_stats +
                "\n    charge: " + charge + "\n";

        StringBuilder status = new StringBuilder("    status: ");
        for (Triplet e : this.status) {
            status.append(e.toString()).append(" ");
        }
        s += status.toString() + "\n";
        StringBuilder attacks = new StringBuilder("    attacks: ");
        for (Attack p : this.attacks) {
            attacks.append(p.toString()).append(" ");
        }
        s += attacks.toString() + "\n";
        return s;
    }
}
