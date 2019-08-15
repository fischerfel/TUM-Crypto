KeyStore trustStore = KeyStore.getInstance("BKS");
InputStream trustStoreStream = _service.getResources().openRawResource(R.raw.mykeystore);
trustStore.load(trustStoreStream, "keystorepass".toCharArray());

TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
trustManagerFactory.init(trustStore);

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
SSLSocketFactory factory = sslContext.getSocketFactory();
_socket = (SSLSocket) factory.createSocket(SERVER_IP, SERVER_PORT);
_socket.setUseClientMode(true);

if (_socket.isConnected()) {
    _nis = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
    _nos = new OutputStreamWriter(_socket.getOutputStream());

    String text;
    while((text = _nis.readLine()) != null){ //This is blocking
        messageReceived(text);
    }
}
