import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;


public class Task {
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private String priority;
    private boolean completed;

    // Define a constant TIME_FORMATTER for time parsing
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public Task(String name, String startTime, String endTime, String priority) {
        this.name = name;
        this.startTime = LocalTime.parse(startTime, TIME_FORMATTER);
        this.endTime = LocalTime.parse(endTime, TIME_FORMATTER);
        this.priority = priority;
        this.completed = false;  // Initially not completed
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

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setStartTime(String startTime) {
        this.startTime = LocalTime.parse(startTime, TIME_FORMATTER);
        //CustomLogger.log(Level.INFO, "Start time updated for task: " + name);
    }

    public void setEndTime(String endTime) {
        this.endTime = LocalTime.parse(endTime, TIME_FORMATTER);
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    
    public static boolean isValidTimeFormat(String time) {
        try {
            LocalTime.parse(time, TIME_FORMATTER);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format("Task{name='%s', startTime=%s, endTime=%s, priority='%s', completed=%b}", 
            name, startTime, endTime, priority, completed);
    }
}
