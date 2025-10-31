package GameLoop;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ReadWriteData {

    private static int level;
    private static int existingCoins;

    private static final String SAVE_FILE_PATH = "GameProgress.txt";
    private static final String DEFAULT_CONFIG_PATH = "Files/DefaultData.txt";

    static {
        loadGameData();
    }

    private ReadWriteData () {

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

        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(":");
            if (parts.length == 2) {
                String key = parts[0].trim();
                String value = parts[1].trim();

                if (key.equals("Level")) {
                    level = Integer.parseInt(value);
                } else if (key.equals("ExistingCoins")) {
                    existingCoins = Integer.parseInt(value);
                }
                System.out.println(level + " " + existingCoins);
            }
        }
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
            System.out.println("Đã lưu dữ liệu game vào: " + SAVE_FILE_PATH);

        } catch (IOException e) {
            System.err.println("Lỗi khi đang lưu dữ liệu game (Không có quyền ghi?): " + e.getMessage());
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