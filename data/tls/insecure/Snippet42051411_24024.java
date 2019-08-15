package com.lge.racss.test;

import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.EncodeException;
import javax.websocket.WebSocketContainer;

import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;

public class Application2 extends WebSocketAdapter {

    static {
        System.out.println("push");
        //disableSslVerification();
    }

    private static void disableSslVerification() {
        try
        {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        final WebSocketContainer container = ContainerProvider.getWebSocketContainer();

        String url = "wss://10.177.170.140:8443/signaling"; // or
        // "wss://echo.websocket.org"
        final ClientManager client = ClientManager.createClient();

        System.getProperties().put(SSLContextConfigurator.KEY_STORE_FILE, "/key/server");
        System.getProperties().put(SSLContextConfigurator.TRUST_STORE_FILE, "/key/server");
        System.getProperties().put(SSLContextConfigurator.KEY_STORE_PASSWORD, "123456");
        System.getProperties().put(SSLContextConfigurator.TRUST_STORE_PASSWORD, "123456");

        System.out.println("propery : " + System.getProperty(SSLContextConfigurator.KEY_STORE_FILE));

        final SSLContextConfigurator defaultConfig = new SSLContextConfigurator();

        defaultConfig.retrieve(System.getProperties());
        // or setup SSLContextConfigurator using its API.

        SSLEngineConfigurator sslEngineConfigurator = new SSLEngineConfigurator(defaultConfig, true, false, false);

        client.getProperties().put(ClientProperties.SSL_ENGINE_CONFIGURATOR, sslEngineConfigurator);
        System.out.println("put properties");
        try (javax.websocket.Session session = client.connectToServer(WebsocketClientEndpoint.class, URI.create(url))) {
            for (int i = 1; i <= 10; ++i) {
                try {
                    System.out.println("send");
                    session.getBasicRemote().sendObject("init");
                    Thread.sleep(1000);
                } catch (EncodeException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DeploymentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Application doesn't exit if container's threads are still running
        // ( ( ClientContainer )container ).stop();
    }

 }
