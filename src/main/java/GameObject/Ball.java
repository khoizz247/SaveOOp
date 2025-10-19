package GameObject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import StartGame.GameApplication;
import java.awt.*;
import java.util.List;

public class Ball {
    private double ballX;
    private double ballY;
    private double radius = 6;
    private double speed = 4;
    private double dx = 0;
    private double dy = -2;

    public Ball() {

    }

    public Ball(double x, double y) {
        this.ballX = x;
        this.ballY = y;
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

    public void setDirection() {
        double length = Math.sqrt(dx * dx + dy * dy);
        if (length == 0) return;
        dx = (dx / length) * speed;
        dy = (dy / length) * speed;
    }

    public double getRadius() {
        return radius;
    }

    private void setRadius(double radius) {
        this.radius = radius;
    }

    public void addOnScene(GraphicsContext gc) {
        gc.setFill(Color.ORANGE);
        gc.fillOval(ballX - radius, ballY - radius, radius * 2, radius * 2);
    }

    private double clamp(double value, double min, double max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    private boolean checkContactToBlock(Block block) {
        double closestX = clamp(ballX, block.getX(), block.getX() + block.getWidth());
        double closestY = clamp(ballY, block.getY(), block.getY() + block.getHeight());
        double dx = ballX - closestX;
        double dy = ballY - closestY;
        return (dx * dx + dy * dy) <= (radius * radius);
    }

    public void updateBall(MyBlock myBlock, List<GameBlock> blocks) {
        ballX += dx;
        ballY += dy;

        //Va chạm góc
        if (ballX - radius <= 0) {
            ballX = radius;
            dx = -dx;
        } else if (ballX + radius >= GameApplication.WIDTH) {
            ballX = GameApplication.WIDTH - radius;
            dx = -dx;
        }
        if (ballY - radius <= 0) {
            ballY = radius;
            dy = -dy;
        }

        // Va cham paddle
        if (checkContactToBlock(myBlock)) {
            double relativeIntersectX = (ballX - (myBlock.getX() + myBlock.getWidth() / 2));
            double normalized = relativeIntersectX / (myBlock.getWidth() / 2);
            double bounceAngle = normalized * (Math.PI / 3); // tối đa 60 độ

            dx = speed * Math.sin(bounceAngle);
            dy = -speed * Math.cos(bounceAngle);

            ballY = myBlock.getY() - radius - 1;
        }

        // Va cham block
        GameBlock collidedBlock = null;
        for (GameBlock b : blocks) {
            if (checkContactToBlock(b)) {
                collidedBlock = b;
                break;
            }
        }

        if (collidedBlock != null) {
            collidedBlock.contactGameBlock(this);
            if (collidedBlock.handleBlock()) {
                blocks.remove(collidedBlock);
            }
        }

        setDirection();
    }

}