import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;

public class ScheduleManager {
    private static ScheduleManager instance;
    private TaskDAO taskDAO;
    private List<ScheduleObserver> observers;

    private ScheduleManager() {
        taskDAO = new TaskDAO();
        observers = new ArrayList<>();
    }

    public static ScheduleManager getInstance() {
        if (instance == null) {
            instance = new ScheduleManager();
        }
        return instance;
    }

    public void addObserver(ScheduleObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers(Task task) {
        for (ScheduleObserver observer : observers) {
            observer.onTaskConflict(task);
        }
    }

    public String addTask(Task task) {
        List<Task> allTasks = taskDAO.getAllTasks();
        ArrayList<String> conflicts = getConflictingTasks(task, allTasks);
        if (!conflicts.isEmpty()) {
            notifyObservers(task);
            CustomLogger.log(Level.WARNING, "Task time conflict detected for task: " + task.getName());
            for (String conflict : conflicts) {
                System.out.println(conflict);
            }
            return "Task not added due to conflicts.";
        }

        if (taskDAO.taskExists(task.getName())) {
            return "Task with this name already exists.";
        }

        boolean inserted = taskDAO.insertTask(task);
        return inserted ? "Task added successfully." : "Failed to add task.";
    }

    public String removeTask(String name) {
        boolean deleted = taskDAO.deleteTask(name);
        return deleted ? "Task removed successfully." : "Task not found.";
    }

    public String viewTasks() {
        List<Task> tasks = taskDAO.getAllTasks();
        tasks.sort(Comparator.comparing(Task::getStartTime));
        return tasks.isEmpty() ? "No tasks found." : tasks.toString();
    }

    public boolean taskExists(String name) {
        return taskDAO.taskExists(name);
    }

    public ArrayList<String> getConflictingTasks(Task newTask, List<Task> allTasks) {
        ArrayList<String> conflicts = new ArrayList<>();
        for (Task existingTask : allTasks) {
            if (isConflict(existingTask, newTask)) {
                conflicts.add(existingTask.toString());
            }
        }
        return conflicts;
    }

    private boolean isConflict(Task task1, Task task2) {
        return task1.getStartTime().compareTo(task2.getEndTime()) < 0 &&
               task1.getEndTime().compareTo(task2.getStartTime()) > 0;
    }

    public String editTask(String name, String newStart, String newEnd, String newPriority) {
        if (!taskDAO.taskExists(name)) {
            return "Task not found.";
        }

        Task updatedTask = new Task(name, newStart, newEnd, newPriority);
        List<Task> allTasks = taskDAO.getAllTasks();
        allTasks.removeIf(t -> t.getName().equalsIgnoreCase(name));  // Ignore self in conflict check

        ArrayList<String> conflicts = getConflictingTasks(updatedTask, allTasks);
        if (!conflicts.isEmpty()) {
            System.out.println("Alert: Task '" + name + "' has a time conflict!");
            for (String conflict : conflicts) {
                System.out.println(conflict);
            }
            return "Task not edited due to conflicts.";
        }

        if (newStart.compareTo(newEnd) >= 0) {
            return "End time must be after start time.";
        }

        boolean updated = taskDAO.updateTask(name, newStart, newEnd, newPriority);
        return updated ? "Task edited successfully." : "Failed to edit task.";
    }

    public String markTasksAsCompleted(ArrayList<String> taskNames) {
        for (String name : taskNames) {
            if (!taskDAO.taskExists(name)) {
                return "Task '" + name + "' not found.";
            }
            taskDAO.markTaskAsCompleted(name);
        }
        return "Tasks marked as completed.";
    }

    public String viewTasksByPriority(String priority) {
        List<Task> tasks = taskDAO.getTasksByPriority(priority);
        if (tasks.isEmpty()) {
            return "No tasks with this priority.";
        }
        StringBuilder result = new StringBuilder();
        for (Task task : tasks) {
            result.append(task.toString()).append("\n");
        }
        return result.toString();
    }

    public String viewCompletedTasks() {
        List<Task> tasks = taskDAO.getTasksByCompletion(true);
        if (tasks.isEmpty()) {
            return "No completed tasks.";
        }
        StringBuilder result = new StringBuilder();
        for (Task task : tasks) {
            result.append(task.toString()).append("\n");
        }
        return result.toString();
    }

    public String viewPendingTasks() {
        List<Task> tasks = taskDAO.getTasksByCompletion(false);
        if (tasks.isEmpty()) {
            return "No pending tasks.";
        }
        StringBuilder result = new StringBuilder();
        for (Task task : tasks) {
            result.append(task.toString()).append("\n");
        }
        return result.toString();
    }
}
