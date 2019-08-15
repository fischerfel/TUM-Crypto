    SSLContext ssl_context = null;
    try {
        ssl_context = SSLContext.getInstance("TLS", "HarmonyJSSE"); 
    } catch (NoSuchAlgorithmException |  KeyManagementException | NoSuchProviderException  e) { //
        Log.e(LOGIDENT,e.getMessage());
    }
_socket = new SocketIO("SOCKETURI");
_socket.connect(new IOCallback() { ... });
