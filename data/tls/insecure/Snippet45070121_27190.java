public static void main(String[] args) 
        throws IOException, KeyStoreException, NoSuchAlgorithmException, 
        CertificateException, UnrecoverableKeyException, KeyManagementException {

    FileInputStream keyFile = new FileInputStream(archivoKey);//Server.jks with Client.crt and .key as well as Server.crt and .key
    char[] archivopwd = mypassword.toCharArray();
    String password = mypassword;

    System.setProperty("javax.net.ssl.trustStore", archivoKey);
    System.setProperty("javax.net.ssl.trustStorePassword", password); 

    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
    keyStore.load(keyFile, archivopwd);

    KeyManagerFactory keyManagerFactory = 
            KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    keyManagerFactory.init(keyStore, archivopwd);
    KeyManager keyManagers[] = keyManagerFactory.getKeyManagers();

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(keyManagers, null, null);


    SSLServerSocketFactory factory=(SSLServerSocketFactory) 
            SSLServerSocketFactory.getDefault();
    SSLServerSocket ss = (SSLServerSocket) factory.createServerSocket(6000);
    System.out.println("Esperando conexion...");
    ss.setEnabledCipherSuites(ss.getSupportedCipherSuites());
    SSLSocket so =(SSLSocket) ss.accept();
    so.startHandshake();
    System.out.println("Conexion realizada");

    BufferedReader in = new BufferedReader
        (new InputStreamReader(so.getInputStream()));
    String msg = in.readLine();
    System.out.println(msg);

}
