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
    public User add(User item) {
        String sql = "INSERT INTO users(name,password) VALUES(?,?,?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1,item.getName() );
            pstmt.setString(2,item.getPassword());
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
    public User update(User item) {
        String sql = "UPDATE users SET name = ? , "
                + "password = ?"
                + "WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, item.getName());
            pstmt.setString(2,item.getPassword());
            pstmt.setInt(3, item.getId());
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
    public List<User> query(String specification) {
        String sql = "SELECT id, name, password FROM users " + specification;
        List<User> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
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
