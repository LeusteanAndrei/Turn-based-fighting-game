package Classes.dbConnection;

import Classes.gameClasses.Effect;
import Classes.gameClasses.Skill;
import preparation.PrepareGame;
import utilities.Csv;
import utilities.Triplet;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SkillConnection implements CRUD<Skill>{


    private static final String getSkillsSQL = "SELECT * FROM skill";
    private static final String getSkillEffectsSQL = "SELECT * FROM skill_effect WHERE id_skill = ?";
    private static final String deleteSkillSQL = "DELETE FROM skill WHERE id = ?";
    private static final StringBuilder updateSkillSQL = new StringBuilder
                            ("UPDATE skill SET name = %s, description = %s, cooldown = %d WHERE id = %d");
    private static final String getSkillSQL  = "SELECT * FROM skill WHERE id = ?";
    private static final String removeEffectSQL = "DELETE FROM skill_effect WHERE id_skill = ? AND id_effect = ?";

    private static final String addEffectSQL = "INSERT INTO skill_effect (id_skill, id_effect, scale, duration) VALUES (?, ?, ?, ?)";

    @Override
    public List<Skill> read() {
        try
        {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(getSkillsSQL);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Skill> skills = new ArrayList<>();

            while (resultSet.next())
            {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                int cooldown = resultSet.getInt("cooldown");

                Skill skill = new Skill();
                skill.setId(id);
                skill.setName(name);
                skill.setDescription(description);
                skill.setCooldown(cooldown);

                PreparedStatement preparedStatement1 = DbConnection.getConnection().prepareStatement(getSkillEffectsSQL);
                preparedStatement1.setInt(1, id);
                ResultSet resultSet1 = preparedStatement1.executeQuery();

                List<Triplet> effects = new ArrayList<>();
                while (resultSet1.next())
                {
                    float scale = resultSet1.getFloat("scale");
                    int duration = resultSet1.getInt("duration");
                    int id_effect = resultSet1.getInt("id_effect");
                    String functionName = (new EffectConnection()).get(id_effect).getFunctionName();

                    Triplet triplet = new Triplet(duration, scale, functionName);
                    effects.add(triplet);
                }

                skill.setEffects(effects);
                skills.add(skill);
            }

            String data = "Read all skills from the database";
            Csv.write(data);
            return skills;

        }
        catch (SQLException e)
        {
            throw new RuntimeException();
        }
    }

    public void removeEffect(int effect_id, int skill_id)
    {
        try
        {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(removeEffectSQL);
            preparedStatement.setInt(1, skill_id);
            preparedStatement.setInt(2, effect_id);
            preparedStatement.executeUpdate();

            String data = "Removed effect with id: " + effect_id  + " from skill with id: " + skill_id;
            Csv.write(data);

        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void addEffect(int skill_id, Triplet triplet) {
        try
        {
            PrepareGame prepareGame = PrepareGame.getInstance();
            Effect effect = prepareGame.getEffect(triplet.getFunctionName());
            int id_effect = effect.getId();

            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(addEffectSQL);
            preparedStatement.setInt(1, skill_id);
            preparedStatement.setInt(2, id_effect);
            preparedStatement.setFloat(3, triplet.getScale());
            preparedStatement.setInt(4, triplet.getDuration());
            preparedStatement.executeUpdate();


            String data = "Added effect with id: " + id_effect + " to skill with id: " + skill_id;
            Csv.write(data);

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
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(deleteSkillSQL);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

            String data = "Deleted skill with id: " + id;
            Csv.write(data);


        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int add(Skill skill) {
        try
        {
            String name = skill.getName();
            String description = skill.getDescription();
            int cooldown = skill.getCooldown();

            String addSkillSQL = "INSERT INTO skill (name, description, cooldown) VALUES ('" + name + "', '" +
                        description + "', " + cooldown + ")";

            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(addSkillSQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next())
            {
                int id_skill = resultSet.getInt(1);
                for (Triplet triplet : skill.getEffects())
                {
                    PrepareGame prepareGame = PrepareGame.getInstance();
                    Effect effect = prepareGame.getEffect(triplet.getFunctionName());

                    int id_effect = effect.getId();
                    String addEffectSQL = "INSERT INTO skill_effect (id_skill, id_effect, scale, duration) VALUES (" + id_skill + ", " +
                            id_effect + ", " + triplet.getScale() + ", " + triplet.getDuration() + ")";
                    PreparedStatement preparedStatement1 = DbConnection.getConnection().prepareStatement(addEffectSQL);
                    preparedStatement1.executeUpdate();
                }
                String data = "Added skill with id: " + id_skill;
                Csv.write(data);
                return id_skill;

            }
            return -1;
        }
        catch ( SQLException e)
        {
            throw new RuntimeException(e);
        }

    }

    public Skill get(int id)
    {
        try {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(getSkillSQL);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            Skill skill = new Skill();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                int cooldown = resultSet.getInt("cooldown");

                skill.setId(id);
                skill.setName(name);
                skill.setDescription(description);
                skill.setCooldown(cooldown);

                PreparedStatement preparedStatement1 = DbConnection.getConnection().prepareStatement(getSkillEffectsSQL);
                preparedStatement1.setInt(1, id);
                ResultSet resultSet1 = preparedStatement1.executeQuery();

                List<Triplet> effects = new ArrayList<>();
                while (resultSet1.next())
                {
                    float scale = resultSet1.getFloat("scale");
                    int duration = resultSet1.getInt("duration");
                    int id_effect = resultSet1.getInt("id_effect");
                    String functionName = (new EffectConnection()).get(id_effect).getFunctionName();

                    Triplet triplet = new Triplet(duration, scale, functionName);
                    effects.add(triplet);
                }

                skill.setEffects(effects);

            }
            String data = "Read skill with id: " + id;
            Csv.write(data);
            return skill;

        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

    }

    public static class Updater {
        private int id = -1;
        private StringBuilder updateString = new StringBuilder();

        public Updater(int id) {
            this.id = id;
        }

        public Updater name(String name) {
            updateString.append("name = '").append(name).append("', ");
            return this;
        }
        public Updater description(String description) {
            updateString.append("description = '").append(description).append("', ");
            return this;
        }
        public Updater cooldown(int cooldown) {
            updateString.append("cooldown = ").append(cooldown).append(", ");
            return this;
        }

        public Updater effectScale(int effectId, float scale)
        {
            String updateSQL = "UPDATE skill_effect SET scale = " + scale + " WHERE id_skill = " + id + " AND id_effect = " + effectId;
            try {
                PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(updateSQL);
                preparedStatement.executeUpdate();
                String data = "Updated effect scale for skill with id: " + id + " and effect with id: " + effectId;
                Csv.write(data);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return this;
        }
        public Updater effectDuration(int effectId, int duration)
        {
            String updateSQL = "UPDATE skill_effect SET duration = " + duration + " WHERE id_skill = " + id + " AND id_effect = " + effectId;
            try {
                PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(updateSQL);
                preparedStatement.executeUpdate();
                String data = "Updated effect duration for skill with id: " + id + " and effect with id: " + effectId;
                Csv.write(data);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return this;
        }

        public void update()
        {
            if (updateString.length() == 0) {
                return;
            }
            updateString.delete(updateString.length() - 2, updateString.length());
            String updateSQL = " UPDATE skill SET " + updateString.toString() + " WHERE id = " + id;
            try {
                PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(updateSQL);
                preparedStatement.executeUpdate();
                String data = "Updated skill with id: " + id ;
                Csv.write(data);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


    }

}
