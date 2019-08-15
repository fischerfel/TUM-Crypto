 public void disconnectWebsocket() {
        if (mConnection != null) {
            mConnection.setClosedCallback(new CompletedCallback() {
                @Override
                public void onCompleted(Exception ex) {
                    Log.i(TAG, "Destroying websocket");
                    MiscUtils.displayToast(activity, "Destroying websocket", Toast.LENGTH_SHORT);
                }
            });
            mConnection.close();
            mConnection = null;
            authenticationInfo=null;
        }
    if (handler != null) {
        handler.removeCallbacks(runnableCallback);
    }
    handler = null;
}

private final TrustManager trm = new X509TrustManager() {
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
};

private void connectWebsocket() {
    disconnectWebsocket();

    if (handler != null) {
        handler.removeCallbacks(runnableCallback);
    }

    try {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[] { trm }, null);
        AsyncHttpClient.getDefaultInstance().getSSLSocketMiddleware().setSSLContext(sslContext);
        AsyncHttpClient.getDefaultInstance().getSSLSocketMiddleware().setTrustManagers(new TrustManager[] {trm});
        AsyncHttpClient.getDefaultInstance().getSSLSocketMiddleware().setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    }
    catch (Exception e) {
        Log.e(TAG, "Unable to set sslcontext", e);
    }

    String reqUrl = MiscUtils.buildUrl( baseUrl, "websocket/graphtools/1");
    //reqUrl = reqUrl.replace("http://", "ws://");
    //reqUrl = reqUrl.replace("https://", "wss://");
    AsyncHttpRequest request = MiscUtils.extendRequest(new AsyncHttpGet(reqUrl), authenticationInfo);
    AsyncHttpClient.getDefaultInstance().websocket(request, null, new AsyncHttpClient.WebSocketConnectCallback() {
        @Override
        public void onCompleted(Exception ex, WebSocket webSocket) {
            if (ex != null) {
                Log.e(TAG, "Unable to connect to websocket", ex);
                if (handler != null) {
                    MiscUtils.displayToast(activity, "Unable to connect to weboscket, trying to reconnect", Toast.LENGTH_SHORT);
                    handler.postDelayed(runnableCallback, 2000);
                }
                return;
            }

            Log.i(TAG, "Websocket onCompleted");
            mConnection = webSocket;

             webSocket.setStringCallback(new WebSocket.StringCallback() {
                @Override
                public void onStringAvailable(String s) {
                    Log.i(TAG, "Ws string available: " + s);
                    if (activity != null) {
                        ((WebSocketHelper) ((AuthenticationHelper) activity).getZoneFragment()).parseWebSocketEvent(s);
                    }
                }
            });
            webSocket.setEndCallback(new CompletedCallback() {
                @Override
                public void onCompleted(Exception ex) {
                    Log.i(TAG, "Websocket disconnected");
                    updateWebsocketStatusLabel(false);
                }
            });
            webSocket.setClosedCallback(new CompletedCallback() {
                @Override
                public void onCompleted(Exception ex) {
                    if (handler != null) {
                        Log.i(TAG, "Websocket closed");
                        MiscUtils.displayToast(activity, "Websocket connection lost, trying to reconnect", Toast.LENGTH_SHORT);
                        handler.postDelayed(runnableCallback, 1000);
                    }
                }
            });
            updateWebsocketStatusLabel(true);
        }
    });
}

Runnable runnableCallback = new Runnable() {
    @Override
    public void run() {
        connectWebsocket();
    }
};
