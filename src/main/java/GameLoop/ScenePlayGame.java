package GameLoop;

import GameObject.*;

import LoadResource.GameStats;
import LoadResource.LoadImage;
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
    private GameSession gameSession;

    private ManageNPC manageNPC;
    private ManageMap manageMap;
    private double preBattleX;
    private double preBattleY;
    private double firstBattleX = 21 * 32;
    private double firstBattleY = 48 * 32;
    private NPC currentOpponent = null;
    private int currentMapLevel = 1;
    private int currentBattleLevel;
    private Ball aimingBall;
    private boolean isAiming = true;
    private boolean isBuffBullet = false;
    private int existingCoins;

    private float blockSpawnTimer = 0.0f;
    private long lastFrameTime = 0;
    private static float BLOCK_SPAWN_TIME = 30.0f;

    private DialogueManager dialogueManager;
    private ShopManager shopManager;
    private boolean isShopUIActive = false; // <-- MỚI
    private NPC currentShopNPC = null; // <-- MỚI (Lưu NPC shop đang tương tác)// <-- MỚI
    private int initialBlockCount = 0; // Để tính 20%

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
        gameSession = new GameSession();
        dialogueManager = new DialogueManager(); // <-- KHỞI TẠO
        shopManager = new ShopManager();
        level = ReadWriteData.getLevel();
        existingCoins = ReadWriteData.getExistingCoins();
        this.currentMapLevel = ReadWriteData.getCurrentMapLevel();

        // 2. Lấy vị trí đã lưu
        double startX = ReadWriteData.getPlayerX();
        double startY = ReadWriteData.getPlayerY();

        // 3. Khởi tạo nhân vật TRƯỚC, SAU ĐÓ set vị trí
        mainCharacter = new MainCharacter();
        mainCharacter.setxOnMap(startX);
        mainCharacter.setyOnMap(startY);

        firstBattleX = startX;
        firstBattleY = startY;

        // 4. Khởi tạo map dựa trên vị trí MỚI
        map = new Map(mainCharacter.getxOnMap(), mainCharacter.getyOnMap(), mainCharacter.getSize());

        // 5. Tải đúng map và NPC cho map đó
        manageMap = new ManageMap();
        manageMap.loadMap(this.currentMapLevel); // Tải va chạm

        manageNPC = new ManageNPC();
        manageNPC.loadNpcsForMap(this.currentMapLevel); // Tải NPC

        map.setMapImage(this.currentMapLevel);
        System.out.println("khoi tao: " + level);
    }

    public void resetObject() {
        myBlock.resetMyBlock();
        initialBlockCount = listBlocks.resetGameBlock(currentBattleLevel); // <--- Chỉ cần 1 lần
        listBalls.resetBall();
        listBuffs.resetBuff();
        isAiming = true;
        isBuffBullet = false;
        lastFrameTime = 0;
        gameSession.reset();
    }

    public void saveData() {
        ReadWriteData.setLevel(level);
        ReadWriteData.setExistingCoins(existingCoins);
        ReadWriteData.setCurrentMapLevel(this.currentMapLevel);
        ReadWriteData.setPlayerX(mainCharacter.getxOnMap());
        ReadWriteData.setPlayerY(mainCharacter.getyOnMap());
        ReadWriteData.saveGameData();

    }

    //Vong lap chinh
    private void startLevel(GraphicsContext gc, Canvas canvas) {
        lastFrameTime = 0;
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastFrameTime == 0) {
                    lastFrameTime = now;
                    return;
                }
                float deltaTime = (now - lastFrameTime) / 1_000_000_000.0f;
                lastFrameTime = now;

                if (running) {
                    dialogueManager.update(deltaTime);
                    shopManager.update(deltaTime);
                    if (isIngame) {
                        updateInGame(deltaTime);
                        renderInGame(gc, canvas);

                        if (listBlocks.getNumberBlock() == 0) {
                            // --- LOGIC THẮNG ARKAOID (ĐÃ SỬA) ---
                            if (level <= 3) {
                                isIngame = false;
                                if (currentOpponent != null) {
                                    // --- BẮT ĐẦU LOGIC MỚI ---
                                    // Kiểm tra xem đây có phải là NPC chơi lại (boss 2 map 3) không
                                    // (Arkanoid level 4 LÀ boss 2)
                                    boolean isRepeatableBoss = (currentMapLevel == 3 && currentOpponent.getArkanoidLevel() == 4);

                                    if (isRepeatableBoss) {
                                        // ---- Đây là NPC chơi lại ----
                                        System.out.println("Thắng NPC chơi lại. Quay về lobby.");
                                        existingCoins += 10;

                                        // ĐẶT LẠI VỊ TRÍ (ĐẨY LÙI)
                                        mainCharacter.setxOnMap(preBattleX);
                                        mainCharacter.setyOnMap(preBattleY + 40); // Đẩy lùi 40 pixel

                                    } else {
                                        // ---- Đây là NPC đánh 1 lần (logic cũ) ----
                                        boolean wasAlreadyDefeated = ReadWriteData.isNpcDefeated(
                                                currentMapLevel,
                                                currentOpponent.getArkanoidLevel()
                                        );

                                        if (!wasAlreadyDefeated) {
                                            System.out.println("Lần đầu hạ gục NPC, tăng level!");
                                            level++;
                                            existingCoins += ManageBuff.extraCoins;

                                            currentOpponent.setDefeated(true);
                                            ReadWriteData.addDefeatedNpc(
                                                    currentMapLevel,
                                                    currentOpponent.getArkanoidLevel()
                                            );

                                            // Kích hoạt spawn quái (ví dụ: hạ boss 3 sẽ spawn boss 4)
                                            manageNPC.onNpcDefeated(currentOpponent);
                                        }

                                        // ĐẶT LẠI VỊ TRÍ (VỀ CHỖ CŨ)
                                        mainCharacter.setxOnMap(preBattleX);
                                        mainCharacter.setyOnMap(preBattleY);
                                    }
                                    // --- KẾT THÚC LOGIC MỚI ---

                                    currentOpponent = null; // Luôn set về null
                                }

                                resetObject();
                                // --- 2 DÒNG mainCharacter.set... TỪNG Ở ĐÂY ĐÃ BỊ XÓA ---
                                // --- VÀ DI CHUYỂN VÀO BÊN TRONG IF/ELSE Ở TRÊN ---
                            } else {
                                blockSpawnTimer = BLOCK_SPAWN_TIME;
                            }
                        }
                        if (listBalls.getNumOfBalls() == 0 && !isAiming) {
                            myBlock.setLife(myBlock.getLife() - 1);
                            isAiming = true;
                        }
                        if (myBlock.getLife() <= 0) {
                            if (level >= 4) {
                                existingCoins += ManageBuff.extraCoins;
                                GameStats.addGameSession(gameSession);
                            }
                            isIngame = false;
                            mainCharacter.setxOnMap(preBattleX);
                            mainCharacter.setyOnMap(preBattleY + 40);
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

    // --- HÀM MỚI: NHẬN INPUT TỪ ControlGameScene ---

    /**
     * Xử lý input riêng cho Shop UI (ấn 1 lần)
     */
    public void handleShopInput(KeyCode code) {
        if (isShopUIActive) {
            existingCoins = shopManager.handleInput(code, existingCoins);

            // Nếu shop vừa đóng (sau khi mua / ấn ESC)
            if (!shopManager.isShopOpen()) {
                isShopUIActive = false;
            }
        }
    }

    // --- HÀM MỚI ---
    public boolean isShopUIActive() {
        return isShopUIActive;
    }

    //Xu li di chuyen cua gach
    private void updateInGame(float deltaTime) {

        if (currentOpponent != null && !currentOpponent.hasSpokenTaunt() && initialBlockCount > 0) {
            int currentBlocks = listBlocks.getNumberBlock();
            double percentage = (double) currentBlocks / initialBlockCount;

            if (currentBlocks == 1) {
                dialogueManager.startDialogue(currentOpponent.getBattleTauntDialogue());
                currentOpponent.setHasSpokenTaunt(true);
            }
        }
        if (!isAiming) {
            if (level >= 4) {
                blockSpawnTimer += deltaTime;
                gameSession.update(deltaTime);
            }
            listBuffs.setTimeCreateObstacle(myBlock.getX(), myBlock.getWidth(), level, deltaTime);
        }
        // --- Logic chung (Luôn chạy) ---
        if (pressedKeys.contains(KeyCode.A)) {
            myBlock.setX(myBlock.getX() - myBlock.getSpeed());
        }
        if (pressedKeys.contains(KeyCode.D)) {
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
        if (blockSpawnTimer >= (BLOCK_SPAWN_TIME - (int) (gameSession.getScore() / 25))) {
            listBlocks.addBlock();
            blockSpawnTimer = 0.0f;
        }
        if (listBlocks.getStateAboutToLose() == 4) {
            existingCoins += ManageBuff.extraCoins;
            GameStats.addGameSession(gameSession);
            isIngame = false;
            resetObject();
            mainCharacter.setxOnMap(preBattleX);
            mainCharacter.setyOnMap(preBattleY + 40);
        }
    }

    //Xu li di chuyen nhan vat
    private void updateInLoppy() {
        // manageNPC sẽ cập nhật tất cả NPC
        manageNPC.update(System.nanoTime());

        if (isShopUIActive) {
            mainCharacter.setRunning(false);
            return; // Dừng, không làm gì khác
        }

        if (dialogueManager.isShowingDialogue()) {
            mainCharacter.setRunning(false);
            if (currentShopNPC != null && pressedKeys.contains(KeyCode.ENTER)) {
                dialogueManager.closeDialogue(); // Đóng hội thoại
                isShopUIActive = true;           // Mở UI Shop
                shopManager.openShop();
                pressedKeys.clear(); // Xóa phím ENTER để tránh lỗi
            }
        } else {
            // ... (toàn bộ code di chuyển W,A,S,D cũ) ...
            // (Cut và Paste toàn bộ code xử lý di chuyển vào trong khối else này)
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
                        firstBattleX = 25 * 16;
                        firstBattleY = 46 * 16;

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
                        firstBattleX = 25 * 16;
                        firstBattleY = 44 * 16;
                    } else {
                        doPushBack = true;
                    }
                } else if (portalIndex == 1) { // Từ 2 -> 1 (Quay về)
                    needsNpcCheck = false;
                    nextMap = 1;
                    nextX = 21 * 32;
                    nextY = 3 * 32;
                    firstBattleX = 21 * 32;
                    firstBattleY = 3 * 32;
                }
            } else if (currentMapLevel == 3) {
                if (portalIndex == 0) { // Từ 3 -> 2 (Quay về)
                    needsNpcCheck = false;
                    nextMap = 2;
                    nextX = 26 * 16;
                    nextY = 4 * 16;
                    firstBattleX = 26 * 16;
                    firstBattleY = 4 * 16;
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
                System.out.println("Đã chuyển map, tự động lưu game...");
                ReadWriteData.setCurrentMapLevel(currentMapLevel);
                ReadWriteData.setPlayerX(mainCharacter.getxOnMap());
                ReadWriteData.setPlayerY(mainCharacter.getyOnMap());
                ReadWriteData.saveGameData();

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
                    npc.getxOnMap(), npc.getyOnMap(), npc.getSize(), npc.getSize()
            );

            if (npc.getNpcType() == 99) {

                // --- LOGIC CHỈ DÀNH CHO SHOP ---
                if (playerBounds.intersects(npc.getProximityBounds().getLayoutBounds())) {
                    if (!npc.hasSpokenProximity() && !dialogueManager.isShowingDialogue()) {
                        dialogueManager.startDialogue(npc.getProximityDialogue());
                        npc.setHasSpokenProximity(true);
                        currentShopNPC = npc; // Đánh dấu là đang tương tác với NPC này
                    }
                } else {
                    // Nếu đi xa, reset cờ
                    npc.setHasSpokenProximity(false);
                    if (currentShopNPC == npc) {
                        currentShopNPC = null; // Không còn tương tác nữa
                    }
                }

            } else {

                // --- LOGIC CHỈ DÀNH CHO NPC CHIẾN ĐẤU (Loại thường) ---
                // 1. Va chạm để BẮT ĐẦU TRẬN ĐẤU
                if (playerBounds.intersects(npcBounds.getLayoutBounds())) {
                    // Chỉ bắt đầu trận nếu không đang bận nói chuyện
                    if (!dialogueManager.isShowingDialogue()) {
                        this.preBattleX = mainCharacter.getxOnMap();
                        this.preBattleY = mainCharacter.getyOnMap();

                        isIngame = true;
                        currentOpponent = npc;
                        currentOpponent.setHasSpokenTaunt(false); // Reset cờ thoại 20%
                        this.currentBattleLevel = npc.getArkanoidLevel();
                        resetObject();
                        break;
                    }
                }
                // 2. Va chạm để NÓI CHUYỆN (vùng lớn hơn)
                else if (playerBounds.intersects(npc.getProximityBounds().getLayoutBounds())) {
                    if (!npc.hasSpokenProximity() && !dialogueManager.isShowingDialogue()) {
                        dialogueManager.startDialogue(npc.getProximityDialogue());
                        npc.setHasSpokenProximity(true); // Đánh dấu đã nói
                    }
                }
                //3. (Tùy chọn) Reset cờ "đã nói" nếu đi xa
                else {
                    npc.setHasSpokenProximity(false);
                }
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
        for (int i = 1; i <= myBlock.getLife(); i++) {
            gc.drawImage(LoadImage.getHeart(), 10 + (i - 1) * 25, 570, 20, 20);
        }
        myBlock.addOnScene(gc);
        listBlocks.addListOnScene(gc, level);
        listBalls.addListOnScene(gc, myBlock, listBlocks.getGameBlocks(), listBuffs, gameSession);
        Boolean b = listBuffs.addBuffOnScene(gc, myBlock, listBalls);
        if (!isBuffBullet) {
            isBuffBullet = b;
        }
        if (isAiming || isBuffBullet) {
            aimingBall.addOnScene(gc);
        }
        gc.setFill(Color.color(0, 0, 0, 0.5));
        gc.fillRect(0, 0, GameApplication.WIDTH, 65);

        if (level >= 4) {
            gameSession.renderClock(gc, listBlocks.getStateAboutToLose(), existingCoins);
        }
        dialogueManager.render(gc, true);
    }

    //Render man hinh sanh
    private void renderInLoppy(GraphicsContext gc, Canvas canvas) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        map.addMapOnScreen(gc);
        mainCharacter.addCharacterOnScreen(gc, map);

        manageNPC.render(gc, map);
        dialogueManager.render(gc, false);
        shopManager.render(gc, existingCoins);
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
        existingCoins = 0; // Thêm reset tiền

        ReadWriteData.clearAllNpcData(); // Xóa dữ liệu NPC đã hạ
        ReadWriteData.setLevel(level); // Cập nhật lại file save
        ReadWriteData.setExistingCoins(existingCoins);
        ReadWriteData.setCurrentMapLevel(currentMapLevel);
        ReadWriteData.setPlayerX(mainCharacter.getxOnMap());
        ReadWriteData.setPlayerY(mainCharacter.getyOnMap());
        ReadWriteData.saveGameData(); // Lưu trạng thái "game mới"

        manageMap.loadMap(currentMapLevel);
        manageNPC.loadNpcsForMap(currentMapLevel); // Tải lại NPC cho map (giờ sẽ trống)
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

    public void addPressedKey(KeyCode code) {
        pressedKeys.add(code);
    }

    public void removePressedKey(KeyCode code) {
        pressedKeys.remove(code);
    }

    public boolean isInArkanoid() {
        return isIngame;
    }

    public void quitToMainGame() {
        isIngame = false;    // quay lại màn hình RPG
        running = true;      // đảm bảo vòng lặp tiếp tục chạy
        mainCharacter.setxOnMap(firstBattleX);
        mainCharacter.setyOnMap(firstBattleY);
    }
}

