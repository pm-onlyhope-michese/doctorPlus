package sample;

import javafx.application.Application;
import javafx.stage.Stage;
import sample.config.Config;
import sample.controller.App;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Config.initialize();

        App.getInstance().setScene(primaryStage, Config.pathDoctorAuthorization, Config.paths.get(Config.pathDoctorAuthorization));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
