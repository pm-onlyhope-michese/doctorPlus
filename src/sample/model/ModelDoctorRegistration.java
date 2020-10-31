package sample.model;

import sample.model.baseModel.Model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ModelDoctorRegistration extends Model {
    public boolean doctorRegistration(String firstName, String lastName, String login, String password) throws SQLException {
        boolean result = false;
        String hashPassword = get_SHA_512_SecurePassword(password, login);
        String sql = "insert into doctors (first_name, last_name, login, password) " +
                "values " +
                "('"+ firstName+"', '"+lastName+"', '"+login+"', '"+hashPassword+"')";

        try {
            if (db.executeUpdate(sql)) {
                result = true;
            }
        } catch (SQLException exp) {
            System.err.println(exp.getMessage());
        }

        return result;
    }

    public boolean findLogin(String login) throws SQLException {
        boolean result = false;
        try {
            ResultSet resultSet = db.executeQuery("select doctor_id from doctors where login = '" + login + "'");
            if (resultSet.next()) {
                result = true;
            }
        } catch (SQLException exp) {
            result = true;
            System.err.println(exp.getMessage());
        } finally {
            db.close();
        }
        return result;
    }
}
