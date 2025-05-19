package Preparation;

import Classes.*;
import Classes.GameFunctionality.SkillEffects;
import Enumerations.EffectType;
import Enumerations.GamePeriod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrepareGame {


    private static Map<String, Function> effectFunctions;
    private static Map<String, Effect> effects;
    private static Map<String, NonPlayable> enemies;

    private static final PrepareGame instance = new PrepareGame();

    private PrepareGame() {
        effectFunctions = new HashMap<>();
        effects = new HashMap<>();
        enemies = new HashMap<>();

    }

    public static PrepareGame getInstance() { return instance;}

    static
    {
        prepareGame();
    }

    private static void prepareGame()
    {
        prepareEffectFunctions();
        prepareEffects();
    }

    private static void prepareEffectFunctions()
    {
        effectFunctions.put("self_attack_up", SkillEffects::selfAttackUp);
        effectFunctions.put("remove_self_attack_up", SkillEffects::removeSelfAttackUp);
        effectFunctions.put("party_attack_up", SkillEffects::partyAttackUp);
        effectFunctions.put("remove_party_attack_up", SkillEffects::removePartyAttackUp);
        effectFunctions.put("self_charge", SkillEffects::selfCharge);
        effectFunctions.put("curse_all_enemies", SkillEffects::curseAllEnemies);
        effectFunctions.put("curse_selected_enemy", SkillEffects::curseSelectedEnemy);
        effectFunctions.put("gain_charge_each_turn", SkillEffects::gainChargeEachTurn);
    }

    private static void prepareEffects()
    {
        effects.put("party_attack_up", new Effect("party_attack_up", "party_attack_up", "remove_party_attack_up",
                GamePeriod.START_OF_TURN, EffectType.ONCE));
        effects.put("self_charge", new Effect("self_charge", "self_charge", "",
                GamePeriod.START_OF_TURN, EffectType.ONCE));
        effects.put("self_attack_up", new Effect("self_attack_up", "self_attack_up", "remove_self_attack_up",
                GamePeriod.START_OF_TURN, EffectType.ONCE));
        effects.put("curse_all_enemies", new Effect("curse_all_enemies", "curse_all_enemies", "",
                GamePeriod.START_OF_TURN, EffectType.ONCE));
        effects.put("curse_selected_enemy", new Effect("curse_selected_enemy", "curse_selected_enemy", "",
                GamePeriod.START_OF_TURN, EffectType.ONCE));
        effects.put("gain_charge_each_turn", new Effect("gain_charge_each_turn", "gain_charge_each_turn", "",
                GamePeriod.START_OF_TURN, EffectType.EVERY_TURN));

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

    public Map<String, NonPlayable> getEnemies()
    {
        Map<String, NonPlayable> enemies = new HashMap<>();
        for (Map.Entry<String, NonPlayable> entry : this.enemies.entrySet()) {
            String key = entry.getKey();
            NonPlayable value = new NonPlayable(entry.getValue());
            enemies.put(key, value);
        }

        return enemies;
    }

    public List<NonPlayable> getEnemiesByName(List<String> name)
    {
        List<NonPlayable> enemies = new ArrayList<>();
        for (String enemyName : name)
        {
            NonPlayable enemy = this.enemies.get(enemyName);
            if (enemy != null) {
                enemies.add(new NonPlayable(enemy));
            }
        }

        return enemies;
    }


    public  Function getFunction(String functionName)
    {
        return effectFunctions.get(functionName);

    }

    public Effect getEffect(String effectName)
    {
        return new Effect( effects.get(effectName) ) ;
    }



}
