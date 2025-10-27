package Scene;

import LoadResource.LoadVideo;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
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
        // ====== GẮN SỰ KIỆN CHO NÚT ======

        if (StartButton != null) {
            StartButton.setOnAction(this::startGame);
            addHoverEffect(StartButton);
        }

        if (SettingButton != null) {
            SettingButton.setOnAction(this::openSetting);
            addHoverEffect(SettingButton);
        }

        if (QuitButton != null) {
            QuitButton.setOnAction(e -> quitGame());
            addHoverEffect(QuitButton);
        }

        // Bỏ focus mặc định để tránh tự kích hoạt
        Platform.runLater(() -> {
            if (StartButton != null && StartButton.getParent() != null) {
                StartButton.getParent().requestFocus();
            }
        });
    }

    // Khi ấn "Start Game"
    private void startGame(ActionEvent event) {
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Gọi hàm phát video intro
            LoadVideo.playIntroVideo(stage, () -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scene/ingame-view.fxml"));
                    Scene scene = new Scene(loader.load());
                    stage.setScene(scene);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Khi ấn "Setting"
    private void openSetting(ActionEvent event) {
        System.out.println("⚙️ Mở giao diện cài đặt (chưa triển khai).");
        // TODO: sau này có thể mở setting-view.fxml nếu cần
    }

    // Khi ấn "Quit Game"
    private void quitGame() {
        Stage stage = (Stage) QuitButton.getScene().getWindow();
        stage.close();
        Platform.exit();
        System.exit(0);
    }

    // ====== HIỆU ỨNG HOVER ======
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