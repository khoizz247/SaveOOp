package GameObject;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ManageBuff {
    private List<Buff> buffs;
    public static int extraCoins = 0;
    private float timeCreateObstacle = 0f;
    private Random rand = new Random();

    public ManageBuff() {
        buffs = new ArrayList<>();
    }

    public List<Buff> getBuffs() {
        return buffs;
    }

    public void setBuffs(List<Buff> buffs) {
        this.buffs = buffs;
    }

    public void addBuff(double xBlock, double yBlock, double widthBlock, double heightBlock,
                        String bufftype, Ball ballCreateBuff) {
        buffs.add(new Buff(xBlock, yBlock, widthBlock, heightBlock, 10, 3, 0, 1,
                bufftype, ballCreateBuff));
    }

    public void resetBuff() {
        buffs.clear();
        extraCoins = 0;
        timeCreateObstacle = 0f;
    }

    public void setTimeCreateObstacle(double xPaddle, double widthPaddle, int level, float deltaTime) {
        timeCreateObstacle += deltaTime;
        if (timeCreateObstacle >= 5.f && level == 3) {
            buffs.add(new Buff(xPaddle + widthPaddle / 2, 0, 13, 4, 0, 1, "Obstacle"));
            timeCreateObstacle = 0;
        }
    }

    // 1. TẠO HÀM UPDATE MỚI
    public boolean update(MyBlock paddle, ManageBall manageBall) {
        boolean bulletActivated = false;
        for (int i = buffs.size() - 1; i >= 0; i--) {
            Buff buff = buffs.get(i);
            // Logic tính toán & va chạm
            String result = buff.updateBuff(paddle, manageBall);

            if (result.equals("AIMING")) {
                bulletActivated = true;
                buffs.remove(i);
            } else if (result.equals("HIT") || buff.checkOutScreen()) {
                buffs.remove(i);
            }
            // Xóa bỏ phần 'buff.addOnScene(gc)' khỏi đây
        }
        return bulletActivated;
    }

    // 2. TẠO HÀM RENDER MỚI
    public void render(GraphicsContext gc) {
        for (Buff buff : buffs) {
            buff.addOnScene(gc); // Chỉ vẽ
        }
    }


    public String trySpawnBuff(double lucky) {
        Map<String, Double> weights = new LinkedHashMap<>();

        weights.put(null, 100.0 / lucky);
        weights.put("Increase Paddle Width", 50.0);
        weights.put("Clone Ball", 25.0);
        weights.put("Add 3 balls", 15.0 * lucky);
        weights.put("Heart", 10.0 * lucky);
        weights.put("Coin", 10.0 * lucky);
        weights.put("Bullet", 5.0 * (lucky * 1.2));

        double totalWeight = 0.0;
        for (double weight : weights.values()) {
            totalWeight += weight;
        }

        double roll = rand.nextDouble() * totalWeight;

        for (Map.Entry<String, Double> entry : weights.entrySet()) {
            roll -= entry.getValue();
            if (roll <= 0) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void spawnBuff(double lucky, double xBlock, double yBlock, double widthBlock, double heightBlock, Ball ballCreateBuff) {
        String buffType = trySpawnBuff(lucky);
        if (buffType != null) {
            addBuff(xBlock, yBlock, widthBlock, heightBlock, buffType, ballCreateBuff);
        }
    }
}