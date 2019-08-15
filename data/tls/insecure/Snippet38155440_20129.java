import android.content.Context;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class SSLUtil {

    /**
     * @param ctx
     * @param certRaw File from /res/raw
     * @return
     * @throws Exception
     */
    public static SSLSocketFactory trustCert(Context ctx, int certRaw) throws Exception {
        // Load CAs from an InputStream

        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        // File at /res/raw
        InputStream caInput = FileUtils.readRawFile(ctx, certRaw);
        Certificate ca;
        try {
            ca = cf.generateCertificate(caInput);
        } finally {
            caInput.close();
        }

// Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);
//      Log.d(TAG, "KeyStore: " + keyStore);

// Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

// Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        SSLSocketFactory socketFactory = context.getSocketFactory();
        HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);

        return socketFactory;
    }
}
