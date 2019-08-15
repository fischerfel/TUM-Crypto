import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.*;

public class FetchCert {

    public static void main(String[] args) throws Exception {
        //REPLACE THIS WITH YOUR TARGET HOST NAME
        String hostname = "example.com";
        SSLSocketFactory factory = HttpsURLConnection.getDefaultSSLSocketFactory();

        SSLSocket socket = (SSLSocket) factory.createSocket(hostname, 443);
        try {
            socket.startHandshake();
            socket.close();
            System.out.println("No errors, certificate is already trusted");
            return;
        } catch (SSLException e) {
            System.out.println("cert likely not found in keystore, will pull cert...");
        }


        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        char[] password = "changeit".toCharArray();
        ks.load(null, password);

        SSLContext context = SSLContext.getInstance("TLS");
        TrustManagerFactory tmf =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);
        X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
        SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
        context.init(null, new TrustManager[]{tm}, null);
        factory = context.getSocketFactory();

        socket = (SSLSocket) factory.createSocket(hostname, 443);
        try {
            socket.startHandshake();
        } catch (SSLException e) {
            //we should get to here
        }
        X509Certificate[] chain = tm.chain;
        if (chain == null) {
            System.out.println("Could not obtain server certificate chain");
            return;
        }

        X509Certificate cert = chain[0];
        String alias = hostname;
        ks.setCertificateEntry(alias, cert);

        System.out.println("saving file jssecacerts to working dir");
        System.out.println("copy this file to your jre/lib/security folder");
        FileOutputStream fos = new FileOutputStream("jssecacerts");
        ks.store(fos, password);
        fos.close();
    }
    private static class SavingTrustManager implements X509TrustManager {

        private final X509TrustManager tm;
        private X509Certificate[] chain;

        SavingTrustManager(X509TrustManager tm) {
            this.tm = tm;
        }

        public X509Certificate[] getAcceptedIssuers() {

        return new X509Certificate[0];  
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            throw new UnsupportedOperationException();
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            this.chain = chain;
            tm.checkServerTrusted(chain, authType);
        }
    }
}
