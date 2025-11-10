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
            NPC npc = new NPC(20 * 32, 37 * 32, 250, 1, 1);

            // --- ĐẶT HITBOX CHIẾN ĐẤU (MỚI) ---
            // (Ví dụ: hitbox rộng 80, cao 120,
            // đặt ở giữa và nửa dưới của ảnh 250x250)
            double drawSizeNpcmap1_1 = npc.getSize(); // Lấy 250
            double hbWidthNpcmap1_1 = 80;
            double hbHeightNpcmap1_1 = 120;
            double hbOffsetXNpcmap1_1 = (drawSizeNpcmap1_1 - hbWidthNpcmap1_1) / 2; // (250 - 80) / 2 = 85
            double hbOffsetYNpcmap1_1 = drawSizeNpcmap1_1 - hbHeightNpcmap1_1;      // 250 - 120 = 130 (đặt ở chân)
            npc.setBattleHitbox(hbWidthNpcmap1_1, hbHeightNpcmap1_1, hbOffsetXNpcmap1_1, hbOffsetYNpcmap1_1);
            // --- KẾT THÚC ---
            npc.setDefeated(ReadWriteData.isNpcDefeated(1, 1));
            npcs.add(npc);

            npc.setProximityDialogue(List.of(
                    new Dialogue("Goblin", "Biết ông Từ không?", 2.0),
                    new Dialogue("Goblin", "Từ nơi đồng xanh thơm hương lúa!", 2.0)
            ));
            npc.setBattleTauntDialogue(List.of(
                    new Dialogue("Goblin", "Để cứu cháu bé ở Đồng Tháp không cần những phương tiện hiện đại", 2.0),
                    new Dialogue("Goblin", "Mà chỉ cần kẹo sữa Mikita được làm từ sữa!", 2.0)
            ));


        } else if (mapLevel == 2) {
            // NPC Map 2, Arkanoid Level 2
            NPC npc = new NPC(23 * 16, 9 * 16, 70, 2, 2);
            double drawSizeNpcmap2_1 = npc.getSize(); // Lấy 250
            double hbWidthNpcmap2_1 = 30;
            double hbHeightNpcmap2_1 = 70;
            double hbOffsetXNpcmap2_1 = (drawSizeNpcmap2_1 - hbWidthNpcmap2_1) / 2; // (250 - 80) / 2 = 85
            double hbOffsetYNpcmap2_1 = drawSizeNpcmap2_1 - hbHeightNpcmap2_1;      // 250 - 120 = 130 (đặt ở chân)
            npc.setBattleHitbox(hbWidthNpcmap2_1, hbHeightNpcmap2_1, hbOffsetXNpcmap2_1, hbOffsetYNpcmap2_1);
            npc.setDefeated(ReadWriteData.isNpcDefeated(2, 2));
            npcs.add(npc);
            npc.setProximityDialogue(List.of(
                    new Dialogue("Evil Eye", "Yodele i Hoo Aha, Biết rap không em!", 3.0),
                    new Dialogue("Evil Eye", "SDsahdbkcksbak sDBaskj ra đây mà lấy!", 2.0)
            ));
            npc.setBattleTauntDialogue(List.of(
                    new Dialogue("Evil Eye", "Mày thì biết cái quái gì về Mumble Rap!", 4.0)
            ));

            NPC shopNpc = new NPC(3 * 16, 5 * 16, 180, 0, 99);
            double drawSizeNpcmap1_2 = shopNpc.getSize(); // Lấy 250
            double hbWidthNpcmap1_2 = 80;
            double hbHeightNpcmap1_2 = 120;
            double hbOffsetXNpcmap1_2 = (drawSizeNpcmap1_2 - hbWidthNpcmap1_2) / 2; // (250 - 80) / 2 = 85
            double hbOffsetYNpcmap1_2 = drawSizeNpcmap1_2 - hbHeightNpcmap1_2;      // 250 - 120 = 130 (đặt ở chân)
            shopNpc.setBattleHitbox(hbWidthNpcmap1_2, hbHeightNpcmap1_2, hbOffsetXNpcmap1_2, hbOffsetYNpcmap1_2);
            shopNpc.setProximityDialogue(List.of(
                    new Dialogue("Chủ Cửa Hàng", "Anh Trai Luôn Giữ Bình Tĩnh (LGBT)! (Ấn ENTER)", 4.0)
            ));
            shopNpc.setDefeated(ReadWriteData.isNpcDefeated(2, 0));
            npcs.add(shopNpc);

        } else if (mapLevel == 3) {
            // --- LOGIC MỚI CHO MAP 3 ---

            // 1. Luôn tải NPC đầu tiên (level 3)
            NPC boss1 = new NPC(241, -15, 300, 3, 3);
            double drawSizeNpcmap2_1 = boss1.getSize(); // Lấy 250
            double hbWidthNpcmap2_1 = 80;
            double hbHeightNpcmap2_1 = 120;
            double hbOffsetXNpcmap2_1 = (drawSizeNpcmap2_1 - hbWidthNpcmap2_1) / 2; // (250 - 80) / 2 = 85
            double hbOffsetYNpcmap2_1 = drawSizeNpcmap2_1 - hbHeightNpcmap2_1;      // 250 - 120 = 130 (đặt ở chân)
            boss1.setBattleHitbox(hbWidthNpcmap2_1, hbHeightNpcmap2_1, hbOffsetXNpcmap2_1, hbOffsetYNpcmap2_1);
            boolean boss1Defeated = ReadWriteData.isNpcDefeated(3, 3); // Kiểm tra "3-3"
            boss1.setDefeated(boss1Defeated);
            npcs.add(boss1);
            boss1.setProximityDialogue(List.of(
                    new Dialogue("Fakerina", "Glory Glory Man United!", 3.0),
                    new Dialogue("Fakerina", "Thằng Lùn mặt quắt tai dơi...", 2.0)
            ));
            boss1.setBattleTauntDialogue(List.of(
                    new Dialogue("Fakerina", "Làm gì vậy? Đừng cắt!!", 4.0)
            ));



            // 2. NẾU boss 1 đã bị hạ (tải từ file save), THÌ tải NPC thứ hai (level 4)
            if (boss1Defeated) {
                // Boss 2 là loại chơi lại, không cần check defeated
                // Dùng hình ảnh type 2 (NPC Map 2) như bạn yêu cầu
                NPC boss2 = new NPC(241, -15, 100, 4, 4);
                double drawSizeNpcmap2_2 = boss2.getSize(); // Lấy 250
                double hbWidthNpcmap2_2 = 80;
                double hbHeightNpcmap2_2 = 120;
                double hbOffsetXNpcmap2_2 = (drawSizeNpcmap2_2 - hbWidthNpcmap2_2) / 2; // (250 - 80) / 2 = 85
                double hbOffsetYNpcmap2_2 = drawSizeNpcmap2_2 - hbHeightNpcmap2_2;      // 250 - 120 = 130 (đặt ở chân)
                boss2.setBattleHitbox(hbWidthNpcmap2_2, hbHeightNpcmap2_2, hbOffsetXNpcmap2_2, hbOffsetYNpcmap2_2);// Cùng tọa độ, Arkanoid Level 4, Type 2
                npcs.add(boss2); // Cứ thêm vào, không cần setDefeated
            }
            // --- KẾT THÚC LOGIC MỚI ---
        }
    }

    /**
     * Hàm này được gọi bởi ScenePlayGame KHI một NPC vừa bị đánh bại.
     * Dùng để "spawn" NPC kế tiếp trong chuỗi nhiệm vụ.
     *
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
            NPC boss2 = new NPC(x, y, 100, 4, 4);
            double drawSizeNpcmap2_2 = boss2.getSize(); // Lấy 100
            double hbWidthNpcmap2_2 = 80;
            double hbHeightNpcmap2_2 = 120;
            double hbOffsetXNpcmap2_2 = (drawSizeNpcmap2_2 - hbWidthNpcmap2_2) / 2; // (100 - 80) / 2 = 10
            double hbOffsetYNpcmap2_2 = drawSizeNpcmap2_2 - hbHeightNpcmap2_2;      // 100 - 120 = -20
            boss2.setBattleHitbox(hbWidthNpcmap2_2, hbHeightNpcmap2_2, hbOffsetXNpcmap2_2, hbOffsetYNpcmap2_2);// Arkanoid Level 4, Type 2
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
            // Mặc định, một NPC là "người chặn cổng"
            boolean isPortalBlocker = true;

            // --- LOGIC LỌC MỚI ---

            // Điều kiện 1: Bỏ qua (ignore) TẤT CẢ NPC Shop
            if (npc.getNpcType() == 99) {
                isPortalBlocker = false;
            }

            // Điều kiện 2: Bỏ qua (ignore) NPC chơi lại (Boss Lvl 4)
            if (npc.getArkanoidLevel() == 4) {
                isPortalBlocker = false;
            }

            // --- KẾT THÚC LOGIC LỌC ---

            // Nếu NPC này LÀ "người chặn cổng" VÀ nó chưa bị đánh bại
            if (isPortalBlocker && !npc.isDefeated()) {
                // ... thì chúng ta bị kẹt.
                return false;
            }
        }

        // Nếu vòng lặp kết thúc (không tìm thấy "người chặn cổng" nào còn sống)
        return true;
    }

}

