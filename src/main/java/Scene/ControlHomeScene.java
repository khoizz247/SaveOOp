package Scene;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControlHomeScene {

    @FXML
    private Button startButton;

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            if (startButton != null && startButton.getParent() != null) {
                startButton.getParent().requestFocus();
            }
        });
    }

    @FXML
    private void startGame(ActionEvent event) throws Exception {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ingame-view.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("My Game");
    }

    @FXML
    private void quitGame(ActionEvent event) {
        Platform.exit();
    }
}
