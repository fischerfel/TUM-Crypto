    String keyPassword = "123456";
    // String keyPassword = "importkey";

    try {
        KeyManagerFactory keyManagerFactory;

        keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        KeyStore keyStore = KeyStore.getInstance("JKS");
        InputStream keyInput = new FileInputStream("c:\\mySrvKeystore");
        keyStore.load(keyInput, keyPassword.toCharArray());
        keyInput.close();
        keyManagerFactory.init(keyStore, keyPassword.toCharArray());

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(keyManagerFactory.getKeyManagers(), trustAllCerts, new java.security.SecureRandom());
        SSLSocketFactory sslsocketfactory = sc.getSocketFactory();
        this.sslsocket = (SSLSocket) sslsocketfactory.createSocket(host, port);

    } catch (java.security.NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (java.security.KeyManagementException e) {
        e.printStackTrace();
    } catch (java.security.KeyStoreException e) {
        e.printStackTrace();
    } catch (java.security.cert.CertificateException e) {
        e.printStackTrace();
    } catch (java.security.UnrecoverableKeyException e) {
        e.printStackTrace();
    } finally {}
}

public void run() {
    try {
        InputStream inputstream = sslsocket.getInputStream();
        InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

        OutputStream outputstream = System.out;
        OutputStreamWriter outputstreamwriter = new OutputStreamWriter(outputstream);
        BufferedWriter bufferedwriter = new BufferedWriter(outputstreamwriter);

        //get text from server and stuff...no deal!
