package GameLoop;

import GameObject.*;

import LoadResource.GameStats;
import LoadResource.LoadImage;
import LoadResource.LoadVideo;
import StartGame.GameApplication;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashSet;
import java.util.Set;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ScenePlayGame {
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private AnimationTimer gameLoop;
    private ManageBuff listBuffs;

    private boolean running = true;
    private int level;
    boolean isIngame = false;
    private boolean showHitboxes = false;


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
    public static int currentMapLevel = 1;
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

    private Canvas gameCanvas;

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
        ReadWriteData.loadGameData();
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

        System.out.println("xu hien co: " + existingCoins);
        System.out.println("Reset: " + level);
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
        this.gameCanvas = canvas;
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

                                            // --- SỬA LỖI: GỌI LƯU GAME NGAY LẬP TỨC ---
                                            System.out.println("Lưu game sau khi tăng level... Level Arkanoid mới: " + level);
                                            saveData(); // Gọi hàm lưu game của chính bạn
                                            // -------------------------------------------
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
                            showLostScene();
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

        if (dialogueManager.isShowingDialogue()) {
            // ...thì KHÔNG cập nhật bóng, gạch, hay thanh đỡ.
            // (Nhưng renderInGame vẫn chạy để vẽ hộp thoại)
            return;
        }

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

        listBlocks.update(level); // <-- MỚI
        listBalls.update(myBlock, listBlocks.getGameBlocks(), listBuffs, gameSession); // <-- MỚI
        Boolean b = listBuffs.update(myBlock, listBalls); // <-- MỚI
        if (!isBuffBullet) {
            isBuffBullet = b;
        }

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
            return;
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

            // --- SỬA TỌA ĐỘ Y TRONG KHỐI NÀY ---
            if (currentMapLevel == 1) {
                if (portalIndex == 0) { // Từ 1 -> 2 (Đi tới)
                    needsNpcCheck = true; // Cần check
                    if (manageNPC.allNpcsDefeated()) {
                        nextMap = 2;
                        nextX = 25 * 16;
                        nextY = 45 * 16;         // <-- SỬA TỪ 47*16
                        firstBattleX = 25 * 16;
                        firstBattleY = 45 * 16;  // <-- SỬA TỪ 47*16
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
                        nextY = 45 * 16;         // <-- Chỗ này giữ nguyên
                        firstBattleX = 25 * 16;
                        firstBattleY = 44 * 16;  // <-- Chỗ này giữ nguyên
                    } else {
                        doPushBack = true;
                    }
                } else if (portalIndex == 1) { // Từ 2 -> 1 (Quay về)
                    needsNpcCheck = false;
                    nextMap = 1;
                    nextX = 21 * 32;
                    nextY = 3 * 32;          // <-- Chỗ này giữ nguyên
                    firstBattleX = 21 * 32;
                    firstBattleY = 3 * 32;   // <-- Chỗ này giữ nguyên
                }
            } else if (currentMapLevel == 3) {
                if (portalIndex == 0) { // Từ 3 -> 2 (Quay về)
                    needsNpcCheck = false;
                    nextMap = 2;
                    nextX = 26 * 16;
                    nextY = 7 * 16;          // <-- SỬA TỪ 4*16
                    firstBattleX = 26 * 16;
                    firstBattleY = 7 * 16;   // <-- SỬA TỪ 4*16
                }
            }
            // --- KẾT THÚC SỬA TỌA ĐỘ ---


            // --- Xử lý kết quả (Dùng hàm saveData() và logic video) ---
            if (nextMap != -1) {
                // --- BƯỚC 1: DỪNG GAME LOOP HIỆN TẠI ---
                gameLoop.stop(); // Rất quan trọng!

                // --- BƯỚC 2: KIỂM TRA XEM CÓ CẦN PHÁT VIDEO KHÔNG ---
                boolean playVideo = false;
                int highestMap = ReadWriteData.getHighestMapReached();

                if (nextMap > highestMap) {
                    playVideo = true; // Đây là map mới, bật video
                    ReadWriteData.setHighestMapReached(nextMap); // Cập nhật map cao nhất
                }
                // (Nếu nextMap <= highestMap, playVideo sẽ là false,
                // có nghĩa là đi ngược về và sẽ bỏ qua video)

                // --- BƯỚC 3: CẬP NHẬT BIẾN VÀ LƯU GAME ---
                this.currentMapLevel = nextMap;
                mainCharacter.setxOnMap(nextX);
                mainCharacter.setyOnMap(nextY);
                // (firstBattleX/Y đã được set ở khối if/else bên trên)

                saveData(); // GỌI HÀM LƯU (sẽ lưu cả highestMap mới)
                System.out.println("Đã gọi saveData(). Map mới: " + this.currentMapLevel);

                final int targetMap = nextMap;
                final boolean finalPlayVideo = playVideo; // Cần final để dùng trong lambda

                // --- BƯỚC 4: GỌI VIDEO (ĐÃ SỬA) ---
                Platform.runLater(() -> {
                    try {
                        Stage stage = (Stage) gameCanvas.getScene().getWindow();

                        // Đây là hàm onFinish (sẽ chạy SAU KHI video kết thúc)
                        Runnable onVideoFinish = () -> {
                            try {
                                // Tải lại scene game y hệt như ControlHomeScene
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scene/ingame-view.fxml"));
                                Scene scene = new Scene(loader.load(), 800, 600);
                                stage.setScene(scene);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        };

                        // KIỂM TRA CỜ "PLAYVIDEO"
                        if (finalPlayVideo) {
                            // Chỉ phát video NẾU LÀ MAP MỚI
                            System.out.println("Lần đầu đến map " + targetMap + ". Đang phát video...");
                            LoadResource.LoadVideo.playTransitionVideo(stage, onVideoFinish, targetMap);
                        } else {
                            // Bỏ qua video, tải map ngay lập tức
                            System.out.println("Đang quay lại map " + targetMap + ". Bỏ qua video.");
                            onVideoFinish.run();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

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

            Rectangle npcBounds = npc.getBattleBounds();

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
                // --- LOGIC CHỈ DÀNH CHO NPC CHIẾN ĐẤU (ĐÃ SỬA THỨ TỰ) ---

                // 1. Va chạm để NÓI CHUYỆN (VÙNG LỚN - CHECK TRƯỚC)
                if (playerBounds.intersects(npc.getProximityBounds().getLayoutBounds())) {
                    if (!npc.hasSpokenProximity() && !dialogueManager.isShowingDialogue()) {
                        dialogueManager.startDialogue(npc.getProximityDialogue());
                        npc.setHasSpokenProximity(true); // Đánh dấu đã nói
                    }
                }
                // 2. Reset cờ "đã nói" nếu đi xa
                else {
                    npc.setHasSpokenProximity(false);
                }

                // 3. Va chạm để BẮT ĐẦU TRẬN ĐẤU (VÙNG NHỎ - CHECK SAU CÙNG)
                // (Lưu ý: Đây là 'if', không phải 'else if')
                if (playerBounds.intersects(npc.getBattleBounds().getLayoutBounds())) {

                    // Chỉ bắt đầu trận nếu không đang bận nói chuyện
                    if (!dialogueManager.isShowingDialogue()) {
                        this.preBattleX = mainCharacter.getxOnMap();
                        this.preBattleY = mainCharacter.getyOnMap();

                        isIngame = true;
                        currentOpponent = npc;
                        currentOpponent.setHasSpokenTaunt(false); // Reset cờ thoại 20%
                        this.currentBattleLevel = npc.getArkanoidLevel();
                        resetObject();
                        break; // Thoát khỏi vòng lặp 'for' (vì đã vào trận)
                    }
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
        Image bg;
        switch (level) {
            case 1 -> bg = LoadImage.getArkanoidBg1();
            case 2 -> bg = LoadImage.getArkanoidBg2();
            case 3 -> bg = LoadImage.getArkanoidBg3();
            case 4 -> bg = LoadImage.getArkanoidBg4();
            default -> bg = LoadImage.getArkanoidBg1();
        }
        gc.drawImage(bg, 0, 0, GameApplication.WIDTH, GameApplication.HEIGHT);
        for (int i = 1; i <= myBlock.getLife(); i++) {
            gc.drawImage(LoadImage.getHeart(), 10 + (i - 1) * 25, 570, 20, 20);
        }
        myBlock.addOnScene(gc);
        listBlocks.render(gc); // <-- SỬA
        listBalls.render(gc); // <-- SỬA
        listBuffs.render(gc); // <-- SỬA

        if (isAiming || isBuffBullet) {
            aimingBall.addOnScene(gc);
        }
        gc.drawImage(bg, 0, 0, bg.getWidth(), bg.getHeight() * 65 / 600,
                0, 0, 800, 65);
        gc.setFill(Color.color(0, 0, 0, 0.5));
        gc.fillRect(0, 0, GameApplication.WIDTH, 65);

        if (level < 4) {
            gc.drawImage(LoadImage.getHealthBar(), 0, 0);
            LoadImage.drawHealthBarWithMonster(gc, level);
        } else {
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

        if (this.showHitboxes) {
            // --- BẮT ĐẦU CODE ĐỂ VẼ HITBOX (DEBUG) ---
            gc.setLineWidth(2); // Đặt độ dày nét vẽ chung

            // 1. Vẽ hitbox TƯỜNG (Màu đỏ)
            gc.setStroke(Color.RED);
            if (manageMap.getCollisionBounds() != null) {
                for (Rectangle r : manageMap.getCollisionBounds()) {
                    double drawX = r.getX() + map.getxOnScreen();
                    double drawY = r.getY() + map.getyOnScreen();
                    gc.strokeRect(drawX, drawY, r.getWidth(), r.getHeight());
                }
            }

            // 2. Vẽ hitbox CỔNG (Màu xanh dương)
            gc.setStroke(Color.BLUE);
            if (manageMap.getPortalBounds() != null) {
                for (Rectangle r : manageMap.getPortalBounds()) {
                    double drawX = r.getX() + map.getxOnScreen();
                    double drawY = r.getY() + map.getyOnScreen();
                    gc.strokeRect(drawX, drawY, r.getWidth(), r.getHeight());
                }
            }

            // --- CODE MỚI ĐỂ VẼ HITBOX NPC ---
            if (manageNPC.getNpcs() != null) {
                for (NPC npc : manageNPC.getNpcs()) {
                    if (npc.isDefeated()) continue; // Bỏ qua nếu đã bị hạ

                    // 3. Vẽ hitbox "VÀO TRẬN" (Màu cam)
                    // Đây là hitbox nhỏ, va chạm sẽ vào Arkanoid.
                    gc.setStroke(Color.ORANGE);
                    Rectangle battleBox = npc.getBattleBounds(); // <-- Dùng hàm mới
                    double battleDrawX = battleBox.getX() + map.getxOnScreen();
                    double battleDrawY = battleBox.getY() + map.getyOnScreen();
                    gc.strokeRect(battleDrawX, battleDrawY, battleBox.getWidth(), battleBox.getHeight());

                    // 4. Vẽ hitbox "THOẠI / SHOP" (Màu xanh lơ)
                    // Đây là hitbox lớn, đi vào sẽ kích hoạt thoại.
                    gc.setStroke(Color.CYAN);
                    Rectangle proxBox = npc.getProximityBounds();
                    double proxDrawX = proxBox.getX() + map.getxOnScreen();
                    double proxDrawY = proxBox.getY() + map.getyOnScreen();
                    gc.strokeRect(proxDrawX, proxDrawY, proxBox.getWidth(), proxBox.getHeight());
                }
            }

            // (Tùy chọn) Vẽ hitbox của chính NGƯỜI CHƠI (Màu vàng)
            // Lấy lại code tạo hitbox từ hàm updateInLoppy
            double playerHitboxWidth = mainCharacter.getSize() / 3;
            double playerHitboxHeight = mainCharacter.getSize() / 2;
            double playerHitboxOffsetX = (mainCharacter.getSize() - playerHitboxWidth) / 2;
            double playerHitboxOffsetY = mainCharacter.getSize() / 2;

            gc.setStroke(Color.YELLOW);
            // Dùng tọa độ VẼ của nhân vật (characterDrawX)
            gc.strokeRect(
                    map.characterDrawX + playerHitboxOffsetX,
                    map.characterDrawY + playerHitboxOffsetY,
                    playerHitboxWidth,
                    playerHitboxHeight
            );
            // --- KẾT THÚC CODE DEBUG ---
        }
    }

    public boolean isIngame() {
        return isIngame;
    }

    public void setIngame(boolean ingame) {
        this.isIngame = ingame;
    }

    /**
     * Bật/tắt chế độ debug hitbox.
     * (Được gọi từ ControlGameScene khi ấn phím H)
     */
    public void toggleDebugHitbox() {
        this.showHitboxes = !this.showHitboxes;
        System.out.println("Debug Hitboxes: " + (this.showHitboxes ? "ON" : "OFF"));
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

    public boolean isDialogueActive() {
        return dialogueManager.isShowingDialogue();
    }

    public void handleDialogueInput(KeyCode code) {
        if (code == KeyCode.ENTER || code == KeyCode.SPACE) {

            // Trường hợp 1: Đây là hội thoại để MỞ SHOP
            // (currentShopNPC được gán khi bạn đến gần shop)
            if (currentShopNPC != null) {
                dialogueManager.closeDialogue(); // Đóng hội thoại
                isShopUIActive = true;           // Mở UI Shop
                shopManager.openShop();
            }
            // Trường hợp 2: Đây là hội thoại bình thường
            else {
                dialogueManager.skipCurrentDialogue();
            }
        }
    }

    public boolean isInArkanoid() {
        return isIngame;
    }

    public void quitToMainGame() {
        isIngame = false;
        running = true;
        mainCharacter.setxOnMap(firstBattleX);
        mainCharacter.setyOnMap(firstBattleY);
    }

    private void showLostScene() {
        Platform.runLater(() -> {
            try {
                Stage stage = (Stage) gameCanvas.getScene().getWindow();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scene/lost-view.fxml"));
                Scene lostScene = new Scene(loader.load(), 800, 600);

                stage.setScene(lostScene);
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

