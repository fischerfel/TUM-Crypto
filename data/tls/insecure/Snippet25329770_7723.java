KeyStore keyStore;
try {
    keyStore = KeyStore.getInstance("BKS");
    keyStore.load(getResources().openRawResource(R.raw.keystore), "aQaKP2n5XRyXycT".toCharArray());
    TrustManagerFactory trustManagerFactory = 
                TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    trustManagerFactory.init(keyStore);
    SSLContext sslctx = SSLContext.getInstance("TLS");
    sslctx.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
    SSLSocketFactory factory = sslctx.getSocketFactory();
    SSLSocket socket = (SSLSocket)factory.createSocket("192.168.1.2", 9999);
    ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
    System.out.println("Connected to server");
    while(true) {
        Object readObject = input.readObject(); // Line 117
        System.out.println(readObject); 
        output.writeObject(new String("Android to Server"));
    }
}
