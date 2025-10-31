package GameLoop;

import GameObject.*;

import StartGame.GameApplication;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashSet;
import java.util.Set;

public class ScenePlayGame {
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private AnimationTimer gameLoop;
    private ManageBuff listBuffs;

    private boolean running = true;
    private int level;
    boolean isIngame = false;

    private MyBlock myBlock;
    private ManageGameBlock listBlocks;
    private ManageBall listBalls;
    private MainCharacter mainCharacter;
    private Map map;


    private ManageNPC manageNPC;
    private ManageMap manageMap;
    private double preBattleX;
    private double preBattleY;
    private NPC currentOpponent = null;
    private int currentMapLevel = 1;

    private Ball aimingBall;             //Thêm Ball ngắm bắn.
    private boolean isAiming = true;     //Biến xác nhận ngắm bắn.
    private boolean isBuffBullet = false;//Biến ngắm bắn lúc có buff.
    private int existingCoins;           //Thêm thuộc tính xu.

    public void runGame(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(e -> pressedKeys.add(e.getCode()));
        canvas.setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));
        canvas.requestFocus();
        initObject();

        startLevel(gc, canvas);
    }

    private void initObject() {
        listBuffs = new ManageBuff();
        myBlock = new MyBlock(70, 8, 4);
        mainCharacter = new MainCharacter();
        map = new Map(mainCharacter.getxOnMap(), mainCharacter.getyOnMap(), mainCharacter.getSize());
        listBlocks = new ManageGameBlock();
        listBalls = new ManageBall();
        listBuffs = new ManageBuff();
        aimingBall = new Ball(myBlock.getX() + myBlock.getWidth() / 2, myBlock.getY() - 6, 6, 1, 1, 0);

        level = ReadWriteData.getLevel();
        existingCoins = ReadWriteData.getExistingCoins();

        manageNPC = new ManageNPC();
        manageMap = new ManageMap();
        System.out.println("khoi tao: " + level);
    }

    public void resetObject() {
        myBlock.resetMyBlock();
        listBlocks.resetGameBlock(level);
        listBalls.resetBall();
        listBuffs.resetBuff();
        isAiming = true;
        isBuffBullet = false;
        System.out.println("xu hien co: " + existingCoins);
        System.out.println("Reset: " + level);
    }

    public void saveData() {
        ReadWriteData.setLevel(level);
        ReadWriteData.setExistingCoins(existingCoins);
        ReadWriteData.saveGameData();
    }

    //Vong lap chinh
    private void startLevel(GraphicsContext gc, Canvas canvas) {
        listBlocks.resetGameBlock(level);
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (running) {
                    System.out.println("level: " + level);
                    if (isIngame) {
                        updateInGame();
                        renderInGame(gc, canvas);
                        if (listBlocks.getNumberBlock() == 0) {
                            // --- LOGIC THẮNG ARKAOID ---
                            isIngame = false;
                            level++;
                            existingCoins += ManageBuff.extraCoins;
                            resetObject();

                            if (currentOpponent != null) {
                                currentOpponent.setDefeated(true);
                                currentOpponent = null;
                            }

                            mainCharacter.setxOnMap(preBattleX);
                            mainCharacter.setyOnMap(preBattleY);

                        }
                        if (listBalls.getNumOfBalls() == 0 && !isAiming) {
                            // --- LOGIC THUA ARKAOID ---
                            isIngame = false;
                            resetObject();

                            mainCharacter.setxOnMap(preBattleX);
                            mainCharacter.setyOnMap(preBattleY);

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

        // --- Logic chung (Luôn chạy) ---
        if (pressedKeys.contains(KeyCode.LEFT)) {
            myBlock.setX(myBlock.getX() - myBlock.getSpeed());
        }
        if (pressedKeys.contains(KeyCode.RIGHT)) {
            myBlock.setX(myBlock.getX() + myBlock.getSpeed());
        }
        myBlock.collisionHandling();

        // --- Logic theo trạng thái ---
        if (isAiming || isBuffBullet) {
            aimingBall.inPaddle(myBlock.getX(), myBlock.getWidth());
            if (pressedKeys.contains(KeyCode.SPACE)) {
                if (isAiming) {
                    listBalls.addNewBall(aimingBall.getBallX(), aimingBall.getBallY());
                    isAiming = false;
                }
                if (isBuffBullet) {
                    listBalls.buffBullet(aimingBall.getBallX(), aimingBall.getBallY());
                    isBuffBullet = false;
                }

            }
        }
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

            // Định nghĩa hitbox cho va chạm tường
            double hitboxWidth = mainCharacter.getSize() / 3;
            double hitboxHeight = mainCharacter.getSize() / 4;
            double hitboxOffsetX = (mainCharacter.getSize() - hitboxWidth) / 2;
            double hitboxOffsetY = mainCharacter.getSize() - hitboxHeight - 5;

            // 1. Kiểm tra va chạm TƯỜNG theo trục X
            Rectangle characterBoundsX = new Rectangle(
                    nextX + hitboxOffsetX,
                    currentY + hitboxOffsetY,
                    hitboxWidth,
                    hitboxHeight
            );
            if (!manageMap.isColliding(characterBoundsX)) {
                mainCharacter.setxOnMap(nextX);
            }

            // 2. Kiểm tra va chạm TƯỜNG theo trục Y
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


        // Cập nhật vị trí camera (bản đồ) dựa trên vị trí MỚI của nhân vật
        map.setXYOnScreen(mainCharacter.getxOnMap(), mainCharacter.getyOnMap(), mainCharacter.getSize());


        double playerHitboxWidth = mainCharacter.getSize() / 3;
        double playerHitboxHeight = mainCharacter.getSize() / 2; // Hitbox cao hơn
        double playerHitboxOffsetX = (mainCharacter.getSize() - playerHitboxWidth) / 2;
        double playerHitboxOffsetY = mainCharacter.getSize() / 2; // Hitbox ở nửa dưới nhân vật

        Rectangle playerBounds = new Rectangle(
                mainCharacter.getxOnMap() + playerHitboxOffsetX,
                mainCharacter.getyOnMap() + playerHitboxOffsetY,
                playerHitboxWidth,
                playerHitboxHeight
        );

        // --- LOGIC: KIỂM TRA VA CHẠM VỚI PORTAL ---
        // --- BẮT ĐẦU THAY THẾ TỪ ĐÂY ---

        // --- LOGIC: KIỂM TRA VA CHẠM VỚI PORTAL ---
        int portalIndex = manageMap.getCollidingPortalIndex(playerBounds);
        if (portalIndex != -1) {

            int nextMap = -1;
            double nextX = 800;
            double nextY = 100;
            boolean needsNpcCheck = false;
            boolean doPushBack = false;

            if (currentMapLevel == 1) {
                if (portalIndex == 0) { // Từ 1 -> 2 (Đi tới)
                    needsNpcCheck = true; // Cần check
                    if (manageNPC.allNpcsDefeated()) {
                        nextMap = 2;
                        nextX = 25 * 16;
                        nextY = 47 * 16;
                    } else {
                        doPushBack = true; // Chưa hạ quái, đẩy lùi
                    }
                }
            } else if (currentMapLevel == 2) {
                if (portalIndex == 0) { // Từ 2 -> 3 (Đi tới)
                    needsNpcCheck = true; // Cần check
                    if (manageNPC.allNpcsDefeated()) {
                        nextMap = 3;
                        nextX = 25 * 16;
                        nextY = 45 * 16;
                    } else {
                        doPushBack = true;
                    }
                } else if (portalIndex == 1) { // Từ 2 -> 1 (Quay về)
                    needsNpcCheck = false;
                    nextMap = 1;
                    nextX = 21 * 32;
                    nextY = 3 * 32;
                }
            } else if (currentMapLevel == 3) {
                if (portalIndex == 0) { // Từ 3 -> 2 (Quay về)
                    needsNpcCheck = false;
                    nextMap = 2;
                    nextX =  26 * 16;
                    nextY = 4 * 16;
                }
            }

            // --- Xử lý kết quả ---
            if (nextMap != -1) {
                // --- CHUYỂN MAP ---
                currentMapLevel = nextMap;
                System.out.println("Chuyển sang map " + currentMapLevel);
                manageMap.loadMap(currentMapLevel);
                manageNPC.loadNpcsForMap(currentMapLevel);
                map.setMapImage(currentMapLevel);
                mainCharacter.setxOnMap(nextX);
                mainCharacter.setyOnMap(nextY);
                map.setXYOnScreen(mainCharacter.getxOnMap(), mainCharacter.getyOnMap(), mainCharacter.getSize());
                // Tính lại hitbox dựa trên vị trí MỚI của player
                playerBounds = new Rectangle(
                        mainCharacter.getxOnMap() + playerHitboxOffsetX,
                        mainCharacter.getyOnMap() + playerHitboxOffsetY,
                        playerHitboxWidth,
                        playerHitboxHeight);

            } else if (doPushBack) {
                // --- ĐẨY LÙI NHÂN VẬT (VÌ CHƯA HẠ HẾT QUÁI) ---

                if (mainCharacter.getyOnMap() > 400) { // Nếu portal ở nửa dưới map
                    mainCharacter.setyOnMap(mainCharacter.getyOnMap() - 20); // Đẩy LÊN
                } else {
                    mainCharacter.setyOnMap(mainCharacter.getyOnMap() + 20); // Đẩy XUỐNG
                }
            }
        }





        // --- LOGIC VA CHẠM VỚI NPC ---

        for (NPC npc : manageNPC.getNpcs()) {

            if (npc.isDefeated()) continue;

            Rectangle npcBounds = new Rectangle(
                    npc.getxOnMap(),
                    npc.getyOnMap(),
                    npc.getSize(),
                    npc.getSize()
            );

            if (playerBounds.intersects(npcBounds.getLayoutBounds())) {


                this.preBattleX = mainCharacter.getxOnMap();
                this.preBattleY = mainCharacter.getyOnMap();


                isIngame = true;
                currentOpponent = npc;
                level = npc.getArkanoidLevel();
                resetObject();
                break;
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
        listBalls.addListOnScene(gc, myBlock, listBlocks.getGameBlocks(), listBuffs);
        Boolean b = listBuffs.addBuffOnScene(gc, myBlock, listBalls);
        if (!isBuffBullet) {
            isBuffBullet = b;
        }
        if (isAiming || isBuffBullet) {
            //aimingArrow.draw(gc);
            aimingBall.addOnScene(gc);
        }
        gc.setFill(Color.color(0, 0 , 0 , 0.5));
        gc.fillRect(0, 0, GameApplication.WIDTH, 65);
    }

    //Render man hinh sanh
    private void renderInLoppy(GraphicsContext gc, Canvas canvas) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        map.addMapOnScreen(gc);
        mainCharacter.addCharacterOnScreen(gc, map);

        manageNPC.render(gc, map);
    }

    public boolean isIngame() {
        return isIngame;
    }

    public void setIngame(boolean ingame) {
        this.isIngame = ingame;
    }

    public void restartRPG(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();


        mainCharacter = new MainCharacter(); // Reset nhân vật về vị trí mặc định (Map 1)
        map = new Map(mainCharacter.getxOnMap(), mainCharacter.getyOnMap(), mainCharacter.getSize());
        pressedKeys.clear();


        isIngame = false;
        running = true;
        level = 1;
        currentMapLevel = 1;


        manageMap.loadMap(currentMapLevel);
        manageNPC.loadNpcsForMap(currentMapLevel);
        map.setMapImage(currentMapLevel);
        map.setXYOnScreen(mainCharacter.getxOnMap(), mainCharacter.getyOnMap(), mainCharacter.getSize());



        if (gameLoop != null) gameLoop.stop();
        startLevel(gc, canvas);
    }

    public void restartArkanoid(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        resetObject();
        pressedKeys.clear();

        isIngame = true;
        //level = 1;
        running = true;

        if (gameLoop != null) gameLoop.stop();
        startLevel(gc, canvas);
    }

    public boolean isInArkanoid() {
        return isIngame;
    }

    public void quitToMainGame() {
        isIngame = false;   // quay lại màn hình RPG
        running = true;     // đảm bảo vòng lặp tiếp tục chạy
        mainCharacter.setxOnMap(preBattleX);
        mainCharacter.setyOnMap(preBattleY);
    }
}
