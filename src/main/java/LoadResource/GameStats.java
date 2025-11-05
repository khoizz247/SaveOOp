package LoadResource;

import GameObject.GameSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        try {
            // KIỂM TRA NẾU FILE TỒN TẠI
            if (Files.exists(filePath)) {
                System.out.println("File " + filePath + " đã tồn tại. Đang đọc...");
                List<String> lines = Files.readAllLines(filePath);

                if (lines.isEmpty()) {
                    System.out.println("File rỗng, trả về stats mặc định.");
                    maxScore = 0;
                }

                // 1. Đọc dòng đầu tiên (Điểm cao nhất)
                try {
                    maxScore = Integer.parseInt(lines.getFirst());
                } catch (NumberFormatException e) {
                    System.err.println("Dòng điểm cao nhất bị lỗi. Dùng điểm 0.");
                    maxScore = 0;
                }

                // 2. Đọc các dòng còn lại (Các lượt chơi)
                for (int i = 1; i < lines.size(); i++) {
                    String line = lines.get(i);
                    String[] parts = line.split(" ");

                    if (parts.length == 2) {
                        try {
                            int score = Integer.parseInt(parts[0]);
                            float time = Float.parseFloat(parts[1]);
                            historyPlay.add(new GameSession(score, time));
                        } catch (NumberFormatException e) {
                            System.err.println("Bỏ qua dòng lượt chơi bị lỗi: " + line);
                        }
                    }
                }
            } else {
                // --- FILE KHÔNG TỒN TẠI: TẠO FILE ---
                System.out.println("File " + filePath + " không tồn tại. Đang tạo file mới...");
                Files.createFile(filePath);

                Files.write(filePath, List.of("0"));
                System.out.println("Đã tạo file với điểm cao nhất mặc định là 0.");
            }
        } catch (IOException e) {
            System.err.println("Lỗi I/O nghiêm trọng: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Hàm để lưu đối tượng GameStats trở lại file (ghi đè).
     */
    public static void saveStats() {
        System.out.println("Đang lưu stats vào file " + filePath + "...");
        List<String> lines = new ArrayList<>();

        // 1. Thêm điểm cao nhất vào dòng đầu tiên
        lines.add(String.valueOf(maxScore));

        // 2. Thêm tất cả các lượt chơi (dùng hàm toString() của GameSession)
        for (GameSession session : historyPlay) {
            String info = String.format("%d %.5f", session.getScore(), session.getTimePlay());
            lines.add(info);
        }
        try {
            // Ghi đè toàn bộ file với List các dòng mới
            Files.write(filePath, lines);
            System.out.println("Lưu stats thành công!");
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void addGameSession(GameSession gameSession) {
        int i = 0;
        for (i = 0; i < historyPlay.size(); i++) {
            if (historyPlay.get(i).getScore() < gameSession.getScore()) {
                break;
            }
        }
        historyPlay.add(i, new GameSession(gameSession.getScore(), gameSession.getTimePlay()));
    }
}
