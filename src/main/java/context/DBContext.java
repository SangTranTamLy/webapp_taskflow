package context;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContext {
    private static final String SERVER = "127.0.0.1";
    private static final String PORT = "3306";
    private static final String DATABASE = "WebApp_db";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + SERVER + ":" + PORT + "/" + DATABASE
                    + "?useSSL=false"
                    + "&serverTimezone=Asia/Ho_Chi_Minh"
                    + "&allowPublicKeyRetrieval=true"
                    + "&useUnicode=true"
                    + "&characterEncoding=UTF-8";
            return DriverManager.getConnection(url, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Không thể kết nối CSDL: " + e.getMessage());
            return null;
        }
    }
}
