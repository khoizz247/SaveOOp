package LoadResource;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class LoadVideo {

    public static void playIntroVideo(Stage stage, Runnable onFinish) {
        try {
            // Nạp video từ thư mục resources
            var resource = LoadVideo.class.getResource("/Video/Video_intro.mp4");

            if (resource == null) {
                System.err.println("Lậy bố, không tìm thấy video");
                onFinish.run();
                return;
            }

            Media media = new Media(resource.toExternalForm());
            MediaPlayer player = new MediaPlayer(media);
            MediaView view = new MediaView(player);

            view.setPreserveRatio(true);
            view.setFitWidth(800);
            view.setFitHeight(600);

            StackPane root = new StackPane(view);
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);

            // video chạy xong
            player.setOnEndOfMedia(() -> {
                player.dispose();
                Platform.runLater(onFinish);
            });

            // nhấn phím Enter → skip video
            scene.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    player.stop();
                    player.dispose();
                    Platform.runLater(onFinish);
                }
            });

            player.play();

        } catch (Exception e) {
            e.printStackTrace();
            onFinish.run();
        }
    }
}
