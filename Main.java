import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        // Initialize the necessary components
        ScheduleManager scheduleManager = ScheduleManager.getInstance();
        TaskFactory taskFactory = new TaskFactory();
        scheduleManager.addObserver(new TaskConflictAlert());

        // Start the application by calling the choice method
        choice(scheduleManager, taskFactory);
    }

    
    private static void choice(ScheduleManager scheduleManager, TaskFactory taskFactory) {
        System.out.println("\nAstronaut Task Management System");
        System.out.println("1. Add a new task");
        System.out.println("2. Remove an existing task");
        System.out.println("3. View all tasks sorted by start time");
        System.out.println("4. Edit an existing task");
        System.out.println("5. Mark tasks as completed");
        System.out.println("6. View tasks by priority level");
        System.out.println("7. View completed tasks");
        System.out.println("8. View pending tasks");
        System.out.println("9. Exit");

        int choice = getUserChoice(); // Get user choice

        switch (choice) {
            case 1:
                addTask(scheduleManager, taskFactory);
                break;
            case 2:
                removeTask(scheduleManager);
                break;
            case 3:
                viewTasks(scheduleManager);
                break;
            case 4:
                editTask(scheduleManager);
                break;
            case 5:
                markTasksAsCompleted(scheduleManager);
                break;
            case 6:
                viewTasksByPriority(scheduleManager);
                break;
            case 7:
                viewCompletedTasks(scheduleManager);
                break;
            case 8:
                viewPendingTasks(scheduleManager);
                break;
            case 9:
                exitProgram();
                return;  // End the recursive loop for exiting the program
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }

        // Recursively call the choice method to prompt user input again
        choice(scheduleManager, taskFactory);
    }

   
    private static int getUserChoice() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your choice (1-9): ");
        return scanner.nextInt();
        
    }

   
    private static void addTask(ScheduleManager scheduleManager, TaskFactory taskFactory) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter task name: ");
        String name = scanner.nextLine();
        System.out.print("Enter start time (HH:mm): ");
        String start = scanner.nextLine();
        System.out.print("Enter end time (HH:mm): ");
        String end = scanner.nextLine();
        System.out.print("Enter priority (High/Medium/Low): ");
        String priority = scanner.nextLine();
    
        Task task = taskFactory.createTask(name, start, end, priority);
        if (task != null) {
            // Check if the task name already exists
            if (scheduleManager.taskExists(name)) {
                System.out.println("Task with this name already exists.");
                // Optionally, display the conflicting tasks by name
                System.out.println("Conflicting tasks:");
                ArrayList<String> conflicts = scheduleManager.getConflictingTasks(task);
                for (String conflict : conflicts) {
                    System.out.println(conflict);
                }
            } else {
                // Add the task and check for time conflicts
                String result = scheduleManager.addTask(task);
                if (result.startsWith("Task added")) {
                    System.out.println(result);
                } else {
                    // Display conflict information
                    System.out.println(result);
                    System.out.println("Conflicting tasks:");
                    ArrayList<String> conflicts = scheduleManager.getConflictingTasks(task);
                    for (String conflict : conflicts) {
                        System.out.println(conflict);
                    }
                }
            }
        }
    }

   
    private static void removeTask(ScheduleManager scheduleManager) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter task name to remove: ");
        String name = scanner.nextLine();

        String result = scheduleManager.removeTask(name);
        System.out.println(result);
    }

    
    private static void viewTasks(ScheduleManager scheduleManager) {
        System.out.println("Tasks :");
        System.out.println(scheduleManager.viewTasks());
    }

    
    private static void editTask(ScheduleManager scheduleManager) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter task name to edit: ");
        String name = scanner.nextLine();
        if (!scheduleManager.taskExists(name)) {
            System.out.println("Task not found. Please try again.");
            return; // Return to the main menu
        }
        System.out.print("Enter new start time (HH:mm): ");
        String newStart = scanner.nextLine();
        System.out.print("Enter new end time (HH:mm): ");
        String newEnd = scanner.nextLine();
        System.out.print("Enter new priority (High/Medium/Low): ");
        String newPriority = scanner.nextLine();

        String result = scheduleManager.editTask(name, newStart, newEnd, newPriority);
        System.out.println(result);
    }

   
    private static void markTasksAsCompleted(ScheduleManager scheduleManager) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of tasks to mark as completed: ");
        int numTasks = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        ArrayList<String> taskNames = new ArrayList<>();
        for (int i = 0; i < numTasks; i++) {
            System.out.print("Enter task name: ");
            taskNames.add(scanner.nextLine());
        }

        String result = scheduleManager.markTasksAsCompleted(taskNames);
        System.out.println(result);
    }

    
    private static void viewTasksByPriority(ScheduleManager scheduleManager) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter priority level to view (High/Medium/Low): ");
        String priority = scanner.nextLine();

        System.out.printf("Tasks with %s priority:\n", priority);
        System.out.println(scheduleManager.viewTasksByPriority(priority));
    }

   
    private static void viewCompletedTasks(ScheduleManager scheduleManager) {
        System.out.println("Completed Tasks:");
        System.out.println(scheduleManager.viewCompletedTasks());
    }

   
    private static void viewPendingTasks(ScheduleManager scheduleManager) {
        System.out.println("Pending Tasks:");
        System.out.println(scheduleManager.viewPendingTasks());
    }

   
    private static void exitProgram() {
        System.out.println("Exiting the program. Goodbye!");
        System.exit(0);
    }
}
