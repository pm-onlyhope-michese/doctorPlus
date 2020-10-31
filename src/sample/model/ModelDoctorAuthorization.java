package sample.model;

import sample.classes.Doctor;
import sample.model.baseModel.Model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ModelDoctorAuthorization extends Model {
    public Doctor doctorAuthorization(String login, String password) throws SQLException {
        Doctor doctor = null;
        String hashPassword = get_SHA_512_SecurePassword(password, login);
        String sql = "select * from doctors where login = '" + login + "' and password = '" + hashPassword + "'";

        try {
            ResultSet resultSet = db.executeQuery(sql);
            if(resultSet.next()) {
                doctor = new Doctor(resultSet.getInt("doctor_id"), resultSet.getString("first_name"),
                        resultSet.getString("last_name"));
            }
        } catch (SQLException exp) {
            System.err.println(exp.getMessage());
        } finally {
            db.close();
        }

        return doctor;
    }
}
