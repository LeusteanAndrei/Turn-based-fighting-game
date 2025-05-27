package Classes.gameFunctionality;

import Classes.gameClasses.*;
import Classes.gameClasses.Character;
import enumerations.EffectType;
import enumerations.GamePeriod;
import preparation.PrepareGame;
import utilities.Service;
import utilities.Triplet;

import java.util.ArrayList;
import java.util.List;

public class GameState {

     PrepareGame prepareGame = PrepareGame.getInstance();
     User user = Service.getInstance().getUser();
     List<Character> activeCharacters;
     List<Character> opposingCharacters;

     List<Character> playerCharacters;
     List<Character> enemyCharacters;

     List<Integer> hasHitCount;
     List<Integer> beenHitCount;

     Integer selectedCharacterIndex = 0;
     Integer selectedOpposingCharacterIndex = 0;
     Integer turnCount = 0;
     Triplet usingEffect;

    private List<Battle> battles;

    private static final GameState instance = new GameState();

    private GameState() {
        this.activeCharacters = new ArrayList<>();
        this.opposingCharacters = new ArrayList<>();
        this.hasHitCount = new ArrayList<>();
        this.beenHitCount = new ArrayList<>();
    }
    public static GameState getInstance() { return instance; }

    public void setGameState(List<Playable> activeCharacters, List<Battle> battles)
    {

        this.setActiveCharacters(activeCharacters);
        this.setBattles(battles);

        this.hasHitCount = new ArrayList<>( );
        this.beenHitCount = new ArrayList<>( );
        for (int i = 0; i < activeCharacters.size(); i++)
        {
            this.hasHitCount.add( 0);
            this.beenHitCount.add( 0);
        }
    }

    public void printGameState()
    {
        System.out.println("Active Characters: ");
        for (Character character : this.activeCharacters) {
            System.out.println(character.getName());
        }
        System.out.println("Battles:");
        for (Battle battle : this.battles) {
            System.out.println(battle);
        }
    }


    public void startFight()
    {

        for (int i=0;i<activeCharacters.size();i++)
        {
            Playable playable = (Playable) activeCharacters.get(i);
            Relic relic = playable.getRelic();
            if (relic != null)
            {
                applyRelic(relic, i);
            }
        }
        for (Battle battle : battles)
        {
            this.startBattle( battle );
        }
    }

    public void playerTurn()
    {
        boolean attacked = false;
        while(!attacked) {
            Printer.choiceFight();
            int choice = Printer.getChoice(1, 6);
            switch (choice) {
                case 1: {
                    this.selectCharacter();
                    this.reloadScreen(0);
                    break;
                }
                case 2: {
                    this.selectEnemy();
                    this.reloadScreen(0);
                    break;
                }
                case 3: {
                    this.selectSkill();
//                    this.reloadScreen(0);
                    break;
                }
                case 4:
                {
                    this.reloadScreen(0);
                    Character character = this.activeCharacters.get(this.selectedCharacterIndex);
                    Printer.printStats(character);
                    break;
                }
                case 5:
                {
                    this.reloadScreen(0);
                    Character character = this.activeCharacters.get(this.selectedCharacterIndex);
                    Printer.printStatus(character.getStatus(), character.getName());
                    break;
                }
                case 6: {
                    this.player_attack();
                    attacked = true;
                    break;
                }
            }
//            this.playerCharacters = this.activeCharacters;
        }
    }

    public void selectCharacter(){
        this.reloadScreen(0);
        int right = this.activeCharacters.size();
        StringBuilder list = new StringBuilder();
        for (Character character : this.activeCharacters) {
            list.append(character.getName()).append(", ");
        }
        Printer.choiceMenu(1, right, list.toString());
        int choice = Printer.getChoice(1, right);
        this.selectedCharacterIndex = choice - 1;

    }
    public void selectEnemy(){
        this.reloadScreen(0);
        int right = this.opposingCharacters.size();
        StringBuilder list = new StringBuilder();
        for (Character character : this.opposingCharacters) {
            list.append(character.getName()).append(", ");
        }
        Printer.choiceMenu(1, right, list.toString());
        int choice = Printer.getChoice(1, right);
        this.selectedOpposingCharacterIndex = choice - 1;

    }
    public void selectSkill(){
        this.reloadScreen(0);
        Playable activeCharacter = (Playable) this.activeCharacters.get(this.selectedCharacterIndex);
        System.out.printf("Select a skill from your selected active character ( %s ), or press 0 to go back",
                activeCharacter.getName());
        String str = "";
        for (Skill skill : activeCharacter.getSkills()) {
           str = str  + skill.toString()+ ", ";
        }
        Printer.choiceMenu(1, activeCharacter.getSkills().size(), str);
        boolean onCooldown = true;
        int choice = 0;
        Skill skill = new Skill();
        while(onCooldown) {
            choice = Printer.getChoice(1, activeCharacter.getSkills().size());
            if (choice == 0)
            {
                return;
            }
            skill = activeCharacter.getSkills().get(choice - 1);
            if (skill.getCurrentCooldown() ==0)
            {
                onCooldown = false;
            }
            else
            {
                System.out.printf("The skill is on a  %dT cooldown, please select another skill\n ", skill.getCurrentCooldown());
            }
        }
        this.applySkill(choice-1);
        this.reloadScreen(0);
        System.out.println("You used the skill: " + skill.getName());

    }

