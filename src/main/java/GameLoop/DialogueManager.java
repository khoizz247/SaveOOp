// Trong package GameLoop (hoặc một package mới)
package GameLoop;

import GameObject.Dialogue;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DialogueManager {
    private Queue<Dialogue> dialogueQueue = new LinkedList<>();
    private Dialogue currentDialogue = null;
    private double timer = 0;

    /**
     * Bắt đầu một chuỗi hội thoại mới
     */
    public void startDialogue(List<Dialogue> dialogues) {
        // Nếu đang nói dở, không bắt đầu hội thoại mới
        if (isShowingDialogue()) return;

        dialogueQueue.clear();
        dialogueQueue.addAll(dialogues);
        showNextDialogue();
    }

    /**
     * Cập nhật logic (đếm ngược thời gian)
     */
    public void update(float deltaTime) {

    }

    public void closeDialogue() {
        currentDialogue = null;
        timer = 0;
        dialogueQueue.clear();
    }

    /**
     * Lấy câu thoại tiếp theo từ hàng đợi
     */
    private void showNextDialogue() {
        if (!dialogueQueue.isEmpty()) {
            currentDialogue = dialogueQueue.poll();
            timer = currentDialogue.getDuration();
        } else {
            currentDialogue = null;
            timer = 0;
        }
    }

    /**
     * Vẽ hộp thoại lên màn hình
     */
    public void render(GraphicsContext gc, boolean isIngame) {
        if (currentDialogue == null) return;

        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();
        double boxHeight = 100;
        double boxY; // Khai báo

        // --- BƯỚC 1: TÍNH TOÁN boxY TRƯỚC ---
        if (isIngame) {
            // NẾU TRONG TRẬN (Arkanoid) -> Vẽ ở TRÊN
            // (Bạn đã đổi các giá trị này, tôi giữ nguyên theo ý bạn)
            double topBarHeight = 50;
            boxY = 10;
        } else {
            // NẾU NGOÀI SẢNH (Lobby) -> Vẽ ở DƯỚI
            boxY = canvasHeight - boxHeight - 20; // Đặt ở dưới
        }

        // --- BƯỚC 2: DÙNG boxY ĐỂ VẼ HỘP THOẠI ---
        gc.setFill(Color.color(0, 0, 0, 0.25)); // Màu đen mờ (theo ý bạn)
        gc.fillRoundRect(20, boxY, canvasWidth - 40, boxHeight, 15, 15);

        // --- BƯỚC 3: DÙNG boxY ĐỂ VẼ CHỮ ---
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 15)); // Font (theo ý bạn)
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText(currentDialogue.getSpeaker() + ":", 40, boxY + 30);

        gc.setFont(Font.font("Arial", 12)); // Font (theo ý bạn)
        gc.fillText(currentDialogue.getLine(), 40, boxY + 60);
    }

    public void skipCurrentDialogue() {
        // Chỉ bỏ qua nếu đang có hội thoại
        if (isShowingDialogue()) {
            showNextDialogue(); // Hàm này sẽ tự lấy câu tiếp theo hoặc đóng hộp thoại
        }
    }

    public boolean isShowingDialogue() {
        return currentDialogue != null;
    }
}