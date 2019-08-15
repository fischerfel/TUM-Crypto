import java.io.*;
import java.net.*;
import java.security.*;


import java.util.Enumeration;
import javax.net.ssl.*;

public class SSLConnect {

public String MakeSSlCall(String meternum) {
    String message = "";
    FileWriter file = null;
    try {
        file = new FileWriter("C:\\SSLCERT\\ClientJavalog.txt");

    } catch (Exception ee) {
        message = ee.getMessage();

    }
    //writer = new BufferedWriter(file );
    try {
        file.write("KeyStore Generated\r\n");
        KeyStore keystore = KeyStore.getInstance("JKS");
        keystore.load(new FileInputStream("C:\\SSLCERT\\newclientkeystore"), "client".toCharArray());
        file.write("KeyStore Generated\r\n");
        Enumeration enumeration = keystore.aliases();
        while (enumeration.hasMoreElements()) {
            String alias = (String) enumeration.nextElement();
            file.write("alias name: " + alias + "\r\n");
            keystore.getCertificate(alias);
            file.write(keystore.getCertificate(alias).toString() + "\r\n");
        }
        TrustManagerFactory tmf =TrustManagerFactory.getInstance("SunX509");
        tmf.init(keystore);
        file.write("KeyStore Stored\r\n");
        SSLContext context = SSLContext.getInstance("SSL");
        TrustManager[] trustManagers = tmf.getTrustManagers();
        context.init(null, trustManagers, null);

        SSLSocketFactory f = context.getSocketFactory();
        file.write("About to Connect to Ontech\r\n");
        SSLSocket c = (SSLSocket) f.createSocket("192.168.1.16", 4447);
        file.write("Connection Established to 196.14.30.33 Port: 8462\r\n");
        file.write("About to Start Handshake\r\n");
        c.startHandshake();

        file.write("Handshake Established\r\n");
        file.flush();
        file.close();
        return "Connection Established";
    } catch (Exception e) {
        try {
            file.write("An Error Occured\r\n");
            file.write(e.getMessage() + "\r\n");
            file.flush();
            file.close();
        } catch (Exception eee) {
            message = eee.getMessage();
        }
        return "Connection Failed";
    }
}
}