    public static void player_attack(){}

    public void enemyTurn()
    {

    }

    public void startBattle(Battle battle)
    {

        this.setOpposingCharacters( battle.getEnemies() );

        Printer.printStartFightScreen(this);

        applyEffects(GamePeriod.START_OF_BATTLE);
        System.out.println(this.selectedCharacterIndex);


        swap();
        applyEffects(GamePeriod.START_OF_BATTLE);

        swap();

        while(!opposingCharacters.isEmpty() || !activeCharacters.isEmpty())
        {
//            this.reloadScreen(0);
            applyEffects(GamePeriod.START_OF_TURN);

            playerTurn();
            applyEffects(GamePeriod.END_OF_TURN);

            swap();

            applyEffects(GamePeriod.START_OF_TURN);
            enemyTurn();
            applyEffects(GamePeriod.END_OF_TURN);

            swap();
            this.turnCount++;
            turnPassed();

            removeEffects();
            swap();
            removeEffects();
            swap();
        }


        applyEffects(GamePeriod.END_OF_BATTLE);
        swap();
        applyEffects(GamePeriod.END_OF_BATTLE);
        swap();


    }

    public List<Character> getActiveCharacters()
    {
        List<Character> activeCharacters = new ArrayList<>();
        for (Character character : this.activeCharacters) {
            if (character instanceof Playable) {
                activeCharacters.add(new Playable((Playable) character));
            }
            else if (character instanceof NonPlayable)
                activeCharacters.add(new NonPlayable((NonPlayable) character));
        }
        return activeCharacters;
    }

    public void setBattles(List<Battle> battles)
    {
        this.battles = new ArrayList<>();
        for (Battle battle : battles) {
            this.battles.add(new Battle(battle));
        }
    }

    public void setActiveCharacters(List< ? extends Character> currentCharacters)
    {
        this.activeCharacters = new ArrayList<>();

        for ( Character character : currentCharacters) {
            if (character instanceof Playable)
                this.activeCharacters.add(new Playable((Playable) character));
            else if (character instanceof NonPlayable)
                this.activeCharacters.add(new NonPlayable((NonPlayable) character));
        }
        this.playerCharacters = this.activeCharacters;
    }

    public void setOpposingCharacters(List< ? extends Character> opposingCharacters)
    {
        this.opposingCharacters = new ArrayList<>();
        for ( Character character : opposingCharacters) {
            if (character instanceof Playable)
                this.opposingCharacters.add(new Playable((Playable) character));
            else if (character instanceof NonPlayable)
                this.opposingCharacters.add(new NonPlayable((NonPlayable) character));
        }
        this.enemyCharacters = this.opposingCharacters;
    }

    public List<Character> getOpposingCharacters()
    {
        List<Character> opposingCharacters = new ArrayList<>();
        for (Character character : this.opposingCharacters) {
            if (character instanceof Playable)
                opposingCharacters.add(new Playable((Playable) character));
            else if (character instanceof NonPlayable)
                opposingCharacters.add(new NonPlayable((NonPlayable) character));
        }
        return opposingCharacters;
    }

    public void applySkill(int index)
    {

        List<Skill> skills = ((Playable)this.activeCharacters.get(this.selectedCharacterIndex)).getSkills();
        Skill skill = skills.get(index);
        for(Triplet triplet : skill.getEffects()) {

            Character activeCharacter = this.activeCharacters.get(this.selectedCharacterIndex);

//            if (triplet.getDuration() != -1) {
//
//                List<Triplet> activeCharacterStatus = activeCharacter.getStatus();
//
//                activeCharacterStatus.add(triplet);
//                activeCharacter.setStatus(activeCharacterStatus);
//            }
            this.usingEffect = triplet;
            System.out.println("Applying effect: " + triplet.getFunctionName() + " with scale: " + triplet.getScale());
            Effect effect = prepareGame.getEffect(triplet.getFunctionName());

            if (effect.getType() == EffectType.ONCE)
            {
                effect.applyEffect(this, triplet.getScale());
            }
            else {
                SkillEffects.addTriplet(this, triplet);
            }

        }
        skill.setCurrentCooldown(skill.getCooldown());

        ((Playable)this.activeCharacters.get(this.selectedCharacterIndex)).setSkills(skills);
    }

