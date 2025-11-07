package Scene;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ControlLostScene {

    @FXML
    private Label loseLabel;

    private final String message = "YOU LOSE !";

    @FXML
    public void initialize() {
        showTypingEffect();
    }

    /** Hiệu ứng hiện từng chữ "YOU LOSE !" rồi tự quay lại home-view.fxml */
    private void showTypingEffect() {
        Timeline timeline = new Timeline();
        StringBuilder textBuilder = new StringBuilder();

        for (int i = 0; i < message.length(); i++) {
            final int index = i;
            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.millis(200 * i), e -> {
                        textBuilder.append(message.charAt(index));
                        loseLabel.setText(textBuilder.toString());
                    })
            );
        }

        // Khi hiện xong dòng chữ thì chờ 2 giây rồi quay lại home scene
        timeline.setOnFinished(e -> {
            Timeline delay = new Timeline(new KeyFrame(Duration.seconds(2), ev -> returnToRPG()));
            delay.play();
        });

        timeline.play();
    }

    /** Quay lại home-view.fxml an toàn trên JavaFX thread */
    private void returnToRPG() {
        Platform.runLater(() -> {
            try {
                Stage stage = (Stage) loseLabel.getScene().getWindow();

                // Đảm bảo đường dẫn đúng và file có trong resources/Scene/
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scene/ingame-view.fxml"));
                Scene homeScene = new Scene(loader.load(), 800, 600);

                stage.setScene(homeScene);
                stage.show();

            } catch (Exception ex) {
                System.err.println("❌ Lỗi khi load ingame-view.fxml: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }
}