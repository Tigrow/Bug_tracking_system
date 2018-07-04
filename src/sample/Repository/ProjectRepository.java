package sample.Repository;

import sample.Project;
import sample.User;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectRepository implements Repository<Project>{

    private String url;

    public ProjectRepository(File fileName) throws SQLException {
        url = "jdbc:sqlite:" + fileName.getAbsolutePath();
        Connection conn = DriverManager.getConnection(url);
    }
    @Override
    public void add(Project item) {
        String sql = "INSERT INTO projects(name) VALUES(?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1,item.getName() );
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void add(Iterable<Project> items) {

    }

    @Override
    public void update(Project item) {
        String sql = "UPDATE projects SET name = ?"
                + "WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void remove(Project item) {
        String sql = "DELETE FROM projects WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, item.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void remove(String specification) {

    }

    @Override
    public List<Project> query(String specification) {
        String sql = "SELECT id, name FROM projects " + specification;
        List<Project> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt  = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                Project user = new Project();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                list.add(user);
            }
            return list;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}