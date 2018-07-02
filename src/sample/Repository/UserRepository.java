package sample.Repository;

import sample.User;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements Repository<User> {
    private String url;

    public UserRepository(File fileName) throws SQLException {
        url = "jdbc:sqlite:" + fileName.getAbsolutePath();
        Connection conn = DriverManager.getConnection(url);
    }

    @Override
    public void add(User user) {
        String sql = "INSERT INTO users(name,priority,password) VALUES(?,?,?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1,user.getName() );
            pstmt.setInt(2, user.getPriority());
            pstmt.setString(3,user.getPassword());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void add(Iterable<User> items) {

    }

    @Override
    public void update(User item) {

    }

    @Override
    public void remove(User item) {
        String sql = "DELETE FROM users WHERE id = ?";

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
    public List<User> query(String specification) {
        String sql = "SELECT id, name, priority, password FROM users " + specification;
        List<User> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setPriority(rs.getInt("priority"));
                user.setPassword(rs.getString("password"));
                list.add(user);
            }
            return list;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
