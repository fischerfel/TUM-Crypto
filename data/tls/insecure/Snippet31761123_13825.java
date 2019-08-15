import android.content.Context;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;


public class SSLSocket {

public static javax.net.ssl.SSLSocketFactory newSslSocketFactory(Context ctx) {
    char[] KEYSTORE_PASSWORD = "MYKEYSTOREPASSWORD".toCharArray();
    try {
        KeyStore trusted = KeyStore.getInstance("BKS");
        InputStream in = ctx.getResources().openRawResource(R.raw.ssl);
        try {
            trusted.load(in, KEYSTORE_PASSWORD);
        } finally {
            in.close();
        }

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(trusted);

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

        return context.getSocketFactory();
    } catch (Exception e) {
        throw new AssertionError(e);
    }
}
}
