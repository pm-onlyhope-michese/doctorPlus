package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import sample.classes.AnalysisNameList;
import sample.classes.Patient;
import sample.controller.baseController.Controller;
import sample.model.ModelPrintDocumentation;

public class ControllerPrintDocumentation extends Controller {
    private ModelPrintDocumentation model;
    private ObservableList<String> choiceBoxItems;

    @FXML
    private ChoiceBox<String> choiceBox;
    @FXML
    private ComboBox<Patient> comboBoxPatients;
    @FXML
    private ComboBox<AnalysisNameList> comboBoxAnalysis;

    @FXML
    private void initialize() {
        System.out.println("initialize ControllerPrintDocumentation");
        if (app.getCurrentDoctor() == null) {
            choiceBox.getScene().getWindow().hide();
            return;
        }
        model = new ModelPrintDocumentation();
        choiceBoxItems = FXCollections.observableArrayList(
                "Статистика анализа",
                "Анализы пациента",
                "Полная статистика"
        );
        choiceBox.setItems(choiceBoxItems);
        choiceBox.setOnAction(this::changeChoiceBox);
        comboBoxPatients.setVisibleRowCount(20);
        comboBoxAnalysis.setVisibleRowCount(20);
        comboBoxPatients.setVisible(false);
        comboBoxAnalysis.setVisible(false);
    }


    @FXML
    private void changeChoiceBox(ActionEvent event) {
        if (app.getCurrentDoctor() == null) {
            choiceBox.getScene().getWindow().hide();
        }

        if (choiceBox.getValue() == null) {
            comboBoxPatients.setVisible(false);
            comboBoxAnalysis.setVisible(false);
        } else if (choiceBox.getValue().equals(choiceBoxItems.get(0))) { // Статистика анализа
            comboBoxPatients.setVisible(false);
            comboBoxAnalysis.setVisible(true);
            if (comboBoxAnalysis.getItems().size() == 0) {
                comboBoxAnalysis.setItems(model.getAllAnalysisNames());
            }
            comboBoxAnalysis.show();
        } else if (choiceBox.getValue().equals(choiceBoxItems.get(1))) { // Анализы пациента
            comboBoxPatients.setVisible(true);
            comboBoxAnalysis.setVisible(false);
            if (comboBoxPatients.getItems().size() == 0) {
                comboBoxPatients.setItems(model.getAllPatientsByDoctorId(app.getCurrentDoctor().getId()));
            }
            comboBoxPatients.show();
        } else if (choiceBox.getValue().equals(choiceBoxItems.get(2))) { // Полная статистика
            comboBoxPatients.setVisible(false);
            comboBoxAnalysis.setVisible(false);
        }
    }

    @FXML
    private void clickButtonPrint() {
        if (app.getCurrentDoctor() == null) {
            choiceBox.getScene().getWindow().hide();
            return;
        }

        if (choiceBox.getValue() == null) {
            showMessageError("Ошибка!", "Выберите статистику!");
        } else if (choiceBox.getValue().equals(choiceBoxItems.get(0))) { // Статистика анализа
            if (comboBoxAnalysis.getValue() == null) {
                showMessageError("Ошибка!", "Выберите анализ!");
                return;
            }

            model.printPatientsByAnalysis(comboBoxAnalysis.getValue().getId());
            showMessageConfirmation("Успех", "Документ успешно создан");
        } else if (choiceBox.getValue().equals(choiceBoxItems.get(1))) { // Анализы пациента
            if (comboBoxPatients.getValue() == null) {
                showMessageError("Ошибка!", "Выберите пациента!");
                return;
            }

            model.printAnalyzes(comboBoxPatients.getValue());
            showMessageConfirmation("Успех", "Документ успешно создан");
        } else if (choiceBox.getValue().equals(choiceBoxItems.get(2))) { // Полная статистика
            model.printFullStatistic(app.getCurrentDoctor());
            showMessageConfirmation("Успех", "Документ успешно создан");
        }
    }
}
