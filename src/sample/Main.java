package sample;

import javafx.application.Application;
import javafx.stage.Stage;
import sample.config.Config;
import sample.controller.App;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage){
        Config config = new Config();
        App.getInstance().setScene(primaryStage, config.pathDoctorAuthorization, config.getPath(config.pathDoctorAuthorization));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
