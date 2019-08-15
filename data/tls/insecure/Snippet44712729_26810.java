SSLContext context = NaiveSSLContext.getInstance("TLS");

ws = new WebSocketFactory().setSSLContext(context).setConnectionTimeout(5000)
            .createSocket("wss://192.168.1.164/chat/")
            .addListener(new WebSocketAdapter() {
                @Override
                public void onTextMessage(WebSocket websocket, String message) {
                    // Received a text message.
                   }
                @Override
                public void onConnectError(WebSocket websocket, WebSocketException e){
                    mTextView.setText(e.getMessage());
                }
            });
ws.connectAsynchronously();`
