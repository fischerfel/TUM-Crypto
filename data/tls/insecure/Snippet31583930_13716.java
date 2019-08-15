public class GCMServer {

public static final Logger logger = Logger.getLogger(GCMServer.class.getName());
public static SSLContext sslCtx;
public static XMPPTCPConnection connection;

private static final String GCM_SERVER = "gcm.googleapis.com";
private static final int GCM_PORT = 5235;

private static final String GCM_ELEMENT_NAME = "gcm";
private static final String GCM_NAMESPACE = "google:mobile:data";

private static final String YOUR_PROJECT_ID = "xxxxxxxxxxxx";
private static final String YOUR_API_KEY = "xxxx";



public static void main(String[] args) {

    ConnectionListener cl;

    try {        

        KeyStore windowsRootTruststore = KeyStore.getInstance("Windows-ROOT", "SunMSCAPI");
        windowsRootTruststore.load(null, null);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(windowsRootTruststore);
        sslCtx = SSLContext.getInstance("TLS");
        sslCtx.init(null, tmf.getTrustManagers(), null);

       } catch (KeyStoreException | NoSuchProviderException | IOException | NoSuchAlgorithmException | CertificateException | KeyManagementException ex) {
            Logger.getLogger(GCMServer.class.getName()).log(Level.SEVERE, null, ex);
        } 



        XMPPTCPConnectionConfiguration conf = XMPPTCPConnectionConfiguration.builder()
                .setSecurityMode(SecurityMode.ifpossible)
                .setUsernameAndPassword(YOUR_PROJECT_ID, YOUR_API_KEY)
                .setHost(GCM_SERVER)
                .setServiceName(GCM_SERVER)
                .setPort(5235)
                .setDebuggerEnabled(true)
                .setCompressionEnabled(false)
                .setSocketFactory(sslCtx.getSocketFactory())
                .build();


    cl = new ConnectionListener() {

        @Override
        public void connected(XMPPConnection xmppc) {
             Logger.getLogger(GCMServer.class.getName()).log(Level.INFO, "connected");
             System.out.println("Conncetion is secure: "+connection.isSecureConnection());
        }

        @Override
        public void authenticated(XMPPConnection xmppc, boolean bln) {
            Logger.getLogger(GCMServer.class.getName()).log(Level.INFO, "authenticated");
        }

        @Override
        public void connectionClosed() {
            Logger.getLogger(GCMServer.class.getName()).log(Level.INFO, "connection closed");
        }

        @Override
        public void connectionClosedOnError(Exception excptn) {
            Logger.getLogger(GCMServer.class.getName()).log(Level.INFO, "conncetion closed on error");
        }

        @Override
        public void reconnectionSuccessful() {
            Logger.getLogger(GCMServer.class.getName()).log(Level.INFO, "reconnection successful");
        }

        @Override
        public void reconnectingIn(int i) {
            Logger.getLogger(GCMServer.class.getName()).log(Level.INFO, "reconnecting..");
        }

        @Override
        public void reconnectionFailed(Exception excptn) {
            Logger.getLogger(GCMServer.class.getName()).log(Level.INFO, "reconnection failed");
        }
    };

        connection = new XMPPTCPConnection(conf);

        //disable Roster; it seems it's not supported by GCM
        Roster roster = Roster.getInstanceFor(connection);  
        roster.setRosterLoadedAtLogin(false);  


        try {
            connection.connect();
            connection.addAsyncStanzaListener(new MyStanzaListener(),new StanzaTypeFilter(Message.class));
            connection.addConnectionListener(cl);
            connection.login(YOUR_PROJECT_ID + "@gcm.googleapis.com", YOUR_API_KEY);
        } catch (SmackException ex) {
            Logger.getLogger(GCMServer.class.getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(GCMServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMPPException ex) {
            Logger.getLogger(GCMServer.class.getName()).log(Level.SEVERE, null, ex);
        }

}
