        import java.io.BufferedReader;

        import java.io.File;
        import java.io.FileInputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.io.OutputStream;

        import java.net.MalformedURLException;
        import java.net.ProtocolException;
        import java.net.URL;
        import java.security.KeyStore;
        import java.util.ResourceBundle;
        import javax.net.ssl.HostnameVerifier;
        import javax.net.ssl.HttpsURLConnection;
        import javax.net.ssl.KeyManager;
        import javax.net.ssl.KeyManagerFactory;
        import javax.net.ssl.SSLContext;
        import javax.net.ssl.SSLSession;
        import javax.net.ssl.SSLSocketFactory;
        import javax.net.ssl.TrustManager;
        import javax.net.ssl.TrustManagerFactory;

        public class Transporter {

            private static ResourceBundle resource = ResourceBundle.getBundle("resourece_00");
            private static final String keystore = resource.getString("server_keystore");
            private static final String truststore = resource.getString("server_truststore");
            private static final String keypass = resource.getString("server_keystore_pwd");
            private static final String trustpass = resource.getString("server_truststore_pwd");

            // secure channel variables
            private static SSLSocketFactory sslSocketFactory = null;

            public Transporter() {
                // setupSocketFactory();
            }

            static {
                try {
                    String protocol = "TLS";
                    String type = "JKS";

                    String algorithm = KeyManagerFactory.getDefaultAlgorithm();
                    String trustAlgorithm = TrustManagerFactory.getDefaultAlgorithm();

                    // create and initialize an SSLContext object
                    SSLContext sslContext = SSLContext.getInstance(protocol);
                    sslContext.init(getKeyManagers(type, algorithm), getTrustManagers(type, trustAlgorithm), null);

                    // obtain the SSLSocketFactory from the SSLContext
                    sslSocketFactory = sslContext.getSocketFactory();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            private static KeyStore getStore(String type, String filename, String pwd) throws Exception {

                KeyStore ks = KeyStore.getInstance(type);
                InputStream istream = null;

                try {

                    File ksfile = new File(filename);
                    istream = new FileInputStream(ksfile);
                    ks.load(istream, pwd != null ? pwd.toCharArray() : null);
                } finally {
                    if (istream != null) {
                        istream.close();
                    }
                }

                return ks;
            }

            private static KeyManager[] getKeyManagers(String type, String algorithm) throws Exception {
                KeyStore ks = getStore(type, keystore, keypass);
                KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
                kmf.init(ks, keypass.toCharArray());
                return kmf.getKeyManagers();
            }

            private static TrustManager[] getTrustManagers(String type, String algorithm) throws Exception {
                KeyStore ts = getStore(type, truststore, trustpass);
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
                tmf.init(ts);
                return tmf.getTrustManagers();

            }

            public String sendToVD(String msg, String urll, Long timeOut) {

                byte[] bytes = msg.getBytes();
                HttpsURLConnection sconn = null;
                URL url = null;
                OutputStream out = null;
                BufferedReader read = null;
                String recu = null;

                try {

                    url = new URL(urll);
                    sconn = (HttpsURLConnection) url.openConnection();
                    sconn.setHostnameVerifier(new HostnameVerifier() {

                        public boolean verify(String hostname, SSLSession sslSession) {

                            return true;
                        }
                    });
                    sconn.setSSLSocketFactory(sslSocketFactory);
                    // sconn.setReadTimeout((timeOut.intValue()) * 1000);// set timeout
                    sconn.setRequestMethod("POST");
                    sconn.addRequestProperty("Content-Length", "" + bytes.length);
                    sconn.setRequestProperty("Content-Type", "application/xml; charset=utf-8");
                    sconn.setDoOutput(true);
                    sconn.setDoInput(true);
                    // send POST data
                    // This is the crash location
                    out = sconn.getOutputStream();
                    // OutputStreamWriter osw = new OutputStreamWriter(out, "UTF-8");
                    out.write(bytes);
                    out.flush();
                    // logger.info("flush!!!!!!!!!!!!!");
                    // out.close();
                    read = new BufferedReader(new InputStreamReader(sconn.getInputStream()));
                    String query = null;
                    recu = read.readLine();
                    while ((query = read.readLine()) != null) {
                        recu += query;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        // close all connections here
                        if (out != null)
                            out.close();

                        if (read != null)
                            read.close();

                        if (sconn != null)
                            sconn.disconnect();
                    } catch (Exception ce) {

                    }
                }
                return recu;
            }
        }
