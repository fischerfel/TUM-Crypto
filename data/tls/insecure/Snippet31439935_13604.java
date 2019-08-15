package testDownload;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Downloader {

    public Downloader(boolean sslTrigger) throws IOException {

        try {
            this.allowSelfSignCerts();
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        URL url;
        // Http Download
        if (sslTrigger) {
            System.out.println("HTTPS-Mode");
            url                 = new URL("HTTPS LINK");
        } else {
            System.out.println("HTTP-Mode");
            url                 = new URL("HTTP LINK");
        }

        URLConnection conn      = url.openConnection();
        InputStream     input   = conn.getInputStream();
        OutputStream    output  = new FileOutputStream(new File(System.getProperty("user.home")+System.getProperty("file.separator")+"test.dat"));

        ReadableByteChannel inputChannel    = Channels.newChannel(input);
        WritableByteChannel outputChannel   = Channels.newChannel(output);
        System.out.println("start download");
        this.fastChannelCopy(inputChannel, outputChannel);
        inputChannel.close();
        outputChannel.close();
        System.out.println("finish");


    }

    private void fastChannelCopy(ReadableByteChannel src, WritableByteChannel dest) throws IOException {
          ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);
            while (src.read(buffer) != -1) {
              buffer.flip();
              dest.write(buffer);
              buffer.compact();
            }
            buffer.flip();
            while (buffer.hasRemaining()) {
              dest.write(buffer);
            }
    }

    private void allowSelfSignCerts() throws NoSuchAlgorithmException, KeyManagementException {
     TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
             public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                 return null;
             }
             public void checkClientTrusted(X509Certificate[] certs, String authType) {
             }
             public void checkServerTrusted(X509Certificate[] certs, String authType) {
             }
         }
     };

     SSLContext sc = SSLContext.getInstance("SSL");
     sc.init(null, trustAllCerts, new java.security.SecureRandom());
     HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

     HostnameVerifier allHostsValid = new HostnameVerifier() {
         public boolean verify(String hostname, SSLSession session) {
             return true;
         }
     };
     HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

}
