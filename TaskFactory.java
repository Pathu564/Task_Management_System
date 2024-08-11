import java.util.logging.Level;
 //Factory class for creating Task objects.

public class TaskFactory {
    
    public Task createTask(String name, String start, String end, String priority) {
        if (name.isEmpty()) {
            CustomLogger.log(Level.WARNING, "Task name cannot be empty.");            return null;
        }

        if (!Task.isValidTimeFormat(start) || !Task.isValidTimeFormat(end)) {
            CustomLogger.log(Level.WARNING, "Invalid time format. Please use HH:mm format.");            return null;
        }

        if (!isValidPriority(priority)) {
            CustomLogger.log(Level.WARNING, "Invalid priority level. Please choose High, Medium, or Low.");            return null;
        }

        if (start.compareTo(end) >= 0) {
            CustomLogger.log(Level.WARNING, "End time must be after start time.");            return null;
        }

        return new Task(name, start, end, priority);
    }

    /**
     * Check if the priority level is valid.
     *
     * @param priority The priority level to check.
     * @return True if valid, false otherwise.
     */
    private boolean isValidPriority(String priority) {
        return priority.equalsIgnoreCase("High") ||
               priority.equalsIgnoreCase("Medium") ||
               priority.equalsIgnoreCase("Low");
    }
}
