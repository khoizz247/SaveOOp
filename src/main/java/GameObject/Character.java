package GameObject;

import javafx.scene.canvas.GraphicsContext;

public abstract class Character {
    private double xOnMap;
    private double yOnMap;
    private double size;


    public double getxOnMap() {
        return xOnMap;
    }

    public void setxOnMap(double xOnMap) {
        this.xOnMap = xOnMap;
    }

    public double getyOnMap() {
        return yOnMap;
    }

    public void setyOnMap(double yOnMap) {
        this.yOnMap = yOnMap;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }
}
