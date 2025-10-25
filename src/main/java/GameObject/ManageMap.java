package GameObject;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class ManageMap {
    private List<Rectangle> collisionBounds = new ArrayList<>();
    private List<Rectangle> portalBounds = new ArrayList<>(); // --- MỚI ---
    private int currentMapLevel = 1;


    public ManageMap() {
        loadMap(currentMapLevel);
    }

    public void loadMap(int mapLevel) {
        this.currentMapLevel = mapLevel;
        initializeCollisionBounds();
        initializePortalBounds(); // Gọi hàm khởi tạo portal
    }

    private void initializeCollisionBounds() {
        collisionBounds.clear();

        if (currentMapLevel == 1) {
            // --- TƯỜNG BAO QUANH MAP 1 ---


            // --- CÁC NGÔI NHÀ MAP 1 ---


        } else if (currentMapLevel == 2) {
            //map 2
        }
    }

    private void initializePortalBounds() {
        portalBounds.clear();
        if (currentMapLevel == 1) {
            // Portal ở map 1, đặt ở tường trên
            portalBounds.add(new Rectangle(750, 64, 100, 10)); // (x, y, width, height)
        } else if (currentMapLevel == 2) {
            // Portal ở map 2, đặt ở tường dưới để quay lại
            portalBounds.add(new Rectangle(750, 64, 100, 10));
        }
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

    public boolean isCollidingWithPortal(Rectangle bounds) {
        for (Rectangle portalBox : portalBounds) {
            if (portalBox.intersects(bounds.getLayoutBounds())) {
                return true;
            }
        }
        return false;
    }

}
