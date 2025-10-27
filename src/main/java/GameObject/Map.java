package GameObject;

import LoadResource.LoadImage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import StartGame.GameApplication;

public class Map {
    private double xOnScreen;
    private double yOnScreen;


    public double characterDrawX;
    public double characterDrawY;


    Image map1;
    Image map2;
    Image map3;
    Image nowMap;

    public Map(double xCharOnMap, double yCharOnMap, double charSize) {
        this.map1 = LoadImage.getMap1();
        this.map2 = LoadImage.getMap2();
        this.map3 = LoadImage.getMap3();
        this.nowMap = map1;

        setXYOnScreen(xCharOnMap, yCharOnMap, charSize);
    }

    public double getxOnScreen() {
        return xOnScreen;
    }

    public double getyOnScreen() {
        return yOnScreen;
    }

    private double clamp(double value, double min, double max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }


    public void setXYOnScreen(double xCharOnMap, double yCharOnMap, double charSize) {
        // 1. Tính toán vị trí camera
        double idealX = GameApplication.WIDTH / 2.0 - xCharOnMap;
        double idealY = GameApplication.HEIGHT / 2.0 - yCharOnMap;

        // Trừ đi một nửa kích thước nhân vật để căn giữa
        idealX -= charSize / 2;
        idealY -= charSize / 2;

        double mapWidth = nowMap.getWidth();
        double mapHeight = nowMap.getHeight();

        // 2. Xử lý logic cho trục X (Ngang)
        if (mapWidth < GameApplication.WIDTH) {

            this.xOnScreen = (GameApplication.WIDTH - mapWidth) / 2;
        } else {

            this.xOnScreen = clamp(idealX, GameApplication.WIDTH - mapWidth, 0);
        }

        // 3. Xử lý logic cho trục Y (Dọc)
        if (mapHeight < GameApplication.HEIGHT) {

            this.yOnScreen = (GameApplication.HEIGHT - mapHeight) / 2;
        } else {

            this.yOnScreen = clamp(idealY, GameApplication.HEIGHT - mapHeight, 0);
        }

        // 4. Tính toán vị trí VẼ cuối cùng của nhân vật

        if (mapWidth < GameApplication.WIDTH) {

            this.characterDrawX = xCharOnMap + this.xOnScreen;
        } else {

            this.characterDrawX = clamp(GameApplication.WIDTH / 2.0 - charSize / 2,
                    xCharOnMap + this.xOnScreen,
                    xCharOnMap + this.xOnScreen);

            this.characterDrawX = xCharOnMap + this.xOnScreen;
        }

        if (mapHeight < GameApplication.HEIGHT) {
            this.characterDrawY = yCharOnMap + this.yOnScreen;
        } else {
            this.characterDrawY = yCharOnMap + this.yOnScreen;
        }
    }


    public void addMapOnScreen(GraphicsContext gc) {
        gc.drawImage(nowMap, xOnScreen, yOnScreen);
    }

    public void setMapImage(int mapLevel) {
        if (mapLevel == 1) {
            this.nowMap = map1;
        } else if (mapLevel == 2) {
            this.nowMap = map2;
        } else if (mapLevel == 3) {
            this.nowMap = map3;
        }
    }
}