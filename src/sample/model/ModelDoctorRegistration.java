package sample.model;

import sample.model.baseModel.Model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ModelDoctorRegistration extends Model {
    public boolean doctorRegistration(String firstName, String lastName, String login, String password) {
        String hashPassword = get_SHA_512_SecurePassword(password, login);
        String sql = "insert into doctors (first_name, last_name, login, password) " +
                "values " +
                "('"+ firstName+"', '"+lastName+"', '"+login+"', '"+hashPassword+"')";
        return db.executeUpdate(sql);
    }

    public boolean findLogin(String login) {
        boolean result = false;
        try {
            ResultSet resultSet = db.executeQuery("select doctor_id from doctors where login = '" + login + "'");
            if (resultSet.next()) {
                result = true;
            }
        } catch (SQLException exp) {
            System.err.println(exp.getMessage());
        } finally {
            db.close();
        }
        return result;
    }
}
