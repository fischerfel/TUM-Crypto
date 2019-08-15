import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import java.io.FileInputStream;

import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;



public class HTTPSserver {

public static void main(String[] args) throws Exception {

    char[] password = "SSLauthentication".toCharArray();
    KeyStore ks = KeyStore.getInstance("JKS");
    FileInputStream fileInput = new FileInputStream("C:\\Files\\Certificates\\clientkeystore");

    ks.load(fileInput, password);

    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(ks, password);

    TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
    tmf.init(ks);

    SSLContext ssl = SSLContext.getInstance("TLS");
    ssl.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);



    HttpsServer server = HttpsServer.create(new InetSocketAddress(8000), 0);

    server.setHttpsConfigurator (new HttpsConfigurator(ssl)
        {
            @Override
            public void configure (HttpsParameters params)
            {
        try
                {

                    SSLContext c = getSSLContext();
                    SSLParameters sslparams = c.getDefaultSSLParameters();
                    params.setSSLParameters(sslparams);
                    System.out.println("SSL context created ...\n");
                }
                catch(Exception e2)
                {
                    System.out.println("Invalid parameter ...\n");
                    e2.printStackTrace();
                }
            }
        });



    server.createContext("/test", new MyHandler());
    server.setExecutor(null); 
    server.start();


       }

static class MyHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange HTTPmessage) throws IOException {



        if("GET".equals(HTTPmessage.getRequestMethod()))
        {
        String response = "This is aaa response";
        HTTPmessage.sendResponseHeaders(200, response.length());

        OutputStream os = HTTPmessage.getResponseBody();
        os.write(response.getBytes());
        os.close();
        }

    }



}
}
