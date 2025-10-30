package GameObject;

import StartGame.GameApplication;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Circle {
    private double ballX;
    private double ballY;
    private double radius = 6;
    private double speed = 4;
    private double dx = 0;
    private double dy = -2;
    boolean isInScreen;

    public Circle(double ballX, double ballY, double radius, double speed, double dx, double dy) {
        this.ballX = ballX;
        this.ballY = ballY;
        this.radius = radius;
        this.speed = speed;
        this.dx = dx;
        this.dy = dy;
        this.isInScreen = true;
    }

    public double getBallX() {
        return ballX;
    }

    public void setBallX(double ballX) {
        this.ballX = ballX;
    }

    public double getBallY() {
        return ballY;
    }

    public void setBallY(double ballY) {
        this.ballY = ballY;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getRadius() {
        return radius;
    }

    private void setRadius(double radius) {
        this.radius = radius;
    }

    private double clamp(double value, double min, double max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    public boolean isInScreen() {
        return isInScreen;
    }

    public void setInScreen(boolean inScreen) {
        isInScreen = inScreen;
    }

    public boolean checkContactToBlock(double xBlock, double yBlock, double widthBlock, double heightBlock) {
        double closestX = clamp(ballX, xBlock, xBlock + widthBlock);
        double closestY = clamp(ballY, yBlock, yBlock + heightBlock);
        double dx = ballX - closestX;
        double dy = ballY - closestY;
        return (dx * dx + dy * dy) <= (radius * radius);
    }

    public boolean checkOutScreen() {
        if ((getBallY() - getRadius()) > GameApplication.HEIGHT) {
            isInScreen = false;
            return true;
        }
        return false;
    }

    public void addOnScene(GraphicsContext gc) {
        gc.setFill(Color.ORANGE);
        gc.fillOval(ballX - radius, ballY - radius, radius * 2, radius * 2);
    }
}
