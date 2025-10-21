package Scene;

import GameLoop.ScenePlayGame;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;

public class ControlGameScene {
    ScenePlayGame scenePlayGame;

    @FXML private Canvas canvas;
    @FXML
    private void initialize() {
        scenePlayGame = new ScenePlayGame();
        scenePlayGame.runGame(canvas);
    }

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
