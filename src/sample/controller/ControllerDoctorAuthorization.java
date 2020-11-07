package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.config.Config;
import sample.controller.baseController.Controller;
import sample.model.ModelDoctorAuthorization;

public class ControllerDoctorAuthorization extends Controller {
    private ModelDoctorAuthorization model;
    @FXML
    private TextField textFieldLogin;
    @FXML
    private PasswordField textFieldPassword;
    @FXML
    private Label labelError;

    @FXML
    private void initialize() {
        System.out.println("initialize ControllerDoctorAuthorization");
        model = new ModelDoctorAuthorization();
    }

    @FXML
    private void clickButtonLogin() {
        String login = textFieldLogin.getText().trim();
        String password = textFieldPassword.getText().trim();

        app.setCurrentDoctor(model.doctorAuthorization(login, password));

        if (app.getCurrentDoctor() == null) {
            labelError.setText("Неверный логин или пароль!");
            return;
        }

        Config config = new Config();
        app.setScene(config.pathPatientsTable, config.getPath(config.pathPatientsTable));
    }

    @FXML
    private void clickButtonRegistration() {
        Config config = new Config();
        app.setScene(config.pathDoctorRegistration, config.getPath(config.pathDoctorRegistration));
    }
}
