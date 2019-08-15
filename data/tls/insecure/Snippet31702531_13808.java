public class Client {

    private static String ip = 1.2.3.4";
    private static int DEFAULT_PORT= 8888;


    private static File keyStore = new File(
            "\\Program Files\\Java\\jdk1.8.0_51\\bin\\truststore.jks");

    //this is the password for the keystore
    private static String keyStorePassword = "password";

    private static SSLSocketFactory getSSLSocketFactory() throws IOException,
            GeneralSecurityException {
        // Call getTrustManagers to get suitable trust managers
        TrustManager[] tms = getTrustManagers();

        // Call getKeyManagers to get suitable key managers
        KeyManager[] kms = getKeyManagers();

        // Now construct a SSLContext using these KeyManagers. We
        // specify a null TrustManager and SecureRandom, indicating that the
        // defaults should be used.
        SSLContext context = SSLContext.getInstance("SSL");
        context.init(kms, tms, null);

        // Finally, we get a SocketFactory, and pass it to SimpleSSLClient.
        SSLSocketFactory ssf = context.getSocketFactory();
        return ssf;
    }

    private static TrustManager[] getTrustManagers() throws IOException,
            GeneralSecurityException {

        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs,
                    String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs,
                    String authType) {
            }
        } };
        return trustAllCerts;
        }

    private static KeyManager[] getKeyManagers() throws IOException,
            GeneralSecurityException {
        // First, get the default KeyManagerFactory.
        String alg = KeyManagerFactory.getDefaultAlgorithm();
        KeyManagerFactory kmFact = KeyManagerFactory.getInstance(alg);

        // Next, set up the KeyStore to use. We need to load the file into
        // a KeyStore instance.
        FileInputStream fis = new FileInputStream(keyStore);
        KeyStore ks = KeyStore.getInstance("jks");
        ks.load(fis, keyStorePassword.toCharArray());
        fis.close();

        // Now we initialize the TrustManagerFactory with this KeyStore
        kmFact.init(ks, keyStorePassword.toCharArray());

        // And now get the TrustManagers
        KeyManager[] kms = kmFact.getKeyManagers();
        return kms;
    }

    public static void main(String[] args) throws InterruptedException, Exception {
        try {
            SSLSocketFactory sslSocketFactory = getSSLSocketFactory();

            SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket(
                    ip, DEFAULT_PORT);
            socket.setSoTimeout(99999999);

            System.out.println("Connecting to "
                    + socket.getRemoteSocketAddress().toString() + " : "
                    + socket.getPort());
            System.out.println("Is Connected? " + socket.isConnected());
            socket.startHandshake();

            System.out.println("We Made it!!");


            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();
            DataOutputStream dos = new DataOutputStream(out);
            DataInputStream dis = new DataInputStream(in);

            //RequestMessage requestMessage = new RequestMessage((int)(System.currentTimeMillis() / 100), (short)1);
            NullMessage nullMessage = new NullMessage();
            byte[] bytes = nullMessage.getBytes();

            dos.writeInt(bytes.length);

            if(bytes.length > 0) {
                dos.write(bytes, 0, bytes.length);
            }

            socket.close();
