package sample.controller;

import javafx.animation.PauseTransition;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import sample.classes.Patient;
import sample.config.Config;
import sample.controller.baseController.Controller;
import sample.model.ModelPatientsTable;

import java.io.IOException;
import java.sql.SQLException;

public class ControllerPatientsTable extends Controller {
    private ModelPatientsTable model;
    @FXML
    private TableView<Patient> tableViewPatients;
    @FXML
    private TableColumn<Patient, Integer> tableColumnPatientsId;
    @FXML
    private TableColumn<Patient, String> tableColumnPatientsName;
    @FXML
    private Label doctorName;
    @FXML
    private TextField textFieldSearch;

    @FXML
    private void initialize() throws IOException, SQLException {
        System.out.println("initialize ControllerPatientsTable");
        if (app.getCurrentDoctor() == null) {
            app.setScene(Config.pathDoctorAuthorization, Config.paths.get(Config.pathDoctorAuthorization));
        }
        model = new ModelPatientsTable();
        doctorName.setText(app.getCurrentDoctor().getFull_name());
        tableColumnPatientsId.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("id"));
        tableColumnPatientsName.setCellValueFactory(new PropertyValueFactory<Patient, String>("full_name"));
        tableViewPatients.setItems(model.getAllPatientsByDoctorId(app.getCurrentDoctor().getId()));

        textFieldSearch.textProperty().addListener((obs, oldText, textSearch) -> {
            try {
                inputTextFieldSearch(obs, oldText, textSearch);
            } catch (SQLException exp) {
                System.out.println(exp.getMessage());
            }
        });
    }

    @FXML
    private void clickButtonLogout(ActionEvent event) throws IOException {
        if(!showMessageConfirmation("Выход", "Вы уверены, что хотите выйти?")) {
            return;
        }

        app.setCurrentDoctor();
        app.setScene(Config.pathDoctorAuthorization, Config.paths.get(Config.pathDoctorAuthorization));
    }

    @FXML
    private void clickButtonAddPatient(ActionEvent event) throws IOException {
        app.setScene(Config.pathPatientRegistration, Config.paths.get(Config.pathPatientRegistration));
    }

    @FXML
    private void clickButtonOpenPatientProfile(ActionEvent event) throws IOException {
        if (tableViewPatients.getSelectionModel().getSelectedItem() != null) {
            app.setCurrentPatient(tableViewPatients.getSelectionModel().getSelectedItem());
            app.setScene(Config.pathPatientProfile, Config.paths.get(Config.pathPatientProfile));
        }
    }

    @FXML
    private void clickButtonDeletePatient(ActionEvent event) throws IOException {
        if(!showMessageConfirmation("Предупреждение", "Вы уверены, что хотите удалить пациента?")) {
            return;
        }

        int patient_id = tableViewPatients.getSelectionModel().getSelectedItem().getId();
        model.deletePatientByPatientIdAndDoctorId(app.getCurrentDoctor().getId(), patient_id);
        app.setScene(Config.pathPatientsTable, Config.paths.get(Config.pathPatientsTable));
    }

    private void inputTextFieldSearch(ObservableValue<? extends String> obs, String oldText, String textSearch) throws SQLException {
        tableViewPatients.setItems(model.getAllPatientsByDoctorId(app.getCurrentDoctor().getId(), textSearch));
    }
}
