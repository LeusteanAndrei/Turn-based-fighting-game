package Classes.GameFunctionality;

import Classes.*;
import Classes.Character;
import Enumerations.EffectType;
import Enumerations.GamePeriod;
import Preparation.PrepareGame;
import Utilities.Triplet;

import java.util.ArrayList;
import java.util.List;

public class GameState {

     PrepareGame prepareGame = PrepareGame.getInstance();
     User user = User.getInstance();
     List<Classes.Character> activeCharacters;
     List<Classes.Character> opposingCharacters;

     List<Integer> hasHitCount;
     List<Integer> beenHitCount;

     Integer selectedCharacterIndex = 0;
     Integer selectedOpposingCharacterIndex = 0;
     Integer turnCount = 0;

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

        this.hasHitCount = new ArrayList<>( activeCharacters.size() );
        this.beenHitCount = new ArrayList<>( activeCharacters.size() );
        for (int i = 0; i < activeCharacters.size(); i++)
        {
            this.hasHitCount.set(i, 0);
            this.beenHitCount.set(i, 0);
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

    }

    public void enemyTurn()
    {

    }

    public void startBattle(Battle battle)
    {

        this.setOpposingCharacters( battle.getEnemies() );

        applyEffects(GamePeriod.START_OF_BATTLE);
        swap();
        applyEffects(GamePeriod.START_OF_BATTLE);
        swap();

        while(!opposingCharacters.isEmpty() || !activeCharacters.isEmpty())
        {
            applyEffects(GamePeriod.START_OF_TURN);
            playerTurn();
            applyEffects(GamePeriod.END_OF_TURN);

            swap();

            applyEffects(GamePeriod.START_OF_TURN);
            enemyTurn();
            applyEffects(GamePeriod.END_OF_TURN);

            swap();
            this.turnCount++;

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



    public List<Classes.Character> getActiveCharacters()
    {
        List<Classes.Character> activeCharacters = new ArrayList<>();
        for (Classes.Character character : this.activeCharacters) {
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

    public void setActiveCharacters(List< ? extends Classes.Character> currentCharacters)
    {
        this.activeCharacters = new ArrayList<>();
        for ( Classes.Character character : currentCharacters) {
            if (character instanceof Playable)
                this.activeCharacters.add(new Playable((Playable) character));
            else if (character instanceof NonPlayable)
                this.activeCharacters.add(new NonPlayable((NonPlayable) character));
        }
    }

    public void setOpposingCharacters(List< ? extends Classes.Character> opposingCharacters)
    {
        this.opposingCharacters = new ArrayList<>();
        for ( Classes.Character character : opposingCharacters) {
            if (character instanceof Playable)
                this.opposingCharacters.add(new Playable((Playable) character));
            else if (character instanceof NonPlayable)
                this.opposingCharacters.add(new NonPlayable((NonPlayable) character));
        }
    }

    public List<Classes.Character> getOpposingCharacters()
    {
        List<Classes.Character> opposingCharacters = new ArrayList<>();
        for (Classes.Character character : this.opposingCharacters) {
            if (character instanceof Playable)
                opposingCharacters.add(new Playable((Playable) character));
            else if (character instanceof NonPlayable)
                opposingCharacters.add(new NonPlayable((NonPlayable) character));
        }
        return opposingCharacters;
    }

    public void applySkill(Skill skill)
    {

        for(Triplet triplet : skill.getEffects()) {

            Character activeCharacter = this.activeCharacters.get(this.selectedCharacterIndex);

            if (triplet.getDuration() != -1) {

                List<Triplet> activeCharacterStatus = activeCharacter.getStatus();

                activeCharacterStatus.add(triplet);
                activeCharacter.setStatus(activeCharacterStatus);
            }

            Effect effect = prepareGame.getEffect(triplet.getFunctionName());

            if (effect.getType() == EffectType.ONCE)
            {
                effect.applyEffect(this, triplet.getScale());
            }

        }
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

            if (duration != -1) {
                List<Triplet> status = playable.getStatus();
                status.add(triplet);
                playable.setStatus(status);
            }

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
        for (int i=0;i<activeCharacters.size();i++) {
            Character character = this.activeCharacters.get(i);
            this.selectedCharacterIndex = i;
            for(Triplet triplet: character.getStatus())
           {
                Effect effect = prepareGame.getEffect(triplet.getFunctionName());

                if(effect.getPeriod() == Period && effect.getType() != EffectType.ONCE)
                {
                    effect.applyEffect(this, triplet.getScale());
                }
           }
        }
    }

    private void removeEffects()
    {
        List<Integer> to_remove = new ArrayList<>();
        for ( int i=0; i < activeCharacters.size();i++)
        {
            this.selectedCharacterIndex = i;
            for(Triplet triplet: activeCharacters.get(i).getStatus())
            {
                if(triplet.getDuration() == 0)
                {
                    Effect effect = prepareGame.getEffect(triplet.getFunctionName());
                    effect.removeEffect(this, triplet.getScale());
                    to_remove.add(i);
                }
            }
            List<Triplet> status = activeCharacters.get(i).getStatus();
            for (int j=to_remove.size()-1;j>=0;j--)
            {
                status.remove(to_remove.get(j));
            }
            activeCharacters.get(i).setStatus(status);
        }
    }


    public void setSelectedCharacterIndex(int index)
    {
        this.selectedCharacterIndex = index;
    }


    public int getSelectedCharacterIndex()
    {
        return this.selectedCharacterIndex;
    }

}
