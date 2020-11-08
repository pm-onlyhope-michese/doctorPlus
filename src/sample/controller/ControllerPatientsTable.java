package sample.controller;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.classes.Patient;
import sample.config.Config;
import sample.controller.baseController.Controller;
import sample.model.ModelPatientsTable;

import java.io.IOException;

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
    private void initialize() {
        System.out.println("initialize ControllerPatientsTable");
        if (app.getCurrentDoctor() == null) {
            Config config = new Config();
                app.setScene(config.pathDoctorAuthorization, config.getPath(config.pathDoctorAuthorization));
        }
        model = new ModelPatientsTable();
        doctorName.setText(app.getCurrentDoctor().getFull_name());
        tableColumnPatientsId.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("id"));
        tableColumnPatientsName.setCellValueFactory(new PropertyValueFactory<Patient, String>("full_name"));
        tableViewPatients.setItems(model.getAllPatientsByDoctorId(app.getCurrentDoctor().getId()));

        textFieldSearch.textProperty().addListener((obs, oldText, textSearch) -> {
                inputTextFieldSearch(obs, oldText, textSearch);
        });
    }

    @FXML
    private void clickButtonLogout() {
        if(!showMessageConfirmation("Выход", "Вы уверены, что хотите выйти?")) {
            return;
        }

        app.setCurrentDoctor();
        Config config = new Config();
            app.setScene(config.pathDoctorAuthorization, config.getPath(config.pathDoctorAuthorization));
    }

    @FXML
    private void clickButtonAddPatient() {
        Config config = new Config();
            app.setScene(config.pathPatientRegistration, config.getPath(config.pathPatientRegistration));
    }

    @FXML
    private void clickButtonOpenPatientProfile() {
        if (tableViewPatients.getSelectionModel().getSelectedItem() != null) {
            app.setCurrentPatient(tableViewPatients.getSelectionModel().getSelectedItem());
            Config config = new Config();
            app.setScene(config.pathPatientProfile, config.getPath(config.pathPatientProfile));
        }
    }

    @FXML
    private void clickButtonDeletePatient() {
        if(!showMessageConfirmation("Предупреждение", "Вы уверены, что хотите удалить пациента?")) {
            return;
        }

        int patient_id = tableViewPatients.getSelectionModel().getSelectedItem().getId();
        model.deletePatientByPatientIdAndDoctorId(app.getCurrentDoctor().getId(), patient_id);
        Config config = new Config();
        app.setScene(config.pathPatientsTable, config.getPath(config.pathPatientsTable));
    }

    @FXML
    private void clickButtonPrint() {
        Stage stage = new Stage();
        Config config = new Config();
        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getResource(config.pathPrintDocumentation));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        stage.setTitle(config.getPath(config.pathPrintDocumentation));
        stage.setScene(new Scene(root, 259, 254));
        stage.show();
    }

    private void inputTextFieldSearch(ObservableValue<? extends String> obs, String oldText, String textSearch) {
        tableViewPatients.setItems(model.getAllPatientsByDoctorId(app.getCurrentDoctor().getId(), textSearch));
    }
}
