package Controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;


@WebServlet(name = "controller", loadOnStartup=1, urlPatterns = {"/"})
public class web_controller extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String userPath=request.getServletPath();
        if ("/".equals(userPath)){
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
        else {}
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);

    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);

    }
    private static Connection getSQLConnection() {
        Connection connection = null;
        String dbUser = "root";
        String dbPwd = "root";
        String dbUrl = "jdbc:mysql://localhost:3306/test";
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
            return connection;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private static ResultSet history(Connection connection, String user) {
        ResultSet result = null;
        int userID = 0;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE login=?");
            preparedStatement.setString(1, user);
            result = preparedStatement.executeQuery();
            if (result.next()) {
                userID = result.getInt("id");
            }
            result = null;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        try{
            PreparedStatement history = connection.prepareStatement("SELECT * FROM history WHERE login=?");
            getHistory.setInt(1, userID);
            result = history.executeQuery();
            return result;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
    }

    private static int USER_NOT_FOUND = 2;
    private static int WRONG_PWD = 0;
    private static int SUCCESS = 1;

    private static int checkUser(Connection connection, String user, String passwd) {
        ResultSet result = null;
        String pwdMD5 = getMD5(passwd);
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE login=?");
            preparedStatement.setString(1, user);
            result = preparedStatement.executeQuery();
            if (result.next()) {
                String storedPwd = result.getString("passwd");
                if (storedPwd.equals(pwdMD5)) {
                    return SUCCESS;
                }
                else return WRONG_PWD;
            } else return USER_NOT_FOUND;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static void addEntry(HttpServletRequest request, Connection connection, String user, String passwd, int success) {
        int userID = 0;

        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE login=?");
            preparedStatement.setString(1, user);
            ResultSet result = preparedStatement.executeQuery();
            result.next();
            userID = result.getInt("id");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            PreparedStatement updHistory = connection.prepareStatement("INSERT INTO history (user_id, success, ipaddr) VALUES (?,?,?,?)");
            updHistory.setInt(1, userID);
            updHistory.setInt(2, success);
            updHistory.setString(3, request.getRemoteAddr());
            ResultSet result = updHistory.executeQuery();
            result = updHistory.executeQuery();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static String getMD5(String pwd) {
        String generatedMD5 = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pwd.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sbuilder = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sbuilder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedMD5 = sbuilder.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedMD5;

    }
}
