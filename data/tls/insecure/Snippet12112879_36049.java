package test.ssl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyStore;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class SSLClient {

    public void provider() throws Exception {
        // first call
        invokeWebServiceSSL(".../.../.../name.keystore", "changeit",
                "https://../../");
        // second call
        // invokeWebServiceSSL(String keystorePath, String pass, String
        // endpointURL)
    }

    public static void invokeWebServiceSSL(String keystorePath, String pass, String endpointURL) {
        HttpsURLConnection conn = null;
        try {

            char[] password = pass.toCharArray();
            FileInputStream fis = new FileInputStream(keystorePath);
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(fis, password);

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, password);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);
            fis.close();

            SSLContext ctx = SSLContext.getInstance("SSL");
            ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            SSLSocketFactory sf = ctx.getSocketFactory();

            URL url = new URL(endpointURL);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(sf);

            InputStream inputstream = conn.getInputStream();
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

            String rs = null;
            while ((rs = bufferedreader.readLine()) != null) {
                System.out.println("Received: " + rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.getInputStream().close();
            } catch (Exception e) {
            }
        }
    }
}
