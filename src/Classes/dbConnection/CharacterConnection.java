package Classes.dbConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import Classes.gameClasses.*;
import Classes.gameClasses.Character;
import enumerations.CharacterType;
import utilities.Csv;
import utilities.Triplet;

public class CharacterConnection implements CRUD<Character> {

    private static final String deleteCharacterSQL = "DELETE FROM `character` WHERE id = ?";
    private static final String addCharacterSQL = " INSERT INTO `character` (name, id_stats, playable) VALUES (?, ?, ?)";
    private static final String getCharacterSQL = "SELECT * FROM `character`";
    private static final String updateCharacterSQL = "UPDATE `character` SET name = ?, id_stats = ? WHERE id = ?";
    private static final String getAttacksSQL = "SELECT id_attack FROM character_attack WHERE id_character = ?";
    private static final String deleteAttacksSQL = "DELETE FROM character_attack WHERE id_character = ?";
    private static final String addAttackSQL = "INSERT INTO character_attack (id_character, id_attack) VALUES (?, ?)";
    private static final String getSingleCharacter = "SELECT * FROM `character` WHERE id = ?";

    private static final String getHighestIndex = "SELECT MAX(id) FROM `character`";

    private static final String addPlayable = "INSERT INTO playable_skills (id_playable, id_skill) VALUES (?, ?)";
    private static final String getPlayable = "SELECT * FROM `character` WHERE playable = 1";
    private static final String getSkills = "SELECT DISTINCT id_skill FROM playable_skills WHERE id_playable = ?";
    private static final String removeSkills = "DELETE FROM playable_skills WHERE id_playable = ?";

    private static final String addNonPlayable = "INSERT INTO nonplayable (id_character, max_charge) VALUES (?, ?)";
    private static final String addNonPlayableHealth = "INSERT INTO nonplayable_health (id_nonplayable,id_health) VALUES (?, ?)";
    private static final String getIdenticHealth = "SELECT * FROM health WHERE health = ? AND bar_number = ?";
    private static final String addHealth = "INSERT INTO health (health, bar_number) VALUES (?, ?)";
    private static final String addNonPlayableEffect = "INSERT INTO nonplayable_effect (id_nonplayable, id_effect, scale, duration) VALUES (?, ?, ?, ?)";


    private static final String getNonplayable = "SELECT * FROM nonplayable";
//    public static final String getNonplayable = "SELECT * FROM `character` WHERE playable = 0";
    public static final String getNonPlayableEffects = " Select * from nonplayable_effect where id_nonplayable = ?";
    public static final String getNonPlayableHealth = "SELECT * FROM nonplayable_health WHERE id_nonplayable = ?";
    public static final String getHealth = "SELECT * FROM health WHERE id_health = ? Order by bar_number";


    public static final String getNonplayableCharacterId = "SELECT * FROM nonplayable WHERE id = ?";
    public static final String deleteNonplayableSQL = "DELETE FROM nonplayable WHERE id = ?";
    public static final String deletePlayableSQL = "DELETE FROM playable_skills WHERE id_playable = ?";
    public static final String removeSkill  = "DELETE FROM playable_skills WHERE id_playable = ? AND id_skill = ?";


    public static final String removeHealth = "Delete from health h\n" +
            "where not exists(\n" +
            "\tselect * from nonplayable_health np where np.id_health = h.id_health\n" +
            "    )";


