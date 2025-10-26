package GameObject;

import LoadResource.LoadImage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class NPC extends Character {
    private Image[] idleFrames;
    private int frameIndex = 0;
    private long lastFrameTime = 0;

    private int arkanoidLevel;
    private boolean isDefeated = false;

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
}
