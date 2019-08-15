package pdslipay;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import za.co.ipay.retail.system.bizswitch.IpayMessageWrapper;
import za.co.ipay.retail.system.bizswitch.IpayMessageWrapper2;

public class ConnectionManager{

    private static ConnectionManager connectionManagerInstance = null;
    static Socket socket = null;
    InputStream inputStream = null;
    public static SSLClientTest sslConnect = new SSLClientTest();

    private ConnectionManager() {

       }
    public static ConnectionManager getConnectionManagerInstance() {
          if(connectionManagerInstance == null) {
              connectionManagerInstance = new ConnectionManager();
          }
          return connectionManagerInstance;
       }

    /**
     * Waits on response from server
     *
     * @param socket Server socket
     */
     public String readServerResponse(Socket socket) {
            String results = null;
            try { 
                BufferedInputStream serverReader = new BufferedInputStream(socket.getInputStream());
                IpayMessageWrapper2 wrap = new IpayMessageWrapper2();
                StringBuilder build = new StringBuilder();
                byte[] all = wrap.unWrap(serverReader);
                for (int n = 0; n < all.length; n++) {
                    char c = (char) all[n];
                    build.append(c);
                    System.out.print(c);
                }
                results = build.toString();
                System.out.println("\n\nStringBuilder " + build.toString());
                //pay.responsePrepaid(build.toString());
                //pay.responsePostPay();
                return results;
            } catch (IOException ex) {
                System.out.println("Error: Unable to read server response\n\t" + ex);
            }
            return "ERROR";
        }

    public String connection(String build) {
        BufferedOutputStream buff = null;
        String results = "ERROR";

        try {

            char[] passw = "clientpw".toCharArray();
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            ks.load(new FileInputStream ( "resource/bizswitch-keys" ), passw );

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, passw);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);
            TrustManager[] tm = tmf.getTrustManagers();

            SSLContext sclx = SSLContext.getInstance("TLS");
            sclx.init( kmf.getKeyManagers(), tm, null);

            SSLSocketFactory factory = sclx.getSocketFactory();
            SSLSocket socket = (SSLSocket) factory.createSocket( "01.000.000.198", 9102 );
            socket.startHandshake();


            IpayMessageWrapper wrap = new IpayMessageWrapper();

            buff = new BufferedOutputStream(socket.getOutputStream());
            buff.write(wrap.wrap(build.getBytes()));
            buff.flush();

            System.out.println("Info: Message has been sent..." + build);
            // Wait for server response
            results = getConnectionManagerInstance().readServerResponse(socket);

            return results;
        } catch (IOException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);

        } 
        return results;
    }


}
