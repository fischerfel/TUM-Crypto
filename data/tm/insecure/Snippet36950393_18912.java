import java.io.IOException;
import java.net.URI;
import java.security.KeyStore;
import java.security.cert.*;
import java.util.concurrent.*;

import javax.net.ssl.*;
import javax.websocket.*;

import org.glassfish.tyrus.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class WsClient {

    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private static final Logger log = LoggerFactory.getLogger(WsClient.class);

    public static void main(String[] args) {

        System.getProperties().put("javax.net.debug", "ssl,handshake,data,sslctx");
        try {
            new WsClient().echoDemo();
            log.info("done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // localhost does not work via squid proxy
    String destUri = "wss://127.0.0.1:8443/wstest/ws/echoAsyncAnnotation";
    String proxyUri = "http://localhost:3128";
    volatile Session wsSession = null;

    void echoDemo() throws Exception {

        final ClientEndpointConfig cec = ClientEndpointConfig.Builder.create().build();
        ClientManager client = ClientManager.createClient();
        client.getProperties().put(ClientProperties.PROXY_URI, proxyUri);
        client.getProperties().put(ClientProperties.SHARED_CONTAINER, false);

        SSLContext sslCtx = createAllTrustingContext();
        SslEngineConfigurator sslEngineConfigurator = new SslEngineConfigurator(sslCtx, true, false, false);
        sslEngineConfigurator.setHostVerificationEnabled(false); //skip host verification
        client.getProperties().put(ClientProperties.SSL_ENGINE_CONFIGURATOR, sslEngineConfigurator);
        // time-out is set to 30 seconds for all operations, so handshake timeout does not work ...
        // see org.glassfish.tyrus.container.grizzly.client.GrizzlyClientContainer.CLIENT_SOCKET_TIMEOUT
        client.getProperties().put(ClientProperties.HANDSHAKE_TIMEOUT, 3000);
        final CountDownLatch messageLatch = new CountDownLatch(1);
        client.connectToServer(new Endpoint() {

            @Override
            public void onOpen(final Session session, EndpointConfig config) {
                try {
                    session.addMessageHandler(new MessageHandler.Whole<String>() {

                        @Override
                        public void onMessage(String message) {
                            log.info("Received message: {}", message);
                            wsSession = session;
                            messageLatch.countDown();
                        }
                    });
                    session.getBasicRemote().sendText("Hello world");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, cec, new URI(destUri));
        try {
            messageLatch.await(5, TimeUnit.SECONDS);
            if (wsSession != null) {
                wsSession.close();
            }
        } finally {
            client.shutdown();
        }
    }

    public static SSLContext createAllTrustingContext() throws Exception {

        SSLContext ctx = SSLContext.getInstance("TLSv1.2");
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init((KeyStore) null, "changeit".toCharArray());
        ctx.init(kmf.getKeyManagers(), new TrustManager[] { new TrustServerCertAlways() }, null);
        return ctx;
    }

    static class TrustServerCertAlways implements X509TrustManager {

        @Override public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            log.debug("Trusting all client certificates.");
        }

        @Override public void checkServerTrusted(X509Certificate[] arg0, String arg1)   throws CertificateException {
            log.debug("Trusting all server certificates.");
        }

        @Override public X509Certificate[] getAcceptedIssuers() {
            log.debug("No accepted issuers.");
            return null;
        }
    }
}
