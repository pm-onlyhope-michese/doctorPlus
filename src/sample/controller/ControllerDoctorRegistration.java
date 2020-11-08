package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.config.Config;
import sample.controller.baseController.Controller;
import sample.model.ModelDoctorRegistration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerDoctorRegistration extends Controller {
    private ModelDoctorRegistration model;
    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldLastName;
    @FXML
    private TextField textFieldLogin;
    @FXML
    private PasswordField textFieldFirstPassword;
    @FXML
    private PasswordField textFieldSecondPassword;
    @FXML
    private Label labelError;

    @FXML
    private void initialize() {
        System.out.println("initialize ControllerDoctorRegistration");
        model = new ModelDoctorRegistration();
    }

    @FXML
    private void clickButtonApply() {
        String firstName = (textFieldFirstName.getText()).trim().toLowerCase();
        String lastName = (textFieldLastName.getText()).trim().toLowerCase();

        String login = (textFieldLogin.getText().trim());
        String firstPassword = (textFieldFirstPassword.getText().trim());
        String secondPassword = (textFieldSecondPassword.getText()).trim();

        if (firstName.isEmpty() || lastName.isEmpty() || login.isEmpty()
                || firstPassword.isEmpty() || secondPassword.isEmpty()
        ) {
            labelError.setText("Введите данные во все поля!");
            return;
        }

        if (!firstPassword.equals(secondPassword)) {
            labelError.setText("Пароли не совпадают!");
            return;
        }

        if (model.findLogin(login)) {
            labelError.setText("Такой логин уже существует!");
            return;
        }

        Pattern pattern = Pattern.compile("[^a-zA-Zа-яА-ЯёЁ]+");
        Matcher matherFirstName = pattern.matcher(firstName);
        Matcher matherLastName = pattern.matcher(lastName);

        if (matherFirstName.find() || matherLastName.find()) {
            labelError.setText("В имени и фамилии должны содержаться только буквы!");
            return;
        }

        firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
        lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1);

        if (!model.doctorRegistration(firstName, lastName, login, firstPassword)) {
            labelError.setText("Что-то пошло не так");
            return;
        }

        Config config = new Config();
        app.setScene(config.pathDoctorAuthorization, config.getPath(config.pathDoctorAuthorization));
    }

    @FXML
    private void clickButtonBack() {
        Config config = new Config();
        app.setScene(config.pathDoctorAuthorization, config.getPath(config.pathDoctorAuthorization));
    }
}
