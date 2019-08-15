import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;

public class Test {

    static private String PROGVERSION = "V1.00";
    static private String keystoreFile = "";
    static private int listenPort = 0;

    public static void main(String[] args) throws Exception {


    System.out.println("JavaHttpsServer " + PROGVERSION);

    keystoreFile = args[0];
    listenPort = Integer.parseInt(args[1]);
    System.out.println("keystoreFile=[" + keystoreFile + "]");
    System.out.println("listenPort=[" + listenPort + "]");


    SSLContext ssl =  SSLContext.getInstance("SSLv3");

    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()); 
    KeyStore store = KeyStore.getInstance(KeyStore.getDefaultType());


    //Load the JKS file (located, in this case, at D:\keystore.jks, with password 'test'
    //store.load(new FileInputStream("C:\\Users\\Eclipse-workspaces\\Test\\keystore.jks"), "changeit".toCharArray()); 
    store.load(new FileInputStream(keystoreFile), "changeit".toCharArray()); 

    //init the key store, along with the password 'changeit'
    kmf.init(store, "changeit".toCharArray());
    KeyManager[] keyManagers = new KeyManager[1];
    keyManagers = kmf.getKeyManagers();



    // Init the trust manager factory
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

    // It will reference the same key store as the key managers
    tmf.init(store);

    TrustManager[] trustManagers = tmf.getTrustManagers();
    ssl.init(keyManagers, trustManagers, new SecureRandom());

    // Init a configuration with our SSL context
    HttpsConfigurator configurator = new HttpsConfigurator(ssl);
    //configurator.configure(hparams);

    // Create a new HTTPS Server instance, listening on port 8000
    HttpsServer server = HttpsServer.create(new InetSocketAddress(listenPort), 0);

    server.setHttpsConfigurator(configurator);

    server.createContext("/test", new MyHandler());
    server.setExecutor(null); // creates a default executor
    server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String x = t.getRemoteAddress().getHostString();
            System.out.println("In handle: Request from (getHostString) = [" + x + "]");

            x = t.getRequestURI().toASCIIString();
            System.out.println("In handle: getRequestURI = [" + x + "]");

            if (x.equalsIgnoreCase("/test?stop")) {
                System.out.println("In handle: Received request to exit, so will exit now...");
                System.exit(0);
            }

            System.out.println("In handle: About to send response...");
            String response = "This is the response";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            System.out.println("In handle: Finished sending response...");
            os.close();
        }
    }

}
