package GameObject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Block {
    private double X;
    private double Y;
    private double width;
    private double height;

    //Image graphic;

    public Block() {

    }

    public Block(double x, double y, double width, double height) {
        this.X = x;
        this.Y = y;
        this.width = width;
        this.height = height;
    }

    public double getX() {
        return X;
    }

    public void setX(double x) {
        X = x;
    }

    public double getY() {
        return Y;
    }

    public void setY(double y) {
        Y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void addOnScene (GraphicsContext gc) {
        //gc.drawImage(graphic, X, Y, width, height);
        gc.setFill(Color.RED);
        gc.fillRect(X, Y, width, height);
    }

}