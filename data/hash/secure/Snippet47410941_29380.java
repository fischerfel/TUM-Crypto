import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Martin
 */
public class TestApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*
        testing how to salted sha-256 like tomcat digest.sh -> credential:salt$iterations$hash
        /usr/local/apache-tomcat-8.0.26/bin/digest.sh -a SHA-256 NonGuesablePassword
NonGuesablePassword:a2eda424107c276748780bca8e7d46256321345e1fd0d0d0bcdbcee72dcc0a4a$1$8f2027b86b0b475be7d09c737995037864a4dbc44d90f24648680169b246a9c7
        */
        String pwd="NonGuesablePassword";
        String salt="a2eda424107c276748780bca8e7d46256321345e1fd0d0d0bcdbcee72dcc0a4a";
        String hash="8f2027b86b0b475be7d09c737995037864a4dbc44d90f24648680169b246a9c7";
         try {
             System.out.println(hash+" 1st iteration hash");
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");

            md.update(DatatypeConverter.parseHexBinary(salt));
            md.update(pwd.getBytes());

        System.out.println(DatatypeConverter.printHexBinary(md.digest())+ " 1st iteration") ; // encrypte sha-256 versie
        System.out.println(DatatypeConverter.printHexBinary(md.digest())+ " 2nd iteration") ; // encrypte sha-256 versie

        } catch (NoSuchAlgorithmException na) {
            System.out.println(na.getMessage());

        }

    }

}
