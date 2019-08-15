import java.security.*;
import java.security.cert.*;
import javax.net.ssl.*;

public class CertChecker implements X509TrustManager {

    private final X509TrustManager defaultTM;

    public CertChecker() throws GeneralSecurityException {
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init((KeyStore)null);
        defaultTM = (X509TrustManager) tmf.getTrustManagers()[0];
    }

    public void checkServerTrusted(X509Certificate[] certs, String authType) {
        if (defaultTM != null) {
            try {
                defaultTM.checkServerTrusted(certs, authType);
                System.out.println("Certificate valid");
            } catch (CertificateException ex) {
                System.out.println("Certificate invalid: " + ex.getMessage());
            }
        }
    }

    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
    public X509Certificate[] getAcceptedIssuers() { return null;}

    public static void main(String[] args) throws Exception {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, new TrustManager[] {new CertChecker()}, new SecureRandom());
        SSLSocketFactory ssf = (SSLSocketFactory) sc.getSocketFactory();
        ((SSLSocket)ssf.createSocket(args[0], 443)).startHandshake();
    }
}
