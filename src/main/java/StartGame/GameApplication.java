package StartGame;

import LoadResource.LoadImage;
import LoadResource.LoadVideo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameApplication extends Application {

    public static final int WIDTH = 800;

    public static final int HEIGHT = 600;

    @Override
    public void start(Stage stage) throws IOException {
        LoadImage.loadAllImage();
//        LoadVideo.loadAllVideo();

        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("/Scene/menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        stage.setTitle("Arkanoid");
        stage.setScene(scene);
        stage.show();
    }
}
