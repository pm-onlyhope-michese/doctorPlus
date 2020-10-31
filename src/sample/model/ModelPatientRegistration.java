package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.classes.Patient;
import sample.model.baseModel.Model;

import java.sql.*;

public class ModelPatientRegistration extends Model {
    public int patientRegistration(int doctor_id, String firstName, String lastName) throws SQLException {
        boolean result = false;
        int patient_id = 0;
        String sql = "insert into patients (first_name, last_name) values " +
                "('" + firstName + "', '" + lastName + "')";

        try {
            if (db.executeUpdate(sql)) {
                result = true;
            }
        } catch (SQLException exp) {
            System.err.println(exp.getMessage());
        }

        sql = "select max(patient_id) as max from patients";
        try {
            ResultSet resultSet = db.executeQuery(sql);
            if (resultSet.next()) {
                result = true;
                patient_id = resultSet.getInt("max");
            }
        } catch (SQLException exp) {
            result = false;
            System.err.println(exp.getMessage());
        } finally {
            db.close();
        }

        if (result) {
            addPatientToDoctor(doctor_id, patient_id);
        }

        return patient_id;
    }

    public boolean addPatientToDoctor(int doctor_id, int patient_id) {
        boolean result = false;
        String sql = "insert into doctors_patients (doctor_id, patient_id) values " +
                "('" + doctor_id + "', '" + patient_id + "')";

        try {
            if (db.executeUpdate(sql)) {
                result = true;
            }
        } catch (SQLException exp) {
            System.err.println(exp.getMessage());
        }

        return result;
    }

    public ObservableList<Patient> getAllPatientsExceptDoctorPatients(int doctor_id) throws SQLException {
        ObservableList<Patient> patients = FXCollections.observableArrayList();
        String sql = "select distinct patients.patient_id, first_name, last_name from patients \n" +
                "left join doctors_patients on doctors_patients.patient_id = patients.patient_id\n" +
                "where doctors_patients.doctor_id is null or doctors_patients.doctor_id != " + doctor_id + " " +
                "and patients.patient_id != all (select patient_id from doctors_patients " +
                "where doctor_id = " + doctor_id + ")";

        try {
            ResultSet resultSet = db.executeQuery(sql);
            while (resultSet.next()) {
                patients.add(new Patient(resultSet.getInt("patient_id"), resultSet.getString("first_name"),
                        resultSet.getString("last_name")));
            }
        } catch (SQLException exp) {
            System.err.println(exp.getMessage());
        } finally {
            db.close();
        }

        return patients;
    }

    public Patient getPatientById(int patient_id) {
        Patient patient = null;
        String sql = "select patients.patient_id, first_name, last_name " +
                "from patients where patient_id = " + patient_id;

        try {
            ResultSet resultSet = db.executeQuery(sql);
            if (resultSet.next()) {
                patient = new Patient(resultSet.getInt("patient_id"), resultSet.getString("first_name"),
                        resultSet.getString("last_name"));
            }
        } catch (SQLException qxp) {
            System.err.println(qxp.getMessage());
        }

        return patient;
    }
}
