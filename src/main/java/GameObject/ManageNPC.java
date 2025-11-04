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

            npcs.add(new NPC(20*32, 37*32, 250, 1,1));

        } else if (mapLevel == 2) {

            npcs.add(new NPC(23*16, 9*16, 70, 2, 2));
        } else if  (mapLevel == 3) {
            npcs.add(new NPC(241, -15, 300, 3, 3));
        }
        for (NPC npc : npcs) {
            boolean daBiHaGuc = ReadWriteData.isNpcDefeated(mapLevel, npc.getArkanoidLevel());
            if (daBiHaGuc) {
                npc.setDefeated(true);
            }
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
            if (!npc.isDefeated()) {
                npc.render(gc, map);
            }
        }
    }

    public List<NPC> getNpcs() {
        return npcs;
    }

    public boolean allNpcsDefeated() {
        if (npcs.isEmpty()) return true;
        for (NPC npc : npcs) {
            if (!npc.isDefeated()) {
                return false;
            }
        }
        return true;
    }

}
