package GameObject;

import LoadResource.LoadImage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Buff extends Circle{
    private final String buffType;
    private final Ball ballCreateBuff;
    private int state = 0;

    public Buff(double x, double y, double radius, double speed, double dx, double dy, String buffType) {
        super(x, y, radius, speed, dx, dy);
        this.buffType = buffType;
        this.ballCreateBuff = null;
    }

    public Buff(double xMyBlock, double yMyBlock, double widthMyBlock, double heightMyBlock,
                double radius, double speed, double dx, double dy, String buffType, Ball ballCreateBuff) {
        super(xMyBlock + widthMyBlock / 2, yMyBlock + heightMyBlock + radius, radius, speed, dx, dy);
        this.buffType = buffType;
        this.ballCreateBuff = ballCreateBuff;
    }

    public String updateBuff (MyBlock paddle, ManageBall manageBall) {
        setBallX(getBallX() + getDx());
        setBallY(getBallY() + getDy());

        if (checkContactToBlock(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight())) {
            if (buffType.equals("Add 3 balls")) {
                manageBall.addBall(paddle.getX(), paddle.getY(), paddle.getWidth());
            } else if (buffType.equals("Clone Ball")) {
                manageBall.buffCloneBall(ballCreateBuff);
            } else if (buffType.equals("Increase Paddle Width")) {
                paddle.increaseWidth();
            } else if (buffType.equals("Bullet")) {
                return "AIMING";
            } else if (buffType.equals("Coin")) {
                ManageBuff.extraCoins++;
            } else if (buffType.equals("Heart")) {
                paddle.setLife(paddle.getLife() + 1);
                System.out.println(paddle.getLife());
            } else if (buffType.equals("Obstacle")) {
                paddle.setLife(paddle.getLife() - 1);
                paddle.startBlinkingEffect();
                System.out.println(paddle.getLife());
            }
            return "HIT";
        }
        return "NOHIT";
    }

    @Override
    public void addOnScene(GraphicsContext gc) {
        if (state > 23) {
            state = 0;
        }
        if (buffType.equals("Coin")) {
            gc.drawImage(LoadImage.getCoin()[state / 4], getBallX() - getRadius(), getBallY() - getRadius(),
                    getRadius() * 2, getRadius() * 2);
        } else if (buffType.equals("Obstacle")) {
            gc.drawImage(LoadImage.getFireBall()[state / 6], getBallX() - getRadius() * 2 + 1, getBallY() - getRadius() * 2,
                    getRadius() * 2 * 2, getRadius() * 2 * 2);
        } else {
            Image currentImage = getImage();

            double angleRad = Math.toRadians(state * 15.0);
            double scaleX = Math.cos(angleRad);
            double x = getBallX();
            double y = getBallY();
            double r = getRadius();
            double size = r * 2;

            gc.save();
            gc.translate(x, y);
            gc.scale(scaleX, 1.0);
            gc.drawImage(currentImage, -r, -r, size, size);
            gc.restore();
        }
        state++;
    }

    @Nullable
    private Image getImage() {
        Image currentImage = null;
        if (buffType.equals("Add 3 balls")) {
            currentImage = LoadImage.getAddThreeeBalls();
        } else if (buffType.equals("Clone Ball")) {
            currentImage = LoadImage.getCloneBall();
        } else if (buffType.equals("Increase Paddle Width")) {
            currentImage = LoadImage.getIncreasePaddle();
        } else if (buffType.equals("Bullet")) {
            currentImage = LoadImage.getBullet();
        } else if (buffType.equals("Heart")) {
            currentImage = LoadImage.getHeart();
        }
        return currentImage;
    }
}