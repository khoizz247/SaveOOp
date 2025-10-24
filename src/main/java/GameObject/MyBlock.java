package GameObject;

import javafx.scene.image.Image;
import StartGame.GameApplication;

public class MyBlock extends Block {
    private double speed;

    public MyBlock(double speed) {
        this.speed = speed;
    }

    public MyBlock(double width, double height, double speed) {
        super((GameApplication.WIDTH - width) / 2, 500, width, height);
        this.speed = speed;
    }

    public void resetMyBlock() {
        setX((GameApplication.WIDTH - getWidth()) / 2);
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void collisionHandling () {
        if ((getX() + getWidth()) > GameApplication.WIDTH) {
            setX(GameApplication.WIDTH - getWidth());
        } else if ((getX() < 0)) {
            setX(0);
        }
    }
}