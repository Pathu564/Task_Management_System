import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;


 //Singleton class for managing astronaut schedules.
 
public class ScheduleManager {
    private static ScheduleManager instance;
    private List<Task> tasks;
    private List<ScheduleObserver> observers;

    private ScheduleManager() {
        tasks = new ArrayList<>();
        observers = new ArrayList<>();
    }
 //Get the singleton instance of the ScheduleManager.
    
    public static  ScheduleManager getInstance() {
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

    /**
     * Add a new task to the schedule.
     *
     * @param task The task to add.
     * @return The result of adding the task.
     */
    public String addTask(Task task) {
        // Check for conflicts
        ArrayList<String> conflicts = getConflictingTasks(task);
        if (conflicts.size() > 0) {
            notifyObservers(task);
            // Do not add the task if conflicts are found
            CustomLogger.log(Level.WARNING, "Task time conflict detected for task: " + task.getName());
            CustomLogger.log(Level.WARNING, "Conflicting tasks:");
            for (String conflict : conflicts) {
                System.out.println(conflict);
            }
            return "Task not added due to conflicts.";
        }

        // Check if task name already exists
        for (Task existingTask : tasks) {
            if (existingTask.getName().equalsIgnoreCase(task.getName())) {
                System.out.println("Task with this name already exists.");
                return "Task not added due to name conflict.";
            }
        }

        // Add the task
        tasks.add(task);
        return "Task added successfully.";
    }

    /**
     * Remove a task from the schedule by name.
     *
     * @param name The name of the task to remove.
     * @return The result of removing the task.
     */
    public String removeTask(String name) {
        for (Task task : tasks) {
            if (task.getName().equalsIgnoreCase(name)) {
                tasks.remove(task);
                return "Task removed successfully.";
            }
        }
        return "Task not found.";
    }

    /**
     * View all tasks sorted by start time.
     *
     * @return The sorted tasks as a string.
     */
    public String viewTasks() {
        Collections.sort(tasks, Comparator.comparing(Task::getStartTime));
        return tasks.toString();
    }

    public boolean taskExists(String name) {
        for (Task task : tasks) {
            if (task.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
    
    public ArrayList<String> getConflictingTasks(Task newTask) {
        ArrayList<String> conflicts = new ArrayList<>();
        for (Task existingTask : tasks) {
            if (isConflict(existingTask, newTask)) {
                conflicts.add(existingTask.toString()); // Assuming Task class has a meaningful toString() method
            }
        }
        return conflicts;
    }
    
    private boolean isConflict(Task task1, Task task2) {
        // Example conflict check logic (you might need to adjust based on your requirements)
        return task1.getStartTime().compareTo(task2.getEndTime()) < 0 &&
               task1.getEndTime().compareTo(task2.getStartTime()) > 0;
    }

    /**
     * Edit an existing task's details.
     *
     * @param name        The name of the task to edit.
     * @param newStart    The new start time.
     * @param newEnd      The new end time.
     * @param newPriority The new priority level.
     * @return The result of editing the task.
     */
    public String editTask(String name, String newStart, String newEnd, String newPriority) {
        for (Task task : tasks) {
            if (task.getName().equalsIgnoreCase(name)) {
                // Check for conflicts with other tasks
                Task updatedTask = new Task(name, newStart, newEnd, newPriority);
                ArrayList<String> conflicts = getConflictingTasks(updatedTask);
                if (conflicts.size() > 0) {
                    System.out.println("Alert: Task '" + name + "' has a time conflict!");
                    System.out.println("Conflicting tasks:");
                    for (String conflict : conflicts) {
                        System.out.println(conflict);
                    }
                    return "Task not edited due to conflicts.";
                }
    
                // Check if end time is after start time
                if (newStart.compareTo(newEnd) >= 0) {
                    return "End time must be after start time.";
                }
    
                // Update the task details
                task.setStartTime(newStart);
                task.setEndTime(newEnd);
                task.setPriority(newPriority);
                return "Task edited successfully.";
            }
        }
        return "Task not found.";
    }

    /**
     * Mark tasks as completed by their names.
     *
     * @param taskNames The list of task names to mark as completed.
     * @return The result of marking tasks as completed.
     */
    public String markTasksAsCompleted(ArrayList<String> taskNames) {
        for (String name : taskNames) {
            boolean taskFound = false;
            for (Task task : tasks) {
                if (task.getName().equalsIgnoreCase(name)) {
                    task.setCompleted(true);
                    taskFound = true;
                    break;
                }
            }
            if (!taskFound) {
                return "Task '" + name + "' not found.";
            }
        }
        return "Tasks marked as completed.";
    }

    /**
     * View tasks filtered by priority level.
     *
     * @param priority The priority level to filter by.
     * @return The tasks with the specified priority as a string.
     */
    public String viewTasksByPriority(String priority) {
        StringBuilder result = new StringBuilder();
        for (Task task : tasks) {
            if (task.getPriority().equalsIgnoreCase(priority)) {
                result.append(task.toString()).append("\n");
            }
        }
        return result.length() > 0 ? result.toString() : "No tasks with this priority.";
    }

    /**
     * View completed tasks.
     *
     * @return The completed tasks as a string.
     */
    public String viewCompletedTasks() {
        StringBuilder result = new StringBuilder();
        for (Task task : tasks) {
            if (task.isCompleted()) {
                result.append(task.toString()).append("\n");
            }
        }
        return result.length() > 0 ? result.toString() : "No completed tasks.";
    }

    /**
     * View pending tasks.
     *
     * @return The pending tasks as a string.
     */
    public String viewPendingTasks() {
        StringBuilder result = new StringBuilder();
        for (Task task : tasks) {
            if (!task.isCompleted()) {
                result.append(task.toString()).append("\n");
            }
        }
        return result.length() > 0 ? result.toString() : "No pending tasks.";
    }
}
