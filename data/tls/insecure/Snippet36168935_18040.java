package admir.network.ssl.client;

import android.provider.Settings;

import java.io.*;
import java.net.*;
import java.security.*;


import javax.net.ssl.SSLSocketFactory;
import org.apache.commons.io.IOUtils;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.TrustManager;


//import com.sun.net.ssl.SSLContext;
//import com.sun.net.ssl.TrustManagerFactory;
//import com.sun.net.ssl.TrustManager;


public class test2 {

    private static final String HOST = "X.X.X.X";

    private static final int PORT = 5000;
    String pwd = System.getProperty("user.dir");
    final static String pathToStores = System.setProperty("user.dir","user.dir"+"/java/admir.network.ssl.client/");
    final static String trustStoreFile = "cert.jks" ; // filename
    public String app = System.setProperty("user.dir",pwd);


    public static void main(String[] args) throws Exception {
        String trustFileName = pathToStores + "/" + trustStoreFile;
        char[] passphrase = "xxxxx".toCharArray();
        KeyStore keystore = KeyStore.getInstance("JKS");
        keystore.load(new FileInputStream(trustFileName), passphrase);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(keystore);
        SSLContext context = SSLContext.getInstance("TLS");
        TrustManager[] trustManagers = tmf.getTrustManagers();
        context.init(null, trustManagers, null);
        SSLSocketFactory sf = context.getSocketFactory();
        Socket s = sf.createSocket(HOST, PORT);

        String FILE_TO_SEND = ""+ System.getProperty("user.dir")+"/"+"src"+"/"+"Voice 005.wav"+"";

        File soundFile = AudioUtil.getSoundFile(FILE_TO_SEND);

        if (s.isBound()){
            OutputStream out = s.getOutputStream();
            InputStream in =  new FileInputStream(soundFile);
            IOUtils.copy(in, out);
            System.out.println("Done Sending.");
            out.close();
        }

        s.close();
    }
}
