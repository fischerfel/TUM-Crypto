package com.lge.racss.test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.websocket.ClientEndpoint;
import javax.websocket.ClientEndpointConfig;
import javax.websocket.ClientEndpointConfig.Configurator;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

@ClientEndpoint
public class WebSocketClient implements Runnable {

    protected WebSocketContainer container;
    protected Session userSession = null;

    static{
        try {
            getSelfSignSSLContext();
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public WebSocketClient() {
        container = ContainerProvider.getWebSocketContainer();
    }

    public void connectServer(String sServer)
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

        try {

            final String encodedAuthToken = Base64.getEncoder().encodeToString("tomcat:tomcat".getBytes());
            final List<String> values = new ArrayList<>(1);
            values.add("Basic " + encodedAuthToken);

            Configurator clientEndConfigurator = new Configurator() {
                @Override
                public void beforeRequest(Map<String, List<String>> headers) {
                    // insert basic authentication base64 encoding
                    headers.put("Authorization", values);
                }
            };

            ClientEndpointConfig clientEndpointConfig = ClientEndpointConfig.Builder.create()
                    .configurator(clientEndConfigurator).build();
            // For SSL
            //clientEndpointConfig.getUserProperties().put("org.apache.tomcat.websocket.SSL_CONTEXT",   getSelfSignSSLContext());

            userSession = container.connectToServer(new Endpoint() {

                @Override
                public void onOpen(Session sess, EndpointConfig config) {
                    // TODO Auto-generated method stub
                    sess.addMessageHandler(new MessageHandler.Whole<String>() {
                        @Override
                        public void onMessage(String message) {
                            System.out.println("Receive From Server: " + message);
                        }
                    });

                    System.out.println("connected.");
                }
            }, clientEndpointConfig, new URI(sServer));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (DeploymentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private static SSLContext getSelfSignSSLContext()
            throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        System.out.println("get selfsignsslcontext");
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");

        sc.init(new javax.net.ssl.X509KeyManager[] {}, new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }
        } }, new java.security.SecureRandom());

        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        });
        System.out.println("sc");
        return sc;

    }

    public void sendMessage(String sMsg) throws IOException {
        userSession.getBasicRemote().sendText(sMsg);
    }

    public void disconnect() throws IOException {

        if (userSession == null) {
            System.out.println("userSession is a null object!!");
            return;
        }
        userSession.close();
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub

        connectUsingJavaWebSocket();

    }

    private void connectUsingJavaWebSocket() {

        WebSocketClient client = new WebSocketClient();

        String uri = "wss://10.177.172.153:8445/test";

        try {
            client.connectServer(uri);
            client.sendMessage("Hello. This message is from java client!!!");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub

        ExecutorService service = Executors.newFixedThreadPool(1);
        service.submit(new WebSocketClient());

        System.out.println("Thread submitted to fixed pool.");

    }

}  