    public void cleanUpHealth()
    {
        try
        {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(removeHealth);
            preparedStatement.executeUpdate();

            String data = "Cleaned up health table";
            Csv.write(data);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void removeSkill(int playableId, int skillId) {

        try
        {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(removeSkill);
            preparedStatement.setInt(1, playableId);
            preparedStatement.setInt(2, skillId);
            preparedStatement.executeUpdate();

            String data = "Removed skill with id: " + skillId + " from playable character with id: " + playableId;
            Csv.write(data);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }


    public void deletePlayable(int playableId) {

        try
        {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(deletePlayableSQL);
            preparedStatement.setInt(1, playableId);
            preparedStatement.executeUpdate();

            new CharacterConnection.Updater(playableId).playable(false).update();

            String data = "Deleted playable character with id: " + playableId;
            Csv.write(data);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void deleteNonPlayable(int nonPlayableId) {

        try
        {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(deleteNonplayableSQL);
            preparedStatement.setInt(1, nonPlayableId);
            preparedStatement.executeUpdate();

            String data = "Deleted non-playable character with id: " + nonPlayableId;
            Csv.write(data);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }



    public List<Attack> getAttacks(int character_id) {
        try {

            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(getAttacksSQL);
            preparedStatement.setInt(1, character_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Attack> attacks = new ArrayList<>();

            while (resultSet.next()) {

                int id = resultSet.getInt("id_attack");

                Attack attack = (new AttackConnection()).get(id);
                attacks.add(attack);
            }

            String data = "Read all attacks for character with id " + character_id ;
            Csv.write(data);
            return attacks;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Character> read() {

        try {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(getCharacterSQL);
            var resultSet = preparedStatement.executeQuery();

            List<Character> characters = new java.util.ArrayList<>();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int statsId = resultSet.getInt("id_stats");
                Stats stats = (new StatConnection()).get(statsId);


                List<Attack> attacks = getAttacks(id);
                Character character = new Character(name, stats);
                character.setId(id);
                character.setAttacks(attacks);

                characters.add(character);
            }

            String data = "Read all characters from database";
            Csv.write(data);

            return characters;

        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public void removeAttacks(int character_id) {
        try {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(deleteAttacksSQL);
            preparedStatement.setInt(1, character_id);
            preparedStatement.executeUpdate();

            String data = "Removed all attacks for character with id " + character_id ;
            Csv.write(data);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void update(int id, Character character) {

        try {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(updateCharacterSQL);
            preparedStatement.setString(1, character.getName());
            preparedStatement.setInt(2, character.getCurrent_stats().getId());
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();

            List<Attack> attacks = character.getAttacks();

            removeAttacks(id);

            for (Attack attack : attacks) {
                PreparedStatement preparedStatement1 = DbConnection.getConnection().prepareStatement(addAttackSQL);
                preparedStatement1.setInt(1, id);
                preparedStatement1.setInt(2, attack.getId());
                preparedStatement1.executeUpdate();
            }
            String data = "Updated character with id " + id ;
            Csv.write(data);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(int id) {

        try {

            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(deleteCharacterSQL);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

            String data = "Deleted character with id " + id ;
            Csv.write(data);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public int add(Character character) {

        List<Character> characters = read();
        for (Character c : characters) {
            if (c.equals(character) && c.getId() != -1) {
                return c.getId();
            }
        }


        try {


            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(addCharacterSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, character.getName());
            int stat_id =  character.getCurrent_stats().getId();
            if (stat_id == -1) {
                stat_id = (new StatConnection()).add(character.getCurrent_stats());
            }

            preparedStatement.setInt(2,stat_id);

            int playable = 0;
            if (character instanceof Playable) {
                playable = 1;
            }
            preparedStatement.setInt(3, playable);

            preparedStatement.executeUpdate();


            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                character.setId(id);

                List<Attack> attacks = character.getAttacks();
                for (Attack attack : attacks) {
                    PreparedStatement preparedStatement1 = DbConnection.getConnection().prepareStatement(addAttackSQL);

                    preparedStatement1.setInt(1, id);
                    preparedStatement1.setInt(2, attack.getId());
                    preparedStatement1.executeUpdate();
                }

                String data = "Added character with id " + id ;
                Csv.write(data);
                return id;
            }

            return -1;

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }


    }


    public Character get(int id)
    {
        try {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(getSingleCharacter);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                int statsId = resultSet.getInt("id_stats");
                Stats stats = (new StatConnection()).get(statsId);

                Character character = new Character(name, stats);
                character.setId(id);

                List<Attack> attacks = getAttacks(id);
                character.setAttacks(attacks);
                String data = "Read character with id " + id ;
                Csv.write(data);

                return character;
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException();
        }

    }


    public void addSkill(int character_id, int skill_id)
    {
        try
        {
            String SqlFind = "SELECT * FROM playable_skills WHERE id_playable = ? AND id_skill = ?";
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(SqlFind, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, character_id);
            preparedStatement.setInt(2, skill_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next())
            {
                PreparedStatement preparedStatement1 = DbConnection.getConnection().prepareStatement(addPlayable);
                preparedStatement1.setInt(1, character_id);
                preparedStatement1.setInt(2, skill_id);
                preparedStatement1.executeUpdate();
            }
            String data = "Added skill with id " + skill_id + " for character with id " + character_id ;
            Csv.write(data);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

    }

    public List<Skill> getSkills(int character_id)
    {

        try {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(getSkills);
            preparedStatement.setInt(1, character_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Skill> skills = new ArrayList<>();

            while (resultSet.next()) {
                int id = resultSet.getInt("id_skill");
                Skill skill = (new SkillConnection()).get(id);
                skills.add(skill);
            }
            String data = "Read all skills for character with id " + character_id ;
            Csv.write(data);

            return skills;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeSkills(int character_id)
    {
        try {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(removeSkills);
            preparedStatement.setInt(1, character_id);
            preparedStatement.executeUpdate();

            String data = "Removed all skills for character with id " + character_id ;
            Csv.write(data);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int add(Playable playable)
    {
            int id = add((Character)playable);
            removeSkills(id);
            for (Skill skill : playable.getSkills())
            {
                addSkill(id, skill.getId());
            }
            String data = "Added playable character with id " + id ;
            Csv.write(data);
            return id;

    }
    public List<Playable> getPlayables()
    {

        try {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(getPlayable);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Playable> playables = new ArrayList<>();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Character character = get(id);
                Playable playable = new Playable(character);
                playable.setSkills(getSkills(id));
                playables.add(playable);
            }

            String data = "Read all playables from database";
            Csv.write(data);
            return playables;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Playable getPlayable(int id)
    {
        List<Playable> playables = getPlayables();
        for (Playable playable : playables)
        {
            if (playable.getId() == id)
            {
                String data = "Read playable character with id " + id ;
                Csv.write(data);
                return playable;
            }
        }

        return null;
    }

    public void add(NonPlayable nonPlayable)
    {
        int id = add((Character)nonPlayable);
        try
        {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(addNonPlayable, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, nonPlayable.getMaxCharge());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next())
            {
                id = resultSet.getInt(1);
                nonPlayable.setId(id);
            }

            for (Triplet effectTripet : nonPlayable.getEffects())
            {

                String effectName = effectTripet.getFunctionName();
                Effect effect = (new EffectConnection()).get(effectName);
                PreparedStatement preparedStatement1 = DbConnection.getConnection().prepareStatement(addNonPlayableEffect);
                preparedStatement1.setInt(1, id);
                preparedStatement1.setInt(2, effect.getId());
                preparedStatement1.setFloat(3, effectTripet.getScale());
                preparedStatement1.setInt(4, effectTripet.getDuration());
                preparedStatement1.executeUpdate();
            }

            int bar_number=1;
            for (int health : nonPlayable.getHealth_bars())
            {
                PreparedStatement getBarStatement = DbConnection.getConnection().prepareStatement(getIdenticHealth, PreparedStatement.RETURN_GENERATED_KEYS);
                getBarStatement.setInt(1, health);
                getBarStatement.setInt(2, bar_number);
                resultSet = getBarStatement.executeQuery();
                if (resultSet.next())
                {
                    int id_health = resultSet.getInt("id_health");
                    PreparedStatement preparedStatement1 = DbConnection.getConnection().prepareStatement(addNonPlayableHealth);
                    preparedStatement1.setInt(1, id);
                    preparedStatement1.setInt(2, id_health);
                    preparedStatement1.executeUpdate();
                }
                else
                {
                    PreparedStatement preparedStatement1 = DbConnection.getConnection().prepareStatement(addHealth, PreparedStatement.RETURN_GENERATED_KEYS);
                    preparedStatement1.setInt(1, health);
                    preparedStatement1.setInt(2, bar_number);
                    preparedStatement1.executeUpdate();
                    ResultSet resultSet1 = preparedStatement1.getGeneratedKeys();
                    if (resultSet1.next())
                    {
                        int id_health = resultSet1.getInt(1);
                        PreparedStatement preparedStatement2 = DbConnection.getConnection().prepareStatement(addNonPlayableHealth);
                        preparedStatement2.setInt(1, id);
                        preparedStatement2.setInt(2, id_health);
                        preparedStatement2.executeUpdate();
                    }
                }

                bar_number ++;
            }


            String data = "Added non-playable character with id " + id ;
            Csv.write(data);



        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

    }


    public int getNonplayableId(int nonplayable_id)
    {
        try
        {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(getNonplayableCharacterId);
            preparedStatement.setInt(1, nonplayable_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
            {
                int id = resultSet.getInt("id_character");
                String data = "Read non-playable character with id " + id ;
                Csv.write(data);
                return id;
            }
            return -1;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }


    public List<NonPlayable> getNonplayable()
    {
        try
        {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(getNonplayable);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<NonPlayable> nonPlayables = new ArrayList<>();
            while (resultSet.next())
            {
                int id = resultSet.getInt("id_character");
                int non_playable_id = resultSet.getInt("id");
                int max_charge = resultSet.getInt("max_charge");
                NonPlayable nonPlayable = new NonPlayable(get(id));
                nonPlayable.setMaxCharge(max_charge);
                nonPlayable.setId(non_playable_id);

                PreparedStatement preparedStatement1 = DbConnection.getConnection().prepareStatement(getNonPlayableEffects);
                preparedStatement1.setInt(1, non_playable_id);
                ResultSet resultSet1 = preparedStatement1.executeQuery();

                List<Triplet> effects = new ArrayList<>();
                while (resultSet1.next())
                {
                    int id_effect = resultSet1.getInt("id_effect");
                    int duration = resultSet1.getInt("duration");
                    float scale = resultSet1.getFloat("scale");
                    Effect effect = (new EffectConnection()).get(id_effect);
                    effects.add(new Triplet(duration, scale, effect.getFunctionName()));
                }



                nonPlayable.setEffects(effects);

                PreparedStatement preparedStatement2 = DbConnection.getConnection().prepareStatement(getNonPlayableHealth);
                preparedStatement2.setInt(1, non_playable_id);
                ResultSet resultSet2 = preparedStatement2.executeQuery();
                Stack<Integer> health_bars = new Stack<>();

                while (resultSet2.next())
                {
                    int id_health = resultSet2.getInt("id_health");
                    PreparedStatement preparedStatement3 = DbConnection.getConnection().prepareStatement(getHealth);
                    preparedStatement3.setInt(1, id_health);
                    ResultSet resultSet3 = preparedStatement3.executeQuery();
                    if (resultSet3.next())
                    {
                        int health = resultSet3.getInt("health");
                        health_bars.push(health);
                    }
                }
                nonPlayable.setHealth_bars(health_bars);
                nonPlayables.add(nonPlayable);
            }
            String data = "Read all non-playable characters from database";
            Csv.write(data);
            return nonPlayables;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

    }



    public static class Updater
    {
        private int id = -1;
        private StringBuilder sql = new StringBuilder();
        private int stat_id = -1;

        public Updater(int id)
        {
            this.id = id;
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = DbConnection.getConnection().prepareStatement(getSingleCharacter);
                preparedStatement.setInt(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    stat_id = resultSet.getInt("id_stats");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }



        public Updater name(String name)
        {
            sql.append(String.format("name = '%s'", name)).append(", ");
            return this;
        }

        public Updater type(CharacterType type)
        {
            sql.append(String.format("type = '%s'", type)).append(", ");
            return this;
        }

        public Updater playable(boolean playable)
        {
            sql.append(String.format("playable = %d", playable ? 1 : 0)).append(", ");
            return this;
        }

        public Updater attack(int attack)
        {
            new StatConnection.Updater(this.stat_id).attack(attack).update();
            return this;
        }
        public Updater health(int health)
        {
            new StatConnection.Updater(this.stat_id).health(health).update();
            return this;
        }
        public Updater damageRes(float damageRes)
        {
            new StatConnection.Updater(this.stat_id).damageRes(damageRes).update();
            return this;
        }
        public Updater critRate(float critRate)
        {
            new StatConnection.Updater(this.stat_id).critRate(critRate).update();
            return this;
        }
        public Updater critDamage(float critDamage)
        {
            new StatConnection.Updater(this.stat_id).critDamage(critDamage).update();
            return this;
        }
        public Updater gain(float gain)
        {
            new StatConnection.Updater(this.stat_id).gain(gain).update();
            return this;
        }

        public void update()
        {
            if( sql.length() == 0) {
                return;
            }
            sql.delete(sql.length() - 2, sql.length());
            String updateSQL = "UPDATE `character` SET " + sql.toString() + " WHERE id = " + id;
            try {
                sql.append(" WHERE id = " + id);
                PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(updateSQL);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
