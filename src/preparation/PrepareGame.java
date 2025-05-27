package preparation;

import Classes.dbConnection.CharacterConnection;
import Classes.dbConnection.EffectConnection;
import Classes.dbConnection.SkillConnection;
import Classes.gameClasses.Battle;
import Classes.gameClasses.Effect;
import Classes.gameClasses.NonPlayable;
import Classes.gameClasses.Skill;
import Classes.gameFunctionality.Function;
import Classes.gameFunctionality.SkillEffects;

import java.util.*;

public class PrepareGame {


    private  Map<String, Function> effectFunctions = new HashMap<>();
    private  Map<String, Effect> effects = new HashMap<>();
    private static final PrepareGame instance = new PrepareGame();
    private  Set<String> buffList = Set.of(
            "attack",
            "health",
            "damageRes",
            "critRate",
            "critDamage",
            "gainC"
    );
    private  Map<String, Skill> skills = new HashMap<>();
    private  Map<String, NonPlayable>  enemies = new HashMap<>();
    private List<Battle> battles = new ArrayList<>();
    public int s=-1;



    private PrepareGame() {


        prepareGame();
    }

    public static PrepareGame getInstance() { return instance;}



    private    void prepareGame()
    {
        prepareEffectFunctions();
        prepareEffects();
        setupSkills();
        prepareEnemies();
        prepareBattles();

    }

    public  void prepareEnemies()
    {
        List<NonPlayable> enemyList = new CharacterConnection().getNonplayable();
        for (NonPlayable enemy : enemyList) {
            this.enemies.put(enemy.getName(), new NonPlayable(enemy));
        }
    }

    public  NonPlayable getEnemyByName(String name)
    {
        return new NonPlayable( enemies.get(name));
    }
    public  List<NonPlayable> getEnemiesByName(List<String> names)
    {
        List<NonPlayable> enemies = new ArrayList<>();
        for (String name : names) {
            NonPlayable enemy = getEnemyByName(name);
            if (enemy != null) {
                enemies.add(new NonPlayable(enemy));
            }
        }
        return enemies;
    }



    public  Set<String> getBuffList() {
        Set<String> buffList = new HashSet<>();
        for (String buff : buffList) {
            buffList.add(buff);
        }
        return buffList;
    }

    private  void setupSkills()
    {

        SkillConnection skillConnection = new SkillConnection();
        List<Skill> _skills = skillConnection.read();
        for (Skill skill : _skills) {
            skills.put(skill.getName(), new Skill(skill));
        }
    }


    public  Map<String, Skill> getSkills() {
        return skills;
    }


    public  List<Skill> getSkillsList() {
        return new ArrayList<>(skills.values());
    }




    private  void prepareEffectFunctions()
    {
        effectFunctions.put("self_attack_up", SkillEffects::selfAttackUp);
        effectFunctions.put("remove_self_attack_up", SkillEffects::removeSelfAttackUp);
        effectFunctions.put("party_attack_up", SkillEffects::partyAttackUp);
        effectFunctions.put("remove_party_attack_up", SkillEffects::removePartyAttackUp);
        effectFunctions.put("self_charge", SkillEffects::selfCharge);
        effectFunctions.put("curse_all_enemies", SkillEffects::curseAllEnemies);
        effectFunctions.put("curse_selected_enemy", SkillEffects::curseSelectedEnemy);
        effectFunctions.put("gain_charge_each_turn", SkillEffects::gainChargeEachTurn);
        effectFunctions.put("party_charge", SkillEffects::partyCharge);
        effectFunctions.put("party_ultimate_strength", SkillEffects::partyUltimateStrength);

    }

    private  void prepareEffects()
    {

        EffectConnection effectConnection = new EffectConnection();
        List<Effect> _effects = effectConnection.read();
        for (Effect effect : _effects) {
            String name = effect.getName();
            Effect newEffect = new Effect(effect);
            this.effects.put(name, newEffect);
        }

    }

    public Map<String, Function> getEffectFunctions()
    {
        Map<String, Function> effectFunctions = new HashMap<>();
        for (Map.Entry<String, Function> entry : this.effectFunctions.entrySet()) {
            String key = entry.getKey();
            Function value = entry.getValue();
            effectFunctions.put(key, value);
        }

        return effectFunctions;

    }



    public  Function getFunction(String functionName)
    {
        return effectFunctions.get(functionName);

    }

    public Effect getEffect(String effectName)
    {
        return new Effect( effects.get(effectName) ) ;
    }

    public List<Effect> getEffects()
    {
        System.out.println(effects.size());
        List<Effect> effects = new ArrayList<>();
        for (Map.Entry<String, Effect> entry : this.effects.entrySet()) {
            System.out.println("heihis");
            Effect value = new Effect(entry.getValue());
            effects.add(value);
        }
        return effects;
    }

    public Map<String, Effect> getEffectsMap() {
        return effects;
    }


    public List<Battle> getBattles() {
        List<Battle> battles = new ArrayList<>();
        for (Battle battle : this.battles) {
            battles.add(new Battle(battle));
        }
        return battles;
    }

    private void prepareBattles()
    {
        battles = new ArrayList<>();


        Battle battle = new Battle();
        battle.setEnemies(List.of("Lahmu", "Lahmu", "Lahmu"));
        battles.add(new Battle(battle));

        battle = new Battle();
        battle.setEnemies(List.of("GUDAGUDA", "SOLOMON"));
        battles.add(new Battle(battle));

    }

}
