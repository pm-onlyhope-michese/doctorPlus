package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.config.Config;
import sample.controller.baseController.Controller;
import sample.model.ModelDoctorAuthorization;

import java.io.IOException;
import java.sql.*;

import static com.sun.org.apache.xerces.internal.util.XMLChar.trim;

public class ControllerDoctorAuthorization extends Controller {
    private ModelDoctorAuthorization model;
    @FXML
    private TextField textFieldLogin;
    @FXML
    private PasswordField textFieldPassword;
    @FXML
    private Label labelError;

    @FXML
    private void initialize() throws SQLException {
        System.out.println("initialize ControllerDoctorAuthorization");
        model = new ModelDoctorAuthorization();
    }

    @FXML
    private void clickButtonLogin(ActionEvent event) throws IOException, SQLException {
        String login = trim(textFieldLogin.getText());
        String password = trim(textFieldPassword.getText());
        app.setCurrentDoctor(model.doctorAuthorization(login, password));
        if(app.getCurrentDoctor() == null) {
            labelError.setText("Неверный логин или пароль!");
          return;
        }

        app.setScene(Config.pathPatientsTable, Config.paths.get(Config.pathPatientsTable));
    }


    @FXML
    private void clickButtonRegistration(ActionEvent event) throws IOException {
        app.setScene(Config.pathDoctorRegistration, Config.paths.get(Config.pathDoctorRegistration));
    }
}
