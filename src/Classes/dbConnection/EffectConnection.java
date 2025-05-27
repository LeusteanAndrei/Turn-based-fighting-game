package Classes.dbConnection;

import Classes.gameClasses.Effect;
import enumerations.EffectType;
import enumerations.GamePeriod;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EffectConnection implements CRUD<Effect> {

    private static final String getEffectsSQL = "SELECT * FROM effect";

    @Override
    public List<Effect> read() {

        try {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(getEffectsSQL);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Effect> effects = new ArrayList<>();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String functionName = resultSet.getString("functionName");
                String removeFunctionName = resultSet.getString("removeFunctionName");
                EffectType type = EffectType.getEffectType(resultSet.getString("type"));

                int on_hit_count = resultSet.getInt("on_hit_count");
                int when_hit_count = resultSet.getInt("when_hit_count");
                GamePeriod period = GamePeriod.getGamePeriod(resultSet.getString("period"));
                Effect effect = new Effect(name, functionName, removeFunctionName, period, type, on_hit_count, when_hit_count);

                effect.setId(id);

                effects.add(effect);
            }

            return effects;
        } catch (SQLException e) {
            System.out.println("Error while reading effects from database: " + e.getMessage());
            throw new RuntimeException();
        }

    }


    @Override
    public void delete(int id)
    {

        String deleteEffectSQL = "DELETE FROM effect WHERE id = " + id;
        try {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(deleteEffectSQL);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public int  add(Effect effect) {

        String insertEffectSQL = getInsertEffectSQL(effect);
        try {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(insertEffectSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                effect.setId(id);
                return id;
            }

            return -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Effect get(String _name)
    {
        try {
            String getEffectSQL = "SELECT * FROM effect WHERE name = '" + _name + "'";

            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(getEffectSQL);
            ResultSet resultSet = preparedStatement.executeQuery();

            Effect effect = new Effect();
            if (resultSet.next()) {
                int effectId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String functionName = resultSet.getString("functionName");
                String removeFunctionName = resultSet.getString("removeFunctionName");
                EffectType type = EffectType.getEffectType(resultSet.getString("type"));

                int on_hit_count = resultSet.getInt("on_hit_count");
                int when_hit_count = resultSet.getInt("when_hit_count");
                GamePeriod period = GamePeriod.getGamePeriod(resultSet.getString("period"));
                effect = new Effect(name, functionName, removeFunctionName, period, type, on_hit_count, when_hit_count);

                effect.setId(effectId);
            }
            return effect;

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Effect get(int id)
    {
        try {
            String getEffectSQL = "SELECT * FROM effect WHERE id = '" + id + "'";

            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(getEffectSQL);
            ResultSet resultSet = preparedStatement.executeQuery();

            Effect effect = new Effect();
            if (resultSet.next()) {
                int effectId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String functionName = resultSet.getString("functionName");
                String removeFunctionName = resultSet.getString("removeFunctionName");
                EffectType type = EffectType.getEffectType(resultSet.getString("type"));

                int on_hit_count = resultSet.getInt("on_hit_count");
                int when_hit_count = resultSet.getInt("when_hit_count");
                GamePeriod period = GamePeriod.getGamePeriod(resultSet.getString("period"));
                effect = new Effect(name, functionName, removeFunctionName, period, type, on_hit_count, when_hit_count);

                effect.setId(effectId);
            }
            return effect;

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private String getInsertEffectSQL(Effect effect) {
        return "INSERT INTO effect (id, name, functionName, removeFunctionName, type, on_hit_count, when_hit_count, period) " +
                "VALUES (" + effect.getId() + ", '" + effect.getName() + "', '" + effect.getFunctionName() + "', '" +
                effect.getRemoveFunctionName() + "', '" + effect.getType().toString() + "', " + effect.getOn_hit_count() + ", " +
                effect.getWhen_hit_count() + ", '" + effect.getPeriod().toString() + "')";
    }

    public static class Updater
    {
        private int id = -1;
        private StringBuilder sb = new StringBuilder();

        public Updater(int id)
        {
            this.id = id;
//            sb.append("UPDATE effect SET ");
        }
        public Updater name(String name)
        {
            sb.append("name = '").append(name).append("', ");
            return this;
        }
        public Updater functionName(String functionName)
        {
            sb.append("functionName = '").append(functionName).append("', ");
            return this;
        }
        public Updater removeFunctionName(String removeFunctionName)
        {
            sb.append("removeFunctionName = '").append(removeFunctionName).append("', ");
            return this;
        }
        public Updater type(EffectType type)
        {
            sb.append("type = '").append(type.toString()).append("', ");
            return this;
        }
        public Updater onHitCount(int on_hit_count)
        {
            sb.append("on_hit_count = ").append(on_hit_count).append(", ");
            return this;
        }
        public Updater whenHitCount(int when_hit_count)
        {
            sb.append("when_hit_count = ").append(when_hit_count).append(", ");
            return this;
        }
        public Updater period(GamePeriod period)
        {
            sb.append("period = '").append(period.toString()).append("', ");
            return this;
        }
        public void update()
        {
            if (sb.length() == 0) {
                return;
            }
            sb.delete(sb.length() - 2, sb.length());
            String updateSQL = "UPDATE effect SET " + sb.toString() + " WHERE id = " + id;
            String SQL = sb.toString();
            try {
                PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(SQL);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
