package GameObject;

import LoadResource.LoadImage;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import StartGame.GameApplication;
import javafx.util.Duration;

public class MyBlock extends Block {
    private int life;
    private double speed;
    private double defaultWidth;
    private double increasedWidth;
    private double maxBuffedTime;
    private Timeline currentBuff = null;
    private Timeline currentBlinkingEffect;
    private DoubleProperty opacity = new SimpleDoubleProperty(1.0);
    private final Image image;
    public MyBlock(double speed) {
        this.speed = speed;
        this.image = LoadImage.getPaddle();
        life = 2;
        increasedWidth = 30;
        maxBuffedTime = 5;
    }

    public MyBlock(double width, double height, double speed) {
        super((GameApplication.WIDTH - width) / 2, 570, width, height);
        this.speed = speed;
        this.image = LoadImage.getPaddle();
        this.defaultWidth = width;
        this.life = 2;
        this.increasedWidth = 30;
        this.maxBuffedTime = 5;
    }

    public void resetMyBlock() {
        setX((GameApplication.WIDTH - getWidth()) / 2);
        setWidth(defaultWidth);
        stopAllBlinking();
        if (currentBuff != null) {
            currentBuff.stop();
            currentBuff = null;
        }
        life = 2;
        setOpacity(1.0);
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
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

    public double getOpacity() {
        return opacity.get();
    }

    public void setOpacity(double value) {
        opacity.set(value);
    }

    public DoubleProperty opacityProperty() {
        return opacity;
    }

    private void stopAllBlinking() {
        if (currentBlinkingEffect != null) {
            currentBlinkingEffect.stop();
            currentBlinkingEffect = null;
        }
        setOpacity(1.0);
    }

    public void increaseWidth() {
        if (currentBuff != null) {
            currentBuff.stop();
        }
        playBlinkingEffect(0.3, 6);

        if (currentBuff == null) {
            setX(getX() - increasedWidth / 2);
            setWidth(defaultWidth + increasedWidth);
        }

        currentBuff = new Timeline(
                new KeyFrame(Duration.seconds(maxBuffedTime), e -> {
                    currentBuff = null;
                    triggerEndEffect();
                })
        );
        currentBuff.playFromStart();
    }

    private void triggerEndEffect() {
        setWidth(defaultWidth);
        setX(getX() + increasedWidth / 2);

        playBlinkingEffect(0.1, 6);
    }

    public void startBlinkingEffect() {
        playBlinkingEffect(0.1, 6);
    }

    private void playBlinkingEffect(double targetOpacity, int cycles) {
        // 1. Dừng mọi hiệu ứng cũ
        stopAllBlinking();

        // 2. Tạo hiệu ứng mới
        currentBlinkingEffect = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(opacityProperty(), 1.0)),
                new KeyFrame(Duration.millis(50), new KeyValue(opacityProperty(), targetOpacity))
        );
        currentBlinkingEffect.setCycleCount(cycles);
        currentBlinkingEffect.setAutoReverse(true);

        // 3. Đặt sự kiện khi hoàn thành
        currentBlinkingEffect.setOnFinished(e -> {
            setOpacity(1.0);
            currentBlinkingEffect = null; // Xóa tham chiếu khi chạy xong
        });

        // 4. Chạy hiệu ứng
        currentBlinkingEffect.play();
    }

    @Override
    public void addOnScene(GraphicsContext gc) {
        gc.setGlobalAlpha(getOpacity());
        gc.drawImage(image, getX(), getY(), getWidth(), getHeight());
        gc.setGlobalAlpha(1.0);
    }
}