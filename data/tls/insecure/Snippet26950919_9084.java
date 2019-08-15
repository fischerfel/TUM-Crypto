public class Client implements Runnable {

private SSLSocket socket;
// The streams we communicate to the server; these come 
// from the socket private 
DataOutputStream dout;
private DataInputStream din;

// Constructor 
public Client(String host, int port) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, KeyManagementException, java.security.cert.CertificateException {
    // Connect to the server 
    try {
        // Initiate the connection 
        KeyStore ks = initKeyStore(KEYSTORE, "JKS", KEYSTORE_PASSWORD);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(
            KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, KEYSTORE_PASSWORD.toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
        tmf.init(ks);

        SSLContext sc = SSLContext.getInstance("TLS");
        TrustManager[] trustManagers = tmf.getTrustManagers();
        sc.init(kmf.getKeyManagers(), trustManagers, null);

        SSLSocketFactory ssf = sc.getSocketFactory();
        socket = (SSLSocket) ssf.createSocket(host, port);
        socket.startHandshake();
        //socket = new Socket(host, port);
        // We got a connection! Tell the world 
        System.out.println("connected to " + socket);
        // Let's grab the streams and create DataInput/Output streams 
        // from them 
        din = new DataInputStream(socket.getInputStream());
        dout = new DataOutputStream(socket.getOutputStream());
        // Start a background thread for receiving messages 
        new Thread(this).start();
    } catch (IOException ie) {
        System.out.println(ie);
    }
}

private static final String KEYSTORE = "clientkey.store";
private static final String KEYSTORE_PASSWORD = "blackzero@client";

private static KeyStore initKeyStore(String keyStore, String keyStoreType, String keyPass) throws KeyStoreException, IOException, NoSuchAlgorithmException, java.security.cert.CertificateException {
    KeyStore ks = null;
    try {
        ks = KeyStore.getInstance(keyStoreType);
        ks.load(null, keyPass.toCharArray());
    } catch (KeyStoreException | IOException | NoSuchAlgorithmException | java.security.cert.CertificateException e) {
        Logger.getLogger(Client.class.getName()).
                log(Level.SEVERE, null, e);
        ks.load(null, keyPass.toCharArray());
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.genKeyPair();
        Key publicKey = kp.getPublic();
        Key privateKey = kp.getPrivate();
        ks.setKeyEntry("keyForSeckeyDecrypt", privateKey, null, null);
        ks.setKeyEntry("keyForDigitalSignature", publicKey, null, null);
        try (FileOutputStream writeStream = new FileOutputStream(keyStore)) {
            ks.store(writeStream, keyPass.toCharArray());
        }
    }
    return ks;
}

// Gets called when the user types something 
private void processMessage(String message) {
    try {
        // Send it to the server 
        dout.writeUTF(message);
        // Clear out text input field 
        //tf.setText("");
    } catch (IOException ie) {
        System.out.println(ie);
    }
}

// Variables declaration - do not modify                     
// End of variables declaration                   
@Override
public void run() {
    try {
        // Receive messages one-by-one, forever 
        while (true) {
            // Get the next message 
            String message = din.readUTF();
            // Print it to our text window 
            //ta.append(message + "\n");
        }
    } catch (IOException ie) {
        System.out.println(ie);
    }
}

/**
 * @param args the command line arguments
 */
public static void main(String[] args) {
    // Get the port # from the command line 
    int port = 786;
    try {
        Client c1 = new Client("localhost", port);
        c1.processMessage("Hi c1 is here");
    } catch (KeyStoreException 
            | NoSuchAlgorithmException 
            | CertificateException 
            | UnrecoverableKeyException 
            | KeyManagementException 
            | java.security.cert.CertificateException ex) {
        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
    }
}}
