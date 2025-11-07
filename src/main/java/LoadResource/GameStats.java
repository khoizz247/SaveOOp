package LoadResource;

import GameObject.GameSession;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class GameStats {
    private static int maxScore;
    private static List<GameSession> historyPlay = new ArrayList<>();
    private static final Path filePath;

    static {
        filePath = Paths.get("HistoryPlay.txt");
        loadStats();
    }

    public static int getMaxScore() {
        return maxScore;
    }

    public static void setMaxScore(int maxScore) {
        GameStats.maxScore = maxScore;
    }

    public static void loadStats() {
        historyPlay.clear(); // dọn danh sách cũ để không bị trùng
        try {
            if (Files.exists(filePath)) {
                List<String> lines = Files.readAllLines(filePath);

                if (lines.isEmpty()) {
                    maxScore = 0;
                    return;
                }

                // đọc điểm cao nhất ở dòng đầu
                try {
                    maxScore = Integer.parseInt(lines.getFirst());
                } catch (NumberFormatException e) {
                    maxScore = 0;
                }

                // đọc lịch sử chơi
                for (int i = 1; i < lines.size(); i++) {
                    String line = lines.get(i);
                    String[] parts = line.split("\\|");
                    if (parts.length == 3) {
                        try {
                            int score = Integer.parseInt(parts[0]);
                            float time = Float.parseFloat(parts[1]);
                            String date = parts[2];
                            historyPlay.add(new GameSession(score, time, date));
                        } catch (NumberFormatException e) {
                            System.err.println("Bỏ qua dòng lỗi: " + line);
                        }
                    }
                }
            } else {
                // nếu file chưa có thì tạo mới
                Files.write(filePath, List.of("0"));
                maxScore = 0;
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi đọc file: " + e.getMessage());
        }
    }

    /**
     * Lưu toàn bộ lịch sử: lượt mới lên trên cùng, cũ xuống dưới.
     */
    public static void saveStats() {
        List<String> lines = new ArrayList<>();

        // dòng đầu là maxScore
        lines.add(String.valueOf(maxScore));

        // lượt mới nhất trước
        for (GameSession session : historyPlay) {
            String info = String.format("%d|%.5f|%s",
                    session.getScore(),
                    session.getTimePlay(),
                    session.getDateTimePlay());
            lines.add(info);
        }

        try {
            Files.write(filePath, lines);
        } catch (IOException e) {
            System.err.println(" Lỗi khi lưu file: " + e.getMessage());
        }
    }

    /**
     * Thêm lượt chơi mới vào đầu danh sách.
     */
    public static void addGameSession(GameSession gameSession) {
        // thêm lượt chơi mới lên đầu
        historyPlay.add(0, new GameSession(gameSession.getScore(),
                gameSession.getTimePlay(),
                gameSession.getDateTimePlay()));

        // cập nhật maxScore nếu cần
        if (gameSession.getScore() > maxScore) {
            maxScore = gameSession.getScore();
        }

        // lưu lại toàn bộ file
        saveStats();
    }
}
