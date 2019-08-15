public class TestServer {


public static void main(String[] args) {


    String ksName = "/some/path/keystore-server.jks";
    char ksPass[] = "password".toCharArray();
    char ctPass[] = "pswd".toCharArray();

    KeyStore ks;
    try {
        ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(ksName), ksPass);
        KeyManagerFactory kmf = 
        KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, ctPass);
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(kmf.getKeyManagers(), null, null);
        SSLServerSocketFactory ssf = sc.getServerSocketFactory();
        SSLServerSocket s   = (SSLServerSocket) ssf.createServerSocket(SERVER_PORT);                

        while(true){                
            SSLSocket sslsocket = (SSLSocket) s.accept();
            System.out.println("New Client accepted");
            TestThread t = new TestThread(sslsocket);
            t.run();      
        }

    } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException | KeyManagementException ex) {
        Logger.getLogger(TotalControlServer.class.getName()).log(Level.SEVERE, null, ex);
    }        
} 
