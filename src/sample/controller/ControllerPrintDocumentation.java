package sample.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import sample.classes.Analysis;
import sample.classes.AnalysisNameList;
import sample.classes.Patient;
import sample.config.Config;
import sample.controller.baseController.Controller;
import sample.model.ModelPrintDocumentation;

import java.io.IOException;
import java.sql.SQLException;

public class ControllerPrintDocumentation extends Controller {
    ModelPrintDocumentation model;

    @FXML
    private ChoiceBox<String> choiceBox;
    @FXML
    ComboBox<Patient> comboBoxPatients;
    @FXML
    ComboBox<AnalysisNameList> comboBoxAnalysis;

    @FXML
    private void initialize() {
        System.out.println("initialize ControllerPrintDocumentation");
        if (app.getCurrentDoctor() == null) {
            choiceBox.getScene().getWindow().hide();
        }
        model = new ModelPrintDocumentation();
        choiceBox.setItems(FXCollections.observableArrayList(
                "Анализы пациента",
                "Пациенты с анализом",
                "Полная статистика"
        ));
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
        } else if (choiceBox.getValue().equals("Пациенты с анализом")) {
            comboBoxPatients.setVisible(false);
            comboBoxAnalysis.setVisible(true);
            if (comboBoxAnalysis.getItems().size() == 0) {
                try {
                    comboBoxAnalysis.setItems(model.getAllAnalysisNames());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            comboBoxAnalysis.show();
        } else if (choiceBox.getValue().equals("Анализы пациента")) {
            comboBoxPatients.setVisible(true);
            comboBoxAnalysis.setVisible(false);
            if (comboBoxPatients.getItems().size() == 0) {
                try {
                    comboBoxPatients.setItems(model.getAllPatientsByDoctorId(app.getCurrentDoctor().getId()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            comboBoxPatients.show();
        } else if (choiceBox.getValue().equals("Полная статистика")) {
            comboBoxPatients.setVisible(false);
            comboBoxAnalysis.setVisible(false);
        }

    }

    @FXML
    private void clickButtonPrint(ActionEvent action) {
        if (app.getCurrentDoctor() == null) {
            choiceBox.getScene().getWindow().hide();
        }

        if (choiceBox.getValue() == null) {
            showMessageError("Ошибка!", "Выберите статистику!");
        } else if (choiceBox.getValue().equals("Пациенты с анализом")) {
            if (comboBoxAnalysis.getValue() == null) {
                showMessageError("Ошибка!", "Выберите анализ!");
                return;
            }

            if (model.printPatientsByAnalysis(comboBoxAnalysis.getValue().getId())) {
                showMessageConfirmation("Успех", "Документ успешно создан");
            } else {
                showMessageError("Ошибка!", "Что-то пошло не так");
            }
        } else if (choiceBox.getValue().equals("Анализы пациента")) {
            if (comboBoxPatients.getValue() == null) {
                showMessageError("Ошибка!", "Выберите пациента!");
                return;
            }

            if (model.printAnalyzes(comboBoxPatients.getValue())) {
                showMessageConfirmation("Успех", "Документ успешно создан");
            } else {
                showMessageError("Ошибка!", "Что-то пошло не так");
            }
        } else if (choiceBox.getValue().equals("Полная статистика")) {
            if (model.printFullStatistic(app.getCurrentDoctor())) {
                showMessageConfirmation("Успех", "Документ успешно создан");
            } else {
                showMessageError("Ошибка!", "Что-то пошло не так");
            }
        }
    }

}
