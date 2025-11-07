package Scene;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.fxml.FXMLLoader;

public class ControlLostScene {

    @FXML
    private Label loseLabel;

    private final String message = "YOU LOSE !";

    @FXML
    public void initialize() {
        showTypingEffect();
    }

    /** Hiệu ứng hiện từng chữ của "BẠN ĐÃ THUA" */
    private void showTypingEffect() {
        Timeline timeline = new Timeline();
        StringBuilder textBuilder = new StringBuilder();

        for (int i = 0; i < message.length(); i++) {
            final int index = i;
            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.millis(250 * i), e -> {
                        textBuilder.append(message.charAt(index));
                        loseLabel.setText(textBuilder.toString());
                    })
            );
        }

        // Khi hoàn tất dòng chữ, chờ 2s rồi thoát ra game RPG
        timeline.setOnFinished(e -> {
            Timeline delay = new Timeline(new KeyFrame(Duration.seconds(2), ev -> returnToRPG()));
            delay.play();
        });

        timeline.play();
    }

    /** Quay lại game RPG (home scene) sau khi thua */
    private void returnToRPG() {
        try {
            Stage stage = (Stage) loseLabel.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scene/home-view.fxml"));
            Scene homeScene = new Scene(loader.load(), 800, 600);
            stage.setScene(homeScene);
        } catch (Exception ex) {
            ex.printStackTrace();
            Platform.exit();
        }
    }
}
