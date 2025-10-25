package GameLoop;

import GameObject.*;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import java.util.HashSet;
import java.util.Set;

public class ScenePlayGame {
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private AnimationTimer gameLoop;

    private boolean running = true;
    private int level = 1;
    boolean isIngame = false;

    private MyBlock myBlock;
    private ManageGameBlock listBlocks;
    private ManageBall listBalls;
    private MainCharacter mainCharacter;
    private Map map;
    //private NPC npc;

    private ManageNPC manageNPC;
    private ManageMap manageMap;

    public void runGame(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(e -> pressedKeys.add(e.getCode()));
        canvas.setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));

        initObject();

        startLevel(gc, canvas);
    }

    private void initObject() {
        myBlock = new MyBlock(70, 8, 4);
        mainCharacter = new MainCharacter();
        //npc = new NPC(350, 450, 350); // vị trí và kích thước NPC

        map = new Map(mainCharacter.getxOnMap(), mainCharacter.getyOnMap(), mainCharacter.getSize());
        listBlocks = new ManageGameBlock();
        listBalls = new ManageBall(myBlock.getX(), myBlock.getY(), myBlock.getWidth());
        manageNPC = new ManageNPC();
        manageMap = new ManageMap();
    }

    public void resetObject() {
        myBlock.resetMyBlock();
        listBlocks.resetGameBlock(level);
        listBalls.resetBall(myBlock.getX(), myBlock.getY(), myBlock.getWidth());
    }

    //Vong lap chinh
    private void startLevel(GraphicsContext gc, Canvas canvas) {
        listBlocks.resetGameBlock(level);
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (running) {
                    if (isIngame) {
                        updateInGame();
                        renderInGame(gc, canvas);
                        if (listBlocks.getNumberBlock() == 0) {
                            isIngame = false;
                            level ++;
                            resetObject();
                            mainCharacter.setyOnMap(mainCharacter.getyOnMap() + 1000);
                        }
                        if (listBalls.getNumOfBalls() == 0) {
                            isIngame = false;
                            resetObject();
                            mainCharacter.setyOnMap(mainCharacter.getyOnMap() + 1000);
                        }
                    } else {
                        updateInLoppy();
                        renderInLoppy(gc, canvas);
                    }
                }
            }
        };
        gameLoop.start();
    }

    //Xu li di chuyen cua gach
    private void updateInGame() {
        if (pressedKeys.contains(KeyCode.LEFT)) {
            myBlock.setX(myBlock.getX() - myBlock.getSpeed());
        }
        if (pressedKeys.contains(KeyCode.RIGHT)) {
            myBlock.setX(myBlock.getX() + myBlock.getSpeed());
        }
        myBlock.collisionHandling();
    }

    //Xu li di chuyen nhan vat
    private void updateInLoppy() {
        // manageNPC sẽ cập nhật tất cả NPC
        manageNPC.update(System.nanoTime());

        boolean isW = pressedKeys.contains(KeyCode.W);
        boolean isA = pressedKeys.contains(KeyCode.A);
        boolean isS = pressedKeys.contains(KeyCode.S);
        boolean isD = pressedKeys.contains(KeyCode.D);

        if (!(isA || isD || isW || isS) || (isA && isD) || (isW && isS)) {
            mainCharacter.setRunning(false);
        } else {
            mainCharacter.setRunning(true);

            double currentX = mainCharacter.getxOnMap();
            double currentY = mainCharacter.getyOnMap();
            double speed = mainCharacter.getSpeed();
            double diagonalSpeed = speed * Math.sin(Math.toRadians(45));

            double dx = 0;
            double dy = 0;

            if (isW) dy -= (isA || isD) ? diagonalSpeed : speed;
            if (isS) dy += (isA || isD) ? diagonalSpeed : speed;
            if (isA) dx -= (isW || isS) ? diagonalSpeed : speed;
            if (isD) dx += (isW || isS) ? diagonalSpeed : speed;

            if (isA && !isD) mainCharacter.setDirection(1);
            else if (isD && !isA) mainCharacter.setDirection(2);
            else if (isW && !isS) mainCharacter.setDirection(3);
            else if (isS && !isW) mainCharacter.setDirection(0);

            double nextX = currentX + dx;
            double nextY = currentY + dy;

            double hitboxWidth = mainCharacter.getSize() / 3;
            double hitboxHeight = mainCharacter.getSize() / 4;
            double hitboxOffsetX = (mainCharacter.getSize() - hitboxWidth) / 2;
            double hitboxOffsetY = mainCharacter.getSize() - hitboxHeight - 5;

            Rectangle characterBoundsX = new Rectangle(
                    nextX + hitboxOffsetX,
                    currentY + hitboxOffsetY,
                    hitboxWidth,
                    hitboxHeight
            );
            if (!manageMap.isColliding(characterBoundsX)) {
                mainCharacter.setxOnMap(nextX);
            }

            Rectangle characterBoundsY = new Rectangle(
                    mainCharacter.getxOnMap() + hitboxOffsetX,
                    nextY + hitboxOffsetY,
                    hitboxWidth,
                    hitboxHeight
            );
            if (!manageMap.isColliding(characterBoundsY)) {
                mainCharacter.setyOnMap(nextY);
            }
        }

        map.setXYOnScreen(mainCharacter.getxOnMap(), mainCharacter.getyOnMap(), mainCharacter.getSize());

        // Xóa hoặc bình luận dòng kiểm tra phím ENTER
        // if (pressedKeys.contains(KeyCode.ENTER)) {
        //     isIngame = true;
        // }

        // --- LOGIC MỚI: KIỂM TRA VA CHẠM VỚI NPC ĐỂ VÀO GAME ---
        // 1. Lấy hitbox của nhân vật chính
        double playerHitboxWidth = mainCharacter.getSize() / 3;
        double playerHitboxHeight = mainCharacter.getSize() / 2; // Cho hitbox cao hơn một chút để dễ va chạm
        double playerHitboxOffsetX = (mainCharacter.getSize() - playerHitboxWidth) / 2;
        double playerHitboxOffsetY = mainCharacter.getSize() / 2;

        Rectangle playerBounds = new Rectangle(
                mainCharacter.getxOnMap() + playerHitboxOffsetX,
                mainCharacter.getyOnMap() + playerHitboxOffsetY,
                playerHitboxWidth,
                playerHitboxHeight
        );

        // 2. Lặp qua tất cả các NPC để kiểm tra va chạm
        for (NPC npc : manageNPC.getNpcs()) {
            // Lấy hitbox của NPC (có thể lấy toàn bộ kích thước của NPC)
            Rectangle npcBounds = new Rectangle(
                    npc.getxOnMap(),
                    npc.getyOnMap(),
                    npc.getSize(),
                    npc.getSize()
            );

            // 3. Nếu hitbox của nhân vật giao với hitbox của NPC
            if (playerBounds.intersects(npcBounds.getLayoutBounds())) {
                isIngame = true; // Vào game!
                break; // Thoát khỏi vòng lặp ngay khi tìm thấy một va chạm
            }
        }
    }

    public void pause() {
        running = false;
    }

    public void resume() {
        running = true;
    }

    //Render man hinh game
    private void renderInGame(GraphicsContext gc, Canvas canvas) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        myBlock.addOnScene(gc);
        listBlocks.addListOnScene(gc);
        listBalls.addListOnScene(gc, myBlock, listBlocks.getGameBlocks());
    }

    //Render man hinh sanh
    private void renderInLoppy(GraphicsContext gc, Canvas canvas) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        map.addMapOnScreen(gc);
        mainCharacter.addCharacterOnScreen(gc);
        // npc.render(gc, map); // <- Bỏ dòng này
        manageNPC.render(gc, map); // <- Thay bằng dòng này
    }

    public boolean isIngame() {
        return isIngame;
    }

    public void setIngame(boolean ingame) {
        this.isIngame = ingame;
    }

    public void restartRPG(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // reset đối tượng
        mainCharacter = new MainCharacter();
        map = new Map(mainCharacter.getxOnMap(), mainCharacter.getyOnMap(), mainCharacter.getSize());
        pressedKeys.clear();

        // reset trạng thái
        isIngame = false;
        level = 1;
        running = true;

        // chạy lại vòng lặp
        if (gameLoop != null) gameLoop.stop();
        startLevel(gc, canvas);
    }

    public void restartArkanoid(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        resetObject();
        pressedKeys.clear();

        isIngame = true;
        level = 1;
        running = true;

        if (gameLoop != null) gameLoop.stop();
        startLevel(gc, canvas);
    }

    public boolean isInArkanoid() {
        return isIngame; // true = đang trong mini game bắn bóng
    }

    public void quitToMainGame() {
        isIngame = false;   // quay lại màn hình RPG
        running = true;     // đảm bảo vòng lặp tiếp tục chạy
        mainCharacter.setyOnMap(mainCharacter.getyOnMap() + 100);
    }
}
