import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
/**
 * Created by Ohlaph on 8/29/2015.
 */
public class Conn {

    private Connection con = null;
    private String JDBC = "com.mysql.jdbc.Driver";
    private String username = "root", password = "password";
    private Statement statement;
    private ResultSet rs;
    private String user_name, user_password;
    private String dbname = "jdbc:mysql://localhost/thon";

    public Conn(String user, String password) {
        this.user_name = user;
        this.user_password = MD5(password);

    }

    public void Connect() throws Exception {
        try {

            Class.forName(JDBC);
            con = DriverManager.getConnection("jdbc:mysql://localhost/thon", username, password);
            statement = con.createStatement();

        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Failure");
        }
    }//end CONNECT()

    public boolean checkIt() throws Exception {
        String check = "select * from users";

        try {
            rs = statement.executeQuery(check);

            while (rs.next()) {
                String usr = rs.getString("nickname");
                String pwd = rs.getString("password");

                System.out.println("user name " + usr + ", Password " + pwd);

                if (user_name.equals(usr) && user_password.equals(pwd)) {
                    System.out.println("Access Granted");
                    return true;
                } else {
                    System.out.println("Access Denied");
                    return false;
                }
            }

        } catch (Exception e) {
            System.out.println("Error " + e);
        }
        con.close();
        return false;
    }//end checkIt()


    public static String MD5( String source ) {
        try {
            MessageDigest md = MessageDigest.getInstance( "MD5" );
            byte[] bytes = md.digest( source.getBytes("UTF-8") );
            return getString( bytes );
        } catch( Exception e )  {
            e.printStackTrace();
            return null;
        }
    }//end MD5()

    private static String getString( byte[] bytes ) {
        StringBuffer sb = new StringBuffer();
        for( int i=0; i<bytes.length; i++ )
        {
            byte b = bytes[ i ];
            String hex = Integer.toHexString((int) 0x00FF & b);
            if (hex.length() == 1)
            {
                sb.append("0");
            }
            sb.append( hex );
        }
        return sb.toString();
    }// end getString()

}//end Conn.java
