SSLContext sslContext = null;
ServerSocket serverSocket = null;
KeyManagerFactory kmf = null;
KeyStore keystore = loadKeyStore(KEYSTORE_FILE);
if (keystore == null) {
    // throw exception
}
char[] psw = System.console().readPassword("Enter password for the key materials in file \"%s\":", KEYSTORE_FILE);
try {
    kmf = KeyManagerFactory.getInstance("PKIX");
    kmf.init(keystore, psw);
} catch (NoSuchAlgorithmException | UnrecoverableKeyException | KeyStoreException e) {
    e.printStackTrace();
    kmf = null;
    // throw exception
}
try {
    sslContext = SSLContext.getInstance("TLSv1.2");
    System.out.println(kmf==null); // prints false
    sslContext.init(kmf==null?null:kmf.getKeyManagers(), null, null);
} catch (NoSuchAlgorithmException | KeyManagementException e) {
    // throw exception
}

try {
    serverSocket = sslContext.getServerSocketFactory().createServerSocket(PORT, BACKLOG, HOST);
    ((SSLServerSocket)serverSocket).setEnabledProtocols(new String[]{"TLSv1.2"});
} catch (IOException e) {
    // throw exception
}
