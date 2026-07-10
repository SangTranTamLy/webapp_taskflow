package dao;

import context.DBContext;
import model.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {
    public List<Task> findByUserId(int userId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT id, user_id, title, description, deadline, status, priority "
                + "FROM tasks WHERE user_id = ? ORDER BY deadline ASC, id DESC";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapTask(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tasks;
    }

    public Task findByIdAndUserId(int id, int userId) {
        String sql = "SELECT id, user_id, title, description, deadline, status, priority "
                + "FROM tasks WHERE id = ? AND user_id = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapTask(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean insert(Task task) {
        String sql = "INSERT INTO tasks (user_id, title, description, deadline, status, priority) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            fillStatement(ps, task);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean update(Task task) {
        String sql = "UPDATE tasks SET title = ?, description = ?, deadline = ?, status = ?, priority = ? "
                + "WHERE id = ? AND user_id = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getDeadline());
            ps.setString(4, task.getStatus());
            ps.setString(5, task.getPriority());
            ps.setInt(6, task.getId());
            ps.setInt(7, task.getUserId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean delete(int id, int userId) {
        String sql = "DELETE FROM tasks WHERE id = ? AND user_id = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private void fillStatement(PreparedStatement ps, Task task) throws Exception {
        ps.setInt(1, task.getUserId());
        ps.setString(2, task.getTitle());
        ps.setString(3, task.getDescription());
        ps.setString(4, task.getDeadline());
        ps.setString(5, task.getStatus());
        ps.setString(6, task.getPriority());
    }

    private Task mapTask(ResultSet rs) throws Exception {
        return new Task(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("deadline"),
                rs.getString("status"),
                rs.getString("priority")
        );
    }
}
