...
String complete = wsBase + ep;
URI uri = new URI(complete);

mWebSocketClient = new WebSocketClient(uri, new Draft_17()) 
{
            @Override
            public void onOpen(ServerHandshake serverHandshake) 
            {
                Log.i("Websocket", "Opened");
            }

            @Override
            public void onMessage(String s) 
            {
                final String message = s;
                runOnUiThread(new Runnable() 
                {
                    @Override
                    public void run() 
                    {
                        Log.i("onMessage", "running");
                    }
                });
            }

            @Override
            public void onClose(int i, String s, boolean b) 
            {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) 
            {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };

        try 
        {
            // Stuff I thought might have fixed it but didn't
            SSLContext sslcontext = SSLContext.getInstance("TLSv1");

            sslcontext.init(null,
                    null,
                    null);
            SSLSocketFactory noSSLv3Factory = new NoSSLv3SocketFactory(sslcontext.getSocketFactory());

            Socket socket = noSSLv3Factory.createSocket(uri.getHost(), 80);
            mWebSocketClient.setSocket(socket);
            mWebSocketClient.connect();
        } 

        catch (Exception e)
        {
            Log.i("Websocket", e.getMessage());
        }
