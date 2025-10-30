package GameObject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import StartGame.GameApplication;
import javafx.util.Duration;

public class MyBlock extends Block {
    private double speed;
    private double defaultWidth;
    private double increasedWidth = 30;
    private double maxBuffedTime = 5;
    private Timeline currentBuff = null;
    public MyBlock(double speed) {
        this.speed = speed;
    }

    public MyBlock(double width, double height, double speed) {
        super((GameApplication.WIDTH - width) / 2, 570, width, height);
        this.speed = speed;
        this.defaultWidth = width;
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

    public void increaseWidth() {
        if (currentBuff != null) {
            currentBuff.stop();
            currentBuff = null;
        } else {
            setX(getX() - increasedWidth / 2);
            setWidth(defaultWidth + increasedWidth);
        }

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(maxBuffedTime), e -> {
                    setWidth(defaultWidth);
                    setX(getX() + increasedWidth / 2);
                    currentBuff = null;
                })
        );

        currentBuff = timeline;
        timeline.playFromStart();
    }
}