package Preparation;

import Classes.Character;
import Classes.Playable;
import Classes.Skill;
import Classes.Stats;
import Utilities.Triplet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class Setup {

    private PrepareGame game = PrepareGame.getInstance();
    private Map<String, Skill> skills = new HashMap<>();
    private Map<String, Playable> characters = new HashMap<>();


    {
        setupSkills();
        setupCharacters();
    }

    private void setupSkills()
    {
        List<Triplet> effects = new ArrayList<>();
        Skill skill = new Skill();
        skill.setName("Charisma A+");
        skill.setDescription("Increase the attack of all allies by 20%");
        skill.setCooldown(5);
        effects.add(new Triplet(3, (float)0.2, "party_attack_up"));
        skill.setEffects(effects);

        skills.put(skill.getName(), new Skill(skill));



        effects = new ArrayList<>();
        skill = new Skill();
        skill.setName("Treasury of Babylon EX");
        skill.setDescription("Increase charge by 30%");
        skill.setCooldown(5);
        effects.add(new Triplet(-1,30, "self_charge"));
        skill.setEffects(effects);

        skills.put(skill.getName(), new Skill(skill));


        effects = new ArrayList<>();
        skill = new Skill();
        skill.setName("Charisma of Jade A+");
        skill.setDescription("Increase the attack of all allies by 20% and the attack of self by 30%");
        skill.setCooldown(7);
        effects.add(new Triplet(3, (float)0.2, "party_attack_up"));
        effects.add(new Triplet(3, (float)0.3, "self_attack_up"));
        skill.setEffects(effects);

        skills.put(skill.getName(), new Skill(skill));


        effects = new ArrayList<>();
        skill = new Skill();
        skill.setName("Curtain of the Night EX");
        skill.setDescription("Increase the ultimate strength and charge of all allies by 20%");
        skill.setCooldown(5);
        effects.add(new Triplet(3, (float)0.2, "party_charge"));
        effects.add(new Triplet(3, (float)0.2, "party_ultimate_strength"));
        skill.setEffects(effects);

        skills.put(skill.getName(), new Skill(skill));


    }

    private void setupCharacters()
    {
        Stats stats = new Stats();
        Playable playable = new Playable();
        List<Skill> skills = new ArrayList<>();

        playable.setName("Gilgamesh");
        playable.setLevel(1);

        stats.setAttack(1897);
        stats.setHealth(1920);
        stats.setDamageRes((float)0.15);
        stats.setCritRate((float)0.30);
        stats.setCritDamage((float)1.4);
        stats.setGain((float)0.34);

        playable.setInitial_stats(new Stats(stats));
        playable.setCurrent_stats(new Stats(stats));
        playable.setCharge(0);
        playable.setStatus(new ArrayList<>());
        playable.setAttacks(new ArrayList<>());

        skills.add(new Skill(this.skills.get("Charisma A+")));
        skills.add(new Skill(this.skills.get("Treasury of Babylon EX")));
        playable.setSkills(skills);


        characters.put(playable.getName(), new Playable(playable));

        stats = new Stats();
        playable = new Playable();
        skills = new ArrayList<>();

        playable.setName("Oberon");
        playable.setLevel(1);

        stats.setAttack(1825);
        stats.setHealth(2194);
        stats.setDamageRes((float)0.15);
        stats.setCritRate((float)0.30);
        stats.setCritDamage((float)1.4);
        stats.setGain((float)0.34);

        playable.setInitial_stats(stats);
        playable.setCurrent_stats(stats);
        skills.add(new Skill(this.skills.get("Charisma of Jade A+")));
        skills.add(new Skill(this.skills.get("Curtain of the Night EX")));
        playable.setSkills(skills);

        characters.put(playable.getName(), new Playable(playable));



    }

    public PrepareGame getGame() {
        return game;
    }

    public Map<String, Skill> getSkills() {
        return skills;
    }

    public Map<String, Playable> getCharacters() {
        return characters;
    }
}
