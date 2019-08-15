public static void main(String[] args) {
    int port = 6000;
    String host = "localhost";
    String password = mypassword;
    System.setProperty("javax.net.ssl.trustStore", archivoKey);
    System.setProperty("javax.net.ssl.trustStorePassword", password); 
    try {
        SSLContext sc = SSLContext.getInstance("TLS");
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        InputStream keyFile = new FileInputStream(archivoKey); //Client.jks, exactly the same as the Server.jks
        try {
            keyStore.load(keyFile, archivopwd);
        } finally {
            if (keyFile != null) {
                keyFile.close();
            }
        }
        KeyManagerFactory keyManagerFactory = 
                KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, archivopwd);

        sc.init(null,  null, null);

        SocketFactory factory = sc.getSocketFactory();

        System.out.println("Buscando conexion...");

        try (SSLSocket so = (SSLSocket) factory.createSocket(host, port)) {
            so.getEnabledCipherSuites();
            so.startHandshake();

            System.out.println("Conexion exitosa!");

            DataOutputStream os = new DataOutputStream(so.getOutputStream());

            os.writeUTF("Prueba!");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
