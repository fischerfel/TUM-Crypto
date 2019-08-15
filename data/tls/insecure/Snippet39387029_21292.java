    public class SocketHandler
    {
    private final static String LOGTAG = "SocketHandler";
    private int TIMEOUT = 5000;
    private MainActivity main;
    private SocketConnector socketConnector;

    public SocketHandler(MainActivity main) {
        this.main = main;
    }

    public void connect() {
        socketConnector = new SocketConnector();
        socketConnector.execute();
    }

    public void disconnect() {
        socketConnector.ws.disconnect();
    }

    private class SocketConnector extends AsyncTask<Void, Void, Void> {

        WebSocket ws;

        @Override
        protected Void doInBackground(Void... params) {
            try {   
                SSLContext context = NaiveSSLContext.getInstance("TLS");
                WebSocketFactory wsf = new WebSocketFactory();
                wsf.setConnectionTimeout(TIMEOUT);
                wsf.setSSLContext(context);
                ws = wsf.createSocket("wss://" + ADDRESS);
                ws.addListener(new WSListener());
                ws.addExtension(WebSocketExtension.parse(WebSocketExtension.PERMESSAGE_DEFLATE));
                ws.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }       
    }

    private class WSListener extends WebSocketAdapter {

        @Override
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) {
            websocket.sendText("Hello World");
        }

        @Override
        public void onTextMessage(WebSocket websocket, String message) {

        }

        @Override
        public void onError(WebSocket websocket, WebSocketException cause) {

        }
    }
}
