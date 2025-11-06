module Scene {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires com.almasb.fxgl.core;
    requires javafx.media;
    requires annotations;

    opens Scene to javafx.fxml;
    opens GameObject to javafx.fxml;
    opens StartGame to javafx.fxml;

    exports Scene;
    exports GameObject;
    exports GameLoop;
    exports StartGame;
    exports LoadResource;
}
