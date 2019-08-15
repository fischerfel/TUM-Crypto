public class DataRequest 
{
SSLContext sc, sslctx;
KeyManagerFactory clientKeyManager;
TrustManagerFactory trustManager;
String sHostname, sPort;    

void SSLSetup() throws Exception 
{
    //load client private key
    KeyStore clientKeys = KeyStore.getInstance("JKS");
    clientKeys.load(new FileInputStream("client.jks"),"password".toCharArray());
    clientKeyManager = KeyManagerFactory.getInstance("SunX509");
    clientKeyManager.init(clientKeys,"password".toCharArray());
    //load server public key
    KeyStore serverPub = KeyStore.getInstance("JKS");
    serverPub.load(new FileInputStream("serverpub.jks"),"password".toCharArray());
    trustManager = TrustManagerFactory.getInstance("SunX509");
    trustManager.init(serverPub);
    sc = SSLContext.getInstance("SSL");
    sc.init(clientKeyManager.getKeyManagers(), trustManager.getTrustManagers(), new java.security.SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    //create all-trusting host name verifier
    HostnameVerifier allHostsValid = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
    // use keys to create SSLSoket
    sslctx = SSLContext.getInstance("TLS");
    sslctx.init(clientKeyManager.getKeyManagers(), trustManager.getTrustManagers(), null);
}   
public void sendRequest(String sCommand) throws Exception
{           
    //send the request
    //get response from server
}
}
