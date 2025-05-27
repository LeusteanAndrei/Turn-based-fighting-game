package Classes.dbConnection;

import Classes.gameClasses.Character;
import Classes.gameClasses.Playable;
import Classes.gameClasses.User;
import utilities.Csv;
import utilities.Pair;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserConnection implements CRUD<User> {

    public static final String getUserIdSQL = "SELECT * FROM user WHERE id = ?";
    public static final String getUsersSQL = "SELECT * FROM user";
    public static final String deleteUserSQL = "DELETE FROM user WHERE id = ?";
    public static final String addUserSQL = "INSERT INTO user (username, password) VALUES (?, ?)";
    public static final String addUserCharacterSQl = "INSERT INTO user_characters (id_user, id_character, exp, relic) VALUES (?, ?, ?, ?)";
    public static final String getUserCharactersSQL = "SELECT * FROM user_characters WHERE id_user = ?";
    public static final String removeCharacterSQL = "DELETE FROM user_characters WHERE id_user = ? AND id_character = ?";

    public static final String updateUserSQL = "UPDATE user SET username = ?, password = ? WHERE id = ?";

    public static final String getEquippedCharacterSQL = "SELECT * FROM user_characters WHERE id_user = ? AND relic = ?";

    @Override
    public List<User> read()
    {
        try
        {
            List<User> users = new ArrayList<>();
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(getUsersSQL);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                int id = resultSet.getInt("id");

                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                user.setId(id);

                PreparedStatement preparedStatement1 = DbConnection.getConnection().prepareStatement(getUserCharactersSQL);
                preparedStatement1.setInt(1, id);

                ResultSet resultSet1 = preparedStatement1.executeQuery();

                List<Pair> characters = new ArrayList<>();
                while(resultSet1.next())
                {
                    int characterId = resultSet1.getInt("id_character");
                    int exp = resultSet1.getInt("exp");
                    int relic = resultSet1.getInt("relic");

                    CharacterConnection characterConnection = new CharacterConnection();
                    Playable character = characterConnection.getPlayable(characterId);
                    character.setExp(exp);
                    character.setRelic(relic);

                    Pair pair = new Pair( character, relic);
                    characters.add(pair);
                }
                user.setCharacters(characters);

                users.add(user);
            }


            String data = "Read all users from the database ";
            Csv.write(data);

            return users;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

    }


    @Override
    public void delete(int id) {

        try
        {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(deleteUserSQL);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

            String data = "Deleted user with id: " + id;
            Csv.write(data);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public int addCharacter(int user_id, int exp, int relic, Playable character)
    {
        int character_id = character.getId();
        if (character_id == -1)
        {
            CharacterConnection characterConnection = new CharacterConnection();
            character_id = characterConnection.add(character);
        }
        try
        {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(addUserCharacterSQl);
            preparedStatement.setInt(1, user_id);
            preparedStatement.setInt(2, character_id);
            preparedStatement.setInt(3, exp);
            preparedStatement.setInt(4, relic);
            preparedStatement.executeUpdate();

            String data = "Added character with id: " + character_id + " to user with id: " + user_id;
            Csv.write(data);
            return character_id;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public int addCharacter(int user_id, int exp, int relic, int character_id)
    {
        try
        {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(addUserCharacterSQl);
            preparedStatement.setInt(1, user_id);
            preparedStatement.setInt(2, character_id);
            preparedStatement.setInt(3, exp);
            preparedStatement.setInt(4, relic);
            preparedStatement.executeUpdate();
            String data = "Added character with id: " + character_id + " to user with id: " + user_id;
            return character_id;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void removeCharacter(int user_id, int character_id)
    {

        try
        {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(removeCharacterSQL);
            preparedStatement.setInt(1, user_id);
            preparedStatement.setInt(2, character_id);
            preparedStatement.executeUpdate();
            String data = "Removed character with id: " + character_id + " from user with id: " + user_id;
            Csv.write(data);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }


    public int getEquippedCharacter(int user_id, int relic_id) {

        try
        {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(getEquippedCharacterSQL);
            preparedStatement.setInt(1, user_id);
            preparedStatement.setInt(2, relic_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            String data =  "Get equipped character with relic id: " + relic_id + " for user with id: " + user_id;
            Csv.write(data);
            if (resultSet.next())
            {

                return resultSet.getInt("id_character");
            }
            return -1;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }


    @Override
    public int add(User user) {

        try
        {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(addUserSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next())
            {
                int id = resultSet.getInt(1);
                user.setId(id);

                List<Pair> Characters = user.getCharacters();
                for (Pair pair : Characters)
                {
                    int relic = pair.getRelic();
                    int exp = pair.getCharacter().getExp();
                    CharacterConnection characterConnection = new CharacterConnection();
                    int characterId = characterConnection.add((Character) pair.getCharacter());
                    PreparedStatement preparedStatement1 = DbConnection.getConnection().prepareStatement(addUserCharacterSQl);
                    preparedStatement1.setInt(1, id);
                    preparedStatement1.setInt(2, characterId);
                    preparedStatement1.setInt(3, exp);
                    preparedStatement1.setInt(4, relic);
                    preparedStatement1.executeUpdate();

                }

                String data = "Added user with id: " + id;
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



    public static class Updater
    {
        private StringBuilder updateString = new StringBuilder();
        private int id = -1;

        public Updater(int id){
            this.id = id;
        }


        public Updater username(String username)
        {
            updateString.append(String.format("username = '%s'", username) ).append(", ");
            return this;
        }
        public Updater password(String password)
        {
            updateString.append(String.format("password = '%s'", password) ).append(", ");
            return this;
        }

        public Updater relic(int character_id, int relic_id)
        {
            String sql = "UPDATE user_characters SET relic = " + relic_id + " WHERE id_character = " + character_id;
            try
            {
                PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(sql);
                preparedStatement.executeUpdate();
                String data = "Updated user_character with id_user " + id + " and id_character " + character_id;
                Csv.write(data);
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
            return this;

        }
        public void update()
        {
            if (updateString.length() == 0)
            {
                return;
            }
            updateString.delete(updateString.length() - 2, updateString.length());
            String updateSQL = "UPDATE user SET " + updateString.toString() + " WHERE id = " + id;
            try
            {
                updateString.append(" WHERE id = " + id);

                PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(updateSQL);
                preparedStatement.executeUpdate();
                String data = "Updated user with id: " + id;
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
