package utilities;

import Classes.gameClasses.*;
import Classes.gameClasses.Character;
import Classes.gameFunctionality.GameState;

import java.io.IOException;
import java.util.List;

public class Printer {

    private static String generateHealthBar(int hp, int maxHp) {
        int barLength = 20; // Length of the health bar
        int filledBars = (int) ((double) hp / maxHp * barLength);
        int emptyBars = barLength - filledBars;

        StringBuilder bar = new StringBuilder();
        bar.append("[").append("=".repeat(filledBars)).append(" ".repeat(emptyBars)).append("]");
        return bar.toString();
    }


    private static String generateEnemyCharge(int charge, int maxCharge)
    {
        StringBuilder chargeBar = new StringBuilder();
        chargeBar.append("[");
        for (int i=0;i<charge;i++)
        {
            chargeBar.append(">");
        }
        for (int i=0;i<maxCharge-charge;i++)
        {
            chargeBar.append("_");
        }
        chargeBar.append("]");
        return chargeBar.toString();


    }

    private static String generateChargeBar(int charge)
    {
        int barLength = 20; // Length of the charge bar
        int filledBars = (int) ((double) charge / 100 * barLength);
        int emptyBars = barLength - filledBars;

        StringBuilder bar = new StringBuilder();
        bar.append("[").append("=".repeat(filledBars)).append(" ".repeat(emptyBars)).append("]");
        return bar.toString();
    }

