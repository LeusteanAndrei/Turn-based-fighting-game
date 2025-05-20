package Classes.GameFunctionality;

import Classes.Character;
import Classes.Playable;
import Utilities.Reader;
import Utilities.Triplet;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Printer {

    private static String generateHealthBar(int hp, int maxHp) {
        int barLength = 20; // Length of the health bar
        int filledBars = (int) ((double) hp / maxHp * barLength);
        int emptyBars = barLength - filledBars;

        StringBuilder bar = new StringBuilder();
        bar.append("[").append("=".repeat(filledBars)).append(" ".repeat(emptyBars)).append("]");
        return bar.toString();
    }



    public static void printStartFightScreen(GameState state) {
        List<Character> enemies = state.getEnemyCharacters();
        List<Character> characters = state.getPlayerCharacters();

        StringBuilder screen = new StringBuilder();

        screen.append("+------------------------------ BATTLE SCREEN ------------------------------+\n\n");

        // Print enemies
        screen.append("Enemies:\n");
        for (int index = 0; index < enemies.size(); index++) {
            Character enemy = enemies.get(index);
            if (index == state.getSelectedOpposingCharacterIndex()) {
                screen.append("-> ");
            }
            String name = enemy.getName();
            int hp = enemy.getCurrent_stats().getHealth();
            int maxHp = enemy.getMaxHealth();
            String healthBar = generateHealthBar(hp, maxHp);
            screen.append(String.format("[ %-10s ]   %-15s (%3d/%3d)\n", name, healthBar, hp, maxHp));

            // Print status
            List<Triplet> status = enemy.getStatus();
            if (!status.isEmpty()) {
                screen.append("   Status: ");
                for (Triplet effect : status) {
                    screen.append(String.format("[%s] ", effect.getFunctionName()));
                }
                screen.append("\n");
            }
        }
        screen.append("\n");

        screen.append("--------------------------- VS ----------------------------\n\n");

        // Print allies
        screen.append("Your Characters:\n");
        for (int index = 0; index < characters.size(); index++) {
            if (index == state.getSelectedCharacterIndex()) {
                screen.append("-> ");
            }
            Character ch = characters.get(index);
            Playable character = (Playable) ch;
            String name = character.getName();
            int hp = character.getCurrent_stats().getHealth();
            int maxHp = character.getMaxHealth();
            String healthBar = generateHealthBar(hp, maxHp);
            screen.append(String.format("[ %-10s ]   %-15s (%3d/%3d)\n", name, healthBar, hp, maxHp));

            // Print skills
            List<String> skills = character.getSkills().stream().map(skill -> skill.getName()).toList();
            for (int i = 0; i < 3; i++) { // Ensure 3 skill slots are printed
                String skill = i < skills.size() ? skills.get(i) : "----"; // Placeholder for missing skills
                screen.append(String.format("   [%-6s]  ", skill));
            }
            screen.append("\n");

            // Print status
            List<Triplet> status = character.getStatus();
            if (!status.isEmpty()) {
                screen.append("   Status: ");
                for (Triplet effect : status) {
                    screen.append(String.format("[%s] ", effect.getFunctionName()));
                }
                screen.append("\n");
            }
        }
        screen.append("\n");

        System.out.println(screen.toString());



    }


    public static void choiceMenu(int left, int right, String choices)
    {
        StringBuilder menu = new StringBuilder();
        menu.append("Choose a number between ").append(left).append(" and ").append(right).append(":\n");
        String[] options = choices.split(",");
        for (int i = 0; i < options.length; i++) {
            String option = options[i].trim();
            if (option.isEmpty()) {
                continue;
            }
            menu.append(i + 1).append(". ").append(option).append("\n");
        }
        System.out.print(menu.toString());
    }

    public static void choiceFight()
    {
        System.out.println("Choose an action:");
        System.out.println("1. Select a character");
        System.out.println("2. Select an enemy");
        System.out.println("3. Use a skill");
        System.out.println("4. Attack");

    }

    public static void clearTerminal() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error clearing terminal: " + e.getMessage());
        }
    }

    public static void sleep(int milliseconds) {
        if (milliseconds == 0)
                return;
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            System.err.println("Error during sleep: " + e.getMessage());
        }
    }

    public static int getChoice(int left, int right)
    {
        System.out.print("Choose a number between " + left + " and " + right+": ");
        int number = Reader.readIntInRange(1, 4);
        return number;
    }

}