package Classes;

import Classes.GameFunctionality.GameState;
import Utilities.Triplet;

import java.util.ArrayList;
import java.util.List;

public class Character {

    private String name;
    private int level;
    private Stats initial_stats;
    private Stats current_stats;
    public int charge;

    List<Triplet> status;
    List<Attack> attacks;

    public Character() {
    }

    public Character(String name, int level, Stats initial_stats, Stats current_stats, int charge, List<Triplet> status, List<Attack> attacks)
    {
        this.name = name;
        this.level = level;
        this.initial_stats = new Stats(initial_stats);
        this.current_stats = new Stats(current_stats);
        this.charge = charge;
        this.status = new ArrayList<>();
        for (Triplet e : status) {
            this.status.add(new Triplet(e));
        }
        this.attacks = new ArrayList<>();
        for (Attack p : attacks) {
            this.attacks.add(new Attack(p));
        }

    }

    public Character(Character character) {
        this.name = character.name;
        this.level = character.level;
        this.initial_stats = new Stats(character.initial_stats);
        this.current_stats = new Stats(character.current_stats);
        this.charge = character.charge;
        this.status = new ArrayList<>();
        for (Triplet e : character.status) {
            this.status.add(new Triplet(e));
        }
        this.attacks = new ArrayList<>();
        for (Attack p : character.attacks) {
            this.attacks.add(new Attack(p));
        }
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
