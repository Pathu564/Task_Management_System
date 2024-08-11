// Class for alerting task conflicts.

public class TaskConflictAlert implements ScheduleObserver {
    @Override
    public void onTaskConflict(Task task) {
        System.out.printf("Alert: Task '%s' has a time conflict!\n", task.getName());
    }
}
