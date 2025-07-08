import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/astronaut_tasks";
    private static final String USER = "root";
    private static final String PASSWORD = "Pathu#564";

    public boolean insertTask(Task task) {
    String query = "INSERT INTO tasks (name, start_time, end_time, priority, completed) VALUES (?, ?, ?, ?, ?)";
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, task.getName());
        stmt.setTime(2, Time.valueOf(task.getStartTime()));
        stmt.setTime(3, Time.valueOf(task.getEndTime()));
        stmt.setString(4, task.getPriority());
        stmt.setBoolean(5, task.isCompleted());
        return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    public boolean taskExists(String name) {
        String query = "SELECT 1 FROM tasks WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateTask(String name, String newStart, String newEnd, String newPriority) {
        String query = "UPDATE tasks SET start_time = ?, end_time = ?, priority = ? WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setTime(1, Time.valueOf(newStart));
            stmt.setTime(2, Time.valueOf(newEnd));
            stmt.setString(3, newPriority);
            stmt.setString(4, name);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTask(String name) 
    {
    String query = "DELETE FROM tasks WHERE name = ?";
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = conn.prepareStatement(query)) 
         {
            stmt.setString(1, name);
            return stmt.executeUpdate() > 0;
        } 
    catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public void markTaskAsCompleted(String name) {
        String query = "UPDATE tasks SET completed = TRUE WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Task> getTasksByPriority(String priority) {
    List<Task> tasks = new ArrayList<>();
    String query = "SELECT * FROM tasks WHERE priority = ?";
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, priority);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Task task = new Task(
                rs.getString("name"),
                rs.getTime("start_time").toString(),
                rs.getTime("end_time").toString(),
                rs.getString("priority")
            );
            task.setCompleted(rs.getBoolean("completed"));
            tasks.add(task);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return tasks;
}

public List<Task> getTasksByCompletion(boolean completed) {
    List<Task> tasks = new ArrayList<>();
    String query = "SELECT * FROM tasks WHERE completed = ?";
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setBoolean(1, completed);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Task task = new Task(
                rs.getString("name"),
                rs.getTime("start_time").toString(),
                rs.getTime("end_time").toString(),
                rs.getString("priority")
            );
            task.setCompleted(rs.getBoolean("completed"));
            tasks.add(task);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return tasks;
}


    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM tasks";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Task task = new Task(
                    rs.getString("name"),
                    rs.getTime("start_time").toString(),
                    rs.getTime("end_time").toString(),
                    rs.getString("priority")
                );
                task.setCompleted(rs.getBoolean("completed"));
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public List<String> getConflictingTasks(Task newTask, List<Task> existingTasks) {
        List<String> conflicts = new ArrayList<>();
        for (Task existingTask : existingTasks) {
            boolean overlap = existingTask.getStartTime().isBefore(newTask.getEndTime()) &&
                              existingTask.getEndTime().isAfter(newTask.getStartTime());
            if (overlap) {
                conflicts.add(existingTask.toString());
            }
        }
        return conflicts;
    }
}
