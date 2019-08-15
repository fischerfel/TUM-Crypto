package httpstest;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class HttpsTest {

    public static void main(String[] args) throws Exception {
        HttpsServer server = HttpsServer.create(new InetSocketAddress(8000), 0);
        server.setHttpsConfigurator(new HttpsConfigurator(createSSLContext()));
        server.createContext("/test", new TestHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    private static SSLContext createSSLContext() throws Exception {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream is = new FileInputStream("server.crt");
        InputStream caInput = new BufferedInputStream(is);
        Certificate ca = cf.generateCertificate(caInput);
        caInput.close();

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

        return context;
    }

    static class TestHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "Successfully connected!";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
