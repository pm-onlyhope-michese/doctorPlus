package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sample.classes.Patient;
import sample.config.Config;
import sample.controller.baseController.Controller;
import sample.model.ModelPatientRegistration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sun.org.apache.xerces.internal.util.XMLChar.trim;

public class ControllerPatientRegistration extends Controller {
    private ModelPatientRegistration model;
    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldLastName;
    @FXML
    private ComboBox<Patient> comboBoxPatientsList;
    @FXML
    private Label labelError;

    @FXML
    private void initialize() {
        System.out.println("initialize ControllerPatientRegistration");
        if (app.getCurrentDoctor() == null) {
            Config config = new Config();
            app.setScene(config.pathDoctorAuthorization, config.getPath(config.pathDoctorAuthorization));
        }
        model = new ModelPatientRegistration();
        comboBoxPatientsList.setVisibleRowCount(20);
        comboBoxPatientsList.setItems(model.getAllPatientsExceptDoctorPatients(app.getCurrentDoctor().getId()));
    }

    @FXML
    private void clickButtonBack() {
        Config config = new Config();
        app.setScene(config.pathPatientsTable, config.getPath(config.pathPatientsTable));
    }

    @FXML
    private void clickButtonApply() {
        boolean result = false;
        int patient_id = 0;

        if (comboBoxPatientsList.getValue() == null) {
            String firstName = trim(textFieldFirstName.getText()).toLowerCase();
            String lastName = trim(textFieldLastName.getText()).toLowerCase();

            if (firstName.isEmpty() || lastName.isEmpty()) {
                labelError.setText("Введите данные во все поля!");
                return;
            }

            Pattern pattern = Pattern.compile("^[^a-zа-яё]+&");
            Matcher matherFirstName = pattern.matcher(firstName);
            Matcher matherLastName = pattern.matcher(lastName);

            if (matherFirstName.find() || matherLastName.find()) {
                labelError.setText("В имени и фамилии должны содержаться только буквы!");
                return;
            }

            firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
            lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1);

            patient_id = model.patientRegistration(app.getCurrentDoctor().getId(), firstName, lastName);
            result = true;
        } else {
            patient_id = comboBoxPatientsList.getValue().getId();
            result = model.addPatientToDoctor(app.getCurrentDoctor().getId(), patient_id);
        }

        if (result && patient_id != 0) {
            app.setCurrentPatient(model.getPatientById(patient_id));
            Config config = new Config();
            app.setScene(config.pathPatientProfile, config.getPath(config.pathPatientProfile));
        } else {
            labelError.setText("Что-то пошло не так");
        }
    }
}