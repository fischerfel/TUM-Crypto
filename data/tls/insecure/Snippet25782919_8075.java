import android.content.Context;
import android.os.Handler;

import org.java_websocket.client.DefaultSSLWebSocketClientFactory;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.client.WebSocketClient.WebSocketClientFactory;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class WebSocket {

    private static final String WEB_SOCKET_URI = "https://XX.XX.XX.XX:8888/echo/websocket";
    private static final int DELAY_MILLIS = 10;
    private static final int ERROR_DELAY_MILLIS = 1000;

    //SINGLETON
    private static WebSocket sWebSocket;

    //BASE
    private Context mContext;

    //VALUE's
    private URI mURI;
    private WebSocketClientFactory mClientFactory;
    private Handler mHandler;
    private Runnable mRunnable;

    private WebSocketClient mClient;

    private List<String> mMessagesList;
    private WebSocketMessages mMessages;
    private WebSocketSecretary mSecretary;

    private boolean mNetworkAvailable;
    private boolean mSocketWork;

    public static WebSocket getInstance(Context context) {
        if (sWebSocket == null) {
            synchronized (WebSocket.class) {
                if (sWebSocket == null) {
                    sWebSocket = new WebSocket(context);
                }
            }
        }
        return sWebSocket;
    }

    private WebSocket(Context context) {
        mContext = context.getApplicationContext();

        try {
            mURI = new URI(WEB_SOCKET_URI);

        } catch (URISyntaxException e) {
            Utils.toLog(e);
            throw new IllegalStateException(e.getMessage());
        }

        try {
            final KeyStore keyStore = getKeyStore(mContext);
            final KeyManager[] keyManagers = getKeyManagers(keyStore);
            final TrustManager[] trustManagers = getTrustManagers(keyStore);

            mClientFactory = getWebSocketClientFactory(keyManagers, trustManagers);

        } catch (Exception e) {
            Utils.toLog(e);
        }

        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                initWebSocketClient();
            }
        };

        mMessagesList = new ArrayList<>();
        mMessages = WebSocketMessages.getInstance(mContext);
        mSecretary = new WebSocketSecretary(mContext);
    }

    public void startSocket(boolean networkAvailable) {
        mSocketWork = true;
        setNetworkAvailable(networkAvailable);
    }

    public void setNetworkAvailable(boolean networkAvailable) {
        mNetworkAvailable = networkAvailable;

        if (mNetworkAvailable) {
            initWebSocketClient();
        }
    }

    public void sendMessage(MessageType type) {
        sendMessage(mMessages.getMessage(type));
    }

    public void sendMessage(String bytes) {
        if (isOpen()) {
            mClient.send(bytes);

        } else {
            mMessagesList.add(bytes);
        }
    }

    public boolean isOpen() {
        return mSocketWork && mNetworkAvailable &&
    }

    public void stopSocket() {
        mSocketWork = false;
        mMessagesList.clear();

        if (mClient != null) {
            mClient.close();
            mClient = null;
        }
    }

    private void initWebSocketClient() {
        if (mClient != null) {
            return;
        }

        mClient = getWebSocketClient();
        mClient.connect();
    }

    private WebSocketClient getWebSocketClient() {
        final WebSocketClient client = getWebSocketClient(mURI);
        client.setWebSocketFactory(mClientFactory);

        return client;
    }

    private WebSocketClient getWebSocketClient(URI uri) {
        return new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                if (MApp.DEBUG) {
                    Utils.toLog("WebSocketClient\tonOpen");
                }

                send(mMessages.getByteMessage(MessageType.LOGIN_USER));

                if (!mMessagesList.isEmpty()) {
                    final Iterator<String> iterator = mMessagesList.iterator();
                    while (iterator.hasNext()) {
                        send(iterator.next());
                        iterator.remove();
                    }
                }
            }

            @Override
            public void onMessage(String s) {
                mSecretary.onMessage(s);
            }

            @Override
            public void onError(Exception e) {
                if (MApp.DEBUG) {
                    Utils.toLog("WebSocketClient\tonError");
                    Utils.toLog(e);
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                if (mClient != null) {
                    mClient.close();
                }
                mClient = null;

                if (mSocketWork && mNetworkAvailable) {
                    mHandler.postDelayed(mRunnable, code == -1 ? ERROR_DELAY_MILLIS : DELAY_MILLIS);
                }

                if (MApp.DEBUG) {
                    Utils.toLog("WebSocketClient\tonClose\tcode = " + code + "\treason = " + reason);
                }
            }
        };
    }

    public static KeyStore getKeyStore(Context context) throws Exception {
        final KeyStore keyStore = KeyStore.getInstance("BKS");
        keyStore.load(context.getResources().openRawResource(R.raw.websocketcertificate),
                "password".toCharArray());

        return keyStore;
    }

    public static KeyManager[] getKeyManagers(KeyStore keyStore) throws Exception {
        final KeyManagerFactory keyFactory = KeyManagerFactory.
                getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyFactory.init(keyStore, null);

        return keyFactory.getKeyManagers();
    }

    public static TrustManager[] getTrustManagers(KeyStore keyStore) throws Exception {
        final TrustManagerFactory trustFactory = TrustManagerFactory.
                getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustFactory.init(keyStore);

        return trustFactory.getTrustManagers();
    }

    private static WebSocketClientFactory getWebSocketClientFactory(KeyManager[] keyManagers,
                                                                    TrustManager[] trustManagers)
            throws Exception {
        final SSLContext sslContext = SSLContext.getInstance("TLS", "HarmonyJSSE");
        sslContext.init(keyManagers, trustManagers, new SecureRandom());

        return new DefaultSSLWebSocketClientFactory(sslContext);
    }
}
