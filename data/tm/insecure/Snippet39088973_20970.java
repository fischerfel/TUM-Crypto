public final class websocketxxx {

WebSocketClient client=null;
public websocketxxx (){

}


public void run(String host,String cookieVal, String xsrfVal, String resource) throws IOException {

    SslContextFactory sslContextFactory = new SslContextFactory();
    sslContextFactory.setTrustAll(true);
    WebSocketClient client = new WebSocketClient(sslContextFactory);
    MyWebSocket socket = new MyWebSocket();
    try {
        client.start();
        ClientUpgradeRequest request = new ClientUpgradeRequest();
        // Add the authentication and protocol to the request header
        // Crate wss URI from host and resource
        resource = resource + xsrfVal;
        URI destinationUri = new URI("wss://" + host + resource); // set URI
        request.setHeader("cookie",cookieVal);
        request.setHeader("Sec-WebSocket-Protocol", "ao-json");
        //System.out.println("Request Headers print : "  request.getHeaders())
        System.out.println("Connecting to : " + destinationUri);
        client.connect(socket, destinationUri, request);
        socket.awaitClose(5000, TimeUnit.SECONDS);
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
@WebSocket
public class MyWebSocket {
    private final CountDownLatch closeLatch = new CountDownLatch(1);

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("WebSocket Opened in client side");
        try {
            System.out.println("Sending message: Hi server");
            session.getRemote().sendString("Hi Server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnWebSocketMessage
    public void onMessage(String message) {
        System.out.println("Message from Server: " + message);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.println("WebSocket Closed. Code:" + statusCode);
    }

    public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException {
        return this.closeLatch.await(duration, unit);
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

public String[] simple_Login_POST_request(String host, String user, String password, String resource, String data) {
    String resp = null;
    String[] headers = new String[2];

    try {
        // Create a Client instance that supports self-signed SSL certificates
        Client client = getBypassCertVerificationClient();

        // Create a WebTarget instance with host and resource
        WebTarget target = client.target("https://" + host).path(resource);

        // Build HTTP request invocation
        Invocation.Builder invocationBuilder = target.request();

        // Encode the user/password and add it to the request header
        invocationBuilder.header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        Form form = new Form();
        form.param("userid", user);
        form.param("password", password);
        // Invoke POST request and get response as String
        //post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE));

        Response response = invocationBuilder.method("POST", Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE));
        resp = (String) response.readEntity(String.class);

        // Print input URL, input data, response code and response
        System.out.println("URL: [POST] " + target.getUri().toString());
        System.out.println("HTTP Status: " + response.getStatus());
        System.out.println("HTTP Status: " + response.getHeaders());
        headers[0] = response.getHeaderString("Set-Cookie");
        //response.getStringHeaders()
        headers[1] = response.getHeaderString("X-XSRF-TOKEN");
        System.out.println("Response: \n" + resp);

        response.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return headers;
}



public static void main(String[] args) throws IOException {
    String host = "";
    String user = "";
    String password = "";
    String resource = "";

    host ="192.168.122.1:8443";
    user = "ADMIN";
    password ="ADMIN";
    websocketXXX wsNotification = new websocketxxx();
    /////////////////////////////////////////////////////////////////
    // Simple POST LOGIN Request 
    resource = "/api/login";
    String headers[]=  wsNotification.simple_Login_POST_request(host, user, password, resource, null);
    ////////////////////////////////////////////////////////////////
    headers[0] = headers[0].substring(headers[0].lastIndexOf(",") + 1);
    System.out.println("headers[0]: " + headers[0] + "\n");
    String cookie = headers[0];
    String XSRFToken = headers[1];

    resource = "/status?-xsrf-=";
    //wsNotification.simple_websocket_example(host, cookie, XSRFToken, resource);
    wsNotification.run(host, cookie, XSRFToken, resource);

}
}
