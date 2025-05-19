import Classes.GameFunctionality.GameState;
import Classes.Playable;
import Classes.Skill;
import Preparation.PrepareGame;
import Preparation.Setup;
import Utilities.Reader;
import Utilities.Triplet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {


        Setup setup = new Setup();

        Map<String, Playable> characters = setup.getCharacters();
        List<Playable> playables = new ArrayList<>();
        for (Playable playable : characters.values()) {
            System.out.println(playable);
            playables.add(playable);
        }

        GameState state = GameState.getInstance();
        state.setActiveCharacters(playables);
        state.setSelectedCharacterIndex(0);
        Skill skill = playables.get(0).getSkills().get(0);
        state.applySkill(skill);
        System.out.println(skill.getName());
        System.out.println(state.getActiveCharacters().get(0));
    }
}

