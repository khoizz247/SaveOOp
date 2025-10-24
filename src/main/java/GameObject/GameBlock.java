package GameObject;

import LoadResource.LoadImage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class GameBlock extends Block{
    private Image[] graphic;
    private int typeBlock;
    private int durability;

    public GameBlock(int typeBlock) {
        this.graphic = LoadImage.getBlock();
        this.typeBlock = typeBlock;
        this.durability = typeBlock;
    }

    public GameBlock(double x, double y, int typeBlock) {
        super(x, y, 90, 30);
        this.graphic = LoadImage.getBlock();
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
        Image state;
        if (durability == 1) {
            state = graphic[2];
        } else if (durability == 2){
            state = graphic[1];
        } else {
            state = graphic[0];
        }
        gc.drawImage(state, getX(), getY(), getWidth(), getHeight());
    }

    public boolean handleBlock() {
        durability --;
        if (durability > 0) {
            return false;
        }
        return true;
    }
}