 public WebsocketTransport(URI uri, IOConnection connection) {
        super(uri);
        this.connection = connection;
        SSLContext context = null;
        try {
            context = SSLContext.getInstance("TLS", "HarmonyJSSE");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } 
        try {
            context.init(null, null, null);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        if("wss".equals(uri.getScheme()) && context != null) {
            this.setWebSocketFactory(new DefaultSSLWebSocketClientFactory(context));
        }
    }
