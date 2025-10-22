package GameObject;

import LoadResource.LoadImage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import StartGame.GameApplication;

public class MainCharacter extends Character {
    private double speed = 3;
    private int state = 0;
    private int direction = 0;
    private boolean isRunning = false;

    private final Image[] idleAhead;
    private final Image[] idleBehind;
    private final Image[] idleLeft;
    private final Image[] idleRight;

    private final Image[] runAhead;
    private final Image[] runBehind;
    private final Image[] runLeft;
    private final Image[] runRight;

    public MainCharacter() {
        super.setxOnMap(200);
        super.setyOnMap(200);

        this.idleAhead = LoadImage.getIdleAhead();
        this.idleBehind = LoadImage.getIdleBehind();
        this.idleLeft = LoadImage.getIdleLeft();
        this.idleRight = LoadImage.getIdleRight();

        this.runAhead = LoadImage.getRunAhead();
        this.runBehind = LoadImage.getRunBehind();
        this.runLeft = LoadImage.getRunLeft();
        this.runRight = LoadImage.getRunRight();

        super.setSize(this.idleAhead[0].getWidth() * 2);
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    //Render hoat anh nhan vat
    public void addCharacterOnScreen(GraphicsContext gc) {
        Image[] currentAnimation;
        int frameSkip;
        if (isRunning) {
            if (state > 47) {
                state = 0;
            }
            if (direction == 0) {
                currentAnimation = runAhead;
            } else if (direction == 1) {
                currentAnimation = runLeft;
            } else if (direction == 2) {
                currentAnimation = runRight;
            } else {
                currentAnimation = runBehind;
            }
            frameSkip = 6;
        } else {
            if (state > 71) {
                state = 0;
            }
            if (direction == 0) {
                currentAnimation = idleAhead;
                frameSkip = 6;
            } else if (direction == 1) {
                currentAnimation = idleLeft;
                frameSkip = 6;
            } else if (direction == 2) {
                currentAnimation = idleRight;
                frameSkip = 6;
            } else {
                currentAnimation = idleBehind;
                frameSkip = 18;
            }
        }
        gc.drawImage(currentAnimation[state / frameSkip],
                GameApplication.WIDTH / 2.0 - getSize() / 2,
                GameApplication.HEIGHT / 2.0 - getSize() / 2, getSize(), getSize());
        state ++;
    }
}
