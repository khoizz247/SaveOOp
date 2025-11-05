package GameObject;

import LoadResource.GameStats;
import LoadResource.LoadImage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class GameSession {
    private int score;
    private float timePlay;

    public GameSession() {
        reset();
    }

    public GameSession(int score, float timePlay) {
        this.score = score;
        this.timePlay = timePlay;
    }

    public void reset() {
        this.score = 0;
        this.timePlay = 0.0f;
    }

    public void update(float deltaTime) {
        this.timePlay += deltaTime;
    }

    public void addScore(int typeBlock) {
        score += typeBlock;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public float getTimePlay() {
        return timePlay;
    }

    public void setTimePlay(float timePlay) {
        this.timePlay = timePlay;
    }

    public String getFormattedPlayTime() {
        int totalSeconds = (int) this.timePlay;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public void renderClock(GraphicsContext gc, int stateAboutToLose) {
        GameStats.setMaxScore(Math.max(GameStats.getMaxScore(), score));

        String scoreText = "Score: " + score;
        String timeText = "Time: " + getFormattedPlayTime();
        String maxScoreText = "Highest Score: " + GameStats.getMaxScore();

        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 20));
        gc.setLineWidth(1);

        gc.fillText(scoreText, 10, 25);
        gc.fillText(timeText, 150, 25);
        gc.fillText(maxScoreText, 10, 50);

        if (stateAboutToLose >= 1 && (int) (timePlay * 5) % 2 == 0 ) {
            gc.drawImage(LoadImage.getLine()[1], 0, 435);
        } else {
            gc.drawImage(LoadImage.getLine()[0], 0, 435);
        }
    }
}
