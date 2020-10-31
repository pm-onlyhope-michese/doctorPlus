package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sample.classes.Patient;
import sample.config.Config;
import sample.controller.baseController.Controller;
import sample.model.ModelPatientRegistration;

import java.io.IOException;
import java.sql.SQLException;
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
    private ChoiceBox<Patient> choiceBoxPatientsList;
    @FXML
    private Label labelError;

    @FXML
    private void initialize() throws IOException, SQLException {
        System.out.println("initialize ControllerPatientRegistration");
        if (app.getCurrentDoctor() == null) {
            app.setScene(Config.pathDoctorAuthorization, Config.paths.get(Config.pathDoctorAuthorization));
        }
        model = new ModelPatientRegistration();
        choiceBoxPatientsList.setItems(model.getAllPatientsExceptDoctorPatients(app.getCurrentDoctor().getId()));
    }

    @FXML
    private void clickButtonBack(ActionEvent event) throws IOException {
        app.setScene(Config.pathPatientsTable, Config.paths.get(Config.pathPatientsTable));
    }

    @FXML
    private void clickButtonApply(ActionEvent event) throws IOException, SQLException {
        boolean result = false;
        int patient_id = 0;

        if (choiceBoxPatientsList.getValue() == null) {
            String firstName = trim(textFieldFirstName.getText()).toLowerCase();
            String lastName = trim(textFieldLastName.getText()).toLowerCase();

            if (firstName.isEmpty() || lastName.isEmpty()) {
                labelError.setText("Введите данные во все поля!");
                return;
            }

            Pattern pattern = Pattern.compile("[^a-zA-Zа-яА-Я]+");
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
            patient_id = choiceBoxPatientsList.getValue().getId();
            result = model.addPatientToDoctor(app.getCurrentDoctor().getId(), patient_id);
        }

        if (result && patient_id != 0) {
            app.setCurrentPatient(model.getPatientById(patient_id));
            app.setScene(Config.pathPatientProfile, Config.paths.get(Config.pathPatientProfile));
        } else {
            labelError.setText("Что-то пошло не так");
        }
    }
}