package testing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.ws.Endpoint;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.sun.net.httpserver.*;

@WebService
public class test {

    static BoneCP connectionPool = null;
    static Connection con = null;


    @WebMethod
    public String login(@WebParam(name="username")String username,@WebParam(name="password") String password) throws SQLException {

        con = connectionPool.getConnection();
        Statement stmt = null;
        String query = " CALL authorize_user('" + username + "','" + password + "')";

        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String login = rs.getString("au_result");

                if (login != null){
                    con.close();
                    return login;
                }
                else {
                    con.close();
                    return "Login Failed";
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        con.close();
        return "Login Failed";
    }


    public static void main(String[] args) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException, KeyManagementException, NoSuchProviderException {

        try{
            Class.forName("com.mysql.jdbc.Driver");

        }catch(Exception e){
            e.printStackTrace();
            return;
        }

        try{
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl("jdbc:mysql://localhost:" + port + "/test");
            config.setUsername(username);
            config.setPassword(password);
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(1);
            connectionPool = new BoneCP(config);

            if(con != null){
                System.out.println("Connection successful");
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        test test = new test();

        Endpoint endpoint = Endpoint.create(test);
        String uri = "/testing";
        String keystoreFile = "keystore.jks";
        String keyPass = "test_pass";
        int port = 8080;

        SSLContext ssl = SSLContext.getInstance("TLS");

        KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        KeyStore store = KeyStore.getInstance("JKS");

        store.load(new FileInputStream(keystoreFile),keyPass.toCharArray());

        keyFactory.init(store, keyPass.toCharArray());

        TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

        trustFactory.init(store);

        ssl.init(keyFactory.getKeyManagers(),
        trustFactory.getTrustManagers(), new SecureRandom());

        HttpsConfigurator configurator = new HttpsConfigurator(ssl);

        HttpsServer httpsServer = HttpsServer.create(new InetSocketAddress(port), 50);
        System.out.println("https server: " + httpsServer.getAddress());

        httpsServer.setHttpsConfigurator(configurator);

        com.sun.net.httpserver.HttpContext httpContext = httpsServer.createContext(uri);

        httpsServer.start();

        endpoint.publish(httpContext);
    }
}
