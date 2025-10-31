package GameObject;

import StartGame.GameApplication;
import java.awt.*;
import java.util.List;

public class Ball extends Circle{
    boolean isthrough = false;
    private double relativeX = -1;

    public Ball(double ballX, double ballY, double radius, double speed, double dx, double dy) {
        super(ballX, ballY, radius, speed, dx, dy);
    }

    public boolean isIsthrough() {
        return isthrough;
    }

    public void setIsthrough(boolean isthrough) {
        this.isthrough = isthrough;
    }

    public void setDirection() {
        double length = Math.sqrt(getDx() * getDx() + getDy() * getDy());
        if (length == 0) return;
        setDx((getDx() / length) * getSpeed());
        setDy((getDy() / length) * getSpeed());
    }

    public void updateBall(MyBlock myBlock, List<GameBlock> blocks, ManageBuff listBuffs) {

        setBallX(getBallX() + getDx());
        setBallY(getBallY() + getDy());

        //Va chạm góc
        if (getBallX() - getRadius() <= 0) {
            setBallX(getRadius());
            setDx(-getDx());
        } else if (getBallX() + getRadius() >= GameApplication.WIDTH) {
            setBallX(GameApplication.WIDTH - getRadius());
            setDx(-getDx());
        }
        if (getBallY() - getRadius() <= 65) {
            setBallY(getRadius() + 65);
            setDy(-getDy());
            isthrough = false;
            setSpeed(3);
        }

        // Va cham paddle
        if (checkContactToBlock(myBlock.getX(), myBlock.getY(), myBlock.getWidth(), myBlock.getHeight())) {
            double relativeIntersectX = (getBallX() - (myBlock.getX() + myBlock.getWidth() / 2));
            double normalized = relativeIntersectX / (myBlock.getWidth() / 2);
            double bounceAngle = normalized * (Math.PI / 3); // tối đa 60 độ

            setDx(getSpeed() * Math.sin(bounceAngle));
            setDy(-getSpeed() * Math.cos(bounceAngle));

            setBallY(myBlock.getY() - getRadius() - 1);
        }

        // Va cham block
        GameBlock collidedBlock = null;
        for (GameBlock b : blocks) {
            if (checkContactToBlock(b.getX(), b.getY(), b.getWidth(), b.getHeight())) {
                collidedBlock = b;
                break;
            }
        }

        if (collidedBlock != null) {
            collidedBlock.contactGameBlock(this);
            if (collidedBlock.handleBlock()) {
                listBuffs.addBuff(collidedBlock.getX(), collidedBlock.getY(),
                        collidedBlock.getWidth(), collidedBlock.getHeight(), "Bullet", this);
                blocks.remove(collidedBlock);
            }
        }

        setDirection();
    }

    public void inPaddle(double xPaddle, double widthPaddle) {
        setDirection();
        if (this.relativeX == -1) {
            this.relativeX = widthPaddle / 2;
        }

        // 2. Cập nhật vị trí trượt TƯƠNG ĐỐI
        this.relativeX += this.getDx();

        // 3. Kiểm tra biên (so với cạnh của paddle)
        double leftEdgeRelative = getRadius();
        double rightEdgeRelative = widthPaddle - getRadius();

        if (this.relativeX > rightEdgeRelative) {
            this.relativeX = rightEdgeRelative;
            this.setDx(-this.getDx()); // Đổi hướng
        } else if (this.relativeX < leftEdgeRelative) {
            this.relativeX = leftEdgeRelative;
            this.setDx(-this.getDx());// Đổi hướng
        }

        // 4. Đặt vị trí TUYỆT ĐỐI (vị trí paddle + vị trí tương đối)
        setBallX(xPaddle + this.relativeX);
    }
}