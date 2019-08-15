package main;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class CreateJDBCRealmUser {
    private static final String driver = "oracle.jdbc.driver.OracleDriver";
    private static final String jdbcUrl = "jdbc:oracle:thin:@127.0.0.1:1521/XE";
    private static final String userSql = "insert into users values(?, ?)";
    private static final String groupSql = "insert into groups values(?, ?)";



    private static final char[] HEXADECIMAL = { '0', '1', '2', '3',
        '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    private static String hashPassword(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.reset();

        byte[] bytes = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder(2 * bytes.length);
        for (int i = 0; i < bytes.length; i++) {
            int low = (int)(bytes[i] & 0x0f);
            int high = (int)((bytes[i] & 0xf0) >> 4);
            sb.append(HEXADECIMAL[high]);
            sb.append(HEXADECIMAL[low]);
        }
        return sb.toString();
    }

    public static void main(String args[]) throws Exception {
        String dbUser = "dbuser";
        String dbPassword = "dbpass";
        String user = "admin";
        String password = "admin";
        String group = "admin";

        Class.forName(driver);
        String hPassword = hashPassword(password);
        Connection conn = DriverManager.getConnection(
            jdbcUrl, dbUser, dbPassword);
        PreparedStatement userStmt = conn.prepareStatement(userSql);
        userStmt.setString(1, user);
        userStmt.setString(2, hPassword);
        userStmt.executeUpdate();
        userStmt.close();

        PreparedStatement groupStmt = conn.prepareStatement(groupSql);
        groupStmt.setString(1, user);
        groupStmt.setString(2, group);
        groupStmt.executeUpdate();
        groupStmt.close();

        conn.close();
    }
}
