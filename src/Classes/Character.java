package Classes;

import Classes.GameFunctionality.GameState;
import Utilities.Pair;
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
    List<Pair> attacks;

    public Character() {
    }

    public Character(String name, int level, Stats initial_stats, Stats current_stats, int charge, List<Triplet> status, List<Pair> attacks)
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
        for (Pair p : attacks) {
            this.attacks.add(new Pair(p));
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
        for (Pair p : character.attacks) {
            this.attacks.add(new Pair(p));
        }
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


    public void setAttacks(List<Pair> attacks) {
        this.attacks = new ArrayList<>();
        for (Pair p : attacks) {
            this.attacks.add(new Pair(p));
        }
    }

    public List<Pair> getAttacks()
    {
        List<Pair> attacks = new ArrayList<>();
        for (Pair p : this.attacks) {
            attacks.add(new Pair(p));
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




}
