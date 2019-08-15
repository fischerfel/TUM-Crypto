private void createConnection()
{
    URI uri = URI.create("wss://stream.pushbullet.com/websocket/" + pushAT);

    final WebSocketClient mWebSocketClient = new WebSocketClient(uri) {
        @Override
        public void onOpen(ServerHandshake serverHandshake) {
            Log.d(TAG, "onOpen " + serverHandshake);
        }

        @Override
        public void onMessage(String s) {
            Log.d(TAG, "onMessage " + s);
        }

        @Override
        public void onClose(int i, String s, boolean b) {
            Log.d(TAG, "onClose " + i + ", " + s + ", " + b);
        }

        @Override
        public void onError(Exception e) {
            Log.d(TAG, "onError " + e);
            e.printStackTrace();
        }
    };

    Log.d(TAG, "Connecting to " + uri);
    trustAllHosts(mWebSocketClient);

    mWebSocketClient.connect();
    Log.d(TAG, "Connecting to " + uri);
}

public void trustAllHosts(WebSocketClient wsc) {
    // Create a trust manager that does not validate certificate chains
    TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
        }

        public void checkClientTrusted(X509Certificate[] chain,
                                       String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain,
                                       String authType) throws CertificateException {
        }
    }};


    // Install the all-trusting trust manager
    try {
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        wsc.setWebSocketFactory(new DefaultSSLWebSocketClientFactory(sc));
    } catch (Exception e) {
        e.printStackTrace();
    }
}
