package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.classes.Patient;
import sample.model.baseModel.Model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ModelPatientsTable extends Model {
    public ObservableList<Patient> getAllPatientsByDoctorId(int doctor_id) {
        ObservableList<Patient> patients = FXCollections.observableArrayList();
        String sql = "select patient_id, first_name, last_name from doctors_patients " +
                "inner join patients using(patient_id) " +
                "where doctor_id = " + doctor_id;

        try {
            ResultSet resultSet = db.executeQuery(sql);
            while (resultSet.next()) {
                patients.add(new Patient(resultSet.getInt("patient_id"),
                        resultSet.getString("first_name"), resultSet.getString("last_name")));
            }
        } catch (SQLException exp) {
            System.out.println(exp.getMessage());
        } finally {
            db.close();
        }

        return patients;
    }

    public ObservableList<Patient> getAllPatientsByDoctorId(int doctor_id, String textSearch) {
        ObservableList<Patient> patients = FXCollections.observableArrayList();
        int numberSearch = 0;
        String number = null;
        String fullName = null;

        String sql = "select patient_id, first_name, last_name, concat(first_name, ' ', last_name) as full_name " +
                "from doctors_patients " +
                "inner join patients using(patient_id) " +
                "where doctor_id = " + doctor_id;

        try {
            numberSearch = Integer.parseInt(textSearch);
            ResultSet resultSet = db.executeQuery(sql);
            while (resultSet.next()) {
                number = String.valueOf(resultSet.getInt("patient_id"));
                if (number.matches(".*" + textSearch + ".*")) {
                    patients.add(new Patient(resultSet.getInt("patient_id"),
                            resultSet.getString("first_name"), resultSet.getString("last_name")));
                }
            }
        } catch (SQLException exp) {
            System.err.println(exp.getMessage());
        } catch (Exception exp) {
            try {
                ResultSet resultSet = db.executeQuery(sql);
                while (resultSet.next()) {
                    fullName = resultSet.getString("full_name");
                    if (fullName.matches(".*" + textSearch + ".*")) {
                        patients.add(new Patient(resultSet.getInt("patient_id"),
                                resultSet.getString("first_name"), resultSet.getString("last_name")));
                    }
                }
            } catch (SQLException exp2) {
                System.err.println(exp2.getMessage());
            }
        } finally {
            db.close();
        }

        return patients;
    }

    public boolean deletePatientByPatientIdAndDoctorId(int doctor_id, int patient_id) {
        String sql = "delete from doctors_patients " +
                "where doctor_id = " + doctor_id + " " +
                "and patient_id = " + patient_id;
        return db.executeUpdate(sql);
    }
}
