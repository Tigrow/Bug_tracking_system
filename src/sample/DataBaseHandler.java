package sample;

import java.io.File;
import java.sql.*;

class DataBaseHandler {
    private static DataBaseHandler ourInstance = new DataBaseHandler();

    static DataBaseHandler getInstance() {
        return ourInstance;
    }

    private DataBaseHandler() {
    }

    void CreateDB(File fileName) throws SQLException {
        String url = "jdbc:sqlite:" + fileName.getAbsolutePath();

        Connection conn = DriverManager.getConnection(url);
                DatabaseMetaData meta = conn.getMetaData();
                Statement statmt = conn.createStatement();
                statmt.execute("CREATE TABLE if not exists 'users' (" +
                        "'id' INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " 'name' TEXT," +
                        " 'priority' INTEGER," +
                        " 'password' TEXT" +
                        ");");
    }
    Statement LoadDB(File fileName) throws SQLException {
        String url = "jdbc:sqlite:" + fileName.getAbsolutePath();
        Connection conn = DriverManager.getConnection(url);
        return conn.createStatement();
    }
}
