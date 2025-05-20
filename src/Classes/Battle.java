package Classes;

import Preparation.PrepareGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

}
