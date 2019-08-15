    package com.example.gurnaaz.ftpsclient;

    import android.content.pm.PackageManager;
    import android.os.AsyncTask;
    import android.os.Bundle;
    import android.support.annotation.NonNull;
    import android.support.v4.app.ActivityCompat;
    import android.support.v7.app.AppCompatActivity;

    import java.io.*;
    import java.security.KeyManagementException;
    import java.security.KeyStore;

    import org.apache.commons.net.PrintCommandListener;
    import org.apache.commons.net.ftp.FTP;
    import org.apache.commons.net.ftp.FTPReply;
    import org.apache.commons.net.ftp.FTPSClient;

    import java.io.PrintWriter;
    import java.security.KeyStoreException;
    import java.security.NoSuchAlgorithmException;
    import java.security.UnrecoverableKeyException;
    import java.security.cert.CertificateException;
    import java.security.cert.X509Certificate;

    import javax.net.ssl.KeyManagerFactory;

    import javax.net.ssl.SSLContext;
    import javax.net.ssl.SSLSocketFactory;
    import javax.net.ssl.TrustManager;
    import javax.net.ssl.TrustManagerFactory;
    import javax.net.ssl.X509TrustManager;

    public class MainActivity extends AppCompatActivity {
        private static final int REQUEST_CODE = 0x11;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

           // String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
           // ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE); // without sdk version check
            new RetrieveFeedTask().execute();


        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            if (requestCode == REQUEST_CODE) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // save file
                    new RetrieveFeedTask().execute();
                }
            }
        }



        class RetrieveFeedTask extends AsyncTask<Object, Object, String> {

            private Exception exception;

            protected String doInBackground(Object... urls) {


                String server = "192.168.0.141";

                    String protocol = "TLS"; // TLS / null (SSL)
                    int port = 3425;
                    int timeoutInMillis = 5000;
                    FTPSClient client = new FTPSClient(protocol, false);


                KeyStore ks = null;
                try {
                    ks = KeyStore.getInstance(KeyStore.getDefaultType());
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                }
                SSLContext sslContext = null;
                KeyStore trustSt = null;
                TrustManagerFactory trustManagerFactory = null;
                InputStream trustStoreStream = null;
                InputStream ins = getResources().openRawResource(
                        getResources().getIdentifier("client",
                                "raw", getPackageName()));
                KeyStore keyStore = null;
                KeyManagerFactory keyManagerFactory = null;
                InputStream ins2 = getResources().openRawResource(
                        getResources().getIdentifier("clienttruststore",
                                "raw", getPackageName()));
                try {
                    sslContext = SSLContext.getInstance("TLSv1");
                    trustSt = KeyStore.getInstance("BKS");
                    trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                    //trustStoreStream = getResources().openRawResource(R.raw.final_client);
                    trustSt.load(ins, "password".toCharArray());
                    trustManagerFactory.init(trustSt);
                    keyStore = KeyStore.getInstance("BKS");
                    keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                    keyStore.load(ins2, "password".toCharArray());
                    keyManagerFactory.init(keyStore, "password".toCharArray());
                    sslContext.init(keyManagerFactory.getKeyManagers(),trustManagerFactory.getTrustManagers(),null);


                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (UnrecoverableKeyException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                }
                SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                client.setSocketFactory(sslSocketFactory);
    /*

                KeyStore keyStore = KeyStore.getInstance("BKS");
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                InputStream keyStoreStream = this.getResources().openRawResource(R.raw.keystore);
                keyStore.load(keyStoreStream, "<yourpassword>".toCharArray());
                keyManagerFactory.init(keyStore, "<yourpassword>".toCharArray());

                sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
                //fis.close();
                KeyManagerFactory kmf = null;
                try {
                    ks.load(new FileInputStream("/mnt/internal_sd/Pictures/clientfinal.bks"), "abc123".toCharArray());
                    kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                    kmf.init(ks, "abc123".toCharArray());
                    System.out.println("DONE??");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnrecoverableKeyException e) {
                    e.printStackTrace();
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                client.setKeyManager(kmf.getKeyManagers()[0]);
    */
                    client.setDataTimeout(timeoutInMillis);
                    client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

                    System.out.println("################ Connecting to Server ################################");

                    try {
                        int reply;

                        // KeyManager keyManager = org.apache.commons.net.util.KeyManagerUtils.createClientKeyManager(new File(keystorePath), keystorePass);

                        System.out.println("################ Connect Call ################################");
                        client.connect(server, port);
                        System.out.println("################ After Connect Call ################################");

                        client.login("newuser", "newpassword");

                        System.out.println("################ Login Success ################################");

                        //client.setFileType(FTP.BINARY_FILE_TYPE);
                        client.setFileType(FTP.NON_PRINT_TEXT_FORMAT);
                        client.execPBSZ(0);  // Set protection buffer size
                        client.execPROT("P"); // Set data channel protection to private
                        client.enterLocalActiveMode();

                        System.out.println("Connected to " + server + ".");
                        reply = client.getReplyCode();
                        System.out.println("AFTER REPLY CODE WHICH IS " + reply);
                        if (!FTPReply.isPositiveCompletion(reply)) {
                            client.disconnect();
                            System.err.println("FTP server refused connection.");
                            System.exit(1);
                        }
                        System.out.println("BEFORE retrieved FILEs");
                        int length = client.listNames().length;
                        System.out.println("Length is " + length);
                        for (int i = 0; i < length; i++) {
                            System.out.println("Client name is " + client.listNames()[i]);
                        }
                        //boolean deleteFile = client.deleteFile("err.png");
                        //boolean retrieved = client.retrieveFile(remoteFile, new FileOutputStream(localFile));
                        //System.out.println("Retrieved value: " + deleteFile);
                        System.out.println("AFTER LIST FILES");
                        // System.out.println("above is the list of patch");

                    } catch (Exception e) {
                        if (client.isConnected()) {
                            try {
                                client.disconnect();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                        System.err.println("Could not connect to server.");
                        e.printStackTrace();
                        return "";
                    } finally {
                        try {
                            client.disconnect();
                            client.logout();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("# client disconnected");
                    }
                return "";

            }



            protected void onPostExecute() {
                // TODO: check this.exception
                // TODO: do something with the feed
                System.out.println("DONE");
            }
        }
    }
