package Scene;

import GameLoop.ScenePlayGame;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;

public class ControlGameScene {
    ScenePlayGame scenePlayGame;

    @FXML private Canvas canvas;
    @FXML
    private void initialize() {
        scenePlayGame = new ScenePlayGame();
        scenePlayGame.runGame(canvas);
    }
}
