private String token = "<token>";
private String host = "gateway.sandbox.push.apple.com";
private int port = 2195;

private String payload = "{\"aps\":{\"alert\":\"Message from Java o_O\"}}";

public APNSender() {
    try {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        keyStore.load(getClass().getResourceAsStream("cert.p12"), "<password>".toCharArray());
        KeyManagerFactory keyMgrFactory = KeyManagerFactory.getInstance("SunX509");
        keyMgrFactory.init(keyStore, "<password>".toCharArray());

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyMgrFactory.getKeyManagers(), null, null);
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(host, port);
        String[] cipherSuites = sslSocket.getSupportedCipherSuites();
        sslSocket.setEnabledCipherSuites(cipherSuites);
        sslSocket.startHandshake();

        char[] t = token.toCharArray();
        byte[] b = Hex.decodeHex(t);

        OutputStream outputstream = sslSocket.getOutputStream();

        outputstream.write(0);
        outputstream.write(0);
        outputstream.write(32);
        outputstream.write(b);
        outputstream.write(0);
        outputstream.write(payload.length());
        outputstream.write(payload.getBytes());

        outputstream.flush();
        outputstream.close();

    } catch (Exception exception) {
        exception.printStackTrace();
    }
}
