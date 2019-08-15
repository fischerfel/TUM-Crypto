package authserv;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.security.KeyStore;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsExchange;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;

public class AuthServer {
    final static String SERVER_PWD = "aaaaaa";
    final static String KST_SERVER = "keys/server.jks";
    final static String TST_SERVER = "keys/servertrust.jks";

    public static HttpsServer server; 

    public static void main(String[] args) throws Exception {
        server = makeServer();
        server.start();
        //System.out.println("Server running, hit enter to stop.\n"); System.in.read();

        AuthClient cl = new AuthClient(); 
        cl.testIt();

        server.stop(0);
    }

    public static HttpsServer makeServer() throws Exception {
        server = HttpsServer.create(new InetSocketAddress(8888), 0);

        //server.setHttpsConfigurator(new HttpsConfigurator(SSLContext.getInstance("TLS"))); // Default config with no auth requirement.
        SSLContext sslCon = createSSLContext();
        MyConfigger authconf = new MyConfigger(sslCon);
        server.setHttpsConfigurator(authconf);

        server.createContext("/auth", new HelloHandler());
        return server;
    }
    private static SSLContext createSSLContext()  {
        SSLContext sslContext = null;
        KeyStore ks;
        KeyStore ts;

        try{
            sslContext = SSLContext.getInstance("TLS");

            ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(KST_SERVER), SERVER_PWD.toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, SERVER_PWD.toCharArray());

            ts = KeyStore.getInstance("JKS");
            ts.load(new FileInputStream(TST_SERVER), SERVER_PWD.toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ts);

            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);


        } catch (Exception e) {
            e.printStackTrace();
        }       
        return sslContext;
    }
}

class MyConfigger extends HttpsConfigurator {
    public MyConfigger(SSLContext sslContext) {
        super(sslContext);  }

    @Override
    public  void configure(HttpsParameters params) {
        SSLContext sslContext = getSSLContext();
        SSLParameters  sslParams = sslContext.getDefaultSSLParameters();
        sslParams.setNeedClientAuth(true); 
        params.setNeedClientAuth(true);  
        params.setSSLParameters(sslParams);
        super.configure(params);
    /* Other configure options that don't seem to help:
        SSLEngine engine = sslContext.createSSLEngine ();
        engine.setNeedClientAuth(true);
        params.setCipherSuites ( engine.getEnabledCipherSuites () );
        params.setProtocols ( engine.getEnabledProtocols () );  
     */ 
    }
}

class HelloHandler implements HttpHandler {
    public void handle(HttpExchange t) throws IOException {
        HttpsExchange ts = (HttpsExchange) t; 
        SSLSession sess = ts.getSSLSession();
        //if( sess.getPeerPrincipal() != null) System.out.println(sess.getPeerPrincipal().toString()); // Principal never populated.

        t.getResponseHeaders().set("Content-Type", "text/plain");
        t.sendResponseHeaders(200,0);
        String response = "Hello!  You seem trustworthy!\n";
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}


class AuthClient{
    static String KEYSTORE = "";
    static String TRUSTSTORE = "keys/clienttrust.jks";
    static String CLIENT_PWD = "aaaaaa";

    public static void main(String[] args) throws Exception {
        KEYSTORE = "keys/unauthclient.jks"; // Doesn't exist in server trust store, should fail authentication.
        //KEYSTORE = "keys/authclient.jks"; // Exists in server trust store, should pass authentication.

        AuthClient cl = new AuthClient();
        cl.testIt();
    }

    public void testIt(){
        try {
            String https_url = "https://localhost:8888/auth/";
            URL url;
            url = new URL(https_url);
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
            conn.setSSLSocketFactory(getSSLFactory());

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setUseCaches(false);

            // Print response
            BufferedReader bir = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while((line = bir.readLine()) != null) {
                  System.out.println(line);
                }
            bir.close();
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static SSLSocketFactory getSSLFactory() throws Exception {
        // Create key store
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        KeyManager[] kmfs = null;
        if( KEYSTORE.length() > 0 ) {
            keyStore.load(new FileInputStream(KEYSTORE), CLIENT_PWD.toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(
                        KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, CLIENT_PWD.toCharArray());
            kmfs = kmf.getKeyManagers();
        }

        // create trust store (validates the self-signed server!)
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(new FileInputStream(TRUSTSTORE), CLIENT_PWD.toCharArray());
        TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(
                        TrustManagerFactory.getDefaultAlgorithm());
        trustFactory.init(trustStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmfs, trustFactory.getTrustManagers(), null);
        return sslContext.getSocketFactory();
    }
}
