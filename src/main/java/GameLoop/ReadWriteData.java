package GameLoop;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet; // <-- Thêm import này
import java.util.Set;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReadWriteData {

    private static int level;
    private static int currentMapLevel = 1;
    private static double playerX = 21*32;
    private static double playerY = 48*32;
    private static Set<String> defeatedNpcIds = new HashSet<>();
    private static int existingCoins;

    private static final String SAVE_FILE_PATH = "GameProgress.txt";
    private static final String DEFAULT_CONFIG_PATH = "Files/DefaultData.txt";

    private static int baseLife = 2;
    private static double baseWidth = 70.0;
    private static double baseSpeed = 4.0;
    private static double baseLucky = 1.0;
    private static int highestMapReached = 1;
    static {
        loadGameData();
    }

    public static int getBaseLife() { return baseLife; }
    public static void setBaseLife(int life) { baseLife = life; }

    public static int getHighestMapReached() {
        return highestMapReached;
    }
    public static void setHighestMapReached(int map) {
        highestMapReached = map;
    }

    public static double getBaseWidth() { return baseWidth; }
    public static void setBaseWidth(double width) { baseWidth = width; }

    public static double getBaseSpeed() { return baseSpeed; }
    public static void setBaseSpeed(double speed) { baseSpeed = speed; }

    public static double getBaseLucky() {
        return baseLucky;
    }
    public static void setBaseLucky(double baseLucky) {
        ReadWriteData.baseLucky = baseLucky;
    }

    public static int getCurrentMapLevel() {
        return currentMapLevel;
    }

    public static void setCurrentMapLevel(int currentMapLevel) {
        ReadWriteData.currentMapLevel = currentMapLevel;
    }

    public static double getPlayerY() {
        return playerY;
    }

    public static void setPlayerY(double playerY) {
        ReadWriteData.playerY = playerY;
    }

    public static double getPlayerX() {
        return playerX;
    }

    public static void setPlayerX(double playerX) {
        ReadWriteData.playerX = playerX;
    }

    /**
     * Thêm NPC vào danh sách đã bị hạ
     */
    public static void addDefeatedNpc(int mapLevel, int arkanoidLevel) {
        String id = mapLevel + "-" + arkanoidLevel;
        defeatedNpcIds.add(id);
        System.out.println("Đã thêm vào danh sách hạ gục: " + id);
    }

    /**
     * Kiểm tra xem NPC đã bị hạ hay chưa
     */
    public static boolean isNpcDefeated(int mapLevel, int arkanoidLevel) {
        String id = mapLevel + "-" + arkanoidLevel;
        return defeatedNpcIds.contains(id);
    }

    /**
     * Xóa sạch dữ liệu NPC khi chơi game mới
     */
    public static void clearAllNpcData() {
        defeatedNpcIds.clear();
    }

    public static void loadGameData() {
        File saveFile = new File(SAVE_FILE_PATH);
        if (saveFile.exists()) {
            System.out.println("Tìm thấy file lưu. Đang tải từ: " + SAVE_FILE_PATH);
            try (BufferedReader br = new BufferedReader(new FileReader(saveFile))) {
                parseConfigFile(br);
            } catch (IOException | NumberFormatException e) {
                System.err.println("Lỗi đọc file lưu. Tải file mặc định. Lỗi: " + e.getMessage());
                loadDefaultDataFromResources();
            }
        } else {
            System.out.println("Không tìm thấy file lưu. Tải dữ liệu mặc định từ resources.");
            loadDefaultDataFromResources();
        }
    }

    /**
     * Đọc từ file mặc định trong resources.
     */
    private static void loadDefaultDataFromResources() {
        try (InputStream is = ReadWriteData.class.getClassLoader().getResourceAsStream(DEFAULT_CONFIG_PATH)) {
            if (is == null) {
                System.err.println("LỖI NGHIÊM TRỌNG: Không tìm thấy file config mặc định trong resources: " + DEFAULT_CONFIG_PATH);
                level = 1;
                existingCoins = 0;
                return;
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                parseConfigFile(br);
            }

        } catch (IOException | NumberFormatException e) {
            System.err.println("Lỗi đọc file config mặc định: " + e.getMessage());
            level = 1;
            existingCoins = 0;
        }
    }

    /**
     * Hàm chung để phân tích nội dung file.
     */
    private static void parseConfigFile(BufferedReader br) throws IOException, NumberFormatException {
        level = 1;
        existingCoins = 0;
        currentMapLevel = 1;
        playerX = 21 * 32; // Vị trí CỔ ĐIỂN của Map 1
        playerY = 48 * 32;
        baseLife = 2;     // Mặc định
        baseWidth = 70.0;   // Mặc định
        baseSpeed = 4.0;    // Mặc định
        baseLucky = 1.0;
        highestMapReached = 1;
        defeatedNpcIds.clear();
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(":");
            if (parts.length == 2) {
                String key = parts[0].trim();
                String value = parts[1].trim();

                // --- Cập nhật logic đọc ---
                if (key.equals("Level")) {
                    level = Integer.parseInt(value);
                } else if (key.equals("ExistingCoins")) {
                    existingCoins = Integer.parseInt(value);
                } else if (key.equals("CurrentMapLevel")) {
                    currentMapLevel = Integer.parseInt(value);
                } else if (key.equals("PlayerX")) {
                    playerX = Double.parseDouble(value);
                } else if (key.equals("PlayerY")) {
                    playerY = Double.parseDouble(value);
                }
                if (key.equals("Defeated")) {
                    defeatedNpcIds.add(value); // value sẽ là "1-1", "1-2"...
                }
                if (key.equals("BaseLife")) {
                    baseLife = Integer.parseInt(value);
                } else if (key.equals("BaseWidth")) {
                    baseWidth = Double.parseDouble(value);
                } else if (key.equals("BaseSpeed")) {
                    baseSpeed = Double.parseDouble(value);
                } else if (key.equals("BaseLucky")) {
                    baseLucky = Double.parseDouble(value);
                } else if (key.equals("HighestMapReached")) { // <-- THÊM ELSE IF NÀY
                    highestMapReached = Integer.parseInt(value);
                }
            }
        }
        // In ra sau khi đã đọc xong
        System.out.println("Đã tải dữ liệu: Level=" + level
                + ", Coins=" + existingCoins
                + ", Map=" + currentMapLevel
                + ", X=" + playerX
                + ", Y=" + playerY);
        System.out.println("Đã tải " + defeatedNpcIds.size() + " NPC đã bị hạ.");
    }
    /**
     * Ghi file lưu vào thư mục gốc của project
     */
    public static void saveGameData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SAVE_FILE_PATH))) {
            bw.write("Level: " + level); // Bỏ "this."
            bw.newLine();
            bw.write("ExistingCoins: " + existingCoins); // Bỏ "this."
            bw.newLine();
            bw.write("CurrentMapLevel: " + currentMapLevel); // Thêm
            bw.newLine();
            bw.write("PlayerX: " + playerX); // Thêm
            bw.newLine();
            bw.write("PlayerY: " + playerY); // Thêm
            bw.newLine();
            bw.write("BaseLife: " + baseLife);
            bw.newLine();
            bw.write("BaseWidth: " + baseWidth);
            bw.newLine();
            bw.write("BaseSpeed: " + baseSpeed);
            bw.newLine();
            bw.write("BaseLucky: " + baseLucky);
            bw.newLine();

            bw.write("HighestMapReached: " + highestMapReached);
            bw.newLine();

            for (String id : defeatedNpcIds) {
                bw.write("Defeated: " + id);
                bw.newLine();
            }
            System.out.println("Đã lưu dữ liệu game vào: " + SAVE_FILE_PATH);

        } catch (IOException e) {
            System.err.println("Lỗi khi đang lưu dữ liệu game (Không có quyền ghi?): " + e.getMessage());
        }
    }

    public static void resetAllGameData() {
        try {
            Files.deleteIfExists(Paths.get("GameProgress.txt"));

            level = 1;
            existingCoins = 0;
            currentMapLevel = 1;
            playerX = 21 * 32;
            playerY = 48 * 32;
            defeatedNpcIds.clear();
            highestMapReached = 1;
            baseLife = 2;     // Đặt lại mặc định
            baseWidth = 70.0;   // Đặt lại mặc định
            baseSpeed = 4.0;

            saveGameData();
        } catch (IOException e) {
            System.err.println("⚠️ Lỗi khi reset dữ liệu game: " + e.getMessage());
        }
    }


    public static int getLevel() {
        return level;
    }

    public static int getExistingCoins() {
        return existingCoins;
    }

    public static void setLevel(int newLevel) {
        level = newLevel;
    }

    public static void setExistingCoins(int newExistingCoins) {
        existingCoins = newExistingCoins;
    }
}