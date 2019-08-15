public class Server {

// The ServerSocket we'll use for accepting new connections 
private SSLServerSocket server;
private final SSLContext sc;
private final Hashtable outputStreams;

// Constructor and while-accept loop all in one. 
public Server(int port) throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, KeyManagementException {
    this.outputStreams = new Hashtable();
    KeyStore ks = initKeyStore(KEYSTORE, "JKS", KEYSTORE_PASSWORD);
    KeyManagerFactory kmf = KeyManagerFactory.getInstance(
            KeyManagerFactory.getDefaultAlgorithm());

    kmf.init(ks, KEYSTORE_PASSWORD.toCharArray());

    TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
    tmf.init(ks);

    this.sc = SSLContext.getInstance("TLS");
    TrustManager[] trustManagers = tmf.getTrustManagers();
    this.sc.init(kmf.getKeyManagers(), trustManagers, null);
    // All we have to do is listen 
    listen(port);
}
private static final String KEYSTORE = "serverkeystore";
private static final String KEYSTORE_PASSWORD = "blackzero@server";

private static KeyStore initKeyStore(String keyStore, String keyStoreType, String keyPass) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
    KeyStore ks = null;
    try {

        ks = KeyStore.getInstance(keyStoreType);
        //ks.load(new FileInputStream(keyStore), keyPass.toCharArray());
        ks.load(null, keyPass.toCharArray());
    } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
        Logger.getLogger(CNS.class.getName()).
                log(Level.SEVERE, null, e);
        //X509Certificate certificate = generateX509Certificate("CNS"); 
        ks = KeyStore.getInstance(keyStoreType);
        ks.load(null, keyPass.toCharArray());
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.genKeyPair();
        Key publicKey = kp.getPublic();
        Key privateKey = kp.getPrivate();
        Certificate[] certChain = new Certificate[1];  
        certChain[0] = certificate;
        ks.setKeyEntry("key1", privateKey, KEYSTORE_PASSWORD.toCharArray(), certChain);
        try (FileOutputStream writeStream = new FileOutputStream(keyStore)) {
            ks.store(writeStream, keyPass.toCharArray());
        }
    }
    return ks;
}

private static X509Certificate generateX509Certificate(String certificateName) throws CertificateException, FileNotFoundException, IOException {
    InputStream inStream = null;
    X509Certificate cert = null;
    try {
        inStream = new FileInputStream(certificateName);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        cert = (X509Certificate) cf.generateCertificate(inStream);
    } finally {
        if (inStream != null) {
            inStream.close();
        }
    }
    return cert;
}

private void listen(int port) throws IOException {
    // Create the ServerSocket 
    SSLServerSocketFactory ssf = sc.getServerSocketFactory();
    server = (SSLServerSocket) ssf.createServerSocket(port);

    //server = new ServerSocket(port);
    // Tell the world we're ready to go 
    System.out.println("Listening on " + server);
    // Keep accepting connections forever 
    while (true) {
        // Grab the next incoming connection 
        SSLSocket s = (SSLSocket) server.accept();
        //Socket s = server.accept();
        // Tell the world we've got it 
        System.out.println("Connection from " + s);
        // Create a DataOutputStream for writing data to the 
        // other side 
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
        // Save this stream so we don't need to make it again 
        outputStreams.put(s, dout);
        // Create a new thread for this connection, and then forget // about it 
        new ServerThread(this, s);
    }
}

// Get an enumeration of all the OutputStreams, one for each client 
// connected to us 
Enumeration getOutputStreams() {
    return outputStreams.elements();
}

// Send a message to all clients (utility routine) 
void sendToAll(String message) {
    // We synchronize on this because another thread might be 
    // calling removeConnection() and this would screw us up 
    // as we tried to walk through the list 
    synchronized (outputStreams) {
        // For each client ... 
        for (Enumeration e = getOutputStreams(); e.hasMoreElements();) {
            // ... get the output stream ... 
            DataOutputStream dout = (DataOutputStream) e.nextElement();
            // ... and send the message 
            try {
                dout.writeUTF(message);
            } catch (IOException ie) {
                System.out.println(ie);
            }
        }
    }
}

// Remove a socket, and it's corresponding output stream, from our 
// list. This is usually called by a connection thread that has 
// discovered that the connectin to the client is dead. 
void removeConnection(Socket s) {
    // Synchronize so we don't mess up sendToAll() while it walks 
    // down the list of all output streamsa 
    synchronized (outputStreams) {
        // Tell the world 
        System.out.println("Removing connection to " + s);
        // Remove it from our hashtable/list 
        outputStreams.remove(s);
        // Make sure it's closed 
        try {
            s.close();
        } catch (IOException ie) {
            System.out.println("Error closing " + s);
            ie.printStackTrace();
        }
    }
}}
