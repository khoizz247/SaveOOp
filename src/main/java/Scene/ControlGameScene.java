package Scene;

import GameLoop.ScenePlayGame;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.effect.GaussianBlur;

public class ControlGameScene {
    ScenePlayGame scenePlayGame;

    @FXML private Canvas canvas;
    @FXML private VBox pauseMenu;  // thêm fx:id="pauseMenu" trong FXML
    @FXML private Label textMenu;

    private boolean isPaused = false;

    @FXML
    private void initialize() {
        scenePlayGame = new ScenePlayGame();
        scenePlayGame.runGame(canvas);

        // Ẩn menu pause khi bắt đầu
        pauseMenu.setVisible(false);
        textMenu.setVisible(false);

        // Dùng addEventHandler thay vì setOnKeyPressed
        canvas.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE || event.getCode() == KeyCode.P) {
                togglePause();
                event.consume(); // tránh trôi phím vào ScenePlayGame
            }
        });

        ResumeButton.setOnAction(e -> resumeGame());
        RestartButton.setOnAction(e -> restartGame());
        QuitButton.setOnAction(e -> {
            if (scenePlayGame != null && scenePlayGame.isInArkanoid()) {
                quitGameArkanoid(); // nếu đang trong game bắn bóng
            } else {
                quitGameRPG(); // nếu đang ở màn RPG
            }
        });

        addHoverEffect(ResumeButton);
        addHoverEffect(RestartButton);
        addHoverEffect(QuitButton);
    }

    private void togglePause() {
        isPaused = !isPaused;
        pauseMenu.setVisible(isPaused);
        textMenu.setVisible(isPaused);

        if (isPaused) {
            scenePlayGame.pause();
            canvas.setEffect(new GaussianBlur(10));
        } else {
            scenePlayGame.resume();
            canvas.setEffect(null);
        }
    }


    @FXML
    private Button QuitButton;

    @FXML
    private Button RestartButton;

    @FXML
    private Button ResumeButton;

    // Hàm xử lý khi ấn nút "Resume Game"
    private void resumeGame() {
        isPaused = false;
        pauseMenu.setVisible(false);
        textMenu.setVisible(false);

        scenePlayGame.resume();
        canvas.setEffect(null);
        canvas.requestFocus();
    }

    private void quitGameArkanoid() {
        scenePlayGame.resetObject();
        isPaused = false;
        pauseMenu.setVisible(false);
        textMenu.setVisible(false);

        scenePlayGame.quitToMainGame(); // quay lại màn RPG
        canvas.setEffect(null);         // <---- Tắt hiệu ứng mờ
        canvas.requestFocus();          // lấy lại điều khiển nhân vật
    }


    // Hàm xử lý khi ấn nút "Quit Game" game RPG
    private void quitGameRPG() {
        try {
            scenePlayGame.saveData();
            Stage stage = (Stage) QuitButton.getScene().getWindow();
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/Scene/menu-view.fxml"));
            javafx.scene.Parent root = loader.load();
            stage.getScene().setRoot(root); // chuyển về menu
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void restartGame() {
        isPaused = false;
        pauseMenu.setVisible(false);
        textMenu.setVisible(false);
        canvas.setEffect(null);

        if (scenePlayGame != null) {
            if (scenePlayGame.isIngame()) {
                scenePlayGame.restartArkanoid(canvas);
            } else {
                scenePlayGame.restartRPG(canvas);
            }
        }

        canvas.requestFocus();
    }

    private void addHoverEffect(Button button) {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.WHITE);
        glow.setRadius(20);

        button.setOnMouseEntered(e -> {
            button.setEffect(glow);
            button.setTextFill(Color.WHITE);
            button.setScaleX(1.05);
            button.setScaleY(1.05);
        });

        button.setOnMouseExited(e -> {
            button.setEffect(null);
            button.setTextFill(Color.WHITE);
            button.setScaleX(1.0);
            button.setScaleY(1.0);
        });
    }
}