    public static void printStartFightScreen(GameState state) {
        List<Character> enemies = state.getEnemyCharacters();
        List<Character> characters = state.getPlayerCharacters();

        StringBuilder screen = new StringBuilder();
        screen.append(String.format("Turn %d\n", state.getTurn()));

        screen.append("+------------------------------ BATTLE SCREEN ------------------------------+\n\n");

        // Print enemies
        screen.append("Enemies:\n");
        for (int index = 0; index < enemies.size(); index++) {
            NonPlayable enemy = (NonPlayable) enemies.get(index);
            if (index == state.getSelectedOpposingCharacterIndex()) {
                screen.append("-> ");
            }
            String name = enemy.getName();
            int hp = enemy.getCurrent_stats().getHealth();
            int maxHp = enemy.getMaxHealth();
            String healthBar = generateHealthBar(hp, maxHp);
            screen.append(String.format("[ %-10s ]   %-15s (%3d/%3d)\n", name, healthBar, hp, maxHp));
            String chargeBar = generateEnemyCharge(enemy.getCharge(), enemy.getMaxCharge());
            screen.append(String.format("   Charge: %s (%d/%d)\n", chargeBar, enemy.getCharge(), enemy.getMaxCharge()));


            // Print status
            List<Triplet> status = enemy.getStatus();
            if (!status.isEmpty()) {
                screen.append("   Status: ");
                for (Triplet effect : status) {
                    screen.append(String.format("[%s] ", effect.toString()));
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
            screen.append(String.format("[ %-10s ]   %-15s (%3d/%3d) ", name, healthBar, hp, maxHp));
            screen.append(String.format("attack: %d\n", character.getCurrent_stats().getAttack()));
            String chargeBar = generateChargeBar(character.getCharge());
            screen.append(String.format("   Charge: %s (%d)\n", chargeBar, character.getCharge()));

            // Print skills
            if (index == state.getSelectedCharacterIndex()) {
                screen.append("   Skills:\n");
                character.getSkills().forEach(skill -> {
                    screen.append(String.format("   - %-15s: %s   Current Cooldown: %d Cooldown: %d\n", skill.getName(), skill.getDescription(),
                            skill.getCurrentCooldown(), skill.getCooldown()));
                });
            } else {
                List<Skill> skills = character.getSkills();
                for (int i = 0; i < skills.size(); i++) {
                    Skill skill = skills.get(i);
                    screen.append(String.format("   [%-6s]  ( %d / %d )", skill.getName(),skill.getCurrentCooldown(), skill.getCooldown() ));
                }
                screen.append("\n");
            }

            // Print status
            List<Triplet> status = character.getStatus();
            if (!status.isEmpty()) {
                screen.append("   Status: ");
                for (Triplet effect : status) {
                    screen.append(String.format("[%s] ", effect.toString()));
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
        System.out.println("4. See stats");
        System.out.println("5. See detailed status effects");
        System.out.println("6. Attack");

    }


    public static void printSkills(List<Skill> skills)
    {
        System.out.println("Skills:");
        int counter = 0;
        for (Skill skill : skills) {
            counter++;
            System.out.print("    " + counter + ". " + skill.getName() +": " + skill.getDescription() + " (Cooldown: " + skill.getCooldown() + ")");
            for (Triplet effect : skill.getEffects()) {
                System.out.print(" Effects : " + effect.toString());
            }
            System.out.println();
        }
    }

    public static void printAttacks(List<Attack> attacks)
    {
        System.out.println("Attacks:");
        int counter = 0;
        for (Attack attack : attacks) {
            counter++;
            System.out.println(counter + ". " + attack.toString());
        }
    }

    public static void printStatus(List<Triplet> effects, String name)
    {
        System.out.println("Status effects for " + name + ":");
        System.out.println("+--------------+---------+");
        for (Triplet effect : effects) {
            System.out.printf("| %-12s | %-7s | %-6s\n", effect.getFunctionName(), effect.getScale(), effect.getDuration());
        }
        System.out.println("+--------------+---------+");
    }

    public static void printStats(Character character)
    {
        System.out.println("Stats for " + character.getName() + ":");
        Stats stats = character.getCurrent_stats();
        System.out.printf("| %-12s | %-7s |\n", "Stat", "Value");
        System.out.println("+--------------+---------+");
        System.out.printf("| %-12s | %-7d |\n", "Attack", stats.getAttack());
        System.out.printf("| %-12s | %-7d |\n", "Health", stats.getHealth());
        System.out.printf("| %-12s | %-7.2f |\n", "DamageRes", stats.getDamageRes());
        System.out.printf("| %-12s | %-7.2f |\n", "CritRate", stats.getCritRate());
        System.out.printf("| %-12s | %-7.2f |\n", "CritDamage", stats.getCritDamage());
        System.out.printf("| %-12s | %-7.2f |\n", "Gain", stats.getGain());
        System.out.println("+--------------+---------+");
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
        int number = Reader.readIntInRange(left, right);
        return number;
    }


    public static void printUsers(List<User> users)
    {
        System.out.println("Users:");
        System.out.println("+--------------+---------+");
        int counter = 0;
        for (User user : users) {
            counter++;
            System.out.printf(counter + ". " + "| %-12s | %-7s |\n", user.getUsername(), user.getPassword());
        }
        System.out.println("+--------------+---------+");
    }

    public static void printCharacters(List<? extends Character> characters)
    {
        System.out.println("Characters:");
        System.out.println("+--------------+---------+");
        int counter = 0;
        for (Character character : characters) {
            counter ++;
            System.out.println(counter + ". ");
            System.out.println(character.quickPrint());
        }
    }


    public static void detailedPrintCharacter(Character character)
    {

        System.out.println("Character:");
        System.out.println("+--------------+---------+");
        System.out.println(character.toString());
        System.out.println("+--------------+---------+");
    }


    public static void printRelics(List<Relic> relics)
    {
        System.out.println("Relics:");
        System.out.println("+--------------+---------+");
        int counter = 0;
        for (Relic relic : relics) {
            counter++;
            System.out.println(counter + ". " );
            System.out.println(relic.toString());
        }
        System.out.println("+--------------+---------+");
    }


    public static void printEffects(List<Effect> effects)
    {
        System.out.println("Effects:");
        int counter = 0;
        for (Effect effect : effects) {
            counter++;
            System.out.println(counter+ ". " + effect.getName() + " period: " + effect.getPeriod().toString() );

        }
    }

    public static void printBattles(List<Battle> battles)
    {
        System.out.println("Battles:");
        int counter = 0;
        for (Battle battle : battles) {
            counter++;
            System.out.println(counter + ". " + battle.toString());
        }
    }

}