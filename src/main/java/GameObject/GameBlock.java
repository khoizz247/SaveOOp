package GameObject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class GameBlock extends Block{
    private int typeBlock;
    private int durability;

    public GameBlock(int typeBlock) {
        this.typeBlock = typeBlock;
        this.durability = typeBlock;
    }

    public GameBlock(double x, double y, int typeBlock) {
        super(x, y, 90, 30);
        this.typeBlock = typeBlock;
        this.durability = typeBlock;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    @Override
    public void addOnScene(GraphicsContext gc) {
        //gc.drawImage(graphic, X, Y, width, height);
        if (typeBlock == 1) {
            gc.setFill(Color.YELLOW);
        } else {
            gc.setFill(Color.BLUE);
        }
        gc.fillRect(getX(), getY(), getWidth(), getHeight());
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);
        gc.strokeRect(getX(), getY(), getWidth(), getHeight());
    }

    public boolean handleBlock() {
        durability --;
        if (durability > 0) {
            return false;
        }
        return true;
    }
}