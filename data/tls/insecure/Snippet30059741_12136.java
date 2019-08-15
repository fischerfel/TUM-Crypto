private static WebSocket connect() throws Exception
{
    String PROXY  = "http://......";
    String SERVER = "wss://echo.websocket.org";

    return new WebSocketFactory()
        .getProxySettings()
        .setServer(PROXY)
        .getWebSocketFactory()
        .setSSLContext(NaiveSSLContext.getInstance("TLS"))
        .createSocket(SERVER)
        .addListener(new WebSocketAdapter()) {
            ......
        }
        .connect();
}
