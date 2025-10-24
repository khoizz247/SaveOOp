package LoadResource;

import javafx.scene.image.Image;

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

    private static Image[] block;

    private static Image map1;

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

    public static Image[] getBlock() {
        return block;
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

    public static void loadAllImage() {
        loadCharImage();
        map1 = new Image(Objects.requireNonNull(LoadImage.class.getResourceAsStream("/Image/Map/test1.png")));
        block = loadCharFrame("/Image/Block/blue_block_%d.png", 3);
    }
}

