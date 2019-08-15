import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

/**
 * Simple Server SSL Hello World Example.
 *  
 */
public class SimpleSSLHelloWorld implements Container {

    public static int count = 0;

    public static String EMTPY_STRING = "";

    public static int serverPort;

    public static String KEYSTORE_PROPERTY = "javax.net.ssl.keyStore";

    public static String KEYSTORE_PASSWORD_PROPERTY = "javax.net.ssl.keyStorePassword";

    public static String KEYSTORE_TYPE_PROPERTY = "javax.net.ssl.keyStoreType";

    public static String KEYSTORE_ALIAS_PROPERTY = "javax.net.ssl.keyStoreAlias";

    public static void main(final String[] args) throws Exception {

        SimpleSSLHelloWorld.serverPort = 8080;
        // System.setProperty("javax.net.debug", "all");
        System.setProperty(SimpleSSLHelloWorld.KEYSTORE_PROPERTY, "mySrvKeystore");
        System.setProperty(SimpleSSLHelloWorld.KEYSTORE_PASSWORD_PROPERTY, "123456");

        Container container = new SimpleSSLHelloWorld();
        SocketAddress address = new InetSocketAddress(SimpleSSLHelloWorld.serverPort);

        SSLContext sslContext = SimpleSSLHelloWorld.createSSLContext();

        Connection connectionHttps = new SocketConnection(container);
        connectionHttps.connect(address, sslContext);

        System.out.println("Simple Server started on port: " + SimpleSSLHelloWorld.serverPort);
    }

    public void handle(final Request request, final Response response) {
        try {
            System.out.println("what");
            SimpleSSLHelloWorld.logRequest(request);

            SimpleSSLHelloWorld.dummyResponse(response);

            SimpleSSLHelloWorld.logResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SSLContext createSSLContext() throws Exception {

        String keyStoreFile = System.getProperty(SimpleSSLHelloWorld.KEYSTORE_PROPERTY);
        String keyStorePassword = System.getProperty(SimpleSSLHelloWorld.KEYSTORE_PASSWORD_PROPERTY,
                SimpleSSLHelloWorld.EMTPY_STRING);
        String keyStoreType = System.getProperty(SimpleSSLHelloWorld.KEYSTORE_TYPE_PROPERTY, KeyStore.getDefaultType());

        KeyStore keyStore = SimpleSSLHelloWorld.loadKeyStore(keyStoreFile, keyStorePassword, null);
        FileInputStream keyStoreFileInpuStream = null;
        try {
            if (keyStoreFile != null) {
                keyStoreFileInpuStream = new FileInputStream(keyStoreFile);

                keyStore = KeyStore.getInstance(keyStoreType);
                keyStore.load(keyStoreFileInpuStream, keyStorePassword.toCharArray());
            }
        } finally {
            if (keyStoreFileInpuStream != null) {
                keyStoreFileInpuStream.close();
            }
        }

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());

        SSLContext sslContext = SSLContext.getInstance("SSLv3");
        // sslContext.init(keyManagerFactory.getKeyManagers(), new TrustManager[]{new NaiveX509TrustManager()}, null);
        sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

        return sslContext;
    }

    public static KeyStore loadKeyStore(final String keyStoreFilePath, final String keyStorePassword,
            final String keyStoreType) throws Exception {
        KeyStore keyStore = null;
        File keyStoreFile = new File(keyStoreFilePath);

        if (keyStoreFile.isFile()) {
            keyStore = KeyStore.getInstance(keyStoreType != null ? keyStoreType : KeyStore.getDefaultType());
            keyStore.load(new FileInputStream(keyStoreFile), keyStorePassword != null ? keyStorePassword
                    .toCharArray() : SimpleSSLHelloWorld.EMTPY_STRING.toCharArray());
        }

        return keyStore;
    }

    public static void logRequest(final Request request) throws IOException {
        StringBuilder builder = new StringBuilder();

        builder.append(">>> REQUEST\n");
        builder.append(request);
        builder.append(request.getContent());

        System.out.println(builder);
    }

    public static void logResponse(final Response response) throws IOException {
        StringBuilder builder = new StringBuilder();

        builder.append("<<< RESPONSE\n");
        builder.append(response);

        if (response.getContentLength() > 0) {
            builder.append("... ").append(response.getContentLength()).append(" bytes ...\n");
        }

        System.out.println(builder);
    }

    public static void dummyResponse(final Response response) throws IOException {
        PrintStream body = response.getPrintStream();
        long time = System.currentTimeMillis();

        response.set("Content-Type", "text/plain");
        response.set("Server", "SSL HelloWorld/1.0 (Simple 4.0)");
        response.setDate("Date", time);
        response.setDate("Last-Modified", time);

        body.println("Hello World: " + ++SimpleSSLHelloWorld.count);
        body.close();
    }
}
