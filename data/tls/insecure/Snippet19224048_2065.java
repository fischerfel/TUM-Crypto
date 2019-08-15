public class GTAndroidMQTTService extends Service implements MqttCallback {

    private void init() {
        this.clientId = Settings.System.getString(getContentResolver(), Secure.ANDROID_ID);
    }

    @Override
    @Deprecated
    public void onStart(Intent intent, int startId) {
        logger("onStart() called");
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        logger("onStartCommand() called");
        if (client == null) {
            try {

                init();

                conOpt = new MqttConnectOptions();
                conOpt.setCleanSession(false);
                conOpt.setUserName("...");
                conOpt.setPassword("...");

                try {
                    char[] keystorePass = getString(R.string.keystorepass).toCharArray();

                    KeyStore keyStore = KeyStore.getInstance("BKS");
                    keyStore.load(getApplicationContext().getResources().openRawResource(R.raw.prdkey),
                            keystorePass);

                    TrustManagerFactory trustManagerFactory = TrustManagerFactory
                            .getInstance(KeyManagerFactory.getDefaultAlgorithm());

                    trustManagerFactory.init(keyStore);

                    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
                            .getDefaultAlgorithm());
                    kmf.init(keyStore, keystorePass);

                    SSLContext sslContext = SSLContext.getInstance("TLS");

                    sslContext.init(kmf.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

                    conOpt.setSocketFactory(sslContext.getSocketFactory());
                } catch (Exception ea) {
                }

                client = new MqttClient(this.mqttURL, clientId, new MqttDefaultFilePersistence(folder));
                client.setCallback(this);

                conOpt.setKeepAliveInterval(this.keepAliveSeconds);

            } catch (MqttException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (intent == null) {
            Log.i("TAG", "Android restarted the service[START_STICKY]");
            if (client != null) {
                tryToEstablishConnection();
            }
        }
        return START_STICKY;
    }

    public void unsubscribe(String topicName) throws MqttException {
        try {
            client.unsubscribe(topicName);
        } catch (Exception e) {
            Log.i("TAG", "Unsubscribing from topic \"" + topicName + "has failed: " + e.toString());
        }
    }

    private void retry() {
        try {
            notifyUserWithServiceStatus("Status Changed", "Status", "Connecting");
            client.connect(conOpt);
            notifyUserWithServiceStatus("Status Changed", "Status", "User Connected #" + (++retrycnt));
        } catch (Exception e) {
            notifyUserWithServiceStatus("Status Changed", "Status", "Cannot Connect");
            e.printStackTrace();
        }
    }

    public void subscribe(String topicName, int qos) throws MqttException {
        try {
            client.subscribe(topicName, qos);
        } catch (Exception e) {
        }
    }

    public void disconnect() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        logger("onBind() called");
        return null;
    }

    @Override
    public void onCreate() {
        logger("onCreate() called");
        super.onCreate();
    }

    @Override
    public void connectionLost(Throwable arg0) { // Connection lost
        notifyUserWithServiceStatus("Status Changed", "Status", "Connection Lost!");
        tryToEstablishConnection();
    }

    private void tryToEstablishConnection() {
        if (!retrying) {
            retrying = true;
            new Thread(new Runnable() {

                @Override
                public void run() {
                    for (;;) {
                        try {
                            if (isOnline() && !isConnected()) {
                                retry();
                                Thread.sleep(RETRY_INTERVAL);
                            } else if (isConnected()) {
                                retrying = false;
                                break;
                            } else if (!isOnline()) {
                                retrying = false;
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    private class NetworkConnectionIntentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context ctx, Intent intent) {
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MQTT");
            wl.acquire();

            if (isOnline() && !isConnected())
                notifyUserWithServiceStatus("Status Changed", "Status", "Online but not connected");
            else if (!isOnline())
                notifyUserWithServiceStatus("Status Changed", "Status", "Connection Lost!");

            tryToEstablishConnection();
            wl.release();
        }
    }

    private boolean isConnected() {
        try {
            return client.isConnected();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i == null)
            return false;
        if (!i.isConnected())
            return false;
        if (!i.isAvailable())
            return false;
        return true;
    }

    @Override
    public void onDestroy() {
        logger("onDestroy() called");
        try {
            client.disconnect();
            Log.i("TAG", "Service stopped");
        } catch (MqttException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken arg0) {
        // TODO Auto-generated method stub
    }
}
