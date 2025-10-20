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
    private Button QuitButton;

    @FXML
    private Button SettingButton;

    @FXML
    private Button StartButton;

    @FXML
    public void initialize() {
        // Gắn sự kiện cho nút "Start Game"
        if (StartButton != null) {
            StartButton.setOnAction(event -> {
                try {
                    startGame(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        // Gắn sự kiện cho nút "Quit Game"
        if (QuitButton != null) {
            QuitButton.setOnAction(event -> quitGame());
        }

        // Bỏ focus mặc định để tránh tự kích hoạt
        Platform.runLater(() -> {
            if (StartButton != null && StartButton.getParent() != null) {
                StartButton.getParent().requestFocus();
            }
        });
    }

    // Hàm xử lý khi ấn nút "Start Game"
    private void startGame(ActionEvent event) throws Exception {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/myarkanoid/ongame-view.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("My Game");
    }

    // Hàm xử lý khi ấn nút "Quit Game"
    private void quitGame() {
            Stage stage = (Stage) QuitButton.getScene().getWindow();
            stage.close();       // Đóng cửa sổ hiện tại
            Platform.exit();    // Thoát ứng dụng JavaFX
            System.exit(0);     // Thoát hẳn JVM (đảm bảo tắt hoàn toàn)
    }
}
