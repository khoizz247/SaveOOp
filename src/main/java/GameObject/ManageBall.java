package GameObject;

import StartGame.GameApplication;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class ManageBall {
    private List<Ball> balls = new ArrayList<>();

    public ManageBall(double yMyBlock) {
        balls.add(new Ball(yMyBlock));
    }

    public void addListOnScene(GraphicsContext gc, MyBlock myBlock, List<GameBlock> blocks) {
        for (Ball b : balls) {
            b.updateBall(myBlock, blocks);
            b.addOnScene(gc);
        }
    }
}