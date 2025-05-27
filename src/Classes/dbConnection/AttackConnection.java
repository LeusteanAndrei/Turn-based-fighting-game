package Classes.dbConnection;

import Classes.gameClasses.Attack;
import enumerations.AttackType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AttackConnection implements CRUD<Attack> {

    private static final String getAttacksSQL = "SELECT * FROM attack";
    private static final String deleteAttackSQL = "DELETE FROM attack WHERE id_attack = ?";
    private static final String addAttackSQL = "INSERT INTO attack (type) VALUES (?)";
    private static final String updateAttackSQL = "UPDATE attack SET type = ? WHERE id_attack = ?";
    private static final String deleteHitsSQL = "Delete from attack_hit where id_attack = ?";
    private static final String getHitsSQL = "SELECT * FROM attack_hit WHERE id_attack = ?";
    private static final String addHitsSQL = "INSERT INTO attack_hit (id_attack, hit) VALUES (?, ?)";
    private static final String getSingleAttackSQL = "SELECT * FROM attack WHERE id_attack = ?";



    public List<Integer> getHits(int id_attack) {
        try {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(getHitsSQL);
            preparedStatement.setInt(1, id_attack);
            var resultSet = preparedStatement.executeQuery();

            List<Integer> hits = new java.util.ArrayList<>();

            while (resultSet.next()) {
                int id = resultSet.getInt("hit");
                hits.add(id);
            }

            return hits;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addHit(int id_attack, int hit) {
        try {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(addHitsSQL);
            preparedStatement.setInt(1, id_attack);
            preparedStatement.setInt(2, hit);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setHits(int id_attack, List<Integer> hits) {
        try {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(deleteHitsSQL);
            preparedStatement.setInt(1, id_attack);
            preparedStatement.executeUpdate();

            for (Integer hit : hits) {
                addHit(id_attack, hit);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Attack> read(){
        try {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(getAttacksSQL);
            var resultSet = preparedStatement.executeQuery();

            List<Attack> attacks = new ArrayList<>();

            while (resultSet.next()) {
                int id = resultSet.getInt("id_attack");
                String type = resultSet.getString("type");

                Attack attack = new Attack();
                attack.setType(AttackType.getAttackType(type));
                attack.setId(id);

                attacks.add(attack);
            }

            return attacks;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void update(int id, Attack attack) {
        try {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(updateAttackSQL);
            preparedStatement.setString(1, attack.getType().toString());
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
            setHits(id, attack.getHits());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(int id) {
        try {
            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(deleteAttackSQL);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public int add(Attack attack) {

        try {

            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(addAttackSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, attack.getType().toString());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                attack.setId(id);
                setHits(id, attack.getHits());
                return id;
            }
            return -1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public Attack get(int id) {
        try {

            PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(getSingleAttackSQL);
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int attackId = resultSet.getInt("id_attack");
                String type = resultSet.getString("type");

                Attack attack = new Attack();
                attack.setType(AttackType.getAttackType(type));
                attack.setId(attackId);

                List<Integer> hits = getHits(attackId);
                attack.setHits(hits);

                return attack;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static class Updater
    {
        private StringBuilder sql = new StringBuilder();
        private int id = -1;

        public Updater(int id)
        {
            this.id = id;
        }

        public void type(AttackType type)
        {
            sql.append("type = '").append(type.toString()).append("' ");

        }

        public void update()
        {
            if (sql.length() == 0) {
                return;
            }
            sql.delete(sql.length() - 2, sql.length());
            String updateSQL = "UPDATE attack SET " + sql.toString() + " WHERE id_attack = " + id;
            try {
                sql.append(" WHERE id_attack = ").append(id);
                PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(updateSQL);
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


}
