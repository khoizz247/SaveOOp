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
        collisionBounds.clear();

        // --- TƯỜNG BAO QUANH ---
        // Kích thước map: 50 * 32 = 1600 pixels
        // Tường trên (cao 2 tile)
        collisionBounds.add(new Rectangle(0, 0, 1600, 64));
        // Tường dưới (cao 2 tile, bắt đầu từ y = 48 * 32)
        collisionBounds.add(new Rectangle(0, 1536, 1600, 64));
        // Tường trái (rộng 2 tile)
        collisionBounds.add(new Rectangle(0, 0, 64, 1600));
        // Tường phải (rộng 2 tile, bắt đầu từ x = 48 * 32)
        collisionBounds.add(new Rectangle(1536, 0, 64, 1600));

        // --- CÁC NGÔI NHÀ ---
        // Ngôi nhà trên cùng bên trái (bắt đầu từ tile (8, 5))
        collisionBounds.add(new Rectangle(256, 160, 320, 256));

        // Ngôi nhà trên cùng bên phải (bắt đầu từ tile (32, 8))
        collisionBounds.add(new Rectangle(1024, 256, 320, 256));

        // Ngôi nhà lớn ở giữa (bắt đầu từ tile (19, 21))
        collisionBounds.add(new Rectangle(608, 672, 448, 320));

        // Ngôi nhà dưới cùng bên phải (bắt đầu từ tile (35, 34))
        collisionBounds.add(new Rectangle(1120, 1088, 320, 256));
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
