package GameObject;

import LoadResource.LoadImage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle; // Thêm import
import java.util.List; // Thêm import
import java.util.ArrayList; // Thêm import

public class NPC extends Character {
    private Image[] idleFrames;
    private int frameIndex = 0;
    private long lastFrameTime = 0;

    private int arkanoidLevel;
    private boolean isDefeated = false;
    private int npcType;
    private List<Dialogue> proximityDialogue = new ArrayList<>();
    private List<Dialogue> battleTauntDialogue = new ArrayList<>(); // Thoại khi 20%
    private boolean hasSpokenProximity = false;
    private boolean hasSpokenTaunt = false;
    private double proximityRadius = 32;
    private double hitboxWidth;
    private double hitboxHeight;
    private double hitboxOffsetX; // Độ lệch X so với getxOnMap()
    private double hitboxOffsetY; // Độ lệch Y so với getyOnMap()

    public NPC(double x, double y, double size, int arkanoidLevel, int npcType) {
        setxOnMap(x);
        setyOnMap(y);
        setSize(size);
        this.arkanoidLevel = arkanoidLevel;
        this.npcType = npcType;
        if (npcType == 2) {
            idleFrames = LoadImage.getNpcMap2Idle(); // Lấy hoạt ảnh NPC Map 2
        } else if (npcType == 3) {
            idleFrames = LoadImage.getNpcMap3Idle(); // Lấy hoạt ảnh NPC Map 3
        } else if (npcType == 99) {
            // (Tùy chọn) Load ảnh riêng cho NPC Shop
            // idleFrames = LoadImage.getNpcShopIdle();
            // Tạm thời dùng ảnh Map 1
            idleFrames = LoadImage.getNpcWizardIdle();
        } else if  (npcType == 4) {
            idleFrames = LoadImage.getNpcPortalIdle();

        } else {
            // Mặc định là NPC Map 1
            idleFrames = LoadImage.getNpcMap1Idle();
        }
    }

    public int getNpcType() {
        return npcType;
    }

    public void update(long now) {
        // Cập nhật hoạt ảnh
        if (now - lastFrameTime > 120_000_000) {
            if (idleFrames != null && idleFrames.length > 0) {
                frameIndex = (frameIndex + 1) % idleFrames.length;
            }
            lastFrameTime = now;
        }
    }

    public void render(GraphicsContext gc, Map map) {
        // Kiểm tra null trước khi vẽ
        if (idleFrames == null || idleFrames[frameIndex] == null) {
            return;
        }

        double xOnScreen = getxOnMap() + map.getxOnScreen();
        double yOnScreen = getyOnMap() + map.getyOnScreen();

        gc.drawImage(idleFrames[frameIndex], xOnScreen, yOnScreen, getSize(), getSize());
    }

    public int getArkanoidLevel() {
        return arkanoidLevel;
    }

    public boolean isDefeated() {
        return isDefeated;
    }

    public void setDefeated(boolean defeated) {
        isDefeated = defeated;
    }

    public void setProximityDialogue(List<Dialogue> dialogues) {
        this.proximityDialogue = dialogues;
    }

    public List<Dialogue> getProximityDialogue() {
        return this.proximityDialogue;
    }

    public void setBattleTauntDialogue(List<Dialogue> dialogues) {
        this.battleTauntDialogue = dialogues;
    }

    public List<Dialogue> getBattleTauntDialogue() {
        return this.battleTauntDialogue;
    }

    public boolean hasSpokenProximity() { return hasSpokenProximity; }
    public void setHasSpokenProximity(boolean spoken) { this.hasSpokenProximity = spoken; }

    public boolean hasSpokenTaunt() { return hasSpokenTaunt; }
    public void setHasSpokenTaunt(boolean spoken) { this.hasSpokenTaunt = spoken; }

    /**
     * Tạo một vùng chữ nhật lớn hơn NPC để kích hoạt hội thoại
     */
    public Rectangle getProximityBounds() {
        // 1. Lấy hitbox chiến đấu (màu cam) làm CƠ SỞ
        //    (Chúng ta không dùng getBattleBounds() trực tiếp
        //     để tránh lỗi đệ quy vô hạn nếu có)
        Rectangle battleBounds = new Rectangle(
                getxOnMap() + hitboxOffsetX,
                getyOnMap() + hitboxOffsetY,
                hitboxWidth,
                hitboxHeight
        );

        // 2. Mở rộng nó ra "proximityRadius" pixel
        //    (Tức là proximityRadius/2 ở mỗi bên)
        double newX = battleBounds.getX() - (proximityRadius / 2);
        double newY = battleBounds.getY() - (proximityRadius / 2);
        double newWidth = battleBounds.getWidth() + proximityRadius;
        double newHeight = battleBounds.getHeight() + proximityRadius;

        return new Rectangle(newX, newY, newWidth, newHeight);
    }

    public void setBattleHitbox(double width, double height, double offsetX, double offsetY) {
        this.hitboxWidth = width;
        this.hitboxHeight = height;
        this.hitboxOffsetX = offsetX;
        this.hitboxOffsetY = offsetY;
    }

    public Rectangle getBattleBounds() {
        // Tạo hitbox dựa trên vị trí của NPC và các biến offset
        return new Rectangle(
                getxOnMap() + hitboxOffsetX,
                getyOnMap() + hitboxOffsetY,
                hitboxWidth,
                hitboxHeight
        );
    }


}
