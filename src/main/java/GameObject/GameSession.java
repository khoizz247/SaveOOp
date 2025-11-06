package GameObject;

import GameLoop.ScenePlayGame;
import LoadResource.GameStats;
import LoadResource.LoadImage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GameSession {
    private int score;
    private float timePlay;
    private String dateTimePlay;

    public GameSession() {
        reset();
    }

    public GameSession(int score, float timePlay) {
        this.score = score;
        this.timePlay = timePlay;
        this.dateTimePlay = getFormattedDateTimePlay();
    }

    public GameSession(int score, float timePlay, String dateTimePlay) {
        this.score = score;
        this.timePlay = timePlay;
        this.dateTimePlay = dateTimePlay;
    }

    public void reset() {
        score = 0;
        timePlay = 0.0f;
        dateTimePlay = getFormattedDateTimePlay();
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

    public String getDateTimePlay() {
        return dateTimePlay;
    }

    public void setDateTimePlay(String dateTimePlay) {
        this.dateTimePlay = dateTimePlay;
    }

    public String getFormattedPlayTime() {
        int totalSeconds = (int) this.timePlay;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public String getFormattedDateTimePlay() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter f = DateTimeFormatter.ofPattern("HH:mm 'ngày' dd/MM/yyyy");
        return now.format(f);
    }

    public void renderClock(GraphicsContext gc, int stateAboutToLose, int existingCoins) {
        GameStats.setMaxScore(Math.max(GameStats.getMaxScore(), score));

        String scoreText = "Score: " + score;
        String timeText = "Time: " + getFormattedPlayTime();
        String maxScoreText = "Highest Score: " + GameStats.getMaxScore();
        String coin = "Coin: " + existingCoins + " + " + ManageBuff.extraCoins;

        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 20));
        gc.setLineWidth(1);

        gc.fillText(scoreText, 10, 25);
        gc.fillText(timeText, 150, 25);
        gc.fillText(maxScoreText, 10, 50);
        gc.fillText(coin, 600, 25);
        gc.fillText(dateTimePlay, 400, 50);

        if (stateAboutToLose >= 1 && (int) (timePlay * 5) % 2 == 0 ) {
            gc.drawImage(LoadImage.getLine()[1], 0, 435);
        } else {
            gc.drawImage(LoadImage.getLine()[0], 0, 435);
        }
    }

    @Override
    public String toString() {
        return String.format("%-4d | %s | %s", score, getFormattedPlayTime(), dateTimePlay);
    }
}
