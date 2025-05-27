package Classes.dbConnection;

import Classes.gameClasses.Stats;
import utilities.Csv;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatConnection implements CRUD<Stats> {


    private static final String addStatSQL =
            "INSERT INTO stats (attack, health, damageRes, critRate, critDamage, gain) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String getStatsSQL = "SELECT * FROM stats";
    private static final String updateStatSQL = "UPDATE stats SET attack = ?, health = ?, damageRes = ?, critRate = ?, critDamage = ?, gain = ? WHERE id = ?";
    private static final String deleteStatSQL = "DELETE FROM stats WHERE id = ?";
    private static final String getStatSQL = "SELECT * FROM stats WHERE id = ?";

    private static final String cleanUpStatsSQL = "Delete\n" +
            "FROM stats s\n" +
            "WHERE NOT EXISTS (\n" +
            "    SELECT *\n" +
            "    FROM `character` c\n" +
            "    WHERE c.id_stats = s.id\n" +
            ");";

    @Override
    public List<Stats> read() {
        try {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(getStatsSQL);
            var resultSet = preparedStatement.executeQuery();

            List<Stats> statsList = new ArrayList<>();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int attack = resultSet.getInt("attack");
                int health = resultSet.getInt("health");
                float damageRes = resultSet.getFloat("damageRes");
                float critRate = resultSet.getFloat("critRate");
                float critDamage = resultSet.getFloat("critDamage");
                float gain = resultSet.getFloat("gain");

                Stats stats = new Stats(attack, health, damageRes, critRate, critDamage, gain);
                stats.setId(id);

                statsList.add(stats);
            }

            return statsList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(int id, Stats stats) {
        try {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(updateStatSQL);
            preparedStatement.setInt(1, stats.getAttack());
            preparedStatement.setInt(2, stats.getHealth());
            preparedStatement.setFloat(3, stats.getDamageRes());
            preparedStatement.setFloat(4, stats.getCritRate());
            preparedStatement.setFloat(5, stats.getCritDamage());
            preparedStatement.setFloat(6, stats.getGain());
            preparedStatement.setInt(7, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Stats get(int id)
    {
        try {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(getStatSQL);
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int attack = resultSet.getInt("attack");
                int health = resultSet.getInt("health");
                float damageRes = resultSet.getFloat("damageRes");
                float critRate = resultSet.getFloat("critRate");
                float critDamage = resultSet.getFloat("critDamage");
                float gain = resultSet.getFloat("gain");

                Stats stats = new Stats(attack, health, damageRes, critRate, critDamage, gain);
                stats.setId(id);

                return stats;
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public void cleanUp()
    {
        try {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(cleanUpStatsSQL);
            preparedStatement.executeUpdate();

            String data = "Removed unused stats from the database";
            Csv.write(data);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void delete(int id) {

        try {

            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(deleteStatSQL);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public int  add(Stats stats) {
        try
        {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(addStatSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, stats.getAttack());
            preparedStatement.setInt(2, stats.getHealth());
            preparedStatement.setFloat(3, stats.getDamageRes());
            preparedStatement.setFloat(4, stats.getCritRate());
            preparedStatement.setFloat(5, stats.getCritDamage());
            preparedStatement.setFloat(6, stats.getGain());

            preparedStatement.executeUpdate();

            var resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next())
            {
                int id = resultSet.getInt(1);
                stats.setId(id);
                return id;
            }
            return -1;

        }
        catch ( SQLException e)
        {
            throw new RuntimeException(e);
        }

    }


    public static class Updater
    {
        private int id = -1;
        private StringBuilder sql = new StringBuilder();

        public Updater(int id)
        {
            this.id = id;
        }

        public Updater attack(int attack)
        {
            sql.append("attack = ").append(attack).append(", ");
            return this;
        }
        public Updater health(int health)
        {
            sql.append("health = ").append(health).append(", ");
            return this;
        }
        public Updater damageRes(float damageRes)
        {
            sql.append("damageRes = ").append(damageRes).append(", ");
            return this;
        }
        public Updater critRate(float critRate)
        {
            sql.append("critRate = ").append(critRate).append(", ");
            return this;
        }
        public Updater critDamage(float critDamage)
        {
            sql.append("critDamage = ").append(critDamage).append(", ");
            return this;
        }
        public Updater gain(float gain)
        {
            sql.append("gain = ").append(gain).append(", ");
            return this;
        }
        public void update()
        {
            if (sql.length() == 0) {
                return;
            }
            sql.delete(sql.length() - 2, sql.length());
            String updateSQL = "UPDATE stats SET " + sql.toString() + " WHERE id = " + id;
            try {
                PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(updateSQL);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
