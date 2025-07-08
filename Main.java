import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ScheduleManager scheduleManager = ScheduleManager.getInstance();
        TaskFactory taskFactory = new TaskFactory();
        scheduleManager.addObserver(new TaskConflictAlert());

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
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
            System.out.print("Enter your choice (1-9): ");

            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1: {
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
                        String result = scheduleManager.addTask(task);
                        System.out.println(result);
                    }
                    break;
                }

                case 2: {
                    System.out.print("Enter task name to remove: ");
                    String name = scanner.nextLine();
                    String result = scheduleManager.removeTask(name);
                    System.out.println(result);
                    break;
                }

                case 3: {
                    System.out.println("All tasks:");
                    System.out.println(scheduleManager.viewTasks());
                    break;
                }

                case 4: {
                    System.out.print("Enter task name to edit: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter new start time (HH:mm): ");
                    String newStart = scanner.nextLine();
                    System.out.print("Enter new end time (HH:mm): ");
                    String newEnd = scanner.nextLine();
                    System.out.print("Enter new priority (High/Medium/Low): ");
                    String newPriority = scanner.nextLine();

                    String result = scheduleManager.editTask(name, newStart, newEnd, newPriority);
                    System.out.println(result);
                    break;
                }

                case 5: {
                    System.out.print("Enter task names to mark as completed (comma separated): ");
                    String[] names = scanner.nextLine().split(",");
                    ArrayList<String> taskNames = new ArrayList<>();
                    for (String name : names) {
                        taskNames.add(name.trim());
                    }

                    String result = scheduleManager.markTasksAsCompleted(taskNames);
                    System.out.println(result);
                    break;
                }

                case 6: {
                    System.out.print("Enter priority to filter (High/Medium/Low): ");
                    String priority = scanner.nextLine();
                    String result = scheduleManager.viewTasksByPriority(priority);
                    System.out.println(result);
                    break;
                }

                case 7: {
                    System.out.println("Completed tasks:");
                    System.out.println(scheduleManager.viewCompletedTasks());
                    break;
                }

                case 8: {
                    System.out.println("Pending tasks:");
                    System.out.println(scheduleManager.viewPendingTasks());
                    break;
                }

                case 9:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
                    break;
            }

        } while (choice != 9);

        scanner.close();
    }
}
