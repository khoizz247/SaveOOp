package LoadResource;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import java.util.Objects;

public class LoadImage {
    private static Image[] idleAhead;
    private static Image[] idleBehind;
    private static Image[] idleLeft;
    private static Image[] idleRight;

    private static Image[] runAhead;
    private static Image[] runBehind;
    private static Image[] runLeft;
    private static Image[] runRight;
    private static Image[][] allBlocks;

    private static Image map1;
    private static Image map2;
    private static Image map3;

    private static Image[] npcMap1Idle; // NPC Map 1
    private static Image[] npcMap2Idle;  // NPC Map 2
    private static Image[] npcMap3Idle;  // NPC Map 3
    public static Image[] getNpcDemonIdle() {
        return npcMap1Idle;
    }

    public static Image[] getNpcMap2Idle() {
        return npcMap2Idle;
    }

    public static Image[] getNpcMap3Idle() {
        return npcMap3Idle;
    }



    private static void loadNpcImages() {
        npcMap3Idle = loadCharFrame("/Image/NPC/npc_map3_idle_%d.png", 6);
        npcMap1Idle = loadCharFrame("/Image/NPC/idle_000%d.png", 8);
        npcMap2Idle = loadCharFrame("/Image/NPC/map2_%d.png", 4);
    }

    public static Image[] getIdleAhead() {
        return idleAhead;
    }

    public static Image[] getIdleBehind() {
        return idleBehind;
    }

    public static Image[] getIdleLeft() {
        return idleLeft;
    }

    public static Image[] getIdleRight() {
        return idleRight;
    }

    public static Image[] getRunAhead() {
        return runAhead;
    }

    public static Image[] getRunBehind() {
        return runBehind;
    }

    public static Image[] getRunLeft() {
        return runLeft;
    }

    public static Image[] getRunRight() {
        return runRight;
    }

    public static Image getMap1() {
        return map1;
    }

    public static Image[][] getBlockImages() {
        return allBlocks;
    }

    private static Image[] loadCharFrame(String format, int numOfFrame) {
        Image[] frames = new Image[numOfFrame];
        for (int i = 0; i < numOfFrame; i++) {
            String path = String.format(format, i + 1);
            frames[i] = new Image(Objects.requireNonNull(LoadImage.class.getResourceAsStream(path)));
        }
        return frames;
    }

    private static void loadCharImage() {
        idleAhead = loadCharFrame("/Image/MainChar/IdleState/ahead/idle_ahead_%d.png", 12);
        idleBehind = loadCharFrame("/Image/MainChar/IdleState/behind/idle_behind_%d.png", 4);
        idleLeft = loadCharFrame("/Image/MainChar/IdleState/left/idle_left_%d.png", 12);
        idleRight = loadCharFrame("/Image/MainChar/IdleState/right/idle_right_%d.png", 12);

        runAhead = loadCharFrame("/Image/MainChar/RunState/ahead/run_ahead_%d.png", 8);
        runBehind = loadCharFrame("/Image/MainChar/RunState/behind/run_behind_%d.png", 8);
        runLeft = loadCharFrame("/Image/MainChar/RunState/left/run_left_%d.png", 8);
        runRight = loadCharFrame("/Image/MainChar/RunState/right/run_right_%d.png", 8);
    }

    public static Image getMap3() {
        return map3;
    }

    public static Image getMap2() {
        return map2;
    }

    public static void loadAllImage() {
        loadCharImage();
        loadNpcImages();
        map1 = new Image(Objects.requireNonNull(LoadImage.class.getResourceAsStream("/Image/Map/map1.png")));
        map2 = new Image(Objects.requireNonNull(LoadImage.class.getResourceAsStream("/Image/Map/map2.png")));
        map3 = new Image(Objects.requireNonNull(LoadImage.class.getResourceAsStream("/Image/Map/map3.png")));
        allBlocks = new Image[5][3]; // 5 màu, 3 trạng thái
        allBlocks[0] = loadCharFrame("/Image/Block/blue_block_%d.png", 3);
        allBlocks[1] = loadCharFrame("/Image/Block/green_block_%d.png", 3);
        allBlocks[2] = loadCharFrame("/Image/Block/pink_block_%d.png", 3);
        allBlocks[3] = loadCharFrame("/Image/Block/red_block_%d.png", 3);
        allBlocks[4] = loadCharFrame("/Image/Block/yellow_block_%d.png", 3);
    }
}

