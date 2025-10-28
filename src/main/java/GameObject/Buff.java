package GameObject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class Buff extends Circle{
    private String buffType;
    private Ball ballCreateBuff;

    public Buff(double xMyBlock, double yMyBlock, double widthMyBlock, double heightMyBlock,
                double radius, double speed, double dx, double dy, String buffType, Ball ballCreateBuff) {
        super(xMyBlock + widthMyBlock / 2, yMyBlock + heightMyBlock + radius, radius, speed, dx, dy);
        this.buffType = buffType;
        this.ballCreateBuff = ballCreateBuff;
    }

    public boolean updateBuff (MyBlock myBlock, ManageBall manageBall) {
        setBallX(getBallX() + getDx());
        setBallY(getBallY() + getDy());

        if (checkContactToBlock(myBlock.getX(), myBlock.getY(), myBlock.getWidth(), myBlock.getHeight())) {
            if (buffType.equals("Add 3 balls")) {
                manageBall.addBall(myBlock.getX(), myBlock.getY(), myBlock.getWidth());
            } else if (buffType.equals("Clone Ball")) {
                manageBall.buffCloneBall(ballCreateBuff);
            } else if (buffType.equals("Increase Paddle Width")) {
                myBlock.increaseWidth();
            }
            return true;
        }
        return false;
    }

    @Override
    public void addOnScene(GraphicsContext gc) {
        if (buffType.equals("Add 3 ball")) {
            gc.setFill(Color.GREEN);
        } else if (buffType.equals("Clone Ball")) {
            gc.setFill(Color.BLUE);
        } else if (buffType.equals("Increase Paddle Width")) {
            gc.setFill(Color.RED);
        }
        gc.fillOval(getBallX() - getRadius(), getBallY() - getRadius(),
                getRadius() * 2, getRadius() * 2);
    }
}