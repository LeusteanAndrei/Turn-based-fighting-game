package Classes.gameFunctionality;

import Classes.gameClasses.Stats;
import utilities.Triplet;

import java.util.ArrayList;
import java.util.List;

public final class SkillEffects {

    private SkillEffects() {}

    public static void addTriplet(GameState state, Triplet triplet)
    {
        List<Triplet> status = state.activeCharacters.get(state.selectedCharacterIndex).getStatus();
        if (status == null)
            status = new ArrayList<>();
        status.add(triplet);
        state.activeCharacters.get(state.selectedCharacterIndex).setStatus(status);

    }

    public static void selfAttackUp(GameState state, float scale) {
        int index = state.selectedCharacterIndex;

        int initial_attack = state.activeCharacters.get(index).getInitial_stats().getAttack();
        int current_attack = state.activeCharacters.get(index).getCurrent_stats().getAttack();
        int new_attack = current_attack + (int) (initial_attack * scale);
        Stats stats = state.activeCharacters.get(index).getCurrent_stats();
        stats.setAttack(new_attack);
        state.activeCharacters.get(index).setCurrent_stats(stats);
        addTriplet(state, state.usingEffect);

    }

    public static void removeSelfAttackUp(GameState state, float scale) {
        int index = state.selectedCharacterIndex;

        int initial_attack = state.activeCharacters.get(index).getInitial_stats().getAttack();
        int current_attack = state.activeCharacters.get(index).getCurrent_stats().getAttack();
        int new_attack = current_attack - (int) (initial_attack * scale);
        Stats stats = state.activeCharacters.get(index).getCurrent_stats();
        stats.setAttack(new_attack);
        state.activeCharacters.get(index).setCurrent_stats(stats);
    }

    public static void partyAttackUp(GameState state, float scale) {
        int index = state.selectedCharacterIndex;
        state.usingEffect = new Triplet(state.usingEffect.getDuration(), state.usingEffect.getScale(), "self_attack_up");
        for (int i = 0; i < state.activeCharacters.size(); i++) {
            state.selectedCharacterIndex = i;
            selfAttackUp(state, scale);
        }
        state.selectedCharacterIndex = index;
    }

    public static void removePartyAttackUp(GameState state, float scale) {

        int index = state.selectedCharacterIndex;
        for (int i = 0; i < state.activeCharacters.size(); i++) {
            state.selectedCharacterIndex = i;
            removeSelfAttackUp(state, scale);
        }
        state.selectedCharacterIndex = index;
    }

    public static void selfCharge(GameState state, float scale) {
        int index = state.selectedCharacterIndex;
        int charge = state.activeCharacters.get(index).getCharge();
        state.activeCharacters.get(index).setCharge(charge + (int) scale);
    }

    public static void partyCharge(GameState state, float scale) {
        int index = state.selectedCharacterIndex;
        for (int i = 0; i < state.activeCharacters.size(); i++) {
            state.selectedCharacterIndex = i;
            selfCharge(state, scale);
        }
        state.selectedCharacterIndex = index;
    }

    public static void partyUltimateStrength(GameState state, float scale) {}

    public static void curseAllEnemies(GameState state, float scale) {}

    public static void curseSelectedEnemy(GameState state, float scale) {}

    public static void gainChargeEachTurn(GameState state, float scale) {}




}
