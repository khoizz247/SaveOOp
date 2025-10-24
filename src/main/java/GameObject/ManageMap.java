package GameObject;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class ManageMap {
    private List<Rectangle> collisionBounds = new ArrayList<>();
    public ManageMap() {
        initializeCollisionBounds();
    }
    private void initializeCollisionBounds() {
        collisionBounds.add(new Rectangle(730, 480, 480, 280));

        // Ngôi nhà trên cùng bên trái
        collisionBounds.add(new Rectangle(350, 150, 290, 220));

        // Ngôi nhà trên cùng bên phải
        collisionBounds.add(new Rectangle(1280, 220, 290, 220));

        // Ngôi nhà dưới cùng bên phải
        collisionBounds.add(new Rectangle(1180, 760, 290, 220));

        // Tường bao quanh
        collisionBounds.add(new Rectangle(0, 0, 2048, 80)); // Tường trên
        collisionBounds.add(new Rectangle(0, 1160, 2048, 80)); // Tường dưới
        collisionBounds.add(new Rectangle(0, 0, 80, 1200)); // Tường trái
        collisionBounds.add(new Rectangle(1980, 0, 68, 1200));
    }
    public boolean isColliding(Rectangle bounds) {
        for (Rectangle collisionBox : collisionBounds) {
            // intersects() kiểm tra xem hai hình chữ nhật có giao nhau không
            if (collisionBox.intersects(bounds.getLayoutBounds())) {
                return true; // Có va chạm
            }
        }
        return false; // Không có va chạm
    }

}
