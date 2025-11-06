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
import GameLoop.ReadWriteData;

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
//    public MyBlock(double speed) {
//        this.speed = speed;
//        this.image = LoadImage.getPaddle();
//        life = 2;
//        increasedWidth = 30;
//        maxBuffedTime = 5;
//    }

    public MyBlock(double width, double height, double speed) {
        // --- ĐÃ SỬA LỖI ---
        // 1. GỌI SUPER() NGAY LẬP TỨC
        //    và gọi các hàm ReadWriteData BÊN TRONG super()
        super((GameApplication.WIDTH - ReadWriteData.getBaseWidth()) / 2, 570, ReadWriteData.getBaseWidth(), height);

        // 2. Gán các giá trị cho MyBlock (sau khi super đã chạy)
        this.speed = ReadWriteData.getBaseSpeed();
        this.life = ReadWriteData.getBaseLife();

        // 3. Lấy lại giá trị width mà super() vừa set
        //    (Cách này tốt hơn là gọi ReadWriteData.getBaseWidth() 2 lần)
        this.defaultWidth = super.getWidth();

        // 4. Các giá trị không đổi
        this.image = LoadImage.getPaddle();
        this.increasedWidth = 30;
        this.maxBuffedTime = 5;
    }

    public void resetMyBlock() {
        // 1. Lấy các giá trị đã lưu
        double baseWidth = ReadWriteData.getBaseWidth();
        int baseLife = ReadWriteData.getBaseLife();
        double baseSpeed = ReadWriteData.getBaseSpeed();

        // 2. Gán các giá trị
        setWidth(baseWidth);
        setX((GameApplication.WIDTH - baseWidth) / 2);
        setLife(baseLife);
        setSpeed(baseSpeed);

        // 3. Reset hiệu ứng
        stopAllBlinking();
        if (currentBuff != null) {
            currentBuff.stop();
            currentBuff = null;
        }

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