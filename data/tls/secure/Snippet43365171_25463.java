package routines;


import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;



import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;

import javax.net.ssl.KeyManager;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.util.KeyManagerUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

@SuppressWarnings("deprecation")
public class FTPSTest {



    public static void main() throws GeneralSecurityException {
        getFile("*******",990,"***","**********","***************","*.csv");
    }


      public static void getFile(String host,
                          int port,
                          String username,
                          String password,
                          String localDirectory,
                          String remoteDirectory,
                          String filemask) throws GeneralSecurityException {
        try {
//          FTPSClient.setTrustManager(null);
          SSLContext context = SSLContext.getInstance("TLSv1.2");
          context.init(null, null, new java.security.SecureRandom()); 
          FTPSClient ftpClient = new FTPSClient(true,context);
          // Connect to host
          ftpClient.setTrustManager(null);
          ftpClient.setAuthValue("SSL");
          //ftpClient.setTrustManager(getConfiguration().getCertificatesManager());
          KeyManager keyManager = KeyManagerUtils.createClientKeyManager(new File("/home/global/Desktop/wilson.jks"), "W1nd3n3rgy17!");
          ftpClient.setKeyManager(keyManager);
          ftpClient.connect(host, port);
          int reply = ftpClient.getReplyCode();
          System.out.println(reply);
          if (FTPReply.isPositiveCompletion(reply)) {

            // Login
            if (ftpClient.login(username, password)) {

              // Set protection buffer size
              ftpClient.execPBSZ(0);
              // Set data channel protection to private
              ftpClient.execPROT("P");      
              // Enter local passive mode
              ftpClient.enterLocalPassiveMode();

              System.out.println("connected");
