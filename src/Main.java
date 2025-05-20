import Classes.*;
import Classes.Character;
import Classes.GameFunctionality.GameState;
import Classes.GameFunctionality.Printer;
import Enumerations.CharacterType;
import Preparation.PrepareGame;
import Preparation.Setup;
import Utilities.Reader;
import Utilities.Triplet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;



class A
{
    public int test;

    public A(int test)
    {
        this.test = test;
    }
}
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {


        Setup setup = new Setup();

        Map<String, Playable> characters = setup.getCharacters();
        List<Playable> playables = new ArrayList<>();
        for (Playable playable : characters.values()) {
            playables.add(playable);
        }



        Relic relic = new Relic();
        relic.setName("sdasda");
        relic.setDescription("sdasdasd");
        List<Triplet> effects = new ArrayList<>();
        effects.add(new Triplet(5, 0.5f, "self_attack_up"));
        relic.setEffects(effects);
        Map<String, Number> buffs = new HashMap<>();
        buffs.put("attack", 200);
        relic.setBuffs(buffs);
        relic.applyBuffs(playables.get(0));
//        System.out.println(playables.get(0));

        List<String> ls = new    ArrayList<>();
        ls.add("Lahmu");
        ls.add("GUDAGUDA");




        GameState state = GameState.getInstance();
        state.setActiveCharacters(playables);
        state.setSelectedCharacterIndex(0);
        state.setOpposingCharacters(setup.getGame().getEnemiesByName(ls));


//        for (Character character : state.getOpposingCharacters()) {
//            System.out.println(character);
//        }


        Battle battle = new Battle();
        battle.setEnemies(ls);
        List<Battle> battles = new ArrayList<>();
        battles.add(battle);
        state.setBattles(battles);
        state.startFight();




//
//        Skill skill = playables.get(0).getSkills().get(0);
//        state.applySkill(skill);
//
//
//
//
//        Printer.clearTerminal();
//
//        Printer.printStartFightScreen(state);
//        Printer.choiceMenu(1, 4,
//                "Select a character, Select an enemy, Use a skill, Attack");


    }
}

