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

    private NPC currentOpponent = null; // Theo dõi NPC đang giao chiến
    private int currentMapLevel = 1; // Theo dõi map RPG hiện tại

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
        myBlock = new MyBlock(70, 8, 4);
        mainCharacter = new MainCharacter();
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
                            // --- LOGIC THẮNG ARKAOID ---
                            isIngame = false;

                            if (currentOpponent != null) {
                                currentOpponent.setDefeated(true); // Đánh dấu NPC đã bị hạ
                                currentOpponent = null; // Xóa NPC đang giao chiến
                            }
                            // Không reset vội, chỉ di chuyển nhân vật ra
                            mainCharacter.setyOnMap(mainCharacter.getyOnMap() + 50); // Lùi ra 1 chút
                            // --- (Hết) ---
                        }
                        if (listBalls.getNumOfBalls() == 0) {
                            // --- LOGIC THUA ARKAOID ---
                            isIngame = false;
                            resetObject(); // Reset lại màn Arkanoid
                            // Lùi nhân vật ra
                            mainCharacter.setyOnMap(mainCharacter.getyOnMap() + 50);
                            // --- (Hết) ---
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

            // --- ĐÂY LÀ PHẦN CODE BỊ THIẾU ---
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
                mainCharacter.setxOnMap(nextX); // <-- CẬP NHẬT VỊ TRÍ X
            }

            // 2. Kiểm tra va chạm TƯỜNG theo trục Y
            Rectangle characterBoundsY = new Rectangle(
                    mainCharacter.getxOnMap() + hitboxOffsetX, // Dùng X đã được cập nhật (hoặc chưa)
                    nextY + hitboxOffsetY,
                    hitboxWidth,
                    hitboxHeight
            );
            if (!manageMap.isColliding(characterBoundsY)) {
                mainCharacter.setyOnMap(nextY); // <-- CẬP NHẬT VỊ TRÍ Y
            }
            // --- HẾT PHẦN CODE BỊ THIẾU ---
        }

        // --- PHẦN NÀY PHẢI ĐẶT BÊN NGOÀI ELSE ---
        // Cập nhật vị trí camera (bản đồ) dựa trên vị trí MỚI của nhân vật
        map.setXYOnScreen(mainCharacter.getxOnMap(), mainCharacter.getyOnMap(), mainCharacter.getSize());

        // ---
        // CÁC KIỂM TRA VA CHẠM KHÁC (PORTAL, NPC)
        // Chúng ta sẽ dùng một hitbox chung (lớn hơn một chút) cho portal và NPC
        // ---

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
        // (Sử dụng `playerBounds` thay vì `currentBounds` để dễ va chạm hơn)
        if (manageMap.isCollidingWithPortal(playerBounds)) {
            if (manageNPC.allNpcsDefeated()) {
                // --- CHUYỂN MAP ---
                if (currentMapLevel == 1) {
                    currentMapLevel = 2;
                } else {
                    currentMapLevel = 1;
                }
                System.out.println("Chuyển sang map " + currentMapLevel);

                // 1. Tải địa hình & portal mới
                manageMap.loadMap(currentMapLevel);
                // 2. Tải NPC mới
                manageNPC.loadNpcsForMap(currentMapLevel);
                // 3. Đổi ảnh nền
                map.setMapImage(currentMapLevel);
                // 4. Di chuyển nhân vật đến vị trí bắt đầu của map mới
                if (currentMapLevel == 2) {
                    mainCharacter.setxOnMap(800); // Vị trí bắt đầu map 2
                    mainCharacter.setyOnMap(1400); // Gần portal cũ (tường dưới)
                } else {
                    mainCharacter.setxOnMap(800); // Vị trí bắt đầu map 1
                    mainCharacter.setyOnMap(100); // Gần portal cũ (tường trên)
                }

            } else {
                System.out.println("Bạn phải tiêu diệt hết quái!");
                // Đẩy nhân vật lùi lại một chút để tránh spam thông báo
                if (currentMapLevel == 1) {
                    mainCharacter.setyOnMap(mainCharacter.getyOnMap() + 20); // Lùi xuống
                } else {
                    mainCharacter.setyOnMap(mainCharacter.getyOnMap() - 20); // Lùi lên
                }
            }
        }
        // --- (Hết logic portal) ---


        // --- LOGIC VA CHẠM VỚI NPC ---
        // (Dùng lại `playerBounds` đã tính ở trên)
        for (NPC npc : manageNPC.getNpcs()) {
            if (npc.isDefeated()) continue; // Bỏ qua NPC đã bị đánh bại

            Rectangle npcBounds = new Rectangle(
                    npc.getxOnMap(),
                    npc.getyOnMap(),
                    npc.getSize(),
                    npc.getSize()
            );

            if (playerBounds.intersects(npcBounds.getLayoutBounds())) {
                isIngame = true;
                currentOpponent = npc; // Lưu lại NPC đang giao chiến
                level = npc.getArkanoidLevel(); // Lấy level Arkanoid từ NPC
                resetObject(); // Tải màn Arkanoid tương ứng
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
