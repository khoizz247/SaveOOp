package GameObject;

import javafx.scene.canvas.GraphicsContext;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManageGameBlock {
    private List<GameBlock> gameBlocks = new ArrayList<>();

    public List<GameBlock> getGameBlocks() {
        return gameBlocks;
    }

    public int getNumberBlock() {
        return gameBlocks.size();
    }

    public void resetGameBlock(int level) {
        gameBlocks.clear();

        File file;
        try {
            if (level == 1) {
                file = new File("src/main/resources/Files/Level1.txt");
            } else {
                file = new File("src/main/resources/Files/Level2.txt");
            }
            Scanner sc = new Scanner(file);
            int x, y, hard;

            while (sc.hasNext()) {
                x = sc.nextInt();
                y = sc.nextInt();
                hard = sc.nextInt();
                gameBlocks.add(new GameBlock((x - 1) * 100.0, 110 + (y - 1) * 25.0, hard));
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Không tìm thấy file.");
        }
    }

    public void addListOnScene(GraphicsContext gc) {
        for (GameBlock block : gameBlocks) {
            block.addOnScene(gc);
        }
    }
}