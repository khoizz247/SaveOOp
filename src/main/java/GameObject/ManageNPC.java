package GameObject;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;
public class ManageNPC {
    private List<NPC> npcs = new ArrayList<>();

    public ManageNPC() {
        //Khoi tao cac NPC
        npcs.add(new NPC(780, 480, 300));
    }
    public void update(long now) {
        for (NPC npc : npcs) {
            npc.update(now);
        }
    }
    public void render(GraphicsContext gc, Map map) {
        for (NPC npc : npcs) {
            npc.render(gc, map);
        }
    }

    public List<NPC> getNpcs() {
        return npcs;
    }

}
