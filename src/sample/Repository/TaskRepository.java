package sample.Repository;

import sample.Project;
import sample.Task;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository implements Repository<Task> {
    private String url;

    public TaskRepository(File fileName) throws SQLException {
        url = "jdbc:sqlite:" + fileName.getAbsolutePath();
        Connection conn = DriverManager.getConnection(url);
    }
    @Override
    public Task add(Task item) {
        String sql = "INSERT INTO tasks(project_id,subject,type,priority,user_id,description) VALUES(?,?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, item.getProjectId());
            pstmt.setString(2,item.getSubject());
            pstmt.setString(3,item.getType());
            pstmt.setInt(4,item.getPriority());
            pstmt.setInt(5,item.getUserId());
            pstmt.setString(6,item.getDescription());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            item.setId(rs.getInt(1));
            return item;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Task update(Task item) {
        String sql = "UPDATE tasks SET project_id = ?," +
                "subject = ?," +
                "type = ?," +
                "priority = ?," +
                "user_id = ?," +
                "description = ?"
                + " WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, item.getProjectId());
            pstmt.setString(2, item.getSubject());
            pstmt.setString(3,item.getType());
            pstmt.setInt(4,item.getPriority());
            pstmt.setInt(5,item.getUserId());
            pstmt.setString(6,item.getDescription());
            pstmt.setInt(7,item.getId());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            item.setId(rs.getInt(1));
            return item;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public void remove(Task item) {
        String sql = "DELETE FROM tasks WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, item.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Task> query(String specification) {
        String sql = "SELECT id,project_id,subject,type,priority,user_id,description FROM tasks " + specification;
        List<Task> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Task item = new Task();
                item.setId(rs.getInt("id"));
                item.setProjectId(rs.getInt("project_id"));
                item.setSubject(rs.getString("subject"));
                item.setType(rs.getString("type"));
                item.setPriority(rs.getInt("priority"));
                item.setUserId(rs.getInt("user_id"));
                item.setDescription(rs.getString("description"));
                list.add(item);
            }
            return list;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
