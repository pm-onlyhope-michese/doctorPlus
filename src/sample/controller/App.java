package sample.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.classes.Doctor;
import sample.classes.Patient;
import sample.config.Config;

import java.io.IOException;

public class App {
    private static volatile App instance;
    private Patient currentPatient;
    private Doctor currentDoctor;
    public Stage stage;

    public void setCurrentDoctor(Doctor currentDoctor) {
        this.currentDoctor = currentDoctor;
    }
    public void setCurrentDoctor() {
        this.currentDoctor = null;
    }

    public void setCurrentPatient(Patient currentPatient) {
        this.currentPatient = currentPatient;
    }
    public void setCurrentPatient() {
        this.currentPatient = null;
    }


    public Patient getCurrentPatient() {
        return currentPatient;
    }
    public Doctor getCurrentDoctor() {
        return currentDoctor;
    }

    private App() {
        stage = null;
        currentPatient = null;
        currentDoctor = null;
        System.out.println("Create App");
    }

    public static App getInstance() {
        if (instance == null) {
            synchronized (App.class) {
                if (instance == null) {
                    instance = new App();
                }
            }
        }
        return instance;
    }

    public void setScene(Stage newScene, String pathResource, String newTitle) throws IOException {
        stage = newScene;
        Parent root = FXMLLoader.load(getClass().getResource(pathResource));
        stage.setTitle(newTitle);
        stage.setScene(new Scene(root, Config.widthApp, Config.heightApp));
        stage.show();
    }

    public void setScene(String pathResource, String newTitle) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(pathResource));
        double height = stage.getScene().getHeight();
        double width = stage.getScene().getWidth();
        stage.setTitle(newTitle);
        stage.setScene(new Scene(root, width, height));
        stage.show();
    }

}
