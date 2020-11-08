package sample.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.classes.Analysis;
import sample.classes.AnalysisNameList;
import sample.classes.Patient;
import sample.config.Config;
import sample.controller.baseController.Controller;
import sample.model.ModelPatientProfile;

import static com.sun.org.apache.xerces.internal.util.XMLChar.trim;

public class ControllerPatientProfile extends Controller {
    private ModelPatientProfile model;
    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldLastName;
    @FXML
    private TableView<Analysis> tableViewAnalyzes;
    @FXML
    private TableColumn<Analysis, Integer> tableColumnAnalyzesId;
    @FXML
    private TableColumn<Analysis, String> tableColumnAnalyzesTitle;
    @FXML
    private TableColumn<Analysis, String> tableColumnAnalyzesDate;
    @FXML
    private TableColumn<Analysis, String> tableColumnAnalyzesResult;
    @FXML
    private ChoiceBox<AnalysisNameList> choiceBoxAnalysisNames;
    @FXML
    private TextField textFieldAnalysisResult;
    @FXML
    private ChoiceBox<String> choiceBoxAnalysisResult;

    @FXML
    private void initialize() {
        System.out.println("initialize ControllerPatientProfile");

        if (app.getCurrentDoctor() == null) {
            Config config = new Config();
            app.setScene(config.pathDoctorAuthorization, config.getPath(config.pathDoctorAuthorization));
        }

        if (app.getCurrentPatient() == null) {
            Config config = new Config();
            app.setScene(config.pathPatientsTable, config.getPath(config.pathPatientsTable));
        }

        model = new ModelPatientProfile();
        textFieldFirstName.setText(app.getCurrentPatient().getFirst_name());
        textFieldLastName.setText(app.getCurrentPatient().getLast_name());
        tableColumnAnalyzesId.setCellValueFactory(new PropertyValueFactory<Analysis, Integer>("id"));
        tableColumnAnalyzesTitle.setCellValueFactory(new PropertyValueFactory<Analysis, String>("title"));
        tableColumnAnalyzesDate.setCellValueFactory(new PropertyValueFactory<Analysis, String>("date"));
        tableColumnAnalyzesResult.setCellValueFactory(new PropertyValueFactory<Analysis, String>("result"));
        tableViewAnalyzes.setItems(model.getAllAnalyzesByPatientId(app.getCurrentPatient().getId()));
        choiceBoxAnalysisNames.setItems(model.getAllAnalysisNames());
        choiceBoxAnalysisNames.setOnAction(this::changeChoiceBox);
        choiceBoxAnalysisResult.setItems(FXCollections.observableArrayList("Положительный", "Отрицательный"));
    }

    @FXML
    private void clickButtonDeleteAnalysisFromPatient() {
        if (tableViewAnalyzes.getSelectionModel().getSelectedItem() == null) {
            showMessageError("Ошибка!", "Выберите анализ!");
            return;
        }

        if (!showMessageConfirmation("Предупреждение", "Вы уверены, что хотите удалить анализ пациента?")) {
            return;
        }

        if (!model.deleteAnalysisByAnalysisId(tableViewAnalyzes.getSelectionModel().getSelectedItem().getId())) {
            showMessageError("Ошибка!", "Что-то пошло не так");
            return;
        }

        Config config = new Config();
        app.setScene(config.pathPatientProfile, config.getPath(config.pathPatientProfile));
    }

    @FXML
    private void clickButtonBack() {
        app.setCurrentPatient();
        Config config = new Config();
        app.setScene(config.pathPatientsTable, config.getPath(config.pathPatientsTable));
    }

    @FXML
    private void clickButtonAddAnalysis() {
        String result = null;

        if (choiceBoxAnalysisNames.getValue() == null) {
            return;
        } else if (choiceBoxAnalysisNames.getValue().getType().equals("integer")) {
            result = trim(textFieldAnalysisResult.getText());
            if (result.isEmpty()) {
                showMessageError("Ошибка!", "Вы не ввели значение результата!");
                return;
            }

            try {
                Integer.parseInt(result);
            } catch (Exception exp) {
                showMessageError("Ошибка!", "Введите число!");
                return;
            }
        } else if (choiceBoxAnalysisNames.getValue().getType().equals("string")) {
            result = choiceBoxAnalysisResult.getValue();
        }

        if (!model.addAnalysisToPatient(app.getCurrentPatient().getId(), choiceBoxAnalysisNames.getValue().getId(), result)) {
            showMessageError("Ошибка!", "Что-то пошло не так");
            return;
        }

        Config config = new Config();
        app.setScene(config.pathPatientProfile, config.getPath(config.pathPatientProfile));
    }

    @FXML
    private void clickButtonChangeNamePatient() {
        String firstName = trim(textFieldFirstName.getText());
        String lastName = trim(textFieldLastName.getText());

        if (!showMessageConfirmation("Предупреждение", "Вы уверены, что хотите изменить имя пациента?")) {
            return;
        }

        if (firstName.isEmpty() || lastName.isEmpty()) {
            showMessageError("Ошибка!", "Пустое значение заданных параметров!");
            return;
        }

        if (firstName.equals(app.getCurrentPatient().getFirst_name()) &&
                lastName.equals(app.getCurrentPatient().getLast_name())) {
            showMessageError("Ошибка!", "Вы ничего не изменили!");
            return;
        }

        if (!model.changeNamePatient(app.getCurrentPatient().getId(), firstName, lastName)) {
            showMessageError("Ошибка!", "Что-то пошло не так");
            return;
        }

        app.setCurrentPatient(new Patient(app.getCurrentPatient().getId(), firstName, lastName));
        Config config = new Config();
        app.setScene(config.pathPatientProfile, config.getPath(config.pathPatientProfile));
    }

    @FXML
    private void changeChoiceBox(ActionEvent event) {
        if (choiceBoxAnalysisNames.getValue() == null) {
            textFieldAnalysisResult.setVisible(false);
            choiceBoxAnalysisResult.setVisible(false);
        }
        if (choiceBoxAnalysisNames.getValue().getType().equals("integer")) {
            textFieldAnalysisResult.setVisible(true);
            choiceBoxAnalysisResult.setVisible(false);
        } else if (choiceBoxAnalysisNames.getValue().getType().equals("string")) {
            textFieldAnalysisResult.setVisible(false);
            choiceBoxAnalysisResult.setVisible(true);
        }
    }

    @FXML
    private void clickButtonPrintAnalyzes() {
        model.printAnalyzes(app.getCurrentPatient());
        showMessageConfirmation("Успех", "Документ успешно создан");
    }
}
