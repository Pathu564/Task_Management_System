// Interface for schedule observers to get notified of task conflicts.

public interface ScheduleObserver {
    void onTaskConflict(Task task);
}
