package GameObject;

import StartGame.GameApplication;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class ManageBall {
    private List<Ball> balls = new ArrayList<>();

    public ManageBall(double xPaddle, double yPaddle, double widthPaddle) {
        balls.add(new Ball(xPaddle + widthPaddle / 2, yPaddle - 6 - 1, 6, 3, 0, -2));
    }

    public int getNumOfBalls() {
        return balls.size();
    }

    public void resetBall(double xPaddle, double yPaddle, double widthPaddle) {
        balls.clear();
        balls.add(new Ball(xPaddle + widthPaddle / 2, yPaddle - 6 - 1, 6, 3, 0, -2));
    }

    public void addListOnScene(GraphicsContext gc, MyBlock myBlock, List<GameBlock> blocks) {
        for (int i = 0; i < balls.size(); i++) {
            balls.get(i).updateBall(myBlock, blocks);
            balls.get(i).addOnScene(gc);
            if (balls.get(i).checkOutScreen()) {
                balls.remove(i);
                i--;
            }
        }
    }
}