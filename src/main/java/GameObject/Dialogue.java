// Trong package GameObject (hoặc một package mới)
package GameObject;

public class Dialogue {
    private String speaker;
    private String line;
    private double duration; // Thời gian hiển thị (giây)

    public Dialogue(String speaker, String line, double duration) {
        this.speaker = speaker;
        this.line = line;
        this.duration = duration;
    }

    public String getSpeaker() { return speaker; }
    public String getLine() { return line; }
    public double getDuration() { return duration; }
}