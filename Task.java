import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private String priority;
    private boolean completed;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm[:ss]");

    public Task(String name, String startTime, String endTime, String priority) {
        this.name = name;
        this.startTime = LocalTime.parse(startTime, FORMATTER);
        this.endTime = LocalTime.parse(endTime, FORMATTER);
        this.priority = priority;
        this.completed = false;
    }

    public String getName() {
        return name;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getPriority() {
        return priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setStartTime(String startTime) {
        this.startTime = LocalTime.parse(startTime, FORMATTER);
    }

    public void setEndTime(String endTime) {
        this.endTime = LocalTime.parse(endTime, FORMATTER);
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return String.format("Task: %s | %s - %s | Priority: %s | Completed: %s",
                name,
                startTime.toString(),
                endTime.toString(),
                priority,
                completed ? "Yes" : "No");
    }

    // Validate time format as HH:mm (used when creating task via user input)
    public static boolean isValidTimeFormat(String time) {
        try {
            LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
