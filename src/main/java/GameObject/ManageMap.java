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
            // --- TƯỜNG BAO QUANH (Tường Cây) ---
            // Tường Cây trái: x0 y0 width2 height 49
            collisionBounds.add(new Rectangle(0 * 32, 0 * 32, 2 * 32, 49 * 32));
            // Tường cây trên 1: 0 0 20 1
            collisionBounds.add(new Rectangle(0 * 32, 0 * 32, 20 * 32, 1 * 32));
            // Tường cây trên 2: 25 0 25 1
            collisionBounds.add(new Rectangle(25 * 32, 0 * 32, 25 * 32, 1 * 32));
            // Tường cây phải: 49 0 2 49
            collisionBounds.add(new Rectangle(48 * 32, 0 * 32, 2 * 32, 49 * 32));
            // Tường cây dưới 1: 0 49 19 1
            collisionBounds.add(new Rectangle(0 * 32, 48 * 32, 19 * 32, 2 * 32));
            // Tường cây dưới 2: 23 49 26 1
            collisionBounds.add(new Rectangle(23 * 32, 48 * 32, 26 * 32, 2 * 32));
            collisionBounds.add(new Rectangle(19 * 32, 50 * 32, 26 * 32, 2 * 32));

            // --- TẬP HỢP SỌ ---
            collisionBounds.add(new Rectangle(5 * 32, 47 * 32, 2 * 32, 1 * 32));
            collisionBounds.add(new Rectangle(11 * 32, 44 * 32, 1 * 32, 1 * 32));
            collisionBounds.add(new Rectangle(33 * 32, 43 * 32, 1 * 32, 1 * 32));
            collisionBounds.add(new Rectangle(7 * 32, 38 * 32, 1 * 32, 1 * 32));
            collisionBounds.add(new Rectangle(9 * 32, 33 * 32, 1 * 32, 2 * 32));
            collisionBounds.add(new Rectangle(29 * 32, 31 * 32, 3 * 32, 1 * 32));
            collisionBounds.add(new Rectangle(42 * 32, 29 * 32, 2 * 32, 1 * 32));
            collisionBounds.add(new Rectangle(7 * 32, 27 * 32, 1 * 32, 1 * 32));
            collisionBounds.add(new Rectangle(4 * 32, 26 * 32, 1 * 32, 1 * 32));
            collisionBounds.add(new Rectangle(2 * 32, 25 * 32, 1 * 32, 1 * 32));
            collisionBounds.add(new Rectangle(36 * 32, 26 * 32, 3 * 32, 1 * 32));
            collisionBounds.add(new Rectangle(9 * 32, 22 * 32, 1 * 32, 1 * 32));
            collisionBounds.add(new Rectangle(16 * 32, 22 * 32, 1 * 32, 1 * 32));
            collisionBounds.add(new Rectangle(29 * 32, 21 * 32, 1 * 32, 1 * 32));
            collisionBounds.add(new Rectangle(46 * 32, 19 * 32, 1 * 32, 2 * 32));
            collisionBounds.add(new Rectangle(13 * 32, 21 * 32, 1 * 32, 1 * 32));
            collisionBounds.add(new Rectangle(16 * 32, 16 * 32, 1 * 32, 1 * 32));
            collisionBounds.add(new Rectangle(16 * 32, 15 * 32, 3 * 32, 1 * 32));
            collisionBounds.add(new Rectangle(21 * 32, 16 * 32, 2 * 32, 2 * 32));
            collisionBounds.add(new Rectangle(2 * 32, 7 * 32, 1 * 32, 2 * 32));
            collisionBounds.add(new Rectangle(41 * 32, 11 * 32, 1 * 32, 1 * 32));
            collisionBounds.add(new Rectangle(42 * 32, 3 * 32, 2 * 32, 2 * 32));
            collisionBounds.add(new Rectangle(4 * 32, 1 * 32, 1 * 32, 1 * 32));
            collisionBounds.add(new Rectangle(4 * 32, 2 * 32, 2 * 32, 1 * 32));

            // --- TẬP HỢP CÂY ---
            collisionBounds.add(new Rectangle(2 * 32, 43 * 32, 1 * 32, 1 * 32));
            collisionBounds.add(new Rectangle(23 * 32, 46 * 32, 2 * 32, 2 * 32));
            collisionBounds.add(new Rectangle(27 * 32, 43 * 32, 2 * 32, 2 * 32));
            collisionBounds.add(new Rectangle(3 * 32, 35 * 32, 2 * 32, 2 * 32));
            collisionBounds.add(new Rectangle(13 * 32, 36 * 32, 2 * 32, 2 * 32));
            collisionBounds.add(new Rectangle(28 * 32, 33 * 32, 2 * 32, 2 * 32));
            collisionBounds.add(new Rectangle(47 * 32, 34 * 32, 1 * 32, 2 * 32));
            collisionBounds.add(new Rectangle(33 * 32, 26 * 32, 2 * 32, 2 * 32));
            collisionBounds.add(new Rectangle(47 * 32, 24 * 32, 1 * 32, 2 * 32));
            collisionBounds.add(new Rectangle(8 * 32, 19 * 32, 2 * 32, 2 * 32));
            collisionBounds.add(new Rectangle(26 * 32, 17 * 32, 2 * 32, 3 * 32));
            collisionBounds.add(new Rectangle(6 * 32, 13 * 32, 2 * 32, 2 * 32));
            collisionBounds.add(new Rectangle(2 * 32, 10 * 32, 2 * 32, 2 * 32));
            collisionBounds.add(new Rectangle(29 * 32, 10 * 32, 2 * 32, 2 * 32));
            collisionBounds.add(new Rectangle(47 * 32, 14 * 32, 2 * 32, 2 * 32));
            collisionBounds.add(new Rectangle(26 * 32, 2 * 32, 2 * 32, 2 * 32));

            // --- CỌC GỖ ---
            collisionBounds.add(new Rectangle(6 * 32, 42 * 32, 2 * 32, 2 * 32));
            collisionBounds.add(new Rectangle(43 * 32, 38 * 32, 2 * 32, 2 * 32));
            collisionBounds.add(new Rectangle(41 * 32, 34 * 32, 2 * 32, 2 * 32));
            collisionBounds.add(new Rectangle(43 * 32, 32 * 32, 2 * 32, 2 * 32));
            collisionBounds.add(new Rectangle(14 * 32, 14 * 32, 2 * 32, 2 * 32));

            // --- GẠCH ---
            collisionBounds.add(new Rectangle(43 * 32, 44 * 32, 2 * 32, 2 * 32));
            collisionBounds.add(new Rectangle(23 * 32, 36 * 32, 2 * 32, 2 * 32));
            collisionBounds.add(new Rectangle(4 * 32, 31 * 32, 2 * 32, 2 * 32));
            collisionBounds.add(new Rectangle(16 * 32, 19 * 32, 2 * 32, 2 * 32));
            collisionBounds.add(new Rectangle(43 * 32, 10 * 32, 2 * 32, 2 * 32));

            // --- KHỐI HỘP ---
            collisionBounds.add(new Rectangle(18 * 32, 40 * 32, 2 * 32, 3 * 32));
            collisionBounds.add(new Rectangle(44 * 32, 24 * 32, 2 * 32, 3 * 32));
            collisionBounds.add(new Rectangle(12 * 32, 17 * 32, 2 * 32, 3 * 32));

            // --- GIẾNG ---
            collisionBounds.add(new Rectangle(30 * 32, 41 * 32, 2 * 32, 2 * 32));
            collisionBounds.add(new Rectangle(18 * 32, 32 * 32, 2 * 32, 2 * 32));
            collisionBounds.add(new Rectangle(37 * 32, 21 * 32, 2 * 32, 2 * 32));
            collisionBounds.add(new Rectangle(12 * 32, 13 * 32, 2 * 32, 2 * 32));

            // --- NHÀ 1 ---
            collisionBounds.add(new Rectangle(29 * 32, 35 * 32, 2 * 32, 5 * 32));
            collisionBounds.add(new Rectangle(30 * 32, 32 * 32, 6 * 32, 8 * 32));

            // --- NHÀ 2 ---
            collisionBounds.add(new Rectangle(13 * 32, 23 * 32, 12 * 32, 8 * 32));

            // --- NHÀ 3 ---
            collisionBounds.add(new Rectangle(33 * 32, 11 * 32, 7 * 32, 8 * 32));

            // --- NHÀ 4 ---
            collisionBounds.add(new Rectangle(11 * 32, 5 * 32, 7 * 32, 8 * 32));


        } else if (currentMapLevel == 2) {
            //map 2
            // Tường chắn phải trên: 0 5 2 11
            collisionBounds.add(new Rectangle(0 * 16, 5 * 16, 2 * 16, 11 * 16));
            // Tường chắn phải dưới: 0 36 2 13
            collisionBounds.add(new Rectangle(0 * 16, 36 * 16, 2 * 16, 15 * 16));
            // Tường chắn trái trên: 48 5 2 11
            collisionBounds.add(new Rectangle(48 * 16, 5 * 16, 2 * 16, 11 * 16));
            // Tường chắn trái dưới: 48 34 2 13
            collisionBounds.add(new Rectangle(48 * 16, 34 * 16, 2 * 16, 17 * 16));
            collisionBounds.add(new Rectangle(0 * 16, 52 * 16, 50 * 16, 1 * 16));
            // Chặn vực trên trái: 0 16 21 2
            collisionBounds.add(new Rectangle(0 * 16, 16 * 16, 21 * 16, 2 * 16));
            // Chân vực trên phải: 28 16 21 2
            collisionBounds.add(new Rectangle(28 * 16, 16 * 16, 21 * 16, 2 * 16));
            // Chân vực dưới trái: 0 34 21 2
            collisionBounds.add(new Rectangle(0 * 16, 34 * 16, 21 * 16, 2 * 16));
            // Chân vực dưới phải: 31 33 18 2
            collisionBounds.add(new Rectangle(31 * 16, 33 * 16, 18 * 16, 2 * 16));
            // Vách vực dưới trái: 20 33 1 3
            collisionBounds.add(new Rectangle(20 * 16, 33 * 16, 1 * 16, 3 * 16));
            // Vách vực dưới phải: 28 32 2 2
            collisionBounds.add(new Rectangle(28 * 16, 32 * 16, 2 * 16, 2 * 16));

            // Thành cầu trái: 21 15 1 17
            collisionBounds.add(new Rectangle(21 * 16, 15 * 16, 1 * 16, 17 * 16));
            // Thành cầu phải: 27 15 1 17
            collisionBounds.add(new Rectangle(27 * 16, 15 * 16, 1 * 16, 17 * 16));

            // Tập hợp sọ:
            collisionBounds.add(new Rectangle(13 * 16, 46 * 16, 2 * 16, 2 * 16));
            collisionBounds.add(new Rectangle(31 * 16, 45 * 16, 2 * 16, 2 * 16));
            collisionBounds.add(new Rectangle(47 * 16, 46 * 16, 2 * 16, 2 * 16));
            collisionBounds.add(new Rectangle(5 * 16, 36 * 16, 2 * 16, 2 * 16));
            collisionBounds.add(new Rectangle(34 * 16, 37 * 16, 2 * 16, 2 * 16));
            collisionBounds.add(new Rectangle(21 * 16, 32 * 16, 2 * 16, 2 * 16));
            collisionBounds.add(new Rectangle(27 * 16, 32 * 16, 2 * 16, 2 * 16));
            collisionBounds.add(new Rectangle(22 * 16, 28 * 16, 2 * 16, 2 * 16));
            collisionBounds.add(new Rectangle(25 * 16, 23 * 16, 2 * 16, 2 * 16));
            collisionBounds.add(new Rectangle(22 * 16, 16 * 16, 2 * 16, 2 * 16));
            collisionBounds.add(new Rectangle(21 * 16, 14 * 16, 2 * 16, 2 * 16));
            collisionBounds.add(new Rectangle(26 * 16, 14 * 16, 2 * 16, 2 * 16));
            collisionBounds.add(new Rectangle(44 * 16, 8 * 16, 2 * 16, 2 * 16));
            collisionBounds.add(new Rectangle(6 * 16, 6 * 16, 2 * 16, 2 * 16));

            // Tập hợp cây:
            collisionBounds.add(new Rectangle(6 * 16, 42 * 16, 6 * 16, 3 * 16));
            collisionBounds.add(new Rectangle(18 * 16, 43 * 16, 4 * 16, 4 * 16));
            collisionBounds.add(new Rectangle(28 * 16, 39 * 16, 4 * 16, 4 * 16));
            collisionBounds.add(new Rectangle(38 * 16, 43 * 16, 6 * 16, 3 * 16));
            collisionBounds.add(new Rectangle(10 * 16, 10 * 16, 6 * 16, 3 * 16));
            collisionBounds.add(new Rectangle(29 * 16, 7 * 16, 4 * 16, 4 * 16));
            collisionBounds.add(new Rectangle(35 * 16, 7 * 16, 6 * 16, 3 * 16));

            // Lâu Đài
            collisionBounds.add(new Rectangle(0 * 16, 4 * 16, 3 * 16, 1 * 16));
            collisionBounds.add(new Rectangle(3 * 16, 3 * 16, 16 * 16, 1 * 16));
            collisionBounds.add(new Rectangle(19 * 16, 4 * 16, 3 * 16, 1 * 16));
            collisionBounds.add(new Rectangle(21 * 16, 3 * 16, 7 * 16, 1 * 16));
            collisionBounds.add(new Rectangle(28 * 16, 4 * 16, 3 * 16, 1 * 16));
            collisionBounds.add(new Rectangle(31 * 16, 3 * 16, 16 * 16, 1 * 16));
            collisionBounds.add(new Rectangle(47 * 16, 4 * 16, 3 * 16, 1 * 16));
        } else if (currentMapLevel == 3) {
            //map 3
            collisionBounds.add(new Rectangle(0 * 16, 0 * 16, 50 * 16, 2 * 16));  // Trên
            collisionBounds.add(new Rectangle(0 * 16, 0 * 16, 1 * 16, 50 * 16));  // Trái
            collisionBounds.add(new Rectangle(49 * 16, 0 * 16, 1 * 16, 50 * 16)); // Phải (sửa từ width 50 thành 1)
            collisionBounds.add(new Rectangle(0 * 16, 49 * 16, 23 * 16, 1 * 16)); // Dưới trái
            collisionBounds.add(new Rectangle(23 * 16, 50 * 16, 4 * 16, 1 * 16));
            collisionBounds.add(new Rectangle(27 * 16, 49 * 16, 22 * 16, 1 * 16));// Dưới phải
            collisionBounds.add(new Rectangle(17 * 16, 18 * 16, 3 * 16, 4 * 16));
            collisionBounds.add(new Rectangle(30 * 16, 18 * 16, 3 * 16, 4 * 16));

            // Sọ
            collisionBounds.add(new Rectangle(8 * 16, 37 * 16, 2 * 16, 2 * 16));
            collisionBounds.add(new Rectangle(37 * 16, 38 * 16, 2 * 16, 2 * 16));
            collisionBounds.add(new Rectangle(43 * 16, 32 * 16, 2 * 16, 2 * 16));
            collisionBounds.add(new Rectangle(44 * 16, 27 * 16, 2 * 16, 2 * 16));
            collisionBounds.add(new Rectangle(35 * 16, 28 * 16, 2 * 16, 2 * 16));
            collisionBounds.add(new Rectangle(15 * 16, 30 * 16, 2 * 16, 2 * 16));
            collisionBounds.add(new Rectangle(7 * 16, 27 * 16, 2 * 16, 2 * 16));
            collisionBounds.add(new Rectangle(6 * 16, 25 * 16, 2 * 16, 2 * 16));
            collisionBounds.add(new Rectangle(45 * 16, 21 * 16, 2 * 16, 2 * 16));
            collisionBounds.add(new Rectangle(17 * 16, 22 * 16, 2 * 16, 3 * 16));
            collisionBounds.add(new Rectangle(31 * 16, 22 * 16, 2 * 16, 3 * 16));
            collisionBounds.add(new Rectangle(13 * 16, 15 * 16, 4 * 16, 3 * 16));
            collisionBounds.add(new Rectangle(33 * 16, 15 * 16, 4 * 16, 3 * 16));

            // Rương
            collisionBounds.add(new Rectangle(11 * 16, 4 * 16, 1 * 16, 2 * 16));
            collisionBounds.add(new Rectangle(10 * 16, 5 * 16, 1 * 16, 4 * 16));
            collisionBounds.add(new Rectangle(11 * 16, 7 * 16, 1 * 16, 2 * 16));
            collisionBounds.add(new Rectangle(12 * 16, 9 * 16, 2 * 16, 1 * 16));
            collisionBounds.add(new Rectangle(13 * 16, 10 * 16, 2 * 16, 1 * 16));
            collisionBounds.add(new Rectangle(38 * 16, 5 * 16, 1 * 16, 2 * 16));
            collisionBounds.add(new Rectangle(39 * 16, 6 * 16, 1 * 16, 2 * 16));
            collisionBounds.add(new Rectangle(40 * 16, 7 * 16, 1 * 16, 4 * 16));
            collisionBounds.add(new Rectangle(40 * 16, 10 * 16, 3 * 16, 1 * 16));
            collisionBounds.add(new Rectangle(42 * 16, 11 * 16, 2 * 16, 1 * 16));
            collisionBounds.add(new Rectangle(43 * 16, 12 * 16, 1 * 16, 1 * 16));
        }
    }

    private void initializePortalBounds() {
        portalBounds.clear();
        if (currentMapLevel == 1) {

            portalBounds.add(new Rectangle(21*32, 0, 3*32, 1*32)); // (x, y, width, height)
        } else if (currentMapLevel == 2) {

            portalBounds.add(new Rectangle(22*16, 4*16, 5*16, 2*16));
            portalBounds.add(new Rectangle(23 * 16, 51 * 16,4 * 16 , 1 * 16 ));
        } else if (currentMapLevel == 3) {
            portalBounds.add(new Rectangle(23 * 16, 50 * 16,4 * 16 , 1 * 16 ));
        }
    }


    public boolean isColliding(Rectangle bounds) {
        for (Rectangle collisionBox : collisionBounds) {

            if (collisionBox.intersects(bounds.getLayoutBounds())) {
                return true;
            }
        }
        return false;
    }

    public int getCollidingPortalIndex(Rectangle bounds) {

        for (int i = 0; i < portalBounds.size(); i++) {
            if (portalBounds.get(i).intersects(bounds.getLayoutBounds())) {
                return i;
            }
        }
        return -1;
    }

    //debug
    public List<Rectangle> getCollisionBounds() {
        return collisionBounds;
    }

    public List<Rectangle> getPortalBounds() {
        return portalBounds;
    }

}
