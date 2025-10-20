package Scene;

import GameLoop.ScenePlayGame;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Node;

public class ControlGameScene {

    @FXML
    private Button QuitButton;

    @FXML
    private Button RestartButton;

    @FXML
    private Button ResumeButton;



    // Hàm xử lý khi ấn nút "Quit Game"
    private void quitGame() {
        Stage stage = (Stage) QuitButton.getScene().getWindow();
        stage.close();      // Đóng cửa sổ hiện tại
        Platform.exit();    // Thoát ứng dụng JavaFX
        System.exit(0);     // Thoát hẳn JVM (đảm bảo tắt hoàn toàn)
    }

}
