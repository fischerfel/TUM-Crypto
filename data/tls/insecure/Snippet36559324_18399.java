import java.io.*;
import java.net.*;
import java.security.*;

import javax.net.ssl.SSLSocketFactory;

import com.sun.net.ssl.SSLContext;
import com.sun.net.ssl.TrustManagerFactory;
import com.sun.net.ssl.TrustManager;

public class test {

    private static final String HOST = "192.168.0.103";

    private static final int PORT = 5000;
    final static String pathToStores = "/Users/admirmonteiro/Documents/Coding/Python/Thesis_Code/_Codes_/_Code_/Fall_15/fileTransmissionFiles/certs";
    final static String trustStoreFile = "cert.jks" ; // filename


    public static void main(String[] args) throws Exception {

        String currentDirectory;
        currentDirectory = System.getProperty("user.dir");
        System.out.println("Current working directory : "+currentDirectory);

        String trustFileName = pathToStores + "/" + trustStoreFile;

        char[] passphrase = "admir2006".toCharArray();
        KeyStore keystore = KeyStore.getInstance("JKS");
        keystore.load(new FileInputStream(trustFileName), passphrase);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(keystore);

        SSLContext context = SSLContext.getInstance("TLS");
        TrustManager[] trustManagers = tmf.getTrustManagers();

        context.init(null, trustManagers, null);

        SSLSocketFactory sf = context.getSocketFactory();

        Socket s = sf.createSocket(HOST, PORT);

        OutputStream out = s.getOutputStream();
        out.write("\n New Thing sent .\n\n".getBytes());


        out.close();
        s.close();
    }
}
