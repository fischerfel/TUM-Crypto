java.io.IOException: Connection output is closed
    at org.eclipse.jetty.websocket.common.io.IOState.assertOutputOpen(IOState.java:132)
    at org.eclipse.jetty.websocket.common.WebSocketRemoteEndpoint.uncheckedSendFrame(WebSocketRemoteEndpoint.java:303)
    at org.eclipse.jetty.websocket.common.WebSocketRemoteEndpoint.blockingWrite(WebSocketRemoteEndpoint.java:106)
    at org.eclipse.jetty.websocket.common.WebSocketRemoteEndpoint.sendString(WebSocketRemoteEndpoint.java:387)
    at org.eclipse.jetty.websocket.websocket_client.websocket$SampleWebSocketAdapter.startServerPing(websocket.java:149)
    at org.eclipse.jetty.websocket.websocket_client.websocket$SampleWebSocketAdapter$1.run(websocket.java:162)
    at java.util.TimerThread.mainLoop(Unknown Source)
    at java.util.TimerThread.run(Unknown Source)



    public final class websocket {
    WebSocketClient client=null;
    public websocket (){

    }

    public void simple_websocket_example(String host) {
        // Create an SSL context that trusts all
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setTrustAll(true);

        // Create websocket client
        client = new WebSocketClient(sslContextFactory);
        SampleWebSocketAdapter socket = new SampleWebSocketAdapter();

        try {
            Runtime rt = Runtime.getRuntime();
            rt.addShutdownHook(new Thread() {
                public void run() {
                    try {
                        System.out.println("Received ^C. Stopping websocket client.");
                        client.stop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            URI uri = new URI(host); // set URI
            // Start the client and connect
            client.start();
            // Create a request
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            client.connect(socket, uri, request);
            while (true) {
            Thread.sleep(5000);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                client.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class SampleWebSocketAdapter extends WebSocketAdapter {
        JsonParser jsonParser = new JsonParser();
        Gson jsonWriter = new GsonBuilder().setPrettyPrinting().create();
        Session webSocketSession;
        RemoteEndpoint remote;
        @Override
        public void onWebSocketText(String message) {

            if (isConnected()) {
                JsonElement jsonElement = jsonParser.parse(message);
                System.out.println("\n" + jsonWriter.toJson(jsonElement));
            }else {
                System.out.println("Not Connected ");
            }

        }

        @Override
        public void onWebSocketConnect(Session session)
        {
            super.onWebSocketConnect(session);
            System.out.println("Socket Connected: " + session);
            StringBuilder sb = new StringBuilder("{");
            sb.append("\"topic\": \"").append("topics:bp.aeprocessor.v1.alarms").append("\",");
            sb.append("\"ref\": \"0\",");
            sb.append("\"event\": \"phx_join\",");
            sb.append("\"payload\": {}");
            sb.append("}");
            //webSocketSession = session;
             remote = session.getRemote();
            try {


                session.getRemote().sendString(sb.toString());

                startServerPing();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }

        public void startServerPing()
        {

            StringBuilder sb = new StringBuilder("{");
            sb.append("\"topic\": \"").append("phoenix").append("\",");
            sb.append("\"event\": \"heartbeat\",");
            sb.append("\"payload\": {},");
            sb.append("\"ref\": \"0\",");
             sb.append("}");
        try {
                    remote.sendString(sb.toString());


            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            new java.util.Timer().schedule( 
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            startServerPing();
                        }
                    }, 
                    5000 
            );
        }
        @Override
        public void onWebSocketClose(int statusCode, String reason)
        {
            super.onWebSocketClose(statusCode,reason);
            System.out.println("Socket Closed: [" + statusCode + "] " + reason);
        }

    }

    public Client getBypassCertVerificationClient() {
        Client client1 = null;
        try {
            // Create a HostnameVerifier that overrides the verify method to accept all hosts
            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                public boolean verify(String host, SSLSession sslSession) {
                    return true;
                }
            };
            // Create a TrustManager
            TrustManager[] trust_mgr = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(X509Certificate[] certs, String t) {
                        }
                        public void checkServerTrusted(X509Certificate[] certs, String t) {
                        }
                    }
            };
            // Create the SSL Context
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trust_mgr, new SecureRandom());
            // Create the client with the new hostname verifier and SSL context
            client1 = ClientBuilder.newBuilder()
                    .sslContext(sslContext)
                    .hostnameVerifier(hostnameVerifier)
                    .build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return client1;
    }


    public static void main(String[] args) throws IOException {
        String host = "";

        host = "xyx.com/lab/api/";
         websocket wsNotification = new  websocket();

        wsNotification.simple_websocket_example(host);


    }
}
