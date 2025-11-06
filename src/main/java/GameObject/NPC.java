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

    private List<Dialogue> proximityDialogue = new ArrayList<>();
    private List<Dialogue> battleTauntDialogue = new ArrayList<>(); // Thoại khi 20%
    private boolean hasSpokenProximity = false;
    private boolean hasSpokenTaunt = false;
    private double proximityRadius = 96;

    public NPC(double x, double y, double size, int arkanoidLevel, int npcType) {
        setxOnMap(x);
        setyOnMap(y);
        setSize(size);
        this.arkanoidLevel = arkanoidLevel;
        if (npcType == 2) {
            idleFrames = LoadImage.getNpcMap2Idle(); // Lấy hoạt ảnh NPC Map 2
        } else if (npcType == 3) {
            idleFrames = LoadImage.getNpcMap3Idle(); // Lấy hoạt ảnh NPC Map 3
        } else {
            // Mặc định là NPC Map 1
            idleFrames = LoadImage.getNpcDemonIdle();
        }
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
        double newSize = getSize() + proximityRadius;
        double newX = getxOnMap() - (proximityRadius / 2);
        double newY = getyOnMap() - (proximityRadius / 2);
        return new Rectangle(newX, newY, newSize, newSize);
    }
}
