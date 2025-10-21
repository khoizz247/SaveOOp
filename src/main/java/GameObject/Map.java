package GameObject;

import LoadResource.LoadImage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import StartGame.GameApplication;

public class Map {
    private double xOnScreen;
    private double yOnScreen;

    Image map1;
    Image nowMap;

    public Map(double xCharOnMap, double yCharOnMap, double charSize) {
        this.xOnScreen = GameApplication.WIDTH / 2.0 - xCharOnMap - charSize / 2;
        this.yOnScreen = GameApplication.HEIGHT / 2.0 - yCharOnMap - charSize / 2;

        this.map1 = LoadImage.getMap1();
        this.nowMap = map1;
    }

    public double getxOnScreen() {
        return xOnScreen;
    }

    public void setxOnScreen(double xCharOnMap, double charSize) {
        this.xOnScreen = GameApplication.WIDTH / 2.0 - xCharOnMap - charSize / 2;
    }

    public double getyOnScreen() {
        return yOnScreen;
    }

    public void setyOnScreen(double yCharOnMap, double charSize) {
        this.yOnScreen = GameApplication.HEIGHT / 2.0 - yCharOnMap - charSize / 2;
    }

    public void setXYOnScreen(double xCharOnMap, double yCharOnMap, double charSize) {
        this.xOnScreen = GameApplication.WIDTH / 2.0 - xCharOnMap - charSize / 2;
        this.yOnScreen = GameApplication.HEIGHT / 2.0 - yCharOnMap - charSize / 2;
    }

    public void addMapOnScreen(GraphicsContext gc) {
        gc.drawImage(nowMap, xOnScreen, yOnScreen);
    }
}

