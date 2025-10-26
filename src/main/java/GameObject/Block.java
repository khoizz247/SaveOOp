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

        gc.setFill(Color.RED);
        gc.fillRect(X, Y, width, height);
    }

    public void contactGameBlock(Ball ball) {
        double overlapLeft = (ball.getBallX() + ball.getRadius()) - X;
        double overlapRight = (X + width) - (ball.getBallX() - ball.getRadius());
        double overlapTop = (ball.getBallY() + ball.getRadius()) - Y;
        double overlapBottom = (Y + height) - (ball.getBallY() - ball.getRadius());

        boolean ballFromLeft = Math.abs(overlapLeft) < Math.abs(overlapRight);
        boolean ballFromTop = Math.abs(overlapTop) < Math.abs(overlapBottom);

        double minOverlapX = ballFromLeft ? overlapLeft : -overlapRight;
        double minOverlapY = ballFromTop ? overlapTop : -overlapBottom;

        if (Math.abs(minOverlapX) < Math.abs(minOverlapY)) {
            // Va chạm theo trục X
            ball.setBallX(ball.getBallX() - minOverlapX);
            ball.setDx(-ball.getDx());
        } else {
            // Va chạm theo trục Y
            ball.setBallY(ball.getBallY() - minOverlapY);
            ball.setDy(-ball.getDy());
        }
    }

}