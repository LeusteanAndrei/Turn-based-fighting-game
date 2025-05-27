import Classes.dbConnection.*;
import utilities.Service;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {


    public static void main(String[] args) {

        startConnection();
        startGame();
        cleanUpDb();

//        List<Skill> all = new SkillConnection().read();
//        Playable playable = new CharacterConnection().getPlayable(4);
//        List<Skill> playerSkills = playable.getSkills();
//        System.out.println(playerSkills.get(0));
//        System.out.println(all.get(2));
//        System.out.println(playerSkills.get(0).equals(all.get(2)) );
//        for (Skill skill : playerSkills) {
//            System.out.println(skill.toString());
//        }
//        System.out.println("--------------------------------------------------");
//        for (Skill skill : all) {
//            System.out.println(skill.toString());
//        }

    }


    private static void startConnection()
    {
        DbConnection dbConnection = new DbConnection();
        if (dbConnection.getConnection() == null)
        {
            System.out.println("Could not connect to the database.");
            return;
        }
    }
    private static void startGame()
    {
        Service service = Service.getInstance();
        service.start_game();
    }
    private static void cleanUpDb()
    {
        new StatConnection().cleanUp();
        new CharacterConnection().cleanUpHealth();
    }
}

