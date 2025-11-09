package LoadResource;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import java.util.Objects;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

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

    private static Image paddle;

    private static Image[] coin;
    private static Image increasePaddle;
    private static Image bullet;
    private static Image addThreeeBalls;
    private static Image cloneBall;
    private static Image[] fireBall;

    private static Image map1;
    private static Image map2;
    private static Image map3;

    private static Image arkanoidBg1;
    private static Image arkanoidBg2;
    private static Image arkanoidBg3;
    private static Image arkanoidBg4;

    private static Image healthBar;

    private static Image[] npcMap1Idle; // NPC Map 1
    private static Image[] npcMap2Idle;  // NPC Map 2
    private static Image[] npcMap3Idle;  // NPC Map 3

    private static Image[] line;

    private static Image heart;

    public static Image[][] getAllBlocks() {
        return allBlocks;
    }

    public static Image getPaddle() {
        return paddle;
    }

    public static Image[] getCoin() {
        return coin;
    }

    public static Image getIncreasePaddle() {
        return increasePaddle;
    }

    public static Image getBullet() {
        return bullet;
    }

    public static Image getAddThreeeBalls() {
        return addThreeeBalls;
    }

    public static Image getCloneBall() {
        return cloneBall;
    }

    public static Image[] getFireBall() {
        return fireBall;
    }

    public static Image[] getNpcMap1Idle() {
        return npcMap1Idle;
    }

    public static Image[] getNpcDemonIdle() {
        return npcMap1Idle;
    }

    public static Image[] getNpcMap2Idle() {
        return npcMap2Idle;
    }

    public static Image[] getNpcMap3Idle() {
        return npcMap3Idle;
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

    public static Image[] getLine() {
        return line;
    }

    public static Image getHeart() {
        return heart;
    }

    public static Image getHealthBar() {
        return healthBar;
    }

    public static Image getArkanoidBg1() { return arkanoidBg1; }

    public static Image getArkanoidBg2() { return arkanoidBg2; }

    public static Image getArkanoidBg3() { return arkanoidBg3; }

    public static Image getArkanoidBg4() { return arkanoidBg4; }

    private static void loadNpcImages() {
        npcMap3Idle = loadCharFrame("/Image/NPC/npc_map3_idle_%d.png", 6);
        npcMap1Idle = loadCharFrame("/Image/NPC/idle_000%d.png", 8);
        npcMap2Idle = loadCharFrame("/Image/NPC/map2_%d.png", 4);
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

    public static void loadBuffImage() {
        coin = loadCharFrame("/Image/Ball/coin_%d.png", 6);
        increasePaddle = new Image(Objects.requireNonNull(LoadImage.class.getResourceAsStream("/Image/Ball/increase_paddle.png")));
        bullet = new Image(Objects.requireNonNull(LoadImage.class.getResourceAsStream("/Image/Ball/bullet.png")));
        addThreeeBalls = new Image(Objects.requireNonNull(LoadImage.class.getResourceAsStream("/Image/Ball/Add_3_balls.png")));
        cloneBall = new Image(Objects.requireNonNull(LoadImage.class.getResourceAsStream("/Image/Ball/Clone_ball.png")));
        fireBall = loadCharFrame("/Image/Ball/fire_ball_%d.png", 4);
        heart = new Image(Objects.requireNonNull(LoadImage.class.getResourceAsStream("/Image/Ball/heart.png")));
    }

    public static void loadArkanoidBackgrounds() {
        arkanoidBg1 = new Image(Objects.requireNonNull(LoadImage.class.getResourceAsStream("/Image/background/background1.png")));
        arkanoidBg2 = new Image(Objects.requireNonNull(LoadImage.class.getResourceAsStream("/Image/background/background2.png")));
        arkanoidBg3 = new Image(Objects.requireNonNull(LoadImage.class.getResourceAsStream("/Image/background/background3.png")));
        arkanoidBg4 = new Image(Objects.requireNonNull(LoadImage.class.getResourceAsStream("/Image/background/background4.png")));
    }

    public static void loadAllImage() {
        loadCharImage();
        loadNpcImages();
        loadBuffImage();
        map1 = new Image(Objects.requireNonNull(LoadImage.class.getResourceAsStream("/Image/Map/map1.png")));
        map2 = new Image(Objects.requireNonNull(LoadImage.class.getResourceAsStream("/Image/Map/map2.png")));
        map3 = new Image(Objects.requireNonNull(LoadImage.class.getResourceAsStream("/Image/Map/map3.png")));
        allBlocks = new Image[5][3]; // 5 màu, 3 trạng thái
        allBlocks[0] = loadCharFrame("/Image/Block/blue_block_%d.png", 3);
        allBlocks[1] = loadCharFrame("/Image/Block/green_block_%d.png", 3);
        allBlocks[2] = loadCharFrame("/Image/Block/pink_block_%d.png", 3);
        allBlocks[3] = loadCharFrame("/Image/Block/red_block_%d.png", 3);
        allBlocks[4] = loadCharFrame("/Image/Block/yellow_block_%d.png", 3);
        line = loadCharFrame("/Image/background/line_%d.png", 2);
        paddle = new Image(Objects.requireNonNull(LoadImage.class.getResourceAsStream("/Image/Block/paddle.png")));
        healthBar = new Image(Objects.requireNonNull(LoadImage.class.getResourceAsStream("/Image/HealthBar/health_bar.png")));
        loadArkanoidBackgrounds();
    }

    public static void drawHealthBarWithMonster(GraphicsContext gc, int level) {
        // Vẽ nền thanh máu
        gc.drawImage(getHealthBar(), 0, 0);

        double barX = 0;
        double barY = 0;
        double circleCenterX = barX + 25;
        double circleCenterY = barY + 25;
        double radius = 22;

        Image monsterIcon;
        double scale;
        double offsetX = 0;
        double offsetY = 0;

        switch (level) {
            case 1 -> {
                monsterIcon = LoadImage.getNpcMap1Idle()[0];
                scale = 2.8;
                // Không cần offset cho case 1 (hoặc mặc định)
                offsetX = 0;
                offsetY = 0;
            }
            case 2 -> {
                monsterIcon = LoadImage.getNpcMap2Idle()[0];
                scale = 0.9;
                offsetX = 10;
                offsetY = 5;
            }
            case 3 -> {
                monsterIcon = LoadImage.getNpcMap3Idle()[0];
                scale = 3.0;
                offsetX = 10;
                offsetY = -5;
                // ---------------
            }
            default -> {
                monsterIcon = LoadImage.getNpcMap1Idle()[0];
                scale = 3.0;
                offsetX = 0;
                offsetY = 0;
            }
        }

        // Tính kích thước và vị trí
        double drawRadius = radius * scale;

        gc.save();
        gc.beginPath();
        gc.arc(circleCenterX, circleCenterY, radius, radius, 0, 360);
        gc.closePath();
        gc.clip();

        gc.drawImage(
                monsterIcon,
                circleCenterX - drawRadius + offsetX,
                circleCenterY - drawRadius + offsetY,
                drawRadius * 2,
                drawRadius * 2
        );

        gc.restore();
    }
}

