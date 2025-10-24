package GameObject;

import LoadResource.LoadImage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class NPC extends Character {
    private Image[] idleFrames;
    private int frameIndex = 0;
    private long lastFrameTime = 0;

    public NPC(double x, double y, double size) {
        setxOnMap(x);
        setyOnMap(y);
        setSize(size);

        idleFrames = LoadImage.getNpcIdle(); // lấy chuỗi ảnh idle
    }

    public void update(long now) {
        // Cập nhật hoạt ảnh mỗi 120ms (100_000_000ns = 100ms)
        if (now - lastFrameTime > 120_000_000) {
            frameIndex = (frameIndex + 1) % idleFrames.length;
            lastFrameTime = now;
        }
    }

    public void render(GraphicsContext gc, Map map) {
        double xOnScreen = getxOnMap() + map.getxOnScreen();
        double yOnScreen = getyOnMap() + map.getyOnScreen();

        gc.drawImage(idleFrames[frameIndex], xOnScreen, yOnScreen, getSize(), getSize());
    }
}