    private void applyRelic(Relic relic,  int index)
    {
        this.selectedCharacterIndex = index;
        Playable playable = (Playable) this.activeCharacters.get(index);
        relic.applyBuffs(playable);
        List<Triplet> relicEffects = relic.getEffects();
        for (Triplet triplet : relicEffects) {
            int duration = triplet.getDuration();
            float scale = triplet.getScale();
            Effect effect = prepareGame.getEffect(triplet.getFunctionName());
            usingEffect = triplet;


            if (effect.getType() == EffectType.ONCE)
            {
                effect.applyEffect(this, scale);
            }

        }

    }


    private void swap()
    {
        List<Character> aux = this.activeCharacters;
        this.activeCharacters = this.opposingCharacters;
        this.opposingCharacters = aux;
    }

    private void applyEffects(GamePeriod Period)
    {
        int index = this.selectedCharacterIndex;
        for (int i=0;i<activeCharacters.size();i++) {
            Character character = this.activeCharacters.get(i);
            this.selectedCharacterIndex = i;
            for(Triplet triplet: character.getStatus())
           {
                Effect effect = prepareGame.getEffect(triplet.getFunctionName());
                this.usingEffect = triplet;

                if(effect.getPeriod() == Period && effect.getType() != EffectType.ONCE)
                {
                    effect.applyEffect(this, triplet.getScale());

                    reloadScreen(500);
                }
           }
        }
        this.selectedCharacterIndex = index;
        this.reloadScreen(0);
    }

    public void reloadScreen( int time)
    {

        Printer.clearTerminal();
        Printer.printStartFightScreen(this);
        Printer.sleep(time);
    }

    private void turnPassed()
    {

        for (Character character: this.activeCharacters)
            character.turnPassed();
        for (Character character: this.opposingCharacters)
            character.turnPassed();
    }

    private void removeEffects()
    {
        int index = this.selectedCharacterIndex;
        for ( int i=0; i < activeCharacters.size();i++)
        {
            this.selectedCharacterIndex = i;
            List<Triplet> status = activeCharacters.get(i).getStatus();
            List<Integer> to_remove = new ArrayList<>();
            for (int j=0;j<status.size();j++)
            {
                Triplet triplet = status.get(j);
                Effect effect = prepareGame.getEffect(triplet.getFunctionName());
                if (triplet.getDuration() == 0)
                {
                    effect.removeEffect(this, triplet.getScale());
                    to_remove.add(j);
                }
            }
            for (int j = to_remove.size()-1; j >= 0; j--)
            {
                int index_to_remove = to_remove.get(j);
               status.remove(index_to_remove);
            }
            activeCharacters.get(i).setStatus(status);
        }
        this.selectedCharacterIndex = index;
//        this.reloadScreen(0);
    }

    public void setSelectedCharacterIndex(int index)
    {
        this.selectedCharacterIndex = index;
    }

    public int getSelectedCharacterIndex()
    {
        return this.selectedCharacterIndex;
    }

    public void setSelectedOpposingCharacterIndex(int index)
    {
        this.selectedOpposingCharacterIndex = index;
    }
    public int getSelectedOpposingCharacterIndex()
    {
        return this.selectedOpposingCharacterIndex;
    }


    public List<Character> getPlayerCharacters()
    {
        List<Character> playerCharacters = new ArrayList<>();
        for (Character character : this.playerCharacters) {
            if (character instanceof Playable)
                playerCharacters.add(new Playable((Playable) character));
            else if (character instanceof NonPlayable)
                playerCharacters.add(new NonPlayable((NonPlayable) character));
        }
        return  playerCharacters;
    }
    public List<Character> getEnemyCharacters()
    {
        List<Character> enemyCharacters = new ArrayList<>();
        for (Character character : this.enemyCharacters) {
            if (character instanceof Playable)
                enemyCharacters.add(new Playable((Playable) character));
            else if (character instanceof NonPlayable)
                enemyCharacters.add(new NonPlayable((NonPlayable) character));
        }
        return enemyCharacters;
    }


    public int getTurn()
    {
        return this.turnCount;
    }
}
