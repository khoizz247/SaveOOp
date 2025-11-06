package Scene;

import LoadResource.LoadVideo;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import GameLoop.ReadWriteData;

public class ControlHomeScene {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private VBox oldMenuVBox; // Menu gốc (Start / Setting / Quit)

    @FXML
    private VBox newMenuVBox; // Menu mới (New Game / Continue / Cancel)

    @FXML
    private Button StartButton, SettingButton, QuitButton;
    @FXML
    private Button NewGameButton, ContinueButton, CancelButton;

    @FXML
    public void initialize() {
        // Hiệu ứng hover cho tất cả nút
        addHoverEffect(StartButton);
        addHoverEffect(SettingButton);
        addHoverEffect(QuitButton);
        addHoverEffect(NewGameButton);
        addHoverEffect(ContinueButton);
        addHoverEffect(CancelButton);

        // Ẩn menu mới ban đầu
        newMenuVBox.setVisible(false);
        newMenuVBox.setManaged(false);
        newMenuVBox.setOpacity(0);

        // Gắn sự kiện nút
        StartButton.setOnAction(this::showNewMenuAnimated);
        SettingButton.setOnAction(this::openSetting);
        QuitButton.setOnAction(e -> quitGame());

        // Nút New Game đã đúng, giữ nguyên logic
        NewGameButton.setOnAction(e -> startNewGame((Stage) StartButton.getScene().getWindow()));

        // SỬA ĐỔI: Nút Continue bây giờ sẽ gọi hàm continueGame
        ContinueButton.setOnAction(e -> continueGame((Stage) ContinueButton.getScene().getWindow()));

        CancelButton.setOnAction(e -> hideNewMenuAnimated());

        // Tránh focus tự động
        Platform.runLater(() -> oldMenuVBox.requestFocus());
    }

    /** Hiệu ứng chuyển từ menu chính sang menu New Game */
    private void showNewMenuAnimated(ActionEvent event) {
        newMenuVBox.setVisible(true);
        newMenuVBox.setManaged(true);

        TranslateTransition moveLeft = new TranslateTransition(Duration.millis(600), oldMenuVBox);
        moveLeft.setToX(-400);
        FadeTransition fadeOut = new FadeTransition(Duration.millis(400), oldMenuVBox);
        fadeOut.setToValue(0);

        TranslateTransition moveIn = new TranslateTransition(Duration.millis(600), newMenuVBox);
        moveIn.setFromX(400);
        moveIn.setToX(0);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), newMenuVBox);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ParallelTransition outAnim = new ParallelTransition(moveLeft, fadeOut);
        ParallelTransition inAnim = new ParallelTransition(moveIn, fadeIn);

        outAnim.setOnFinished(e -> {
            oldMenuVBox.setVisible(false);
            oldMenuVBox.setManaged(false);
            inAnim.play();
        });

        outAnim.play();
    }

    /** Quay lại menu chính */
    @FXML
    private void hideNewMenuAnimated() {
        oldMenuVBox.setVisible(true);
        oldMenuVBox.setManaged(true);

        TranslateTransition moveRight = new TranslateTransition(Duration.millis(600), newMenuVBox);
        moveRight.setToX(400);
        FadeTransition fadeOut = new FadeTransition(Duration.millis(400), newMenuVBox);
        fadeOut.setToValue(0);

        TranslateTransition moveBack = new TranslateTransition(Duration.millis(600), oldMenuVBox);
        moveBack.setFromX(-400);
        moveBack.setToX(0);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(400), oldMenuVBox);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ParallelTransition outAnim = new ParallelTransition(moveRight, fadeOut);
        ParallelTransition inAnim = new ParallelTransition(moveBack, fadeIn);

        outAnim.setOnFinished(e -> {
            newMenuVBox.setVisible(false);
            newMenuVBox.setManaged(false);
            newMenuVBox.setOpacity(0);
            inAnim.play();
        });

        outAnim.play();
    }

    /** Bắt đầu trò chơi mới (New Game) */
    private void startNewGame(Stage stage) {
        try {
            LoadVideo.playIntroVideo(stage, () -> {
                try {
                    // 🧩 Reset toàn bộ dữ liệu về mặc định
                    ReadWriteData.resetAllGameData();

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scene/ingame-view.fxml"));
                    Scene scene = new Scene(loader.load(), 800, 600);
                    stage.setScene(scene);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** (HÀM MỚI) Tiếp tục trò chơi (Continue Game) */
    private void continueGame(Stage stage) {
        try {
            // đọc file lưu
            ReadWriteData.loadGameData();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scene/ingame-view.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi khi tải game (hoặc chưa có file save).");
        }
    }


    /** Mở cài đặt */
    private void openSetting(ActionEvent event) {
        System.out.println("⚙️ Mở giao diện cài đặt (chưa triển khai).");
    }

    /** Thoát game */
    private void quitGame() {
        Stage stage = (Stage) QuitButton.getScene().getWindow();
        stage.close();
        Platform.exit();
        System.exit(0);
    }

    /** Hiệu ứng hover cho nút */
    private void addHoverEffect(Button button) {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.WHITE);
        glow.setRadius(20);

        button.setOnMouseEntered(e -> {
            button.setEffect(glow);
            button.setScaleX(1.05);
            button.setScaleY(1.05);
        });

        button.setOnMouseExited(e -> {
            button.setEffect(new DropShadow(5, Color.BLACK));
            button.setScaleX(1.0);
            button.setScaleY(1.0);
        });
    }
}