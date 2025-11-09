package GameLoop;

import GameObject.Dialogue;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ShopManager {
    private boolean isShopOpen = false;
    private int selectedOption = 0; // 0 = Life, 1 = Width, 2 = Speed
    private String[] options = {"Tăng Mạng", "Tăng Độ Rộng", "Tăng Tốc Độ", "Tăng May Mắn"};
    private int cost = 1; // Chi phí (để test)

    // Dùng để hiển thị thông báo (mua thành công / thất bại)
    private Dialogue feedbackDialogue = null;
    private double feedbackTimer = 0;

    public boolean isShopOpen() {
        return isShopOpen;
    }

    public void openShop() {
        isShopOpen = true;
        selectedOption = 0;
        feedbackDialogue = null;
        feedbackTimer = 0;
    }

    public void closeShop() {
        isShopOpen = false;
    }

    public void update(float deltaTime) {
        // Cập nhật bộ đếm giờ của thông báo
        if (feedbackTimer > 0) {
            feedbackTimer -= deltaTime;
            if (feedbackTimer <= 0) {
                feedbackDialogue = null;
                closeShop(); // Tự động đóng shop sau khi thông báo biến mất
            }
        }
    }

    // Xử lý input (UP, DOWN, ENTER)
    public int handleInput(KeyCode code, int existingCoins) {
        int currentCoins = existingCoins;
        if (feedbackDialogue != null) return currentCoins;

        if (code == KeyCode.UP || code == KeyCode.W) {
            selectedOption = (selectedOption - 1 + options.length) % options.length;
        } else if (code == KeyCode.DOWN || code == KeyCode.S) {
            selectedOption = (selectedOption + 1) % options.length;
        } else if (code == KeyCode.ENTER || code == KeyCode.SPACE) {
            currentCoins = purchaseSelectedOption(currentCoins);
        } else if (code == KeyCode.ESCAPE) {
            closeShop();
        }
        return currentCoins;
    }

    private int purchaseSelectedOption(int coins) {
        int currentCoins = coins;

        if (currentCoins < cost) {
            // Không đủ tiền
            showFeedback("Không đủ xu!", 2.0);
        } else {
            // Trừ tiền
            currentCoins -= cost;

            // Nâng cấp
            switch (selectedOption) {
                case 0: // Tăng Life
                    ReadWriteData.setBaseLife(ReadWriteData.getBaseLife() + 1);
                    showFeedback("Đã tăng Mạng!", 2.0);
                    break;
                case 1: // Tăng Width
                    ReadWriteData.setBaseWidth(ReadWriteData.getBaseWidth() + 10.0); // Tăng 10px
                    showFeedback("Đã tăng Độ Rộng!", 2.0);
                    break;
                case 2: // Tăng Speed
                    ReadWriteData.setBaseSpeed(ReadWriteData.getBaseSpeed() + 0.5); // Tăng 0.5
                    showFeedback("Đã tăng Tốc Độ!", 2.0);
                    break;
                case 3:
                    ReadWriteData.setBaseLucky(ReadWriteData.getBaseLucky() + 0.1);
                    showFeedback("Đã tăng May Mắn", 2.0);
            }
            int map = ReadWriteData.getCurrentMapLevel();
            if (map == 1 || map == 2) {
                ReadWriteData.addDefeatedNpc(map, 0);
                System.out.println("Shop map " + map + " đã bị đánh dấu là 'đã dùng'.");
            }
        }
        return currentCoins;
    }

    // Hiển thị thông báo nhỏ
    private void showFeedback(String message, double duration) {
        feedbackDialogue = new Dialogue("Cửa Hàng", message, duration);
        feedbackTimer = duration;
    }

    public void render(GraphicsContext gc, int existingCoins) {
        if (!isShopOpen) return;

        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();

        // Nền mờ
        gc.setFill(Color.color(0, 0, 0, 0.8));
        gc.fillRect(0, 0, canvasWidth, canvasHeight);

        // Khung Shop
        double boxWidth = 400;
        double boxHeight = 330;
        double boxX = (canvasWidth - boxWidth) / 2;
        double boxY = (canvasHeight - boxHeight) / 2;

        gc.setFill(Color.color(0.1, 0.1, 0.2, 1.0));
        gc.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 20, 20);
        gc.setStroke(Color.WHITE);
        gc.strokeRoundRect(boxX, boxY, boxWidth, boxHeight, 20, 20);

        // Tiêu đề
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 30));
        gc.fillText("CỬA HÀNG", boxX + 110, boxY + 50);

        // Hiển thị tiền
        gc.setFont(Font.font("Arial", 20));
        gc.fillText("Số xu của bạn: " + existingCoins, boxX + 20, boxY + 90);

        // Hiển thị các lựa chọn
        for (int i = 0; i < options.length; i++) {
            gc.setFont(Font.font("Arial", 24));

            // Nếu là lựa chọn hiện tại -> Đổi màu
            if (i == selectedOption) {
                gc.setFill(Color.YELLOW);
                gc.fillText("> " + options[i] + " (" + cost + " xu)", boxX + 40, boxY + 140 + (i * 40));
            } else {
                gc.setFill(Color.WHITE);
                gc.fillText(options[i] + " (" + cost + " xu)", boxX + 60, boxY + 140 + (i * 40));
            }
        }

        // Hiển thị thông báo (Nếu có)
        if (feedbackDialogue != null) {
            gc.setFill(Color.GREEN);
            gc.setFont(Font.font("Arial", 20));
            gc.fillText(feedbackDialogue.getLine(), boxX + 40, boxY + boxHeight - 30);
        }
    }
}