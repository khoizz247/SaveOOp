package GameObject;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class ManageBuff {
    private List<Buff> buffs;
    public static int extraCoins = 0;

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
        buffs.add(new Buff(xBlock, yBlock, widthBlock, heightBlock,10, 3, 0, 1,
                bufftype, ballCreateBuff));
    }

    public void resetBuff() {
        buffs.clear();
        extraCoins = 0;
    }

    //Đổi thành cái này
    public boolean addBuffOnScene(GraphicsContext gc, MyBlock paddle, ManageBall manageBall) {
        boolean bulletActivated = false;
        for (int i = buffs.size() - 1; i >= 0; i--) {
            Buff buff = buffs.get(i);
            String result = buff.updateBuff(paddle, manageBall);
            if (result.equals("AIMING")) {
                bulletActivated = true;
                buffs.remove(i);
            } else if (result.equals("HIT") || buff.checkOutScreen()) {
                buffs.remove(i);
                System.out.println("xu duoc them: " + extraCoins);
            } else {
                buff.addOnScene(gc);
            }
        }
        return bulletActivated;
    }

}