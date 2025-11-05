package GameObject;

import javafx.scene.canvas.GraphicsContext;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ManageGameBlock {
    private List<GameBlock> gameBlocks = new ArrayList<>();
    private double translation = 25;
    private int stateAboutToLose = 0;

    public List<GameBlock> getGameBlocks() {
        return gameBlocks;
    }

    public int getNumberBlock() {
        return gameBlocks.size();
    }

    public double getTranslation() {
        return translation;
    }

    public int getStateAboutToLose() {
        return stateAboutToLose;
    }

    public void resetGameBlock(int level) {
        gameBlocks.clear();

        if (level <= 3) {
            File file;
            try {
                if (level == 1) {
                    file = new File("src/main/resources/Files/Level1.txt");
                } else if (level == 2) {
                    file = new File("src/main/resources/Files/Level2.txt");
                } else {
                    file = new File("src/main/resources/Files/Level3.txt");
                }
                Scanner sc = new Scanner(file);
                int x, y, hard;

                while (sc.hasNext()) {
                    x = sc.nextInt();
                    y = sc.nextInt();
                    hard = sc.nextInt();
                    gameBlocks.add(new GameBlock((x - 1) * 80.0, 110 + (y - 1) * 25.0, hard));
                }
                sc.close();
            } catch (FileNotFoundException e) {
                System.out.println("Không tìm thấy file.");
            }
        } else {
            Random rand = new Random();
            int hard;
            for (int i = 1; i <= 50; i++) {
                hard = rand.nextInt(3) + 1;
                gameBlocks.add(new GameBlock((i % 10) * 80.0, 65 + (int)((i - 1) / 10) * 25.0, hard));
            }
        }
    }

    public void addBlock() {
        Random rand = new Random();
        int hard;
        for (int i = 1; i <= 10; i++) {
            hard = rand.nextInt(3) + 1;
            gameBlocks.add(new GameBlock((i % 10) * 80.0, 65 - 25, hard));
        }
        translation = 0;
    }

    public void addListOnScene(GraphicsContext gc, int level) {
        double maxY = -1;
        if (translation < 25 && level > 3) {
            for (GameBlock block : gameBlocks) {
                block.setY(block.getY() + 1);
                maxY = Math.max(maxY, block.getY());
                block.addOnScene(gc);
            }
            translation ++;
        } else {
            for (GameBlock block : gameBlocks) {
                maxY = Math.max(maxY, block.getY());
                block.addOnScene(gc);
            }
        }
        final double LINE_1 = 435;
        final double LINE_2 = 435 + 25;

        if (maxY < LINE_1) {
            stateAboutToLose = 0;
        } else if (maxY >= LINE_1 && maxY < LINE_2) {
            if (stateAboutToLose == 0) {
                stateAboutToLose = 1;
            } else if (stateAboutToLose == 1 && translation >= 25) {
                stateAboutToLose = 2;
            }
        } else if (maxY >= LINE_2) {
            if (stateAboutToLose < 3) {
                stateAboutToLose = 3;
            } else if (stateAboutToLose == 3 && translation >= 25) {
                stateAboutToLose = 4;
            }
        }
    }
}