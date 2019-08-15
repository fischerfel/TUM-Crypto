import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
public class WebSocketService extends Service {

public static final String WEB_SOCKET_SERVICE_RECEIVER = "com.mydomain.myapp.WebSocketServiceReceiver";

    public static final String EXTRA_COMMAND = "extraCommand";
    public static final String EXTRA_MESSAGE = "extraMessage";

    public static final int COMMAND_STOP = -2;
    public static final int COMMAND_SEND_MESSAGE = -3;

    public static void stopService(Context context, String message) {
        Intent intent = new Intent(WEB_SOCKET_SERVICE_RECEIVER);
        intent.putExtra(EXTRA_COMMAND, COMMAND_STOP);
        intent.putExtra(EXTRA_MESSAGE, message);

        context.sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("WebSocketService", "onStartCommand");
        if (!mStarted) {
            mStarted = true;
            mWebSocketClosed = false;

            mWakeLock = ((PowerManager) getSystemService(POWER_SERVICE))
                    .newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                            "WebSocketServiceWakeLock");

            pingPackets = new Stack<PingPacket>();

            registerReceiver(mReceiver, new IntentFilter(
                    WEB_SOCKET_SERVICE_RECEIVER));
            try {
                startWebSocket();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("WebSocketService", "onDestroy");

        if (mFallbackHandler != null) {
            mFallbackHandler.removeCallbacksAndMessages(null);
            mFallbackHandler = null;
        }

        if (mListenForConnectivity) {
            mListenForConnectivity = false;
            try {
                unregisterReceiver(mProviderChangedListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mStarted = false;
        try {
            unregisterReceiver(mReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mWebSocketClient != null && mWebSocketClient.isOpen()) {
            stopWebSocket();
        }

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                .cancel(AppConstants.CONNECTION_ERROR_NOTIFICATION_ID);

        if (mWakeLock.isHeld()) {
            mWakeLock.release();
            Log.d("WebSocketService", "WakeLock released");
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        if (mFallbackHandler != null) {
            mFallbackHandler.removeCallbacksAndMessages(null);
            mFallbackHandler = null;
        }

        if (mListenForConnectivity) {
            mListenForConnectivity = false;
            try {
                unregisterReceiver(mProviderChangedListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mStarted = false;
        try {
            unregisterReceiver(mReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mWebSocketClient != null && mWebSocketClient.isOpen()) {
            stopWebSocket();
        }

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                .cancel(AppConstants.CONNECTION_ERROR_NOTIFICATION_ID);

        if (mWakeLock.isHeld()) {
            mWakeLock.release();
            Log.d("WebSocketService", "WakeLock released");
        }
    }

    protected void startWebSocket() throws URISyntaxException {

        Log.d("WebSocketServce", "startWebSocket");

        String webSocketBaseURL = BuildSpecificConstants.WEB_SOCKET_BASE_URL;

        mWebSocketClient = new WebSocketClient(new URI(webSocketBaseURL
                + "?auth_token="
                + SessionManager.getInstance(getApplicationContext())
                        .getAPIKey())) {

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.d("WebSocketServce", "onOpen");

                if (!mWakeLock.isHeld()) {
                    mWakeLock.acquire();
                    Log.d("WebSocketService", "WakeLock acquired");
                }

                startPingProtocol();

                if (mPendingMessage != null) {
                    sendMessage(mPendingMessage);
                }

                Editor editor = getSharedPreferences(
                        SharedPrefKeys.APP_PREFERENCES, MODE_PRIVATE).edit();
                editor.putBoolean(SharedPrefKeys.WEB_SOCKET_ERROR, false);
                editor.commit();

                Intent intent = new Intent(
                        BaseActivity.BROADCAST_RECEIVER_INTENT_FILTER);
                intent.putExtra(BaseActivity.BROADCAST_EXTRA_TYPE,
                        BaseActivity.BROADCAST_TYPE_WEB_SOCKET_CONNECTED);
                sendBroadcast(intent);

                ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                        .cancel(AppConstants.CONNECTION_ERROR_NOTIFICATION_ID);
            }

            @Override
            public void onWebsocketPong(WebSocket conn, Framedata frame) {
                super.onWebsocketPong(conn, frame);

                try {
                    if (frame.getOpcode() == Opcode.PONG) {
                        if (!pingPackets.isEmpty()) {
                            PingPacket returnedPing = pingPackets.get(0);
                            if (returnedPing != null) {
                                returnedPing.consume();
                                pingPackets.remove(0);
                                Log.d("WebSocketService", returnedPing.id
                                        + " pong received");
                                failedPingCount = 0;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessage(String message) {
                try {
                    JSONObject eventJson = new JSONObject(message);

                    Log.d("WebSocketService", "Received: " + message);

                    String eventType = eventJson
                            .getString(IncomingMessageParams.EVENT);
                    if (eventType.equals(EventTypes.NEW_BOOKING)) {
                        Trip trip = Trip
                                .decodeJSON(
                                        eventJson
                                                .getJSONObject(BookingsResponseParams.KEY_BOOKING),
                                        eventJson.getJSONObject("passenger"),
                                        eventJson.getJSONArray("locations"),
                                        eventJson.optJSONObject("coupon"),
                                        SessionManager
                                                .getInstance(WebSocketService.this));
                        Intent intent = new Intent(WebSocketService.this,
                                IncomingBookingActivity.class);
                        intent.putExtra(IncomingBookingActivity.EXTRA_BOOKING,
                                trip);
                        intent.putExtra(
                                MainActivity.EXTRA_COUNT_DOWN_TIME,
                                eventJson
                                        .getInt(IncomingMessageParams.LOOKUP_TIME));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);

                        SessionManager.getInstance(getApplicationContext())
                                .setStatus(DriverStatus.BUSY);
                        LocationUpdateService
                                .sendStatusChange(WebSocketService.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
                Log.d("WebSocketService", "Socket closed: " + ex.getMessage());
                if (!Helper.checkNetworkStatus(WebSocketService.this)) {
                    mListenForConnectivity = true;
                    registerReceiver(mProviderChangedListener,
                            new IntentFilter(
                                    ConnectivityManager.CONNECTIVITY_ACTION));

                    if (mWakeLock.isHeld()) {
                        mWakeLock.release();
                        Log.d("WebSocketService", "WakeLock released");
                    }
                } else {
                    if (!mWebSocketClosed) {
                        reconnect();
                    }
                }

                if (!mWebSocketClosed) {
                    showErrorNotification();

                    Editor editor = getSharedPreferences(
                            SharedPrefKeys.APP_PREFERENCES, MODE_PRIVATE)
                            .edit();
                    editor.putBoolean(SharedPrefKeys.WEB_SOCKET_ERROR, true);
                    editor.commit();

                    Intent intent = new Intent(
                            BaseActivity.BROADCAST_RECEIVER_INTENT_FILTER);
                    intent.putExtra(BaseActivity.BROADCAST_EXTRA_TYPE,
                            BaseActivity.BROADCAST_TYPE_WEB_SOCKET_DISCONNECTED);
                    sendBroadcast(intent);
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.d("WebSocketService", "Socket closed- code: " + code
                        + ", reason: " + reason);
                if (code != CloseFrame.NORMAL) {
                    if (!Helper.checkNetworkStatus(WebSocketService.this)) {
                        mListenForConnectivity = true;
                        registerReceiver(
                                mProviderChangedListener,
                                new IntentFilter(
                                        ConnectivityManager.CONNECTIVITY_ACTION));

                        if (mWakeLock.isHeld()) {
                            mWakeLock.release();
                            Log.d("WebSocketService", "WakeLock released");
                        }
                    } else {
                        reconnect();
                    }

                    showErrorNotification();

                    Editor editor = getSharedPreferences(
                            SharedPrefKeys.APP_PREFERENCES, MODE_PRIVATE)
                            .edit();
                    editor.putBoolean(SharedPrefKeys.WEB_SOCKET_ERROR, true);
                    editor.commit();

                    Intent intent = new Intent(
                            BaseActivity.BROADCAST_RECEIVER_INTENT_FILTER);
                    intent.putExtra(BaseActivity.BROADCAST_EXTRA_TYPE,
                            BaseActivity.BROADCAST_TYPE_WEB_SOCKET_DISCONNECTED);
                    sendBroadcast(intent);
                }
            }
        };

        if (BuildConfig.FLAVOR.equals("production")) {
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[] {};
                }

                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }
            } };

            SSLContext sslContext = null;
            try {
                sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, trustAllCerts, new SecureRandom());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
            SSLSocketFactory factory = sslContext.getSocketFactory();
            try {
                mWebSocketClient.setSocket(factory.createSocket());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mWebSocketClient.connect();
    }

    protected void startPingProtocol() {
        pingCounter = 0;
        failedPingCount = 0;
        pingRunning = true;
        if (pingThread == null || !pingThread.isAlive()) {
            pingThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    while (pingRunning) {
                        if (failedPingCount < 3) {
                            PingPacket packet = new PingPacket(pingCounter);
                            Log.d("WebSocketService", "sending ping "
                                    + pingCounter);
                            pingPackets.add(packet);
                            packet.sendPing(mWebSocketClient);
                            pingCounter++;
                            pingCounter %= Byte.MAX_VALUE;
                        } else {
                            stopPingProtocol();
                            Log.d("WebSocketService", "connection lost");
                        }

                        try {
                            Thread.sleep(AppConstants.PING_SPAWN_TIME);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    private void stopPingProtocol() {

        if (pingThread != null && pingThread.isAlive()) {
            pingRunning = false;
            pingThread.interrupt();

            for (int i = 0; i < pingPackets.size(); i++) {
                pingPackets.get(i).consume();
            }

            pingPackets.clear();
        }
    }
    protected void reconnect() {
        if (mConnectionAttempt < AppConstants.MAX_WEB_SOCKET_RECONNECTS) {
            mFallbackHandler = new Handler(Looper.getMainLooper());
            mFallbackHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (mFallbackHandler != null) {
                        mFallbackHandler.removeCallbacks(this);
                        mFallbackHandler = null;
                    }

                    try {
                        stopWebSocket();
                        startWebSocket();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    mConnectionAttempt++;
                }
            }, AppConstants.WEB_SOCKET_BASE_FALLBACK_TIME * mConnectionAttempt);
        } else {
            if (mWakeLock.isHeld()) {
                mWakeLock.release();
                Log.d("WebSocketService", "WakeLock released");
            }
        }
    }

    protected void sendMessage(String message) {
        if (mWebSocketClient != null && mWebSocketClient.isOpen()) {
            mWebSocketClient.send(message);

            mConnectionAttempt = 1;
            Log.d("WebSocketService", "Send: " + message);
        } else {
            mPendingMessage = message;
        }
    }

    protected void stopWebSocket() {
        Log.d("WebSocketService", "Closing");

        mWebSocketClosed = true;
        mWebSocketClient.close();

        stopPingProtocol();

        Editor editor = getSharedPreferences(SharedPrefKeys.APP_PREFERENCES,
                MODE_PRIVATE).edit();
        editor.putBoolean(SharedPrefKeys.WEB_SOCKET_ERROR, false);
        editor.commit();

        Intent intent = new Intent(
                BaseActivity.BROADCAST_RECEIVER_INTENT_FILTER);
        intent.putExtra(BaseActivity.BROADCAST_EXTRA_TYPE,
                BaseActivity.BROADCAST_TYPE_WEB_SOCKET_CONNECTED);
        sendBroadcast(intent);
    }

    private WakefulBroadcastReceiver mProviderChangedListener = new WakefulBroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Helper.checkNetworkStatus(context)) {
                mListenForConnectivity = false;

                try {
                    unregisterReceiver(this);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                try {
                    startWebSocket();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int command = intent.getIntExtra(EXTRA_COMMAND, -1);

            switch (command) {
            case COMMAND_STOP:
                sendMessage(intent.getStringExtra(EXTRA_MESSAGE));
                stopSelf();

                break;

            case COMMAND_SEND_MESSAGE:
                sendMessage(intent.getStringExtra(EXTRA_MESSAGE));

                break;

            default:
                break;
            }
        }
    };
