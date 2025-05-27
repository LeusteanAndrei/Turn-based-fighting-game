package Classes.gameClasses;

import preparation.PrepareGame;

import java.util.ArrayList;
import java.util.List;

public class Battle {

    private Stage stage ;
    private List<String> enemies;


    public Battle() {
        this.stage = new Stage();
        this.enemies = new ArrayList<>();
    }

    public Battle(Battle battle)
    {
        this.stage = new Stage(battle.stage);

        this.enemies = new ArrayList<>();
        for (String enemy : battle.enemies)
            this.enemies.add(enemy);
    }

    public List<NonPlayable> getEnemies() {
        PrepareGame prepareGame = PrepareGame.getInstance();

        return prepareGame.getEnemiesByName(enemies);
    }

    public Stage getStage()
    {
        return new Stage(this.stage);
    }


    public void setEnemies(List<String> enemies)
    {
        this.enemies = new ArrayList<>();
        for (String enemy : enemies)
            this.enemies.add(enemy);
    }


    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Battle at stage: ").append(stage.getName())
          .append("\n   Enemies: ");
        for (String enemy : enemies)
            s.append(enemy).append(", ");
        return s.toString();
    }
}
