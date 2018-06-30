package sample;

import java.io.File;
import java.sql.*;

public class DataBaseHandler {
    private static DataBaseHandler ourInstance = new DataBaseHandler();

    static DataBaseHandler getInstance() {
        return ourInstance;
    }

    private DataBaseHandler() {
    }
    public static Connection conn;
    private static Statement statmt;

    void CreateDB(File fileName) throws SQLException {
        String url = "jdbc:sqlite:" + fileName.getAbsolutePath();

        Connection conn = DriverManager.getConnection(url);
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
                statmt = conn.createStatement();
                statmt.execute("CREATE TABLE if not exists 'users' (" +
                        "'id' INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " 'name' text," +
                        " 'priority' INTEGER" +
                        ");");
    }
}
