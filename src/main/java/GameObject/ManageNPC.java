package GameObject;
import GameLoop.ReadWriteData;
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
            // NPC Map 1, Arkanoid Level 1
            NPC npc = new NPC(20*32, 37*32, 250, 1, 1);
            npc.setDefeated(ReadWriteData.isNpcDefeated(1, 1));
            npcs.add(npc);

            npc.setProximityDialogue(List.of(
                    new Dialogue("Quái Map 1", "Ngươi nghĩ ngươi có thể thắng ta sao?", 3.0),
                    new Dialogue("Quái Map 1", "Hãy xem đây!", 2.0)
            ));
            npc.setBattleTauntDialogue(List.of(
                    new Dialogue("Quái Map 1", "Không thể nào! Ngươi quá mạnh!", 4.0)
            ));

        } else if (mapLevel == 2) {
            // NPC Map 2, Arkanoid Level 2
            NPC npc = new NPC(23*16, 9*16, 70, 2, 2);
            npc.setDefeated(ReadWriteData.isNpcDefeated(2, 2));
            npcs.add(npc);
            npc.setProximityDialogue(List.of(
                    new Dialogue("Quái Map 1", "Ngươi nghĩ ngươi có thể thắng ta sao?", 3.0),
                    new Dialogue("Quái Map 1", "Hãy xem đây!", 2.0)
            ));
            npc.setBattleTauntDialogue(List.of(
                    new Dialogue("Quái Map 1", "Không thể nào! Ngươi quá mạnh!", 4.0)
            ));

        } else if (mapLevel == 3) {
            // --- LOGIC MỚI CHO MAP 3 ---

            // 1. Luôn tải NPC đầu tiên (level 3)
            NPC boss1 = new NPC(241, -15, 300, 3, 3);
            boolean boss1Defeated = ReadWriteData.isNpcDefeated(3, 3); // Kiểm tra "3-3"
            boss1.setDefeated(boss1Defeated);
            npcs.add(boss1);
            boss1.setProximityDialogue(List.of(
                    new Dialogue("Quái Map 1", "Ngươi nghĩ ngươi có thể thắng ta sao?", 3.0),
                    new Dialogue("Quái Map 1", "Hãy xem đây!", 2.0)
            ));
            boss1.setBattleTauntDialogue(List.of(
                    new Dialogue("Quái Map 1", "Không thể nào! Ngươi quá mạnh!", 4.0)
            ));

            // 2. NẾU boss 1 đã bị hạ (tải từ file save), THÌ tải NPC thứ hai (level 4)
            if (boss1Defeated) {
                // Boss 2 là loại chơi lại, không cần check defeated
                // Dùng hình ảnh type 2 (NPC Map 2) như bạn yêu cầu
                NPC boss2 = new NPC(241, -15, 100, 4, 2); // Cùng tọa độ, Arkanoid Level 4, Type 2
                npcs.add(boss2); // Cứ thêm vào, không cần setDefeated
            }
            // --- KẾT THÚC LOGIC MỚI ---
        }
    }

    /**
     * Hàm này được gọi bởi ScenePlayGame KHI một NPC vừa bị đánh bại.
     * Dùng để "spawn" NPC kế tiếp trong chuỗi nhiệm vụ.
     * @param defeatedNpc NPC vừa bị người chơi đánh bại
     */
    public void onNpcDefeated(NPC defeatedNpc) {
        // Lấy thông tin của NPC vừa bị hạ
        int mapLevel = this.currentMapLevel;
        int arkanoidLevel = defeatedNpc.getArkanoidLevel();

        // --- LOGIC SPAWN ---
        // Nếu chúng ta đang ở Map 3 VÀ con quái vừa bị hạ là level 3...
        if (mapLevel == 3 && arkanoidLevel == 3) {
            System.out.println("Boss level 3 bị hạ! Spawn Boss level 4...");

            // Lấy vị trí của boss cũ
            double x = defeatedNpc.getxOnMap();
            double y = defeatedNpc.getyOnMap();

            // Tạo boss 2 (level 4) và thêm vào danh sách *hiện tại*
            // Dùng hình ảnh type 2 (NPC Map 2)
            NPC boss2 = new NPC(x, y, 100, 4, 2); // Arkanoid Level 4, Type 2
            npcs.add(boss2); // Thêm vào danh sách đang chạy
        }
    }

    public void update(long now) {
        for (NPC npc : npcs) {
            if (!npc.isDefeated()) {
                npc.update(now);
            }
        }
    }

    public void render(GraphicsContext gc, Map map) {
        for (NPC npc : npcs) {
            if (!npc.isDefeated()) {
                npc.render(gc, map);
            }
        }
    }

    public List<NPC> getNpcs() {
        return npcs;
    }

    /**
     * Kiểm tra xem tất cả NPC (có thể bị đánh bại) đã bị hạ chưa.
     * Bỏ qua các NPC chơi lại (ví dụ: level 4).
     */
    public boolean allNpcsDefeated() {
        if (npcs.isEmpty()) return true;
        for (NPC npc : npcs) {
            // Sửa logic: Chỉ quan tâm nếu NPC có thể bị đánh bại
            // (ví dụ: bỏ qua boss chơi lại, level 4)
            if (!npc.isDefeated() && npc.getArkanoidLevel() != 4) {
                return false; // Tìm thấy một NPC 1 lần chưa bị hạ
            }
        }
        return true; // Tất cả NPC *có thể bị đánh bại* đã bị hạ
    }
}

