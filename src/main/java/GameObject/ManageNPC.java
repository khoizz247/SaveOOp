package GameObject;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;
public class ManageNPC {
    private List<NPC> npcs = new ArrayList<>();
    private int currentMapLevel = 1;
    public ManageNPC() {
        //Khoi tao cac NPC
        loadNpcsForMap(currentMapLevel);
    }

    public void loadNpcsForMap(int mapLevel) {
        npcs.clear();
        this.currentMapLevel = mapLevel;

        if (mapLevel == 1) {
            // Map 1 có 1 NPC, tương ứng Arkanoid level 1
            npcs.add(new NPC(780, 480, 300, 1));
            // Thêm NPC khác nếu muốn
            // npcs.add(new NPC(200, 600, 300, 2)); // Ví dụ NPC dùng Arkanoid level 2
        } else if (mapLevel == 2) {
            // map 2
        } else if  (mapLevel == 3) {
        }
    }

    public void update(long now) {
        for (NPC npc : npcs) {
            if (!npc.isDefeated()) { // --- THAY ĐỔI ---
                npc.update(now);
            }
        }
    }

    public void render(GraphicsContext gc, Map map) {
        for (NPC npc : npcs) {
            if (!npc.isDefeated()) { // --- THAY ĐỔI ---
                npc.render(gc, map);
            }
        }
    }

    public List<NPC> getNpcs() {
        return npcs;
    }

    public boolean allNpcsDefeated() {
        if (npcs.isEmpty()) return true; // Không có NPC cũng tính là đã thắng
        for (NPC npc : npcs) {
            if (!npc.isDefeated()) {
                return false; // Còn ít nhất 1 NPC chưa bị đánh bại
            }
        }
        return true; // Tất cả đã bị đánh bại
    }

}
