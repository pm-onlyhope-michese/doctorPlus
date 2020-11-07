package sample.controller.baseController;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import sample.controller.App;

import java.util.Optional;

public abstract class Controller {
    protected App app;

    public Controller() {
        app = App.getInstance();
    }

    public static void showMessageError(String title, String textMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(textMessage);
        alert.showAndWait();
    }

    public static boolean showMessageConfirmation(String title, String textMessage) {
        boolean result = false;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(textMessage);

        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            result = true;
        }

        return result;
    }
}


