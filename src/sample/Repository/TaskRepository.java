package sample.Repository;

import sample.Task;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class TaskRepository implements Repository<Task> {
    private String url;

    public TaskRepository(File fileName) throws SQLException {
        url = "jdbc:sqlite:" + fileName.getAbsolutePath();
        Connection conn = DriverManager.getConnection(url);
    }
    @Override
    public Task add(Task item) {
        return null;
    }

    @Override
    public void add(Iterable<Task> items) {

    }

    @Override
    public Task update(Task item) {
        return null;
    }

    @Override
    public void remove(Task item) {

    }

    @Override
    public void remove(String specification) {

    }

    @Override
    public List<Task> query(String specification) {
        return null;
    }
}
